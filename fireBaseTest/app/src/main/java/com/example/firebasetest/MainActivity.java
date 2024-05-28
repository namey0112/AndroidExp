package com.example.firebasetest;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail;
    private Button buttonAdd, buttonUpdate, buttonDelete;
    private ListView listViewStudents;
    private List<Student> studentList;
    private FirebaseDatabaseHelper databaseHelper;
    private Student selectedStudent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        editTextName = findViewById(R.id.edtName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        listViewStudents = findViewById(R.id.listViewStudents);
        studentList = new ArrayList<>();
        databaseHelper = new FirebaseDatabaseHelper();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent();
            }
        });
        loadStudents();
    }
    private void addStudent() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        // Basic email format check (you can use a more robust regex for better validation)
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid email address");
            editTextEmail.requestFocus();
            return;
        }
        databaseHelper.getReference().orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Duplicate email found
                            editTextEmail.setError("Email already exists");
                            editTextEmail.requestFocus();
                        } else {
                            // No duplicate, proceed with adding
                            String id = databaseHelper.getReference().push().getKey();
                            Student student = new Student(id, name, email);
                            databaseHelper.addStudent(student);

                            editTextName.setText("");
                            editTextEmail.setText("");
                            Toast.makeText(MainActivity.this, "Student added successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        Toast.makeText(MainActivity.this, "Failed to check for duplicates", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateStudent() {
        if (selectedStudent == null) {
            Toast.makeText(this, "Please select a student to update.", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        // Input validation (similar to addStudent)
        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        // Basic email format check
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid email address");
            editTextEmail.requestFocus();
            return;
        }

        // Check for duplicates (only if email has changed)
        if (!email.equals(selectedStudent.getEmail())) {
            editTextEmail.setError("Email already exists");
            editTextEmail.requestFocus();
            return;
        } else if(name.equals(selectedStudent.getName())){
            editTextName.setError("Name already exists");
            editTextName.requestFocus();
        } else {
            Student updatedStudent = new Student(selectedStudent.getId(), name, email);
            databaseHelper.updateStudent(selectedStudent.getId(), updatedStudent);

            editTextName.setText("");
            editTextEmail.setText("");
            Toast.makeText(MainActivity.this, "Student updated successfully", Toast.LENGTH_SHORT).show();
            selectedStudent = null;

        }
    }
    private void deleteStudent() {
        if (selectedStudent == null) {
            Toast.makeText(this, "Please select a student to delete.", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseHelper.deleteStudent(selectedStudent.getId());

        editTextName.setText("");
        editTextEmail.setText("");
        Toast.makeText(MainActivity.this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
        selectedStudent = null;
    }
    private void loadStudents() {
        databaseHelper.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                ArrayList<String> listStudent = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Student student = postSnapshot.getValue(Student.class);
                    studentList.add(student);

                }
                for (Student stu : studentList) {
                    String name = stu.getName();
                    String email = stu.getEmail();
                    String display = "Name: " + name + " - Email: " + email;
                    listStudent.add(display);
                }
                // Update ListView adapter here
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listStudent);
                listViewStudents.setAdapter(adapter);
                listViewStudents.setOnItemClickListener((parent, view, position, id) -> {
                    selectedStudent = studentList.get(position);
                    editTextName.setText(selectedStudent.getName());
                    editTextEmail.setText(selectedStudent.getEmail());
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(MainActivity.this, "Failed to load students", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
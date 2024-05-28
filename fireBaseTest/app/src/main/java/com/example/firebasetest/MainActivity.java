package com.example.firebasetest;

import android.os.Bundle;
import android.view.View;
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
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String id = databaseHelper.getReference().push().getKey();
        Student student = new Student(id, name, email);
        databaseHelper.addStudent(student);
    }
    private void updateStudent() {
        String id = selectedStudent.getId();
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        Student student = new Student(id, name, email);
        databaseHelper.updateStudent(id, student);
    }
    private void deleteStudent() {
        String id = selectedStudent.getId();// get selected student id
        databaseHelper.deleteStudent(id);
    }
    private void loadStudents() {
        databaseHelper.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Student student = postSnapshot.getValue(Student.class);
                    studentList.add(student);
                }
                // Update ListView adapter here
                StudentAdapter adapter = new StudentAdapter(MainActivity.this, studentList);
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
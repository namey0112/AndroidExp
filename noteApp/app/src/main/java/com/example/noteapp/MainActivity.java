package com.example.noteapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<String> arrWork;
    ArrayAdapter<String> arrAdapter;
    EditText edtWork, edtHour, edtMin;
    TextView txtDate;
    Button btnWork;
    SharedPreferences sharedPreferences;
    static final String PREF_NAME = "my_prefs";
    static final String LIST_KEY = "list_data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lv = findViewById(R.id.lv);
        edtWork = findViewById(R.id.edtWorkEnter);
        edtHour = findViewById(R.id.edtEnterHour);
        edtMin = findViewById(R.id.edtEnterMinute);
        btnWork = findViewById(R.id.btnAddWork);
        txtDate = findViewById(R.id.txtVToday);
        arrWork = new ArrayList<>();
        arrAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrWork);
        lv.setAdapter(arrAdapter);
        Date currentDate = Calendar.getInstance().getTime();
        java.text.SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtDate.setText("Hôm nay: " + simpleDateFormat.format(currentDate));
        btnWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtWork.getText().toString().equals("") || edtHour.getText().toString().equals("") || edtMin.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Info missing!");
                    builder.setMessage("Please fill in all the fields!");
                    builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
                else {
                    String str = edtWork.getText().toString() + " - " + edtHour.getText().toString() + ":" + edtMin.getText().toString();
                    arrWork.add(str);
                    arrAdapter.notifyDataSetChanged();
                    edtWork.setText("");
                    edtHour.setText("");
                    edtMin.setText("");
                }
            }
        });
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }
    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrWork);
        editor.putString(LIST_KEY, json);
        editor.apply();
    }
    private void loadData() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LIST_KEY, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        arrWork = gson.fromJson(json, type);
        if (arrWork == null) {
            arrWork = new ArrayList<>();
        }

        // Update your ListView adapter with the retrieved data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrWork);
        lv.setAdapter(adapter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
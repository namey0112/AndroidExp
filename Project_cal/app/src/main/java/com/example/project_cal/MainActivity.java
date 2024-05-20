package com.example.project_cal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText edtA, edtB, edtAnswer;
    Button btnAdd, btnSub, btnMul, btnDiv;

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
        edtA = findViewById(R.id.edtA);
        edtB = findViewById(R.id.edtB);
        edtAnswer = findViewById(R.id.edtAnswer);
        btnAdd = findViewById(R.id.btnTong);
        btnSub = findViewById(R.id.btnHieu);
        btnMul = findViewById(R.id.btnTich);
        btnDiv = findViewById(R.id.btnThuong);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt("0" + edtA.getText());
                int b = Integer.parseInt("0" + edtB.getText());
                // TODO Auto-generated method stub
                edtAnswer.setText("a + b =" + (a + b));
            }
        });
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt("0" + edtA.getText());
                int b = Integer.parseInt("0" + edtB.getText());
                // TODO Auto-generated method stub
                edtAnswer.setText("a - b =" + (a - b));
            }
        });
        btnMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt("0" + edtA.getText());
                int b = Integer.parseInt("0" + edtB.getText());
                // TODO Auto-generated method stub
                edtAnswer.setText("a * b =" + (a * b));
            }
        });
        btnDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt("0" + edtA.getText());
                int b = Integer.parseInt("0" + edtB.getText());
                // TODO Auto-generated method stub
                edtAnswer.setText("a / b =" + (a / b));
            }
        });
    }
}
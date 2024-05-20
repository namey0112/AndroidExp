package com.example.f_to_c;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText etC, etF;
    Button btnC, btnF, btnClear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        etC = findViewById(R.id.etC);
        etF = findViewById(R.id.etF);
        btnC = findViewById(R.id.btnC);
        btnF = findViewById(R.id.btnF);
        btnClear = findViewById(R.id.btnClear);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat df = new DecimalFormat("#.00");
                String takeC = etC.getText()+"";
                //
                if (takeC != "") {
                    double C = Double.parseDouble(takeC);
                    etF.setText(("" + df.format(C * 1.8 + 32).replace(",", ".")));
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat df = new DecimalFormat("#.00");

                String takeF = etF.getText()+"";
                //
                if (takeF != "") {
                    double F = Double.parseDouble(takeF);
                    double C = (F - 32) / 1.8;
                    String.format(Locale.US, "%.2f", C);
                    String Ftext = df.format(C).replace(",", ".");
                    etC.setText(Ftext);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etC.setText("");
                etF.setText("");
            }
        });
    }
}
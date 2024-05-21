package com.example.studentmanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtClassId, edtClassName, edtClassAttend;
    Button btnInsert, btnDel, btnUpdate;
    ListView lvStudent;
    ArrayList<String> mylist;
    ArrayAdapter<String> myAdapter;
    SQLiteDatabase myDB;

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
        edtClassId = findViewById(R.id.edtClassId);
        edtClassName = findViewById(R.id.edtClassName);
        edtClassAttend = findViewById(R.id.edtClassAttend);
        btnInsert = findViewById(R.id.btnInsert);
        btnDel = findViewById(R.id.btnDel);
        btnUpdate = findViewById(R.id.btnUpdate);
        lvStudent = findViewById(R.id.lvStudent);
        mylist = new ArrayList<>();
        myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lvStudent.setAdapter(myAdapter);
        myDB = openOrCreateDatabase("student.db", MODE_PRIVATE, null);
        try {
            String sql = "CREATE TABLE tbllop (malop TEXT PRIMARY KEY, tenlop TEXT, siso INTERGER)";
            myDB.execSQL(sql);
            Log.e("Thông báo", "Table đã tạo thành công");
        } catch (Exception e) {
            Log.e("Error", "Table đã tồn tại");
        }
        dataLoad();
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIdExits()) {
                    Toast.makeText(MainActivity.this, "Mã lớp đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmty()) {
                    Toast.makeText(MainActivity.this, "Bạn chưa nhập dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }
                String malop = edtClassId.getText().toString();
                String tenlop = edtClassName.getText().toString();
                int siso = Integer.parseInt(edtClassAttend.getText().toString());
                ContentValues myValues = new ContentValues();
                myValues.put("malop", malop);
                myValues.put("tenlop", tenlop);
                myValues.put("siso", siso);
                String msg = "";
                if (myDB.insert("tbllop", null, myValues) == -1) {
                    msg = "Thêm dữ liệu thất bại";
                } else {
                    msg = "Thêm dữ liệu thành công";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                dataLoad();
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIdExits()) {
                    Toast.makeText(MainActivity.this, "Mã lớp không tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmty()) {
                    Toast.makeText(MainActivity.this, "Bạn chưa nhập dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }
                String malop = edtClassId.getText().toString();
                int n = myDB.delete("tbllop", "malop=?", new String[]{malop});
                String msg = "";
                if (n == 0) {
                    msg = "Xóa dữ liệu thất bại";
                } else {
                    msg = "Bản ghi" + n + "xóa thành công";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                dataLoad();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!checkIdExits()) {
                    Toast.makeText(MainActivity.this, "Mã lớp không tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkRowExits()) {
                    Toast.makeText(MainActivity.this, "Bản ghi đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmty()) {
                    Toast.makeText(MainActivity.this, "Bạn chưa nhập dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }
                int siso = Integer.parseInt(edtClassAttend.getText().toString());
                String malop = edtClassId.getText().toString();
                ContentValues myValues = new ContentValues();
                myValues.put("siso", siso);
                int n = myDB.update("tbllop", myValues, "malop=?", new String[]{malop});
                String msg = "";
                if (n == 0) {
                    msg = "Cập nhật dữ liệu thất bại";
                } else {
                    msg = "Cập nhật dữ liệu thành công";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                dataLoad();
            }
        });
        lvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtClassId.setText(mylist.get(position).split(" - ")[0]);
                edtClassName.setText(mylist.get(position).split(" - ")[1]);
                edtClassAttend.setText(mylist.get(position).split(" - ")[2]);
            }
        });
    }
    public void dataLoad() {
        mylist.clear();
        Cursor myCursor = myDB.query("tbllop", null, null, null, null, null, null);
        myCursor.moveToNext();
        String data = "";
        while (!myCursor.isAfterLast()) {
            data = myCursor.getString(0) + " - " + myCursor.getString(1) + " - " + myCursor.getString(2);
            myCursor.moveToNext();
            mylist.add(data);
        }
        myCursor.close();
        myAdapter.notifyDataSetChanged();
    }
    public boolean checkIdExits () {
        String malop = edtClassId.getText().toString();
        Cursor myCursor = myDB.query("tbllop", null, "malop=?", new String[]{malop}, null, null, null);
        if (myCursor.getCount() > 0) {
            myCursor.close();
            return true;
        } else {
            myCursor.close();
            return false;
        }
    }
    public boolean checkRowExits() {
        Cursor myCursor = myDB.query("tbllop", null, null, null, null, null, null);
        myCursor.moveToNext();
        String data = "";
        while (!myCursor.isAfterLast()) {
            data = myCursor.getString(0) + " - " + myCursor.getString(1) + " - " + myCursor.getString(2);
            myCursor.moveToNext();
            if (data.equals(edtClassId.getText().toString() + " - " + edtClassName.getText().toString() + " - " + edtClassAttend.getText().toString())) {
                myCursor.close();
                return true;
            }
        }
        myCursor.close();
        return false;
    }
    public boolean isEmty() {
        if (edtClassId.getText().toString().equals("") || edtClassName.getText().toString().equals("") || edtClassAttend.getText().toString().equals("")) {
            return true;
        } else {
            return false;
        }
    }


}
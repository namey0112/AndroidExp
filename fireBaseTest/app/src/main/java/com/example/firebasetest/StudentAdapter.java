package com.example.firebasetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {

    public StudentAdapter(Context context, List<Student> students) {
        super(context, 0, students);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Student student = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.edtName);
        TextView emailTextView = convertView.findViewById(R.id.editTextEmail);

        nameTextView.setText(student.getName());
        emailTextView.setText(student.getEmail());

        return convertView;
    }
}

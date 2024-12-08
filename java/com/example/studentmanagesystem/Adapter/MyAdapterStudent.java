package com.example.studentmanagesystem.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.studentmanagesystem.R;
import com.example.studentmanagesystem.model.Student;

import java.util.ArrayList;

public class MyAdapterStudent extends ArrayAdapter<Student> {
    ArrayList<Student> studentList = new ArrayList<Student>();
    public MyAdapterStudent(Context context, int resource, ArrayList<Student> objects) {
        super(context, resource, objects);
        studentList = objects;

    };

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View v = converView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (v == null) {
            v = inflater.inflate(R.layout.my_student, null);
        }

        ImageView imgStudent =  v.findViewById(R.id.imgStudent);
        TextView txtclassstudent =  v.findViewById(R.id.txtStudentClass);
        TextView txtnamestudent =  v.findViewById(R.id.txtStudentName);
        TextView txtbirthdaystudent =  v.findViewById(R.id.txtStudentBirthday);
        TextView txtgenderstudent = v.findViewById(R.id.txtStudentGender);
        TextView txtaddressstudent = v.findViewById(R.id.txtStudentAddress);


        if (position == 0) {
            txtclassstudent.setBackgroundColor(Color.WHITE);
            txtnamestudent.setBackgroundColor(Color.WHITE);
            txtbirthdaystudent.setBackgroundColor(Color.WHITE);
            txtgenderstudent.setBackgroundColor(Color.WHITE);
            txtaddressstudent.setBackgroundColor(Color.WHITE);

        }

        Student student = studentList.get(position);

        //Cap nhat thong tin sinh vien
        imgStudent.setImageResource(R.drawable.student);
        txtclassstudent.setText("Mã lớp: " + studentList.get(position).getName_class());
        txtnamestudent.setText("Tên sinh viên: " + studentList.get(position).getName_student());
        txtbirthdaystudent.setText("Ngày sinh: " + studentList.get(position).getBirthday());
        txtgenderstudent.setText("Giới tính: " + studentList.get(position).getGender_student());
        txtaddressstudent.setText("Địa chỉ: " + studentList.get(position).getAddress_student());


        return v;
    }
}

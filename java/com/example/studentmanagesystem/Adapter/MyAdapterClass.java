package com.example.studentmanagesystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.example.studentmanagesystem.R;
import com.example.studentmanagesystem.model.Room;

import java.util.ArrayList;

public class MyAdapterClass extends ArrayAdapter<Room> {
    ArrayList<Room> classList = new ArrayList<>();

    public MyAdapterClass(Context context, int resource, ArrayList<Room> objects) {
        super(context, resource, objects);
        classList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (v == null) {
            v = inflater.inflate(R.layout.my_class, null);
        }

        TextView txtCodeClass = v.findViewById(R.id.txtclasscode);
        TextView txtNameClass = v.findViewById(R.id.txtclassname);
        TextView txtNumberClass = v.findViewById(R.id.txtclassnumber);

        // Set background color for the first item in the list
        if (position == 0) {
            txtCodeClass.setBackgroundColor(Color.WHITE);
            txtNameClass.setBackgroundColor(Color.WHITE);
            txtNumberClass.setBackgroundColor(Color.WHITE);
        }

        // Set values for TextViews based on the Room object at the current position
        txtCodeClass.setText(classList.get(position).getCode_class());
        txtNameClass.setText(classList.get(position).getName_class());
        txtNumberClass.setText(classList.get(position).getClass_number());

        return v;
    }
}
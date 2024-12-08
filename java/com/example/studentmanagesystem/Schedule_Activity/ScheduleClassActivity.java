package com.example.studentmanagesystem.Schedule_Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagesystem.Login;
import com.example.studentmanagesystem.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleClassActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Spinner spinnerClasses, spinnerDays;
    TimePicker timePickerStart, timePickerEnd;
    Button btnScheduleClass, btnViewSchedule, btnCloseSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_class);

        // Ánh xạ các view từ layout
        initializeViews();

        // Mở hoặc tạo cơ sở dữ liệu
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);

        // Tải danh sách các lớp học và ngày học vào các spinner
        loadClasses();
        loadDays();

        // Đặt sự kiện cho các nút
        setEventListeners();
    }

    private void initializeViews() {
        spinnerClasses = findViewById(R.id.spinnerClasses);
        spinnerDays = findViewById(R.id.spinnerDays);
        timePickerStart = findViewById(R.id.timePickerStart);
        timePickerEnd = findViewById(R.id.timePickerEnd);
        btnScheduleClass = findViewById(R.id.btnScheduleClass);
        btnViewSchedule = findViewById(R.id.btnViewSchedule);
        btnCloseSchedule = findViewById(R.id.btnCloseSchedule);
    }

    private void loadClasses() {
        List<String> classes = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT id_class, name_class FROM tblclass", null);
            if (cursor.moveToFirst()) {
                do {
                    classes.add(cursor.getInt(0) + " - " + cursor.getString(1));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("LoadClassesError", "Lỗi khi tải danh sách lớp học", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClasses.setAdapter(adapter);
    }

    private void loadDays() {
        List<String> days = new ArrayList<>();
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");
        days.add("Sunday");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapter);
    }

    private void scheduleClass() {
        String selectedClass = (String) spinnerClasses.getSelectedItem();
        String selectedDay = (String) spinnerDays.getSelectedItem();

        int startHour = timePickerStart.getHour();
        int startMinute = timePickerStart.getMinute();
        int endHour = timePickerEnd.getHour();
        int endMinute = timePickerEnd.getMinute();

        if (selectedClass == null || selectedDay == null) {
            Toast.makeText(this, "Vui lòng chọn lớp học và ngày học", Toast.LENGTH_LONG).show();
            return;
        }

        int classId = Integer.parseInt(selectedClass.split(" - ")[0]);

        try {
            String sql = "INSERT INTO tblschedule (id_class, day, start_hour, start_minute, end_hour, end_minute) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            db.execSQL(sql, new Object[]{classId, selectedDay, startHour, startMinute, endHour, endMinute});
            Toast.makeText(this, "Đã xếp lịch học thành công", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("ScheduleClassError", "Xếp lịch học không thành công", e);
            Toast.makeText(this, "Xếp lịch học không thành công: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setEventListeners() {
        btnScheduleClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleClass();
            }
        });

        btnViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScheduleClassActivity.this, ViewScheduleActivity.class));
            }
        });

        btnCloseSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

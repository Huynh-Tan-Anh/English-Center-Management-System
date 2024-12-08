package com.example.studentmanagesystem.Schedule_Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagesystem.Login;
import com.example.studentmanagesystem.R;

import java.util.ArrayList;
import java.util.List;

public class ViewScheduleActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Spinner spinnerViewMode;
    ListView listViewSchedule;
    TextView txtNoSchedule;
    ScheduleAdapter scheduleAdapter;
    List<ScheduleItem> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        // Ánh xạ các view từ layout
        initializeViews();

        // Mở hoặc tạo cơ sở dữ liệu
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);

        // Khởi tạo danh sách lịch học và adapter
        scheduleList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(this, scheduleList);
        listViewSchedule.setAdapter(scheduleAdapter);

        // Tải các ngày vào spinner
        loadDays();

        // Đặt sự kiện cho spinner
        setEventListeners();
    }

    private void initializeViews() {
        spinnerViewMode = findViewById(R.id.spinnerViewMode);
        listViewSchedule = findViewById(R.id.listViewSchedule);
        txtNoSchedule = findViewById(R.id.txtNoSchedule);
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
        spinnerViewMode.setAdapter(adapter);
    }

    private void loadScheduleForDay(String day) {
        scheduleList.clear();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT tblclass.name_class, COUNT(tblstudent.id_student) AS student_count, tblschedule.start_hour, tblschedule.start_minute, " +
                    "tblschedule.end_hour, tblschedule.end_minute FROM tblschedule " +
                    "JOIN tblclass ON tblschedule.id_class = tblclass.id_class " +
                    "LEFT JOIN tblstudent ON tblclass.id_class = tblstudent.id_class " +
                    "WHERE tblschedule.day = ?", new String[]{day});
            if (cursor.moveToFirst()) {
                do {
                    String className = cursor.getString(0);
                    int studentCount = cursor.getInt(1);
                    int startHour = cursor.getInt(2);
                    int startMinute = cursor.getInt(3);
                    int endHour = cursor.getInt(4);
                    int endMinute = cursor.getInt(5);
                    String time = String.format("%02d:%02d - %02d:%02d", startHour, startMinute, endHour, endMinute);
                    scheduleList.add(new ScheduleItem(className, studentCount, day, time));
                } while (cursor.moveToNext());
            }
            scheduleAdapter.notifyDataSetChanged();
            txtNoSchedule.setVisibility(scheduleList.isEmpty() ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            Log.e("LoadScheduleError", "Lỗi khi tải lịch học", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void setEventListeners() {
        spinnerViewMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDay = (String) parent.getItemAtPosition(position);
                loadScheduleForDay(selectedDay);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì cả
            }
        });
    }
}

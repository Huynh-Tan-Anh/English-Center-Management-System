package com.example.studentmanagesystem.Schedule_Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagesystem.Login;
import com.example.studentmanagesystem.R;

public class EditScheduleActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private EditText edtClassName, edtClassCode;
    private TimePicker timePickerStart, timePickerEnd;
    private Button btnSave, btnCancel;
    private String scheduleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shedule);

        edtClassName = findViewById(R.id.edtClassName);
        edtClassCode = findViewById(R.id.edtClassCode);
        timePickerStart = findViewById(R.id.timePickerStart);
        timePickerEnd = findViewById(R.id.timePickerEnd);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        scheduleInfo = intent.getStringExtra("schedule_info");

        // Hiển thị thông tin lịch học cũ
        displayScheduleInfo(scheduleInfo);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScheduleInfo();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayScheduleInfo(String scheduleInfo) {
        // Tách thông tin lịch học từ chuỗi và hiển thị
        String[] parts = scheduleInfo.split(", ");
        String className = parts[0].split(": ")[1];
        String classCode = parts[1].split(": ")[1];
        String[] startParts = parts[2].split(": ")[1].split(" - ")[0].split(":");
        String[] endParts = parts[2].split(": ")[1].split(" - ")[1].split(":");

        edtClassName.setText(className);
        edtClassCode.setText(classCode);
        timePickerStart.setHour(Integer.parseInt(startParts[0]));
        timePickerStart.setMinute(Integer.parseInt(startParts[1]));
        timePickerEnd.setHour(Integer.parseInt(endParts[0]));
        timePickerEnd.setMinute(Integer.parseInt(endParts[1]));
    }

    private void saveScheduleInfo() {
        String newClassName = edtClassName.getText().toString();
        String newClassCode = edtClassCode.getText().toString();
        int newStartHour = timePickerStart.getHour();
        int newStartMinute = timePickerStart.getMinute();
        int newEndHour = timePickerEnd.getHour();
        int newEndMinute = timePickerEnd.getMinute();

        // Thực hiện cập nhật vào cơ sở dữ liệu
        try {
            String sql = "UPDATE tblschedule SET name_class=?, code_class=?, start_hour=?, start_minute=?, end_hour=?, end_minute=? WHERE id_schedule=?";
            db.execSQL(sql, new Object[]{newClassName, newClassCode, newStartHour, newStartMinute, newEndHour, newEndMinute});
            Toast.makeText(this, "Cập nhật lịch học thành công", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Cập nhật lịch học không thành công: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

package com.example.studentmanagesystem.Score_Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagesystem.Login;
import com.example.studentmanagesystem.R;

import java.util.ArrayList;
import java.util.List;

public class EnterScoreActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Spinner spinnerClasses, spinnerStudents;
    EditText edtScore;
    Button btnSaveScore, btnViewScores, btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_score);

        spinnerClasses = findViewById(R.id.spinnerClasses);
        spinnerStudents = findViewById(R.id.spinnerStudents);
        edtScore = findViewById(R.id.edtScore);
        btnSaveScore = findViewById(R.id.btnSaveScore);
        btnViewScores = findViewById(R.id.btnViewScores);
        btnClose = findViewById(R.id.btnClose);

        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);

        loadClasses();

        spinnerClasses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadStudents();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        btnSaveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScore();
            }
        });

        btnViewScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterScoreActivity.this, ViewScoresActivity.class);
                startActivity(intent);
                Toast.makeText(EnterScoreActivity.this, "Đã mở màn hình xem điểm", Toast.LENGTH_LONG).show();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadClasses() {
        List<String> classes = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT id_class, name_class FROM tblclass", null);
        if (cursor.moveToFirst()) {
            do {
                classes.add(cursor.getInt(0) + " - " + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClasses.setAdapter(adapter);
    }

    private void loadStudents() {
        List<String> students = new ArrayList<>();
        String selectedClass = (String) spinnerClasses.getSelectedItem();
        int classId = Integer.parseInt(selectedClass.split(" - ")[0]);

        Cursor cursor = db.rawQuery("SELECT id_student, name_student FROM tblstudent WHERE id_class = " + classId, null);
        if (cursor.moveToFirst()) {
            do {
                students.add(cursor.getInt(0) + " - " + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, students);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudents.setAdapter(adapter);
    }

    private void saveScore() {
        String selectedStudent = (String) spinnerStudents.getSelectedItem();
        String scoreText = edtScore.getText().toString();

        if (selectedStudent == null || selectedStudent.isEmpty() || scoreText.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
            return;
        }

        int studentId = Integer.parseInt(selectedStudent.split(" - ")[0]);
        double score = Double.parseDouble(scoreText);

        // Kiểm tra xem sinh viên đã có điểm chưa
        Cursor cursor = db.rawQuery("SELECT * FROM tblscore WHERE id_student = ? AND subject = ?", new String[]{String.valueOf(studentId), "Default Subject"});
        if (cursor.getCount() > 0) {
            Toast.makeText(this, "Sinh viên này đã có điểm. Không thể nhập thêm.", Toast.LENGTH_LONG).show();
            cursor.close();
            return;
        }
        cursor.close();

        String sql = "INSERT INTO tblscore (id_student, subject, score) VALUES (?, ?, ?)";
        db.execSQL(sql, new Object[]{studentId, "Default Subject", score});

        Toast.makeText(this, "Đã lưu điểm thành công", Toast.LENGTH_LONG).show();

        // Xóa trắng các trường nhập liệu
        edtScore.setText("");
        spinnerStudents.setSelection(0);
    }
}

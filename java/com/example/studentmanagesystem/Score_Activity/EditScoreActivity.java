package com.example.studentmanagesystem.Score_Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagesystem.Login;
import com.example.studentmanagesystem.R;

public class EditScoreActivity extends AppCompatActivity {
    SQLiteDatabase db;
    EditText edtNewScore;
    Button btnSave, btnCancel;
    int scoreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_score);

        edtNewScore = findViewById(R.id.edtNewScore);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);

        Intent intent = getIntent();
        scoreId = intent.getIntExtra("scoreId", -1);
        String currentScore = intent.getStringExtra("currentScore");

        edtNewScore.setText(currentScore);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScore();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveScore() {
        String newScoreText = edtNewScore.getText().toString();
        if (newScoreText.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập điểm mới", Toast.LENGTH_LONG).show();
            return;
        }

        double newScore = Double.parseDouble(newScoreText);

        try {
            // Cập nhật điểm trong cơ sở dữ liệu
            String sql = "UPDATE tblscore SET score = ? WHERE id = ?";
            db.execSQL("UPDATE tblscore SET score = ? WHERE id = ?", new Object[]{newScore, scoreId});
            Toast.makeText(this, "Đã cập nhật điểm", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Cập nhật điểm không thành công: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        finish();
    }
}

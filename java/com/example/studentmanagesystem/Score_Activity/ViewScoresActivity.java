package com.example.studentmanagesystem.Score_Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagesystem.Login;
import com.example.studentmanagesystem.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewScoresActivity extends AppCompatActivity {
    SQLiteDatabase db;
    ListView lvScores;
    List<HashMap<String, String>> scoresList;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scores);

        lvScores = findViewById(R.id.lvScores);

        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);

        loadScores();

        // Đăng ký menu ngữ cảnh cho ListView
        registerForContextMenu(lvScores);
    }

    private void loadScores() {
        scoresList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT s.id, s.id_student, st.name_student, s.subject, s.score FROM tblscore s JOIN tblstudent st ON s.id_student = st.id_student", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(cursor.getInt(0)));
                map.put("id_student", String.valueOf(cursor.getInt(1)));
                map.put("name_student", cursor.getString(2));
                map.put("subject", cursor.getString(3));
                map.put("score", String.valueOf(cursor.getDouble(4)));
                scoresList.add(map);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "Không có điểm đã nhập", Toast.LENGTH_LONG).show();
        }
        cursor.close();

        String[] from = {"id_student", "name_student", "subject", "score"};
        int[] to = {R.id.txtStudentId, R.id.txtStudentName, R.id.txtSubject, R.id.txtScore};

        adapter = new SimpleAdapter(this, scoresList, R.layout.score_item, from, to);
        lvScores.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_score, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        HashMap<String, String> selectedScore = scoresList.get(info.position);

        int itemId = item.getItemId();
        if (itemId == R.id.menu_edit) {
            // Chỉnh sửa điểm
            editScore(selectedScore);
            return true;
        } else if (itemId == R.id.menu_delete) {
            // Xóa điểm
            deleteScore(selectedScore);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    private void editScore(HashMap<String, String> selectedScore) {
        Intent intent = new Intent(ViewScoresActivity.this, EditScoreActivity.class);
        intent.putExtra("scoreId", Integer.parseInt(selectedScore.get("id")));
        intent.putExtra("currentScore", selectedScore.get("score"));
        startActivity(intent);
    }

    private void deleteScore(final HashMap<String, String> selectedScore) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa điểm")
                .setMessage("Bạn có chắc chắn muốn xóa điểm này?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int id = Integer.parseInt(selectedScore.get("id"));
                        db.execSQL("DELETE FROM tblscore WHERE id = ?", new Object[]{id});
                        Toast.makeText(ViewScoresActivity.this, "Đã xóa điểm", Toast.LENGTH_LONG).show();
                        loadScores(); // Refresh the list after deletion
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }
}

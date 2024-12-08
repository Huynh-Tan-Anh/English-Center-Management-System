package com.example.studentmanagesystem;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    public static final String DATABASE_NAME = "student.db";
    SQLiteDatabase db;
    EditText edtUsername, edtPassword;
    Button btnCloseLogin, btnLogin;

    private void initDB() {
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        String sql;
        try {
            // Kiểm tra và tạo bảng tbluser nếu chưa tồn tại
            if (!isTableExist(db, "tbluser")) {
                sql = "CREATE TABLE tbluser (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, password TEXT NOT NULL)";
                db.execSQL(sql);
                sql = "INSERT INTO tbluser (username, password) VALUES ('admin', 'admin')";
                db.execSQL(sql);
            }

            // Kiểm tra và tạo bảng tblclass nếu chưa tồn tại
            if (!isTableExist(db, "tblclass")) {
                sql = "CREATE TABLE tblclass (id_class INTEGER PRIMARY KEY AUTOINCREMENT, code_class TEXT, name_class TEXT, number_student INTEGER)";
                db.execSQL(sql);
            }

            // Kiểm tra và tạo bảng tblstudent nếu chưa tồn tại
            if (!isTableExist(db, "tblstudent")) {
                sql = "CREATE TABLE tblstudent (id_student INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " id_class INTEGER NOT NULL, code_student TEXT, name_student TEXT, " +
                        "gender_student TEXT, birthday_student TEXT, address_student TEXT)";
                db.execSQL(sql);
            }

            // Thêm đoạn mã sau vào phương thức initDB() để tạo bảng điểm
            if (!isTableExist(db, "tblscore")) {
                sql = "CREATE TABLE tblscore (id INTEGER PRIMARY KEY AUTOINCREMENT, id_student INTEGER NOT NULL," +
                        " subject TEXT NOT NULL, score REAL)";
                db.execSQL(sql);
            }

            // Kiểm tra và tạo bảng tblschedule nếu chưa tồn tại
            if(!isTableExist(db, "tblschedule")){
                sql = "CREATE TABLE tblschedule (id_schedule INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " id_class INTEGER NOT NULL, day TEXT NOT NULL, start_hour INTEGER NOT NULL," +
                        " start_minute INTEGER NOT NULL, end_hour INTEGER NOT NULL, " +
                        "end_minute INTEGER NOT NULL, FOREIGN KEY (id_class) REFERENCES tblclass(id_class))";
                db.execSQL(sql);
            }

            // Kiểm tra và tạo bảng tbldocument nếu chưa tồn tại
            if (!isTableExist(db, "tbldocument")) {
                sql = "CREATE TABLE tbldocument (id INTEGER PRIMARY KEY AUTOINCREMENT, document_name TEXT NOT NULL)";
                db.execSQL(sql);
            }

            // Kiểm tra và tạo bảng messages nếu chưa tồn tại
            if (!isTableExist(db, "messages")) {
                sql = "CREATE TABLE messages (id INTEGER PRIMARY KEY AUTOINCREMENT, sender TEXT, message TEXT, timestamp LONG)";
                db.execSQL(sql);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Khởi tạo cơ sở dữ liệu không thành công", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isUser(String username, String password) {
        try {
            db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT * FROM tbluser WHERE username='" + username + "' AND password='" + password + "'", null);
            c.moveToFirst();
            if (c.getCount() > 0) {
                c.close();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private ArrayList<String> getClassList() {
        ArrayList<String> classList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT name_class FROM tblclass", null);
        if (cursor.moveToFirst()) {
            do {
                classList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return classList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnCloseLogin = findViewById(R.id.btnCloseLogin);
        btnLogin = findViewById(R.id.btnLogin);

        initDB();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if (username.isEmpty()) {
                    Toast.makeText(Login.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                    edtUsername.requestFocus();
                } else if (password.isEmpty()) {
                    Toast.makeText(Login.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                    edtPassword.requestFocus();
                } else if (isUser(username, password)) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    // Truyền danh sách lớp học qua intent
                    intent.putStringArrayListExtra("classList", getClassList());
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCloseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

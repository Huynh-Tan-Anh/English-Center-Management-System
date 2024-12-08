package com.example.studentmanagesystem;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentmanagesystem.Activity_class.ClassList;
import com.example.studentmanagesystem.model.Room;

public class InsertEditActivity extends Activity {
    Button btnSaveClass, btnClearClass, btnCloseClass;
    EditText edtClassCode, edtClassName, edtClassNumber;
    String id_class;

    private void initWidget(){
        btnSaveClass = (Button) findViewById(R.id.btnSaveInsertClass);
        btnClearClass = (Button) findViewById(R.id.btnClearInsertClass);
        btnCloseClass = (Button) findViewById(R.id.btnCloseInsertClass);
        edtClassCode = (EditText) findViewById(R.id.edtEditClassCode);
        edtClassName = (EditText) findViewById(R.id.edtEditClassName);
        edtClassNumber = (EditText) findViewById(R.id.edtEditClassNumber);
    }

    private void getData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Room room = (Room) bundle.getSerializable("room");
        id_class = room.getId_class();
        edtClassCode.setText(room.getId_class());
        edtClassName.setText(room.getName_class());
        edtClassNumber.setText(room.getClass_number());
    }

    private boolean saveClass() {
        try {
            SQLiteDatabase db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("id_class", edtClassCode.getText().toString());
            values.put("name_class", edtClassName.getText().toString());
            values.put("class_number", edtClassNumber.getText().toString());
            if (db.insert("tblclass", null, values) != -1) {
                return true;
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Lỗi: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void clearClass(){
        edtClassCode.setText("");
        edtClassName.setText("");
        edtClassNumber.setText("");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_class);
        initWidget();
        getData();

        //gan ivent cho cac nut
        btnSaveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent();
                if (saveClass()) {
                    Room room = new Room(edtClassCode.getText().toString(), edtClassName.getText().toString(), edtClassNumber.getText().toString());
                    bundle.putSerializable("room", room);
                    intent.putExtras(bundle);
                    setResult(ClassList.SAVE_CLASS, intent);
                    Toast.makeText(getApplicationContext(), "Thêm lớp học thành công!!!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        btnClearClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearClass();
            }
        });

        btnCloseClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

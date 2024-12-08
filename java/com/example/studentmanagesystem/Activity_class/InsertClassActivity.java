package com.example.studentmanagesystem.Activity_class;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentmanagesystem.Login;
import com.example.studentmanagesystem.Notify;
import com.example.studentmanagesystem.R;
import com.example.studentmanagesystem.model.Room;

public class InsertClassActivity extends Activity {
    Button btnSaveInsertClass, btnClearInsertClass, btnCloseInsertClass;
    EditText edtClassCode, edtClassName, edtClassNumber;
    SQLiteDatabase db;

    //Khoi tao widget
    private void initWidget() {
        btnSaveInsertClass = findViewById(R.id.btnSaveInsertClass);
        btnClearInsertClass = findViewById(R.id.btnClearInsertClass);
        btnCloseInsertClass = findViewById(R.id.btnCloseInsertClass);
        edtClassCode = findViewById(R.id.edtClassCode);
        edtClassName = findViewById(R.id.edtClassName);
        edtClassNumber = findViewById(R.id.edtClassNumber);
    }

    //Them lop
    private long saveClass() {
        try {
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("code_class", edtClassCode.getText().toString());
            values.put("name_class", edtClassName.getText().toString());
            values.put("number_student", Integer.parseInt(edtClassNumber.getText().toString()));
            long result = db.insert("tblclass", null, values);
            if (result != -1) {
                return result; //Tra ve gia tri id class moi them vao
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Lỗi: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return -1;
    }

    //Xoa all dulieu
    private void clearAll() {
        edtClassCode.setText("");
        edtClassName.setText("");
        edtClassNumber.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_class);
        initWidget();

        btnSaveInsertClass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long result = saveClass();
                Bundle bundle = new Bundle();
                Intent intent = new Intent(InsertClassActivity.this, ClassList.class);
                if (result != -1) { //Them khong thanh cong
                    Room room = new Room(String.valueOf(result), edtClassName.getText().toString(), edtClassNumber.getText().toString());
                    bundle.putSerializable("room", room);
                    intent.putExtras(bundle);
                    setResult(ClassList.SAVE_CLASS, intent);
                    Toast.makeText(getApplicationContext(), "Thêm lớp học thành công!!!", Toast.LENGTH_LONG).show();
                    clearAll();
                }
            }
        });
        btnClearInsertClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });
        btnCloseInsertClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notify.exit(InsertClassActivity.this);
            }
        });
    }


}

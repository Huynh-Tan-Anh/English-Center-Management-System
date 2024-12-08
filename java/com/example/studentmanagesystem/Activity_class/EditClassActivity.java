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

public class EditClassActivity extends Activity {
    private EditText edtEditClassCode, edtEditClassName, edtEditClassNumber;
    private Button btnSave, btnClear, btnClose;
    private Room room; // The room object to be edited
    private SQLiteDatabase db;
    String id_class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class); // Ensure this matches your layout file name
        initWidget();
        getData();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = getIntent();
                if(saveClass()){
                    Room r = new Room(id_class,edtEditClassCode.getText().toString(),
                            edtEditClassName.getText().toString(),edtEditClassNumber.getText().toString());
                    bundle.putSerializable("room",r);
                    intent.putExtra("data",bundle);
                    setResult(ClassList.SAVE_CLASS,intent);
                    Toast.makeText(getApplication(),"Cập nhật lớp học thành công ",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearClass();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notify.exit(EditClassActivity.this);

            }
        });
        // Initialize views

        // Get the Room object from the intent
//        Intent intent = getIntent();
//        Bundle bundle = intent.getBundleExtra("data");
//        room = (Room) intent.getSerializableExtra("room");
//
//        // Populate fields with existing room data
//        if (room != null) {
//            edtEditClassCode.setText(room.getId_class());
//            edtEditClassName.setText(room.getName_class());
//            edtEditClassNumber.setText(room.getClass_number());
//        }

//        // Set up button click listeners
//        btnSave.setOnClickListener(v -> saveChanges());
//        btnClear.setOnClickListener(v -> clearFields());
//        btnClose.setOnClickListener(v -> finish()); // Close activity without saving
    }

//    private void saveChanges() {
//        // Get updated values
//        String classCode = edtEditClassCode.getText().toString();
//        String className = edtEditClassName.getText().toString();
//        String classSize = edtEditClassNumber.getText().toString();
//
//        // Validate inputs
//        if (classCode.isEmpty() || className.isEmpty() || classSize.isEmpty()) {
//            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Open database and update the record
//        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
//        String updateQuery = "UPDATE tbiclass SET name = ?, size = ? WHERE id_class = ?";
//        db.execSQL(updateQuery, new Object[]{className, classSize, classCode});
//
//        // Return updated room object to ClassList
//        room.setName_class(className);
//        room.setClass_number(classSize);
//
//        Intent resultIntent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("room", room);
//        resultIntent.putExtra("data", bundle);
//        setResult(ClassList.SAVE_CLASS, resultIntent);
//        finish(); // Close this activity
//    }
//
//    private void clearFields() {
//        edtEditClassCode.setText("");
//        edtEditClassName.setText("");
//        edtEditClassNumber.setText("0"); // Resetting to default value
//    }

    private void initWidget() {
        edtEditClassCode = findViewById(R.id.edtEditClassCode);
        edtEditClassName = findViewById(R.id.edtEditClassName);
        edtEditClassNumber = findViewById(R.id.edtEditClassNumber);
        btnSave = findViewById(R.id.btnSaveEditClass);
        btnClear = findViewById(R.id.btnClearEditClass);
        //btnClose = findViewById(R.id.btnCloseEditClass);
    }

    private void getData(){
//        Intent intent = getIntent();
//        Bundle bundle = intent.getBundleExtra("data");
//        Room room = (Room) bundle.getSerializable("room");
//        id_class = room.getId_class();
//        edtEditClassCode.setText(room.getCode_class());
//        edtEditClassName.setText(room.getName_class());
//        edtEditClassNumber.setText(room.getClass_number());

        Intent intent = getIntent();
        Room room = (Room) intent.getSerializableExtra("room");  // Lấy dữ liệu từ Intent
        if (room != null) {
            id_class = room.getId_class();
            edtEditClassCode.setText(room.getCode_class());
            edtEditClassName.setText(room.getName_class());
            edtEditClassNumber.setText(room.getClass_number());
        }

    }
    private boolean saveClass() {
        try {
            SQLiteDatabase db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("code_class", edtEditClassCode.getText().toString());
            values.put("name_class", edtEditClassName.getText().toString());
            values.put("number_student", Integer.parseInt(edtEditClassNumber.getText().toString()));
            if (db.update("tblclass", values, "id_class=?", new String[]{id_class}) !=1) {
                return true;
            }else {
                Toast.makeText(getApplication(), "Cập nhật lớp học không thành công", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex){
            Toast.makeText(getApplication(),"Cập nhật lớp học không thành công ",Toast.LENGTH_LONG).show();

        }
        return false;

    }


    private void clearClass(){
        edtEditClassCode.setText("");
        edtEditClassName.setText("");
        edtEditClassNumber.setText("");
    }

}
package com.example.studentmanagesystem.Activity_student;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.studentmanagesystem.Login;
import com.example.studentmanagesystem.R;
import com.example.studentmanagesystem.model.Student;

public class EditStudentActivity extends Activity {
    private SQLiteDatabase db;
    private Spinner spnEditClassCode;
    private EditText edtEditStudentCode;
    private EditText edtEditStudentName;
    private RadioGroup rdgEditStudentGender;
    private EditText edtEditStudentBirthday;
    private EditText edtEditStudentAddress;
    private Button btnSaveEditStudent;
    private Button btnClearEditStudent;
    private Button btnCloseEditStudent;
   private Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        // Initialize UI elements and set up event listeners
        initWidget();
        edtEditStudentCode = findViewById(R.id.edtEditStudentCode);
        edtEditStudentName = findViewById(R.id.edtEditStudentName);
        edtEditStudentBirthday = findViewById(R.id.edtEditStudentBirthday);
        edtEditStudentAddress = findViewById(R.id.edtEditStudentAddress);
        rdgEditStudentGender = findViewById(R.id.rdgEditStudentGender);
        btnSaveEditStudent = findViewById(R.id.btnSaveEditStudent);
        btnClearEditStudent = findViewById(R.id.btnClearEditStudent);
        btnCloseEditStudent = findViewById(R.id.btnCloseEditStudent);

        // Lấy thông tin sinh viên từ Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        student = (Student) bundle.getSerializable("student");

        // Điền thông tin sinh viên vào các EditText
        edtEditStudentCode.setText(student.getCode_student());
        edtEditStudentName.setText(student.getName_student());
        edtEditStudentBirthday.setText(student.getBirthday());
        edtEditStudentAddress.setText(student.getAddress_student());

        // Chọn giới tính
        if ("Male".equals(student.getGender_student())) {
            rdgEditStudentGender.check(R.id.rdiMale);
        } else {
            rdgEditStudentGender.check(R.id.rdiFemale);
        }

        // Xử lý sự kiện cho các nút
        btnSaveEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStudentDetails();
            }
        });

        btnClearEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });

        btnCloseEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initWidget() {
        spnEditClassCode = findViewById(R.id.spnEditClassCode);
        edtEditStudentCode = findViewById(R.id.edtEditStudentCode);
        edtEditStudentName = findViewById(R.id.edtEditStudentName);
        rdgEditStudentGender = findViewById(R.id.rdgEditStudentGender);
        edtEditStudentBirthday = findViewById(R.id.edtEditStudentBirthday);
        edtEditStudentAddress = findViewById(R.id.edtEditStudentAddress);
        btnSaveEditStudent = findViewById(R.id.btnSaveEditStudent);
        btnClearEditStudent = findViewById(R.id.btnClearEditStudent);
        btnCloseEditStudent = findViewById(R.id.btnCloseEditStudent);

        // Set up event listeners for buttons
        btnSaveEditStudent.setOnClickListener(v -> saveStudentDetails());
        btnClearEditStudent.setOnClickListener(v -> clearFields());
        btnCloseEditStudent.setOnClickListener(v -> finish());
    }

    private void saveStudentDetails() {
        String studentCode = edtEditStudentCode.getText().toString();
        String studentName = edtEditStudentName.getText().toString();
        String studentBirthday = edtEditStudentBirthday.getText().toString();
        String studentAddress = edtEditStudentAddress.getText().toString();
        String studentGender = ((RadioButton) findViewById(rdgEditStudentGender.getCheckedRadioButtonId())).getText().toString();

        // Cập nhật lại thông tin sinh viên
        student.setCode_student(studentCode);
        student.setName_student(studentName);
        student.setBirthday(studentBirthday);
        student.setAddress_student(studentAddress);
        student.setGender_student(studentGender);

        // Lưu vào cơ sở dữ liệu
        SQLiteDatabase db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("code_student", studentCode);
        values.put("name_student", studentName);
        values.put("birthday_student", studentBirthday);
        values.put("address_student", studentAddress);
        values.put("gender_student", studentGender);

        // Cập nhật sinh viên trong cơ sở dữ liệu
        int result = db.update("tblstudent", values, "id_student = ?", new String[]{student.getId_student()});

        if (result == -1) {
            Toast.makeText(getApplication(), "Cập nhật thông tin sinh viên thất bại", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplication(), "Cập nhật thông tin sinh viên thành công", Toast.LENGTH_LONG).show();

            // Trả lại kết quả cho Activity chính (StudentListActivity)
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("student", student);
            intent.putExtras(bundle);
            setResult(StudentListActivity.EDIT_STUDENT, intent);
            finish();
        }
    }

    private void clearFields() {
        edtEditStudentCode.setText("");
        edtEditStudentName.setText("");
        edtEditStudentBirthday.setText("");
        edtEditStudentAddress.setText("");
        rdgEditStudentGender.clearCheck();
    }


}
package com.example.studentmanagesystem.Activity_student;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.studentmanagesystem.Activity_class.ClassList;
import com.example.studentmanagesystem.Adapter.MyAdapterStudent;
import com.example.studentmanagesystem.Login;
import com.example.studentmanagesystem.MainActivity;
import com.example.studentmanagesystem.Notify;
import com.example.studentmanagesystem.R;
import com.example.studentmanagesystem.model.Student;

import java.util.ArrayList;

public class StudentListActivity extends Activity {
    Button btnOpenStudent, btnBackToMain;
    ListView lstStudent;
    SQLiteDatabase db;
    ArrayList<Student> studentList = new ArrayList<Student>();
    MyAdapterStudent adapter;
    int posSelected = -1; //Giu vi tri tren listview

    //Khai bao cac bien nhan ket qua
    public static final int OPEN_STUDENT = 113;
    public static final int EDIT_STUDENT = 114;
    public static final int SAVE_STUDENT = 115;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        btnOpenStudent = findViewById(R.id.btnOpenStudent);
        btnBackToMain = findViewById(R.id.btnBackToMain);
        lstStudent = findViewById(R.id.lstStudent);

        getStudentList();
        registerForContextMenu(lstStudent);

        btnOpenStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentListActivity.this, InsertStudentActivity.class);
                startActivityForResult(intent, StudentListActivity.OPEN_STUDENT);
            }
        });

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lstStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posSelected = position;
                return false;
            }
        });
    }



//    private void getStudentList() {
//        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
//        Cursor c = db.rawQuery("SELECT tblclass.id_class, tblclass.name_class, tblstudent.id_student, " +
//                "tblstudent.code_student, tblstudent.name_student, tblstudent.gender_student, " +
//                "tblstudent.birthday_student, tblstudent.address_student FROM tblclass " +
//                "tblstudent WHERE tblclass.id_class = tblstudent.id_class", null);
////
////
//        if (c.getCount() == 0) {
//            Toast.makeText(this, "Không có dữ liệu sinh viên!", Toast.LENGTH_SHORT).show();
//            studentList.clear();
//            return;
//        }
//
//        c.moveToFirst();
//        studentList.clear(); // Xóa dữ liệu cũ trước khi thêm mới
//        while (!c.isAfterLast()) {
//            studentList.add(new Student(
//                    c.getString(0).toString(), c.getString(1).toString(),
//                    c.getString(2).toString(), c.getString(3).toString(),
//                    c.getString(4).toString(), c.getString(5).toString(),
//                    c.getString(6).toString(), c.getString(7).toString()
//            ));
//            c.moveToNext();
//        }
////        c.close(); // Đừng quên đóng Cursor sau khi sử dụng.
//        adapter = new MyAdapterStudent(this, android.R.layout.simple_list_item_1, studentList);
//        lstStudent.setAdapter(adapter);
//    }

    // Lấy danh sách sinh viên từ cơ sở dữ liệu
    private void getStudentList() {
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        String query = "SELECT tblclass.id_class, tblclass.name_class, tblstudent.id_student, " +
                "tblstudent.code_student, tblstudent.name_student, tblstudent.gender_student, " +
                "tblstudent.birthday_student, tblstudent.address_student " +
                "FROM tblclass INNER JOIN tblstudent ON tblclass.id_class = tblstudent.id_class";

        Cursor c = null;
        try {
            c = db.rawQuery(query, null);

            if (c.getCount() == 0) {
                Toast.makeText(this, "Không có dữ liệu sinh viên!", Toast.LENGTH_SHORT).show();
                studentList.clear();
                return;
            }

            c.moveToFirst();
            studentList.clear(); // Xóa dữ liệu cũ trước khi thêm dữ liệu mới

            while (!c.isAfterLast()) {
                studentList.add(new Student(
                        c.getString(0), // Mã lớp
                        c.getString(1), // Tên lớp
                        c.getString(2), // ID sinh viên
                        c.getString(3), // Mã sinh viên
                        c.getString(4), // Tên sinh viên
                        c.getString(6), // Ngày sinh
                        c.getString(5), // Giới tính
                        c.getString(7)  // Địa chỉ
                ));
                c.moveToNext();
            }

            // Cập nhật adapter
            adapter = new MyAdapterStudent(this, android.R.layout.simple_list_item_1, studentList);
            lstStudent.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (c != null) {
                c.close(); // Đảm bảo đóng Cursor sau khi sử dụng
            }
        }
    }


//

    public void confirmDelete(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Xác nhận để xóa sinh viên..!!!");
        alertDialogBuilder.setIcon(R.drawable.question);
        alertDialogBuilder.setMessage("Bạn có chắc chắn muốn xóa sinh viên?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
                String id_student = studentList.get(posSelected).getId_student();
                if(db.delete("tblstudent", "id_student = ?", new String[]{id_student}) != -1) {
                    studentList.remove(posSelected);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplication(), "Xóa sinh viên thành công", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("Không đồng ý ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnustudent,menu);
//        getMenuInflater().inflate(R.menu.mnustudent, menu);
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int posSelected = info.position;

        if (item.getItemId() == R.id.mnueditstudent) {
            Student student = studentList.get(posSelected);
            Bundle bundle = new Bundle();
            Intent intent = new Intent(StudentListActivity.this, EditStudentActivity.class);
            bundle.putSerializable("student", student);
            intent.putExtras(bundle);
            startActivityForResult(intent, EDIT_STUDENT);
            return true;
        } else if (item.getItemId() == R.id.mnudeletestudent) {
            confirmDelete();
            return true;
        } else if (item.getItemId() == R.id.mnuclosestudent) {
            Notify.exit(this);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case StudentListActivity.OPEN_STUDENT:
                if (StudentListActivity.SAVE_STUDENT==resultCode  ) {
                    Bundle bundle = data.getBundleExtra("data");
                    Student student = (Student) bundle.getSerializable("student");
                    studentList.add(student);
                    adapter.notifyDataSetChanged();
                }
                break;
              case StudentListActivity.EDIT_STUDENT:
                Bundle bundle = data.getBundleExtra("data");
                Student student = (Student) bundle.getSerializable("student");
                studentList.set(posSelected, student);
                adapter.notifyDataSetChanged();
                break;
        };
    }

}

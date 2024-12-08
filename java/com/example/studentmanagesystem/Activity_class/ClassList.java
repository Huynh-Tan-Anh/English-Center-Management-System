package com.example.studentmanagesystem.Activity_class;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.studentmanagesystem.Adapter.MyAdapterClass;
import com.example.studentmanagesystem.Login;
import com.example.studentmanagesystem.MainActivity;
import com.example.studentmanagesystem.Notify;
import com.example.studentmanagesystem.R;
import com.example.studentmanagesystem.model.Room;

import java.util.ArrayList;

public class ClassList extends Activity {
    private ListView lstClass;
    private Button btnOpenClass, btnBackmain;
    private final ArrayList<Room> classList = new ArrayList<>();
    private MyAdapterClass adapter;
    private SQLiteDatabase db;
    private int posSelected = -1;

    public static final int OPEN_CLASS = 113;
    public static final int EDIT_CLASS = 114;
    public static final int SAVE_CLASS = 115;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        lstClass = findViewById(R.id.lstClass);
        btnOpenClass = findViewById(R.id.btnOpenClass);
        btnBackmain = findViewById(R.id.btnBackToMain);
        registerForContextMenu(lstClass);

        // Initialize database
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        populateClassList();

        btnOpenClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassList.this, InsertClassActivity.class);
                startActivityForResult(intent, OPEN_CLASS);
            }
        });


        btnBackmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassList.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        lstClass.setOnItemLongClickListener((parent, view, position, id) -> {
            posSelected = position;
            return false; // Return false to allow the context menu to be shown
        });
    }

    private void populateClassList() {
        classList.clear(); // Clear the existing list
        classList.add(new Room("Mã lớp", "Tên lớp", "Sỉ số"));

        try (Cursor c = db.query("tblclass", null, null, null, null, null, null)) {
            if (c.moveToFirst()) {
                do {
                    classList.add(new Room(String.valueOf(c.getString(1)), c.getString(2), String.valueOf(c.getInt(3))));
                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Lỗi: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        adapter = new MyAdapterClass(this, android.R.layout.simple_list_item_1, classList);
        lstClass.setAdapter(adapter);
    }

    public void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Xác nhận để xóa lớp học..!!!")
                .setIcon(R.drawable.question)
                .setMessage("Bạn có chắc chắn muốn xóa lớp học?")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    String id_class = classList.get(posSelected).getId_class();
                    if (db.delete("tblclass", "id_class = ?", new String[]{id_class}) > 0) {
                        classList.remove(posSelected);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Xóa lớp học thành công!!!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Không đồng ý", (dialog, which) -> dialog.dismiss())
                .create()
                .show();


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclass, menu);
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.mnueditclass) {
//
//                Room room = classList.get(posSelected);
//                Intent intent = new Intent(ClassList.this, EditClassActivity.class);
//                intent.putExtra("room", room);
//
//                startActivityForResult(intent, EDIT_CLASS);
//                return true;
//            } else if (id == R.id.mnudeleteclass) {
//                confirmDelete();
//                return true;
//            } else if (id == R.id.mnucloseclass) {
//                Notify.exit(this);
//                return true;
//            } else {
//                return super.onContextItemSelected(item);
//            }

        int id = item.getItemId();

        if (id == R.id.mnueditclass) {
            if (posSelected != -1) {
                Room room = classList.get(posSelected);
                Intent intent = new Intent(ClassList.this, EditClassActivity.class);
                intent.putExtra("room", room);  // Truyền đúng đối tượng Room vào Intent

                startActivityForResult(intent, EDIT_CLASS);  // Gọi activity với mã yêu cầu đúng
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Chưa chọn lớp học!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (id == R.id.mnudeleteclass) {
            confirmDelete();
            return true;
        } else if (id == R.id.mnucloseclass) {
            Notify.exit(this);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SAVE_CLASS) {
            Bundle bundle = data.getBundleExtra("data");
            Room room = (Room) bundle.getSerializable("room");


            if (requestCode == EDIT_CLASS && posSelected != -1) {
                // Replace the old room with the updated one
                classList.set(posSelected, room);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Cập nhật lớp học thành công", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
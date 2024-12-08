package com.example.studentmanagesystem.Document_Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagesystem.Login;
import com.example.studentmanagesystem.R;

import java.util.ArrayList;
import java.util.List;

public class DocumentManagementActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private ListView listViewDocuments;
    private Button btnAddDocument;
    private ArrayAdapter<String> documentAdapter;
    private List<String> documentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_management);

        listViewDocuments = findViewById(R.id.listViewDocuments);
        btnAddDocument = findViewById(R.id.btnAddDocument);

        // Mở hoặc tạo cơ sở dữ liệu
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        setupDocumentList();

        btnAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến Activity thêm tài liệu mới
                Intent intent = new Intent(DocumentManagementActivity.this, AddDocumentActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Làm mới danh sách tài liệu khi quay trở lại từ AddDocumentActivity
        loadDocumentData();
        documentAdapter.notifyDataSetChanged();
    }


    private void setupDocumentList() {
        documentList = new ArrayList<>();
        loadDocumentData();

        documentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, documentList);
        listViewDocuments.setAdapter(documentAdapter);
    }

    private void loadDocumentData() {
        Cursor cursor = db.rawQuery("SELECT * FROM tbldocument", null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("document_name");
            if (columnIndex >= 0) {
                do {
                    String documentName = cursor.getString(columnIndex);
                    documentList.add(documentName);
                } while (cursor.moveToNext());
            } else {
                // Xử lý trường hợp cột không tồn tại
                Toast.makeText(this, "Cột 'document_name' không tồn tại trong bảng tbldocument", Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
    }

}

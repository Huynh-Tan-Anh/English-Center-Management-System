package com.example.studentmanagesystem.Document_Activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagesystem.Login;
import com.example.studentmanagesystem.R;

public class AddDocumentActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private EditText edtDocumentName;
    private Button btnSaveDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);

        edtDocumentName = findViewById(R.id.edtDocumentName);
        btnSaveDocument = findViewById(R.id.btnSaveDocument);

        // Mở hoặc tạo cơ sở dữ liệu
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);

        btnSaveDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documentName = edtDocumentName.getText().toString().trim();
                if (documentName.isEmpty()) {
                    Toast.makeText(AddDocumentActivity.this, "Vui lòng nhập tên tài liệu", Toast.LENGTH_SHORT).show();
                } else {
                    saveDocument(documentName);
                }
            }
        });
    }

    private void saveDocument(String documentName) {
        ContentValues values = new ContentValues();
        values.put("document_name", documentName);

        long result = db.insert("tbldocument", null, values);
        if (result != -1) {
            Toast.makeText(this, "Lưu tài liệu thành công", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity sau khi lưu thành công
        } else {
            Toast.makeText(this, "Lưu tài liệu thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}

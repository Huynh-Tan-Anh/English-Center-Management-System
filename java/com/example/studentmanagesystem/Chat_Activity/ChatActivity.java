package com.example.studentmanagesystem.Chat_Activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagesystem.R;
import com.example.studentmanagesystem.Schedule_Activity.ScheduleAdapter;
import com.example.studentmanagesystem.Schedule_Activity.ScheduleItem;

import java.util.ArrayList;


public class ChatActivity extends AppCompatActivity {

    private ListView lvClasses;
    private ListView lvMessages;
    private EditText edtMessage;
    private Button btnSend;
    private MessageAdapter messageAdapter;
    private ArrayAdapter<String> classAdapter;
    private ArrayList<String> messages;
    private ArrayList<String> classList;  // Đảm bảo khai báo biến classList
    private String selectedClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        lvClasses = findViewById(R.id.lvClasses);
        lvMessages = findViewById(R.id.lvMessages);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messages);
        lvMessages.setAdapter(messageAdapter);

        // Nhận danh sách lớp học từ Intent
        classList = getIntent().getStringArrayListExtra("classList");
        if (classList == null) {
            classList = new ArrayList<>();  // Khởi tạo danh sách lớp trống nếu không nhận được dữ liệu
        }
        classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, classList);
        lvClasses.setAdapter(classAdapter);
        lvClasses.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lvClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedClass = classList.get(position);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMessage.getText().toString().trim();
                if (!message.isEmpty() && selectedClass != null) {
                    messages.add("[" + selectedClass + "] " + message);
                    messageAdapter.notifyDataSetChanged();
                    edtMessage.setText("");

                    // Lưu tin nhắn vào cơ sở dữ liệu
                    saveMessage("User", message, selectedClass);
                }
            }
        });

        // Tạo bảng messages nếu chưa tồn tại
        createMessagesTable();
    }

    private void createMessagesTable() {
        SQLiteDatabase db = openOrCreateDatabase("ChatDB", MODE_PRIVATE, null);
        String sql = "CREATE TABLE IF NOT EXISTS messages (id INTEGER PRIMARY KEY AUTOINCREMENT, sender TEXT, message TEXT, class TEXT, timestamp LONG)";
        db.execSQL(sql);
    }

    private void saveMessage(String sender, String message, String className) {
        SQLiteDatabase db = openOrCreateDatabase("ChatDB", MODE_PRIVATE, null);
        String sql = "INSERT INTO messages (sender, message, class, timestamp) VALUES (?, ?, ?, ?)";
        db.execSQL(sql, new Object[]{sender, message, className, System.currentTimeMillis()});
    }
}



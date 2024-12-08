package com.example.studentmanagesystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagesystem.Activity_class.ClassList;
import com.example.studentmanagesystem.Activity_student.StudentListActivity;
import com.example.studentmanagesystem.Chat_Activity.ChatActivity;
import com.example.studentmanagesystem.Document_Activity.DocumentManagementActivity;
import com.example.studentmanagesystem.Schedule_Activity.ScheduleClassActivity;
import com.example.studentmanagesystem.Score_Activity.EnterScoreActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnOpenClass, btnOpenStudent;
    Button btnScheduleClass, btnEnterScore;
    Button btnDocument, btnSupportStudent;
    ArrayList<String> classList;

    Button btnExitApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnOpenClass = (Button) findViewById(R.id.btnOpenClass);
        btnOpenStudent = (Button) findViewById(R.id.btnOpenStudent);

        btnScheduleClass = (Button) findViewById(R.id.btnScheduleClass);
        btnEnterScore = (Button) findViewById(R.id.btnEnterScore);


        btnExitApp = (Button) findViewById(R.id.btnExitApp);

        btnOpenClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClassList.class);
                startActivity(intent);
            }
        });

        btnOpenStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, StudentListActivity.class);
                startActivity(intent);
            }
        });


        btnEnterScore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, EnterScoreActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Nhập điểm", Toast.LENGTH_SHORT).show();
            }
        });

        btnScheduleClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScheduleClassActivity.class);
                startActivity(intent);
            }
        });

        btnDocument = findViewById(R.id.btnDocument);
        btnDocument.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DocumentManagementActivity.class);
                startActivity(intent);
            }
        });

        //
        classList = getIntent().getStringArrayListExtra("classList");

        btnSupportStudent = findViewById(R.id.btnSupportStudent);
        btnSupportStudent.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putStringArrayListExtra("classList", classList);
                startActivity(intent);
            }
        });

        btnExitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notify.exit(MainActivity.this);
            }
        });

    }
}
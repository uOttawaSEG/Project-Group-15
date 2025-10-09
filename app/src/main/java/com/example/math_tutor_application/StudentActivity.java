package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {



    ArrayList<Session> sessionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Intent intent = getIntent();
        sessionList = (ArrayList<Session>) intent.getSerializableExtra("sessionList");

        TextView textView = (TextView) findViewById(R.id.sessions);

        textView.setText(sessionList.get(0).getSessionTime().toString() + "\n" + sessionList.get(0).getCourse() + "\n");




    }
}
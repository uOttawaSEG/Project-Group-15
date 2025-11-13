package com.example.math_tutor_application.div1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.div2.Dashboard_admin;
import com.example.math_tutor_application.div3.Dashboard_tutor;
import com.example.math_tutor_application.div4.StudentDashboard;

public class Welcome extends AppCompatActivity {
    String message;
    String email;
    String password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.div1_activity_welcome);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        message = intent.getStringExtra("message");



        TextView textView = findViewById(R.id.welcomeMessage);
        textView.setText(message);

    }

    public void logoutHandler(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void dashBoardHandler(View view) {

        Intent intent;
        if (message.contains("Admin")) {
            intent = new Intent(this, Dashboard_admin.class);
        } else if (message.contains("Tutor")) {
            intent = new Intent(this, Dashboard_tutor.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);

        } else {
            String docId = getIntent().getStringExtra("docId");
            intent = new Intent(this, StudentDashboard.class);
            intent.putExtra("docId", docId);
        }
        startActivity(intent);


    }


}
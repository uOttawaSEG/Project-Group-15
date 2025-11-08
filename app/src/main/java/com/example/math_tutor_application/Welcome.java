package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Welcome extends AppCompatActivity {
    String message;
    String email;
    String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

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
        } else {
            intent = new Intent(this, Dashboard_tutor.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);

        }
        startActivity(intent);


    }


}
package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void signUpHandler(View view){
        Intent intent = new Intent(this, SignUpActivity.class);

        startActivity(intent);
    }

    public void logInHandler(View view){
        Intent intent = new Intent(this, LogInActivity.class);

        startActivity(intent);

    }

    public void adminHandler(View view){

        Intent intent = new Intent(this, AdminActivity.class);

        startActivity(intent);
    }

}
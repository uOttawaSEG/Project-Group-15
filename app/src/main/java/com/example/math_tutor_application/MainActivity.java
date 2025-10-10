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


    public void studentHandler(View view) {

        Intent intent = new Intent(this, StudentActivity.class);

        startActivity(intent);
    }

    public void tutorHandler(View view) {
        Intent intent = new Intent(this, TutorActivity.class);
        startActivity(intent);
    }

}
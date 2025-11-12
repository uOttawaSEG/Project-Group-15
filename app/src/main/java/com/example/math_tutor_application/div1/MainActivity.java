package com.example.math_tutor_application.div1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.math_tutor_application.R;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.div1_activity_main);


    }

    public void signUpHandler(View view){
        Intent intent = new Intent(this, SignUpActivity.class);

        startActivity(intent);
    }

    public void logInHandler(View view){
        Intent intent = new Intent(this, LogInActivity.class);

        startActivity(intent);

    }


}
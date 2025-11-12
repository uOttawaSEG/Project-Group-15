package com.example.math_tutor_application.div1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.math_tutor_application.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.div1_activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }

    public void studentHandler(View view) {

        Intent intent = new Intent(this, StudentActivity.class);

        startActivity(intent);
    }

    public void tutorHandler(View view) {
        Intent intent = new Intent(this, TutorActivity.class);
        startActivity(intent);
    }

    public void backToMainHandler(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
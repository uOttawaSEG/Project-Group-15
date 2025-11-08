package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard_admin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void pendingRequestHandler(View view) {
        Intent intent = new Intent(this, AdminPendingRequest.class);
        startActivity(intent);
    }

    public void acceptedRequstHandler(View view) {
        Intent intent = new Intent(this, AdminAcceptedRequest.class);
        startActivity(intent);

    }

    public void rejectedStudnetHandler(View view) {
        Intent intent = new Intent(this, AdminRejectedStudent.class);
        startActivity(intent);

    }

    public void pendingRequestHandler2(View view) {
        Intent intent = new Intent(this, AdminPendingRequestTutor.class);
        startActivity(intent);
    }

    public void acceptedRequstHandler2(View view) {
        Intent intent = new Intent(this, AdminAcceptedRequestTutor.class);
        startActivity(intent);

    }

    public void rejectedStudnetHandler2(View view) {
        Intent intent = new Intent(this, AdminRejectedTutor.class);
        startActivity(intent);


    }
}
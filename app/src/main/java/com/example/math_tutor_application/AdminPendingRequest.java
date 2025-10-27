package com.example.math_tutor_application;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminPendingRequest extends AppCompatActivity {

    //
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<RegistrationRequest> pendingRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_pending_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void logoutHandler(View view) {
    }

    public void approveHandler1(View view) {
    }

    public void rejectHandler1(View view) {
    }

    public void approveHandler2(View view) {
    }

    public void rejectHandler2(View view) {
    }

    public void approveHandler3(View view) {
    }

    public void rejectHandler3(View view) {
    }
}
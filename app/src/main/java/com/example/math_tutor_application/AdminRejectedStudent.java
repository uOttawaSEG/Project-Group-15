package com.example.math_tutor_application;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import android. view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminRejectedStudent extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<DocumentSnapshot> rejectedDocs = new ArrayList<>();
    private TextView[] rowViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_rejected_student);

        db = FirebaseFirestore.getInstance();

        rowViews = new TextView[]{
                findViewById(R.id.rejectedRequest1),
                findViewById(R.id.rejectedRequest2),
                findViewById(R.id.rejectedRequest3)
        };

        loadRejectedRequests();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    protected void onStart() {
        super.onStart();
        loadRejectedRequests();
    }

    private void loadRejectedRequests() {
        db.collection("registration_requests")
                .whereEqualTo("status", "rejected")
                .get()
                .addOnSuccessListener(snap -> {
                    rejectedDocs.clear();
                    rejectedDocs.addAll(snap.getDocuments());
                    renderRejectedRows();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "failed to load rejected requests", Toast.LENGTH_SHORT).show();
                });
    }

    private void renderRejectedRows() {
        for (int i = 0; i < rowViews.length; i++) {
            if (i < rejectedDocs.size()) {
                RegistrationRequest req = rejectedDocs.get(i).toObject(RegistrationRequest.class);
                String text = buildSummary(req);
                rowViews[i].setText(text);

                final int index = i;

                rowViews[i].setOnClickListener(v -> reapproveRequest(index));

            } else {
                rowViews[i].setText("No rejected requests here.");
            }

        }
    }

    private void reapproveRequest(int index) {

        if (index < 0 || index >= rejectedDocs.size()) {
            Toast.makeText(this, "No request in this slot.", Toast.LENGTH_SHORT).show();
            return;
        }
        String docId = rejectedDocs.get(index).getId();
        RegistrationRequest req = rejectedDocs.get(index).toObject(RegistrationRequest.class);

        FirestoreHelper helper = new FirestoreHelper();
        helper.approveRequestAndCreateUser(docId, req, v -> {
                    Toast.makeText(this, "Request re-approved.", Toast.LENGTH_SHORT).show();
                    loadRejectedRequests();
                },
                e -> Toast.makeText(this, "Re-approval failed.", Toast.LENGTH_SHORT).show());

    }

    private String buildSummary (RegistrationRequest req){
        if (req == null) {
            return "";
        }

        String base = String.format(
                "[%s] %s %s | %s | %s",
                safe(req.getRole()),
                safe(req.getFirstName()),
                safe(req.getLastName()),
                safe(req.getEmail()),
                safe(req.getPhoneNumber()));

        if ("student".equalsIgnoreCase(safe(req.getRole()))) {
            return base + " | Program: " + safe(req.getProgramOfStudy());
        } else {
            return base + " | Courses: " + safe(req.getCoursesOffered());
        }
    }


        private String safe (String s){
            return s == null ? "": s;
        }

    public void backHandler(View view) {
        finish();
    }


}


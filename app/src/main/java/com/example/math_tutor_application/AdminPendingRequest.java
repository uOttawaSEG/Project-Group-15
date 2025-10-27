package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class AdminPendingRequest extends AppCompatActivity {

    private static final int MAX_ROWS = 3;

    private FirebaseFirestore db;
    private FirestoreHelper helper;

    private final List<DocumentSnapshot> pendingDocs = new ArrayList<>(MAX_ROWS);

    private TextView[] rowViews;


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

        db = FirebaseFirestore.getInstance();
        helper = new FirestoreHelper();

        rowViews = new TextView[]{
            findViewById(R.id.AdminPendingRequest1),
            findViewById(R.id.AdminPendingRequest2),
            findViewById(R.id.AdminPendingRequest3)
        };

    }

    protected void onStart(){
        super.onStart();
        loadPending();
    }

    private void loadPending(){
        pendingDocs.clear();
        for (TextView tv: rowViews){
            if (tv != null) tv.setText("Loading");
        }
        db.collection("registration_requests")
                .whereEqualTo("status", "pending")
                .orderBy("firstName", Query.Direction.ASCENDING)
                .limit(MAX_ROWS)
                .get()
                .addOnSuccessListener(snap -> {
                    pendingDocs.clear();
                    pendingDocs.addAll(snap.getDocuments());
                    renderRows();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "failed to load pending requests", Toast.LENGTH_SHORT).show();
                    for (TextView tv: rowViews) {
                        if (tv != null) tv.setText("-");
                    }
                });
    }


    private void renderRows() {
        for (int i = 0; i < MAX_ROWS; i++) {
            TextView tv = rowViews[i];
            if (tv == null) continue;

            if (i < pendingDocs.size()) {
                DocumentSnapshot doc = pendingDocs.get(i);
                RegistrationRequest req = doc.toObject(RegistrationRequest.class);

                String line = buildSummary(req);
                tv.setText(line);
            } else {
                tv.setText("No pending request here.");
            }
        }
    }

    private String buildSummary(RegistrationRequest req) {
        if (req == null) return "—";

        String base = String.format(
                "[%s] %s %s | %s | %s",
                safe(req.getRole()),
                safe(req.getFirstName()),
                safe(req.getLastName()),
                safe(req.getEmail()),
                safe(req.getPhoneNumber())
        );

        if ("student".equalsIgnoreCase(safe(req.getRole()))) {
            return base + " | Program: " + safe(req.getProgramOfStudy());
        } else {
            return base + " | Courses: " + safe(req.getCoursesOffered());
        }
    }

    private String safe(String s) { return s == null ? "" : s; }


    public void logoutHandler(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void approveHandler1(View view) {
        approveAtIndex(0);
    }

    public void rejectHandler1(View view) {
        rejectAtIndex(0);
    }

    public void approveHandler2(View view) {
        approveAtIndex(1);
    }

    public void rejectHandler2(View view) {
        rejectAtIndex(1);
    }

    public void approveHandler3(View view) {
        approveAtIndex(2);
    }

    public void rejectHandler3(View view) {
        rejectAtIndex(2);
    }

    private void approveAtIndex(int i) {
        if (!validIndex(i)) return;

        DocumentSnapshot doc = pendingDocs.get(i);
        String docId = doc.getId();
        RegistrationRequest req = doc.toObject(RegistrationRequest.class);

        helper.approveRequestAndCreateUser(
                docId,
                req,
                v -> {
                    Toast.makeText(this, "Approved & user created.", Toast.LENGTH_SHORT).show();
                    loadPending(); // refresh list
                },
                e -> Toast.makeText(this, "Approval failed.", Toast.LENGTH_SHORT).show()
        );
    }

    private void rejectAtIndex(int i) {
        if (!validIndex(i)) return;

        String docId = pendingDocs.get(i).getId();

        helper.updateRequestStatus(
                docId,
                "rejected",
                v -> {
                    Toast.makeText(this, "Request rejected.", Toast.LENGTH_SHORT).show();
                    loadPending(); // refresh list
                },
                e -> Toast.makeText(this, "Reject failed.", Toast.LENGTH_SHORT).show()
        );
    }

    private boolean validIndex(int i) {
        if (i < 0 || i >= pendingDocs.size()) {
            Toast.makeText(this, "No request in this slot.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TutorPastSessions extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout sessionBox;
    private TextView errorText;
    private String docID;
    private List<Sessions> pastSessionLst = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_past_sessions);

        db = FirebaseFirestore.getInstance();
        sessionBox = findViewById(R.id.sessionBox);
        errorText = findViewById(R.id.errorText);

        Intent intent = getIntent();
        docID = intent.getStringExtra("docID");
        String email = intent.getStringExtra("email");

        // If docID is available I prefer using it first directly to avoid mishaps
        if (docID != null && !docID.isEmpty()) {
            loadPastSessions();
        }
        // If docID is null but email is available, query for docID first
        else if (email != null && !email.isEmpty()) {
            errorText.setText("Loading tutor information..");
            resolveDocIdFromEmail(email);
        }
        // If both are missing, show error
        else {
            errorText.setText("Error: Tutor information not found");
        }
    }

    // Helper method to get docID from email
    private void resolveDocIdFromEmail(String email) {
        db.collection("ApprovedTutors")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener(querySnapshot -> {
                if (!querySnapshot.isEmpty()) {
                    docID = querySnapshot.getDocuments().get(0).getId();
                    loadPastSessions();
                } else { // invalid search attempt
                    errorText.setText("Tutor not found");
                }
            })

            // just in case
            .addOnFailureListener(e -> {
                errorText.setText("Error: " + e.getMessage());
            });
    }

    private void loadPastSessions() {
        errorText.setText("Loading past sessions..");

        // Query Firebase for all sessions
        db.collection("ApprovedTutors")
            .document(docID)
            .collection("sessionsArrayList")
            .get()
            .addOnSuccessListener(querySnapshot -> {
                pastSessionLst.clear();

                // Filter for past sessions
                for (DocumentSnapshot doc : querySnapshot) {
                    Sessions sesh = doc.toObject(Sessions.class);
                    if (sesh != null && sesh.isPastSession()) {
                        pastSessionLst.add(sesh);
                    }
                }

                // Display the sessions results
                displaySessions();
            })
            .addOnFailureListener(e -> {
                errorText.setText("Error loading sessions: " + e.getMessage());
            });
    }


    private void displaySessions() {
        sessionBox.removeAllViews();

        if (pastSessionLst.isEmpty()) {
            errorText.setText("No past sessions found");
            return;
        }

        errorText.setText("");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

        // Create one single view for each session
        for (Sessions session : pastSessionLst) {
            TextView viewSesh = new TextView(this);

            String sessionInfo = "Session:\n" +
                    "Start: " + dateFormat.format(session.getStartDate().toDate()) + "\n" +
                    "End: " + dateFormat.format(session.getEndDate().toDate()) + "\n" +
                    "Manual Approval: " + (session.getManualApproval() ? "Yes" : "No");

            viewSesh.setText(sessionInfo);
            viewSesh.setPadding(16, 16, 16, 16);
            viewSesh.setTextSize(16);
            viewSesh.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

            LinearLayout.LayoutParams prm = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            prm.setMargins(0, 0, 0, 16);
            viewSesh.setLayoutParams(prm);

            sessionBox.addView(viewSesh);
        }
    }


    public void backToDashboard(View view) {
        finish();
    }



}

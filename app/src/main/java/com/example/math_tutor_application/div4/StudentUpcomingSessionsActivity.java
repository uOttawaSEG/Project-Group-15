package com.example.math_tutor_application.div4;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.ApprovedTutor;
import com.example.math_tutor_application.uml_classes.Notification;
import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentUpcomingSessionsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private StudentUpcomingSessionsAdapter adapter;
    private final List<RegisteredSessions> upcomingSessionsList = new ArrayList<>();

    private String studentDocId; // passed via Intent when opening this activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.div4_activity_student_upcoming_sessions);

        db = FirebaseFirestore.getInstance();
        studentDocId = getIntent().getStringExtra("studentDocId");

        recyclerView = findViewById(R.id.recyclerViewStudentUpcoming);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentUpcomingSessionsAdapter(upcomingSessionsList, this::onCancelClicked);
        recyclerView.setAdapter(adapter);

        if (isNonEmpty(studentDocId)) {
            fetchUpcomingSessions();
        } else {
            Toast.makeText(this, "Missing student ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchUpcomingSessions() {
        db.collection("RegisteredSessions")
                //move the filter logic in the if statement, because firebase can either sort or filter
                .orderBy("startDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snap -> {
                    upcomingSessionsList.clear();

                    for (QueryDocumentSnapshot doc : snap) {
                        RegisteredSessions s = doc.toObject(RegisteredSessions.class);
                        s.setDocumentId(doc.getId());

                        //null check
                        if(s.getApprovedStudentID() == null || s.getApprovedTutorId() == null || s.getStatus() == null)  {
                            continue;
                        }

                        if (!s.getApprovedStudentID().equals(studentDocId) || s.getStatus().equals("rejected")) {
                            continue;
                        }

                        if (s.isUpcomingSession()) {
                            // Only add upcoming sessions to the list
                            //also get tutor info
                            db.collection("ApprovedTutors")
                                    .whereEqualTo("documentId", s.getApprovedTutorId())
                                    .get().addOnSuccessListener(tutorSnap -> {
                                        if (tutorSnap.isEmpty()) {
                                            return;
                                        }
                                        s.setApprovedTutor(tutorSnap.getDocuments().get(0).toObject(ApprovedTutor.class));
                                        upcomingSessionsList.add(s);
                                        adapter.notifyDataSetChanged();

                                    });

                        }
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Query failed", e);
                    Toast.makeText(this, "Failed to load upcoming sessions", Toast.LENGTH_SHORT).show();
                });
    }




    private void onCancelClicked(RegisteredSessions session) {
        String fullName = getIntent().getStringExtra("fullName");

        if (session.getStartDate() == null) {
            Toast.makeText(this, "Session date missing", Toast.LENGTH_SHORT).show();
            return;
        }

        long diffMillis = session.getStartDate().toDate().getTime() - System.currentTimeMillis();
        long hoursDiff = diffMillis / (1000 * 60 * 60);

        if (hoursDiff < 24) {
            Toast.makeText(this, "Cannot cancel less than 24 hours before session", Toast.LENGTH_LONG).show();
            return;
        }

        // Confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Cancel Session")
                .setMessage("Are you sure you want to cancel the session on "
                        + DateFormat.getDateTimeInstance().format(session.getStartDate().toDate()) + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.collection("RegisteredSessions")
                            .document(session.getDocumentId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Session canceled", Toast.LENGTH_SHORT).show();
                                upcomingSessionsList.remove(session);
                                adapter.notifyDataSetChanged();

                                String message = "Student " + fullName + " has canceled their session for " + session.getCourse()
                                        + " at " + DateFormat.getDateTimeInstance().format(session.getStartDate().toDate())
                                        + " to " + DateFormat.getDateTimeInstance().format(session.getEndDate().toDate());

                                sendNotification(message, session.getApprovedTutorId(), studentDocId);
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Failed to cancel session", Toast.LENGTH_SHORT).show()
                            );

                    db.collection("Sessions")
                            .document(session.getDocumentId())
                            .update("isStudentRegister", false, "status", "pending")
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Failed to cancel session", Toast.LENGTH_SHORT).show()
                            );
                })
                .setNegativeButton("No", null)
                .show();
    }

    private boolean isNonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private void sendNotification(String message, String receiver, String sender) {
        Notification notification = new Notification();
        notification.setMsg(message);
        notification.setReceiver(receiver);
        notification.setSender(sender);
        notification.setTimestamp(new Timestamp(new Date()));
        db.collection("Notifications").add(notification);

    }

}

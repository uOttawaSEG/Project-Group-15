package com.example.math_tutor_application.div3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.example.math_tutor_application.uml_classes.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TutorUpcomingSessionsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private UpcomingSessionsAdapter adapter;
    private final List<RegisteredSessions> upcomingSessionsList = new ArrayList<>();
    private final List<RegisteredSessions> upcomingRegistedSessionsList = new ArrayList<>();




    private String tutorDocId;
    private String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.div3_tutor_upcoming_sessions);


        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        tutorDocId = intent.getStringExtra("approvedTutorDocId");
        email      = intent.getStringExtra("email");

        recyclerView = findViewById(R.id.recyclerViewUpcomingSessions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UpcomingSessionsAdapter(upcomingSessionsList, this::onSessionClicked);
        recyclerView.setAdapter(adapter);

        if (isNonEmpty(tutorDocId)) {
            fetchUpcomingSessions();
        } else {
            resolveTutorDocIdAndThenFetch();
        }



    }

    private void onSessionClicked(RegisteredSessions session) {
        showStudentInformation(session);
    }

    private void resolveTutorDocIdAndThenFetch() {
        if (!isNonEmpty(email)) {
            Toast.makeText(this, "Missing tutor email (cannot resolve ID)", Toast.LENGTH_LONG).show();
            return; // stay here; don't finish()
        }

        db.collection("ApprovedTutors")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener(snap -> {
                    if (!snap.isEmpty()) {
                        tutorDocId = snap.getDocuments().get(0).getId();
                        fetchUpcomingSessions();
                    } else {
                        Toast.makeText(this, "Tutor not found in ApprovedTutors", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to resolve tutor id", Toast.LENGTH_LONG).show();
                });
    }

    private void fetchUpcomingSessions() {
        if (!isNonEmpty(tutorDocId)) {
            Toast.makeText(this, "Tutor ID not available yet", Toast.LENGTH_SHORT).show();
            return; // don't finish(); allow later retry if needed
        }


        Log.d("Firestore", "Fetching upcoming for tutorDocId: " + tutorDocId);

        //goes overed RegisterSessions

        db.collection("RegisteredSessions")
                .get()
                .addOnCompleteListener(task3 -> {
                    Date now2 = new Date();
                    if (task3.isSuccessful() && task3.getResult() != null) {
                        for (QueryDocumentSnapshot doc : task3.getResult()) {
                            RegisteredSessions s = doc.toObject(RegisteredSessions.class);
                            try { s.setDocumentId(doc.getId()); } catch (Exception ignored) {}
                            if (s.getStartDate() != null && s.getStartDate().toDate().after(now2)) {
                                upcomingRegistedSessionsList.add(s);
                            }
                        }
                    } else {
                        Log.e("Firestore", "Error fetching upcoming sessions", task3.getException());
                        Toast.makeText(this, tutorDocId, Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(task5 -> {
                    Date now = new Date();
                    //GOES OVER SESSIONS
                    db.collection("ApprovedTutors")
                            .document(tutorDocId)
                            .collection("sessionsArrayList")
                            .whereGreaterThanOrEqualTo("startDate", now)
                            .orderBy("startDate")
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    upcomingSessionsList.clear();
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        RegisteredSessions s = doc.toObject(RegisteredSessions.class);
                                        try { s.setDocumentId(doc.getId()); } catch (Exception ignored) {}
                                        //checks to see if they are no dublicates
                                        if (s.getStartDate() != null) {

                                            for (RegisteredSessions r : upcomingRegistedSessionsList) {
                                                if( r.getDocumentId().equals(s.getDocumentId())) {
                                                    s = r;
                                                }
                                            }
                                            upcomingSessionsList.add(s);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                    Toast.makeText(
                                            this,
                                            upcomingSessionsList.isEmpty()
                                                    ? "No upcoming sessions"
                                                    : "Found " + upcomingSessionsList.size() + " upcoming sessions",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                } else {
                                    Log.e("Firestore", "Error fetching upcoming sessions", task.getException());
                                    Toast.makeText(this, tutorDocId, Toast.LENGTH_SHORT).show();
                                }
                            });

                });

    };

    private void showStudentInformation(RegisteredSessions session) {
        if (session == null || !isNonEmpty(session.getApprovedStudentID())) {
            Toast.makeText(this, "No student information available", Toast.LENGTH_SHORT).show();
            return;
        }


        db.collection("Students")
                .document(session.getApprovedStudentID())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                        Student student = task.getResult().toObject(Student.class);
                        if (student != null) {
                            String startTime =
                                    (session.getStartDate() != null)
                                            ? session.getStartDate().toDate().toString()
                                            : "TBD";

                            String studentInfo =
                                    "Name: " + safe(student.getFirstName()) + " " + safe(student.getLastName()) + "\n" +
                                            "Email: " + safe(student.getEmail()) + "\n" +
                                            "Phone: " + safe(student.getPhoneNumber()) + "\n" +
                                            "Program: " + safe(student.getProgramOfStudy()) + "\n\n" +
                                            "Session Time: " + startTime;

                            new AlertDialog.Builder(this)
                                    .setTitle("Student Information")
                                    .setMessage(studentInfo)
                                    .setPositiveButton("OK", null)
                                    .show();
                        } else {
                            Toast.makeText(this, "Student information not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to load student information", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isNonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}

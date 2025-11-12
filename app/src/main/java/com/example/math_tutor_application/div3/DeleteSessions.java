package com.example.math_tutor_application.div3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DeleteSessions extends AppCompatActivity {

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
        setContentView(R.layout.div3_delete_sessions);


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
            return;
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
            return;
        }

        db.collection("ApprovedTutors")
                .document(tutorDocId)
                .collection("sessionsArrayList")
                .orderBy("startDate")
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) {
                        return;
                    }

                    upcomingSessionsList.clear();

                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        RegisteredSessions s = doc.toObject(RegisteredSessions.class);
                        s.setDocumentId(doc.getId());
                        upcomingSessionsList.add(s);
                    }
                    adapter.notifyDataSetChanged();
                });
    }


    private void showStudentInformation(RegisteredSessions session) {

        db.collection("RegisteredSessions").
                document(session.getDocumentId())
                .delete()
                .addOnCompleteListener(aVoid -> {
                    db.collection("ApprovedTutors")
                            .document(tutorDocId)
                            .collection("sessionsArrayList")
                            .document(session.getDocumentId())
                            .delete()
                            .addOnCompleteListener(aVoid2 -> {
                                fetchUpcomingSessions();

                            });

                });



    }

    private boolean isNonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }


}
package com.example.math_tutor_application.div4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.ApprovedTutor;
import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class PastSessions extends AppCompatActivity {

    String studentDocId;

    ArrayList<RegisteredSessions> registeredSessions = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;

    private PastSessionsAdaptor adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.div4_past_sessions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        studentDocId = getIntent().getStringExtra("docId");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PastSessionsAdaptor(registeredSessions, new PastSessionsAdaptor.OnRequestActionListener() {

            @Override
            public void onRateTutor(RegisteredSessions session) {

                ApprovedTutor tutor = session.getApprovedTutor();

                if (session.getIsRated()) {
                    Toast.makeText(PastSessions.this, "You have already rated this tutor!", Toast.LENGTH_SHORT).show();
                    return;
                }


                showRatingDialog(tutor);

                session.setIsRated(true);
                db.collection("RegisteredSessions").document(session.getDocumentId()).update("isRated", true);



            }

            @Override
            public void onDisplay(ApprovedTutor tutor) {
                display(tutor);

            }
        });

        recyclerView.setAdapter(adapter);

        fetchAndDisplay();



    }

    private void fetchAndDisplay() {
        db.collection("RegisteredSessions")
                .orderBy("startDate", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) {
                        return;
                    }
                    registeredSessions.clear();
                    for (DocumentSnapshot doc : task.getResult()) {
                        RegisteredSessions r = doc.toObject(RegisteredSessions.class);
                        r.setDocumentId(doc.getId());
                        if(r.isUpcomingSession()) {
                            continue;
                        }
                        if(!r.getApprovedStudentID().equals(studentDocId)) {
                            continue;
                        }

                        //add tutor
                        db.collection("ApprovedTutors").document(r.getApprovedTutorId()).get().addOnCompleteListener(tutorTask -> {
                            if (!tutorTask.isSuccessful() || tutorTask.getResult() == null) {
                                return;
                            }

                            r.setApprovedTutor(tutorTask.getResult().toObject(ApprovedTutor.class));
                            registeredSessions.add(r);
                            //adapter.notifyItemInserted(registeredSessions.size() - 1);
                            adapter.notifyDataSetChanged();

                        });




                    }

                });
    }

    public void display(ApprovedTutor tutor) {
        StringBuilder ratingStars = new StringBuilder();
        int rating = (int) tutor.getRating();
        for (int i = 0; i < rating; i++) {
            ratingStars.append("⭐");
        }


        String studentInfo =
                "Name: " + (tutor.getFirstName()) + " " + (tutor.getLastName()) + "\n" +
                        "Email: " + (tutor.getEmail()) + "\n" +
                        "Phone: " + (tutor.getPhoneNumber()) + "\n" +
                        "Course Offered: " + (tutor.getCoursesOffered().toString())  + "\n"
                        + "Ratings: " + ("" + tutor.getRating() + ratingStars) + "\n"
                        + "Number of Ratings: " + (tutor.getNumberOfRating());


        new AlertDialog.Builder(this)
                .setTitle("Tutor Information")
                .setMessage(studentInfo)
                .setPositiveButton("OK", null)
                .show();

    }

    private void showRatingDialog(final ApprovedTutor tutor) {
        if (tutor == null || tutor.getDocumentId() == null) {
            return;
        }


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.div4_item_rate_tutor, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        final Button submitButton = dialogView.findViewById(R.id.submitButton);
        final Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        final AlertDialog dialog = builder.create();
        dialog.show();

        submitButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();

            updateTutorRating(tutor, rating);
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    private void updateTutorRating(ApprovedTutor tutor, float newRating) {
        double updatedRating = tutor.addRating(newRating);
        tutor.setRating(updatedRating);
        db.collection("ApprovedTutors").document(tutor.getDocumentId())
                .update("rating", updatedRating, "numberOfRating", FieldValue.increment(1)).addOnCompleteListener(task -> {
                    fetchAndDisplay();
                        });
        Toast.makeText(this, "Rating submitted!", Toast.LENGTH_SHORT).show();

    }



}
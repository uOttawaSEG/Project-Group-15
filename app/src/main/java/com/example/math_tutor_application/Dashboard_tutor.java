package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.math_tutor_application.div3.PendingRequestActivity;
import com.example.math_tutor_application.div3.TutorPastSessions;
import com.example.math_tutor_application.div3.TutorUpcomingSessionsActivity;
import com.example.math_tutor_application.uml_classes.ApprovedTutor;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard_tutor extends AppCompatActivity {

    String email, password, docID, firstName, lastName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard_tutor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        db.collection("ApprovedTutors")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .addOnCompleteListener(task5 -> {
                    if (task5.isSuccessful() && !task5.getResult().isEmpty()) {
                        DocumentSnapshot document = task5.getResult().getDocuments().get(0);
                        ApprovedTutor tutor = document.toObject(ApprovedTutor.class);
                        docID = document.getId();
                        firstName = tutor.getFirstName();
                        lastName = tutor.getLastName();

                    }
                });


    }

    public void slotCreationHandler(View view) {
        Intent intent = new Intent(this, SlotCreationTutor.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("docID", docID);

        startActivity(intent);
    }

    public void tutorHandler(View view) {
        Intent intent = new Intent(this, PendingRequestActivity.class);
        intent.putExtra("docID", docID);
        intent.putExtra("email", email);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        startActivity(intent);
    }

    public void upcomingSessionsHandler(View view) {
        Intent intent = new Intent(this, TutorUpcomingSessionsActivity.class);
        intent.putExtra("approvedTutorDocId", docID);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public void pastSessionsHandler(View view) {
        Intent intent = new Intent(this, TutorPastSessions.class);
        intent.putExtra("docID", docID);
        intent.putExtra("email", email); //if docID unavailable or null
        startActivity(intent);
    }



}

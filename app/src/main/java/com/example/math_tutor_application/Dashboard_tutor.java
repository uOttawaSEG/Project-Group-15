package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard_tutor extends AppCompatActivity {

    String email, password, docID;
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
        startActivity(intent);
    }

    public void upcomingSessionsHandler(View view) {
        Intent i = new Intent(this, TutorUpcomingSessionsActivity.class);
        i.putExtra("approvedTutorDocId", docID);
        i.putExtra("email", email);
        startActivity(i);
    }



}

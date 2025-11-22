package com.example.math_tutor_application.div3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.div4.NotificationDisplay;
import com.example.math_tutor_application.uml_classes.ApprovedTutor;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard_tutor extends AppCompatActivity {

    String email, password, docID, firstName, lastName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView notification;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.div3_dashboard_tutor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        docID = intent.getStringExtra("docID");



        db.collection("Notifications").whereEqualTo("receiver", docID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(int i = 0; i < task.getResult().size(); i++){
                    count++;
                }
                notification = findViewById(R.id.notification);
                notification.setText("You have " + count + "new notifications");
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


    public void deleteHandler(View view) {

        Intent intent = new Intent(this, DeleteSessions.class);
        intent.putExtra("approvedTutorDocId", docID);
        intent.putExtra("email", email);
        startActivity(intent);

    }

    public void notificationHandler(View view) {
        Intent intent = new Intent(this, NotificationDisplay.class);
        intent.putExtra("docId", docID);
        startActivity(intent);
    }
}

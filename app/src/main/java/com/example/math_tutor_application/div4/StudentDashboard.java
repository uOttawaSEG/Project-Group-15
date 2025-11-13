package com.example.math_tutor_application.div4;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.ApprovedStudent;
import com.example.math_tutor_application.uml_classes.ApprovedTutor;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentDashboard extends AppCompatActivity {

    String docId;

    FirebaseFirestore db= FirebaseFirestore.getInstance();

    ApprovedStudent approvedStudent;

    TextView fullName;
    TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.div4_student_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        docId = getIntent().getStringExtra("docId");

        db.collection("ApprovedStudents").document(docId).get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               approvedStudent = task.getResult().toObject(ApprovedStudent.class);

               fullName = findViewById(R.id.fullName);
               email = findViewById(R.id.email);

               //confirms the docId is correct
               fullName.setText(approvedStudent.getFirstName() + " " + approvedStudent.getLastName());
               email.setText(approvedStudent.getEmail());

           }

        });







    }

    public void sessionRegistrationHandler(View view) {
    }
}
package com.example.math_tutor_application.div1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.example.math_tutor_application.uml_classes.Sessions;
import com.example.math_tutor_application.uml_classes.Student;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;


public class StudentActivity extends AppCompatActivity {

    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String phoneNumber;
    protected String programOfStudy;

    // firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.div1_activity_student);

//        db.collection("Students")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Student student = document.toObject(Student.class);
//                            student.setDocumentId(document.getId());
//                            student.setStatus("pending");
//                            db.collection("Students").document(student.getDocumentId()).set(student);
//                        }
//
//                    }
//                });

        }

    public void submitHandler(View view) {

        // --- Step 1: Get all the views and their string values ---
        EditText firstNameText = findViewById(R.id.firstName);
        EditText lastNameText = findViewById(R.id.lastName);
        EditText emailText = findViewById(R.id.email);
        EditText passwordText = findViewById(R.id.password);
        EditText phoneNumberText = findViewById(R.id.phoneNumber);
        EditText fieldOfStudyText = findViewById(R.id.fieldOfStudy);
        TextView errorText = findViewById(R.id.errorText);

        firstName = firstNameText.getText().toString().trim(); // Use trim() to remove whitespace
        lastName = lastNameText.getText().toString().trim();
        email = emailText.getText().toString().trim();
        password = passwordText.getText().toString().trim();
        phoneNumber = phoneNumberText.getText().toString().trim();
        programOfStudy = fieldOfStudyText.getText().toString().trim();

        boolean isFormValid = true;

        // Validate First Name
        if (firstName.isEmpty()) {
            isFormValid = false;
            firstNameText.setError("Please enter a valid first name.");
        }
        // Validate Last Name
        if (lastName.isEmpty()) {
            isFormValid = false;
            lastNameText.setError("Please enter a valid last name.");
        }
        // Validate Email
        if (email.isEmpty()) {

            isFormValid = false;
            emailText.setError("Please enter a valid email.");
        } else if(!email.contains("@") || !email.contains(".")){
            isFormValid = false;
            emailText.setError("Please enter a valid email.");
        }

        // Validate Password
        if (password.isEmpty()) {
            passwordText.setError("Please enter a valid password.");
            isFormValid = false;
        } else if (password.length() < 6) {
            passwordText.setError("Password must be at least 6 characters.");
            isFormValid = false;
        }

        // Validate Phone Number
        if (phoneNumber.isEmpty()) {
           phoneNumberText.setError("Please enter a valid phone number.");
            isFormValid = false;
        } else if (!phoneNumber.matches("\\d+")) {
            phoneNumberText.setError("Please enter a valid phone number.");
            isFormValid = false;
        }

        // Validate Program of Study
        if (programOfStudy.isEmpty()) {
            fieldOfStudyText.setError("Please enter a valid program of study.");
            isFormValid = false;
        }

        // final validation result
        if (isFormValid) {

            errorText.setText("");


            // Uploading User to Firebase, new version
            Student student = new Student(firstName, lastName, email, password, phoneNumber, "Student", "pending", programOfStudy);
            db.collection("Students")
                    .add(student)
                    .addOnSuccessListener(documentRef -> {
                        String generatedId = documentRef.getId();
                        student.setDocumentId(generatedId);
                        documentRef.update("documentId", generatedId);
                    });





            String message = "Your registration is currently being reviewed.\nThank you for your patience";
            Intent intent = new Intent(this, Welcome_unapproved.class);
            intent.putExtra("message", message);
            startActivity(intent);

        } else {
            errorText.setText("Please fill in all highlighted fields");
        }
    }
}
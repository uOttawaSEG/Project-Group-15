package com.example.math_tutor_application;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

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
        setContentView(R.layout.activity_student);

        String docIDTut = "VNGdzKkDMu9TySAPnn2r";

        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        boolean requiresManualApproval;

        //pusing past date every month year 2022
        for (int i = 0; i < 10; i++) {
            requiresManualApproval = i % 2 == 0;
            calendarStart.set(Calendar.YEAR, 2022);
            calendarStart.set(Calendar.MONTH, i);
            calendarStart.set(Calendar.DAY_OF_MONTH, 10);
            calendarStart.set(Calendar.HOUR_OF_DAY, 14);
            calendarStart.set(Calendar.MINUTE, 0);
            calendarEnd.set(Calendar.YEAR, 2022);
            calendarEnd.set(Calendar.MONTH, i);
            calendarEnd.set(Calendar.DAY_OF_MONTH, 10);
            calendarEnd.set(Calendar.HOUR_OF_DAY, 15);
            calendarEnd.set(Calendar.MINUTE, 0);

            Sessions session = new Sessions(new Timestamp(calendarStart.getTime()), new Timestamp(calendarEnd.getTime()), requiresManualApproval);
            session.setApprovedTutorId(docIDTut);

            //pushing past dates
            // Inside your for loop...
/*            db.collection("ApprovedTutors")
                    .document(docIDTut)
                    .collection("sessionsArrayList")
                    .add(session)
                    .addOnSuccessListener(documentRef -> {

                        String generatedId = documentRef.getId();

                        session.setDocumentId(generatedId);
                        documentRef.update("documentId", generatedId);

                        RegisteredSessions registeredSessions = new RegisteredSessions(session);

                        db.collection("RegisteredSessions")
                                .document(generatedId)
                                .set(registeredSessions);

                    });*/

        }




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
            Intent intent = new Intent(this, Welcome_non_admin.class);
            intent.putExtra("message", message);
            startActivity(intent);

        } else {
            errorText.setText("Please fill in all highlighted fields");
        }
    }
}
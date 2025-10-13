package com.example.math_tutor_application;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String phoneNumber;
    protected String programOfStudy;

    // firebase
    private FirestoreHelper db;

    List<Student> studentList= new ArrayList<>(); //should be added to FireBase





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        // firebase
        db = new FirestoreHelper(); // create database "assistant"
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

        // --- Step 2: Validate each field and track overall validity ---
        boolean isFormValid = true;

        // Validate First Name
        if (firstName.isEmpty()) {
            firstNameText.setBackgroundColor(Color.RED); // Use Color.RED for simplicity
            isFormValid = false;
        } else {
            firstNameText.setBackgroundColor(Color.TRANSPARENT); // Or your default color
        }

        // Validate Last Name
        if (lastName.isEmpty()) {
            lastNameText.setBackgroundColor(Color.RED);
            isFormValid = false;
        } else {
            lastNameText.setBackgroundColor(Color.TRANSPARENT);
        }

        // Validate Email
        if (email.isEmpty()) {
            emailText.setBackgroundColor(Color.RED);
            isFormValid = false;
        } else {
            emailText.setBackgroundColor(Color.TRANSPARENT);
        }

        // Validate Password
        if (password.isEmpty()) {
            passwordText.setBackgroundColor(Color.RED);
            isFormValid = false;
        } else {
            passwordText.setBackgroundColor(Color.TRANSPARENT);
        }

        // Validate Phone Number
        if (phoneNumber.isEmpty()) {
            phoneNumberText.setBackgroundColor(Color.RED);
            isFormValid = false;
        } else {
            phoneNumberText.setBackgroundColor(Color.TRANSPARENT);
        }

        // Validate Program of Study
        if (programOfStudy.isEmpty()) {
            fieldOfStudyText.setBackgroundColor(Color.RED);
            isFormValid = false;
        } else {
            fieldOfStudyText.setBackgroundColor(Color.TRANSPARENT);
        }

        // --- Step 3: Act based on the final validation result ---
        if (isFormValid) {
            // All fields are valid, proceed with submission
            errorText.setText(""); // Clear any previous error message

            Student student = new Student(firstName, lastName, email, password, phoneNumber, programOfStudy);
            studentList.add(student);
            db.uploadStudent(student); // upload the student to firebase

            String message = "Welcome! You are registered as a Student";
            Intent intent = new Intent(this, Welcome.class);
            intent.putExtra("message", message);
            startActivity(intent);

        } else {
            // At least one field is invalid, show the error message
            errorText.setText("Please fill in all highlighted fields");
        }
    }
}
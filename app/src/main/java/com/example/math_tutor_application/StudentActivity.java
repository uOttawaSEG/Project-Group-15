package com.example.math_tutor_application;

import android.content.Intent;
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

        EditText firstNameText = findViewById(R.id.firstName);
        EditText lastNameText = findViewById(R.id.lastName);
        EditText emailText = findViewById(R.id.emal);
        EditText passwordText = findViewById(R.id.password);
        EditText phoneNumberText = findViewById(R.id.phoneNumber);
        EditText fieldOfStudyText = findViewById(R.id.fieldOfStudy);


         firstName = firstNameText.getText().toString();
         lastName = lastNameText.getText().toString();
         email = emailText.getText().toString();
         password = passwordText.getText().toString();
         phoneNumber = phoneNumberText.getText().toString();
         programOfStudy = fieldOfStudyText.getText().toString();

         if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || programOfStudy.isEmpty()) {
             TextView errorText = findViewById(R.id.errorText);
             errorText.setText("Please fill in all fields");
             return;
         } else {
             TextView errorText = findViewById(R.id.errorText);
             errorText.setText("");
         }


        Student student = new Student(firstName, lastName, email, password, phoneNumber, programOfStudy);
        studentList.add(student);
        db.uploadStudent(student); // upload the student to firebase

        String message = "Welcome! You are logged in as Student";

        Intent intent = new Intent(this, Welcome.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }
}
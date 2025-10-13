package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.res.ColorStateList;

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

    List<Student> studentList= new ArrayList<>(); //should be added to FireBase





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);


    }

    public void submitHandler(View view) {

        EditText firstNameText = findViewById(R.id.firstName);
        EditText lastNameText = findViewById(R.id.lastName);
        EditText emailText = findViewById(R.id.email);
        EditText passwordText = findViewById(R.id.password);
        EditText phoneNumberText = findViewById(R.id.phoneNumber);
        EditText fieldOfStudyText = findViewById(R.id.fieldOfStudy);

        String valFirstName = firstNameText.getText().toString().trim();
        String valLastName = lastNameText.getText().toString().trim();
        String valEmail = emailText.getText().toString().trim();
        String valPassword = passwordText.getText().toString().trim();
        String valPhoneNumber = phoneNumberText.getText().toString().trim();
        String valFieldofStudy = fieldOfStudyText.getText().toString().trim();


        // Storing these so it becomes easier to check for incomplete fields
        // by just running a small loop
        EditText[] fields = {firstNameText, lastNameText, emailText, passwordText, phoneNumberText, fieldOfStudyText};
        String[] values = {valFirstName, valLastName, valEmail, valPassword, valPhoneNumber, valFieldofStudy};


        // red = incomplete, grey = default (nothing wrong)
        int redColor = getColor(android.R.color.holo_red_light);
        int defaultColor = getColor(android.R.color.darker_gray);
        boolean incompleteFields = false;
        boolean invalidEmail = false;
        TextView errorText = findViewById(R.id.errorText);

        // Looping throuhg each element and highlighting the incomplete ones
        int mistakeCount = 0;
        for (int i = 0; i < fields.length; i++) {
            EditText f = fields[i];
            String v = values[i];
            f.setBackgroundTintList(android.content.res.ColorStateList.valueOf(defaultColor));
            if (v.isEmpty()) {
                f.setBackgroundTintList(android.content.res.ColorStateList.valueOf(redColor));
                mistakeCount++;
                incompleteFields = true;
            }
        }

        if (!valEmail.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(valEmail).matches()) {
            emailText.setBackgroundTintList(ColorStateList.valueOf(redColor));
            invalidEmail = true;
            mistakeCount++;
        }

        // Password length making sure, but only if the user entered something to begin with
        Boolean invalidPassword = false;
        if (!valPassword.isEmpty() && valPassword.length() < 6) {
            passwordText.setBackgroundTintList(ColorStateList.valueOf(redColor));
            invalidPassword = true;
            mistakeCount++;
        }

        if (mistakeCount > 1) {
            errorText.setText("Please fill in all the required fields correctly!");
            return;
        }
        else if (invalidEmail) {
            errorText.setText("Please enter a valid email address");
            return;
        } else if (invalidPassword) {
            errorText.setText("Password must be at least 6 charcters");
            return;
        } else if (incompleteFields) {
            errorText.setText("Please fill in all the required fields correctly!");
            return;
        } else {
             errorText.setText("");
         }

        this.firstName = valFirstName;
        this.lastName = valLastName;
        this.email = valEmail;
        this.password = valPassword;
        this.phoneNumber = valPhoneNumber;
        this.programOfStudy = valFieldofStudy;

        Student student = new Student(firstName, lastName, email, password, phoneNumber, programOfStudy);
        studentList.add(student);

        String message = "Welcome! You are logged in as Student";

        Intent intent = new Intent(this, Welcome.class);
        intent.putExtra("message", message);
        startActivity(intent);





    }
}
package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class TutorActivity extends AppCompatActivity {

    // Text boxes for user input
    EditText firstName, lastName, email, password, confirmPassword, phone, coursesOffered;

    // Spinner for degree selection
    Spinner degreeSpinner;

    // Sections that show up one by one
    LinearLayout lastNameSection, emailSection, passwordSection, phoneNumberSection, degreeSection, coursesOfferedSection;

    // Arrow icons next to each section
    ImageView arrowFirstName, arrowLastName, arrowEmail, arrowPassword, arrowPhone, arrowDegree, arrowCourses;

    // Buttons to submit the form or go back
    Button submitButton, goBackButton;

    // firebase "assistant"
    FirestoreHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Makes the app use the full screen
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutor);

        // firebase
        db = new FirestoreHelper(); // create the firebase "assistant"

        // Adjusts padding so content doesn't get hidden behind system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Connect layout items to code
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        phone = findViewById(R.id.phone);
        coursesOffered = findViewById(R.id.courses_offered);
        degreeSpinner = findViewById(R.id.degree_spinner);

        lastNameSection = findViewById(R.id.last_name_section);
        emailSection = findViewById(R.id.email_section);
        passwordSection = findViewById(R.id.password_section);
        phoneNumberSection = findViewById(R.id.phone_number_section);
        degreeSection = findViewById(R.id.degree_section);
        coursesOfferedSection = findViewById(R.id.courses_offered_section);

        arrowFirstName = findViewById(R.id.arrow_first_name);
        arrowLastName = findViewById(R.id.arrow_last_name);
        arrowEmail = findViewById(R.id.arrow_email);
        arrowPassword = findViewById(R.id.arrow_password);
        arrowPhone = findViewById(R.id.arrow_phone);
        arrowDegree = findViewById(R.id.arrow_degree);
        arrowCourses = findViewById(R.id.arrow_courses);

        submitButton = findViewById(R.id.submit_button);
        goBackButton = findViewById(R.id.go_back_button);

        // Populate the degree spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.degree_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degreeSpinner.setAdapter(adapter);

        // Reveal each section in order
        revealOnInput(firstName, lastNameSection, arrowFirstName);
        revealOnInput(lastName, emailSection, arrowLastName);
        revealOnInput(email, passwordSection, arrowEmail);
        revealOnInput(password, phoneNumberSection, arrowPassword);
        revealOnInput(phone, degreeSection, arrowPhone);

        // Reveal courses section after degree is selected
        degreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (coursesOfferedSection.getVisibility() != View.VISIBLE) {
                        coursesOfferedSection.setVisibility(View.VISIBLE);
                        coursesOfferedSection.setAlpha(0f);
                        coursesOfferedSection.animate().alpha(1f).setDuration(300).start();
                    }

                    if (arrowDegree.getVisibility() != View.VISIBLE) {
                        arrowDegree.setVisibility(View.VISIBLE);
                        arrowDegree.setAlpha(0f);
                        arrowDegree.animate().alpha(1f).setDuration(300).start();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Reveal submit button after courses are filled
        revealOnInput(coursesOffered, submitButton, arrowCourses);

        // When user clicks submit, check password and show message
        submitButton.setOnClickListener(v -> {

            if (validInputFields()) {
                // grab valid elements
                String firstName0 = firstName.getText().toString();
                String lastName0 = lastName.getText().toString();
                String email0 = email.getText().toString();
                String phoneNum = phone.getText().toString();
                String confirmedPassword = confirmPassword.getText().toString();
                String highestDegree = degreeSpinner.getSelectedItem().toString();
                ArrayList<String> courses = stringSplitter(coursesOffered.getText().toString());

                Toast.makeText(this, "Form submitted", Toast.LENGTH_SHORT).show();

                Tutor t = new Tutor(firstName0, lastName0, email0, phoneNum, confirmedPassword, highestDegree, courses);
                db.uploadTutor(t);

                String message = "Welcome! You are registered as a Tutor";
                Intent intent = new Intent(this, Welcome.class);
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });
    }

    /**
     * Shows the next section and arrow when the current field is filled in.
     */
    private void revealOnInput(EditText currentField, View nextSection, View arrowIcon) {
        currentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    if (nextSection.getVisibility() != View.VISIBLE) {
                        nextSection.setVisibility(View.VISIBLE);
                        nextSection.setAlpha(0f);
                        nextSection.animate().alpha(1f).setDuration(300).start();
                    }

                    if (arrowIcon.getVisibility() != View.VISIBLE) {
                        arrowIcon.setVisibility(View.VISIBLE);
                        arrowIcon.setAlpha(0f);
                        arrowIcon.animate().alpha(1f).setDuration(300).start();
                    }
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    /**
     * Checks if password is long enough and matches the confirmation.
     */
    private boolean isPasswordValid() {
        String pass = password.getText().toString();
        String confirm = confirmPassword.getText().toString();

        if (pass.length() < 6) {
            password.setError("Password must be at least 6 characters");
            return false;
        }

        if (!pass.equals(confirm)) {
            confirmPassword.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    // Optional: go back to sign-up screen
    public void signUPHandler(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    // input field validators
    public boolean validInputFields() {
        String firstName0 = firstName.getText().toString();
        String lastName0 = lastName.getText().toString();
        String courses = coursesOffered.getText().toString();

        if (firstName0.isEmpty()) {
            firstName.setError("Please enter a valid first name.");
            return false;
        }

        if (lastName0.isEmpty()) {
            lastName.setError("Please enter a valid last name.");
            return false;
        }

        if (courses.isEmpty()) {
            coursesOffered.setError("Please enter the courses you offer.");
            return false;
        }

        return isPasswordValid() && isEmailValid() && isPhoneValid();
    }

    public boolean isEmailValid() {
        String email0 = email.getText().toString();

        if (email0.contains("@") && email0.contains(".")) { return true; }

        email.setError("Please enter a valid email.");
        return false;
    }

    public boolean isPhoneValid() {
        String phoneNum = phone.getText().toString().trim();

        // if the phone field contains characters
        if (!phoneNum.matches("\\d+")) {
            phone.setError("Please enter a valid phone number PARSE ERROR.");
            return false;
        }

        // if the phone number is too long or short
        if (phoneNum.length() < 10 || phoneNum.length() > 15) {
            phone.setError("Please enter a valid phone number.");
            return false;
        }

        return true; // hooray
    }

    // to be implemented later (NOT D1)
    public boolean checkDupes() {

        return true;
    }

    // used for coursesOffered - String to ArrayList
    public static ArrayList<String> stringSplitter(String input) {
        String[] parts = input.split("\\s*,\\s*");
        Set<String> uniqueSet = new LinkedHashSet<>(Arrays.asList(parts));

        return new ArrayList<>(uniqueSet);
    }
}

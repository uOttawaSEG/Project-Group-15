package com.example.math_tutor_application;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * This activity displays a progressive registration form for tutors.
 * Each section of the form becomes visible only after the previous field is filled.
 */
public class TutorActivity extends AppCompatActivity {

    // Declare UI components
    EditText firstName, lastName, email; // Input fields
    LinearLayout lastNameSection, emailSection; // Containers for each section
    ImageView arrowFirstName, arrowLastName; // Arrows that appear when a section is completed
    Button submitButton; // Final submit button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge layout to allow content to extend behind system bars
        EdgeToEdge.enable(this);

        // Load the layout from XML file
        setContentView(R.layout.activity_tutor);

        // Apply padding to the root view so content doesn't overlap with system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Link Java variables to their corresponding views in the XML layout
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        //Ass the rest of them (phone number, courseOffered, password, highest degree)

        lastNameSection = findViewById(R.id.lastNameSection);
        emailSection = findViewById(R.id.emailSection);

        arrowFirstName = findViewById(R.id.arrowFirstName);
        arrowLastName = findViewById(R.id.arrowLastName);

        submitButton = findViewById(R.id.submitButton);
       // goBackButton = findViewById(R.id.submitButton);//forgot to add it to the activity_tutor.xml file

        // Add a listener to the First Name field
        // When the user types something, reveal the Last Name section and arrow
        firstName.addTextChangedListener(new SimpleWatcher(() -> {
            lastNameSection.setVisibility(View.VISIBLE);
            arrowFirstName.setVisibility(View.VISIBLE);
        }));

        // Add a listener to the Last Name field
        // When the user types something, reveal the Email section and arrow
        lastName.addTextChangedListener(new SimpleWatcher(() -> {
            emailSection.setVisibility(View.VISIBLE);
            arrowLastName.setVisibility(View.VISIBLE);
        }));

        // You can continue this pattern for additional fields like password, phone number, etc.
    }

    /**
     * A helper class that simplifies TextWatcher usage.
     * It runs a given action (Runnable) only when the text field is not empty.
     */
    private static class SimpleWatcher implements TextWatcher {
        Runnable onTextFilled; // Action to run when field is filled

        // Constructor takes a Runnable (a block of code to execute)
        SimpleWatcher(Runnable onTextFilled) {
            this.onTextFilled = onTextFilled;
        }

        // Called after the text has changed
        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().isEmpty()) {
                onTextFilled.run(); // Trigger the reveal logic
            }
        }

        // These two methods are required but unused in this case
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }
}

package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class LogInActivity extends AppCompatActivity {



    FirebaseFirestore db= FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }


    public void welcomeHandler(View view) {
        TextView errorText = findViewById(R.id.errorText);
        EditText emailText = findViewById(R.id.Email);
        EditText passwordText = findViewById(R.id.password);

        String email = emailText.getText().toString().trim().toLowerCase();
        String password = passwordText.getText().toString().trim();

        // Clear previous errors
        errorText.setText("");

        if (email.isEmpty() || password.isEmpty()) {
            errorText.setText("Please enter email and password.");
            return;
        }

        // 1. First, check if the user is an Admin in Firestore
        db.collection("User")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {

                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        User user = document.toObject(User.class); // Convert document to User object

                        if (user.getRole().equals("Admin")) {

                            String message = "Welcome! You are registered as an Administrator";
                            message += "\n\nWelcome, " + user.getFirstName() + " " + user.getLastName() + "!";

                            Intent intent = new Intent(LogInActivity.this, Welcome.class);
                            intent.putExtra("message", message);
                            startActivity(intent);

                        } else {

                            String message = "Welcome! You are registered as an ";
                            message += user.getRole();
                            message += "\n\nWelcome, " + user.getFirstName() + " " + user.getLastName() + "!\n";
                            message += "Your current registration status is " + user.getStatus();
                            message += "\n Please contact Administrator Micheal @ 647-888-9999 to inquire about your registration status";

                            Intent intent = new Intent(LogInActivity.this, Welcome_non_admin.class);
                            intent.putExtra("message", message);
                            startActivity(intent);

                        }

                    }
                });


        errorText.setText("An error occurred during login. Please try again.");
    }
    public void backToMainHandler(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    //adding methods linked to the firebase

}
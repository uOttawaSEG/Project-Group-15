package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.math_tutor_application.uml_classes.Student;
import com.example.math_tutor_application.uml_classes.Tutor;
import com.example.math_tutor_application.uml_classes.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogInActivity extends AppCompatActivity {



    FirebaseFirestore db= FirebaseFirestore.getInstance();
    String docID;


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

        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        // Clear previous errors
        errorText.setText("");

        if (email.isEmpty() || password.isEmpty()) {
            errorText.setText("Please enter email and password.");
            return;
        }


        // 1. First, check if the user is an Admin in Firestore
        db.collection("Admins")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {

                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        User user = document.toObject(User.class); // Convert document to User object
                        String message = "Welcome! You are registered as an Administrator";
                        message += "\n\nWelcome, " + user.getFirstName() + " " + user.getLastName() + "!";
                        Intent intent = new Intent(LogInActivity.this, Welcome.class);
                        intent.putExtra("message", message);
                        startActivity(intent);

                    } else {
                        db.collection("Students")
                                .whereEqualTo("email", email)
                                .whereEqualTo("password", password)
                                .limit(1)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful() && task2.getResult() != null && !task2.getResult().isEmpty()) {
                                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task2.getResult().getDocuments().get(0);
                                        Student student = document.toObject(Student.class); // Convert document to Student object
                                        String message = "Welcome! You are registered as an ";
                                        message += student.getRole();
                                        message += "\n\nWelcome, " + student.getFirstName() + " " + student.getLastName() + "!\n";
                                        message += "Your current registration status is " + student.getStatus();
                                        message += "\n Please contact Administrator Micheal @ 647-888-9999 to inquire about your registration status";

                                        Intent intent = new Intent(LogInActivity.this, Welcome_non_admin.class);
                                        intent.putExtra("message", message);
                                        startActivity(intent);
                                    } else {
                                        db.collection("Tutors")
                                                .whereEqualTo("email", email)
                                                .whereEqualTo("password", password)
                                                .limit(1)
                                                .get()
                                                .addOnCompleteListener(task3 -> {

                                                    if (task3.isSuccessful() && task3.getResult() != null && !task3.getResult().isEmpty()) {
                                                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task3.getResult().getDocuments().get(0);
                                                        Tutor tutor = document.toObject(Tutor.class); // Convert document to Tutor object
                                                        String message = "Welcome! You are registered as an ";
                                                        message += tutor.getRole();
                                                        message += "\n\nWelcome, " + tutor.getFirstName() + " " + tutor.getLastName() + "!\n";
                                                        if (tutor.getStatus().equals("approved")) {
                                                            Intent intent = new Intent(LogInActivity.this, Welcome.class);
                                                            intent.putExtra("message", message);
                                                            intent.putExtra("email", email);
                                                            intent.putExtra("password", password);
                                                            startActivity(intent);
                                                        //otherwise
                                                        } else {
                                                            message += "Your current registration status is " + tutor.getStatus();
                                                            message += "\n Please contact Administrator Micheal @ 647-888-9999 to inquire about your registration status";
                                                            Intent intent = new Intent(LogInActivity.this, Welcome_non_admin.class);
                                                            intent.putExtra("message", message);
                                                            startActivity(intent);
                                                        }


                                                    } else {
                                                        errorText.setText("Invalid username or password");
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }




    public void backToMainHandler(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    //adding methods linked to the firebase

}
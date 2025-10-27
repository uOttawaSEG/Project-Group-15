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

/*
    Student user1 = new Student("Bob", "Smith", "bob.smith@hotmail.com", "1111", "123-456-7890", "Computer Science");
    Tutor tutor1 = new Tutor("Alice", "Borderland", "aliceintheborderlands@gmail.com", "2222", "123-456-7890", "PHD", new ArrayList<String>(Arrays.asList("Math", "Science")));
    //Administrator admin1 = new Administrator("Micheal", "Jordan", "michealjordan@gmail.com", "3333", "647-888-9999");
*/

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    private TextView errorText;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        errorText = findViewById(R.id.errorText);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void welcomeHandler(View view) {
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

        checkRegistrationStatus(email, password);


    }

    private void checkRegistrationStatus(String email, String password) {
        db.collection("registration_requests")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String status = doc.getString("status");
                            String storedEmail = doc.getString("email");
                            String storedPassword = doc.getString("password");



                            switch (status) {
                                case "approved":
                                    String role = doc.getString("role");
                                    String firstName = doc.getString("firstName");
                                    String lastName = doc.getString("lastName");
                                    String phoneNumber = doc.getString("phoneNumber");

                                    String message = "Welcome! You are logged in as " + role + "\n\nWelcome, " + firstName + " " + lastName + "!";

                                    Intent intent = new Intent(LogInActivity.this, Welcome.class);
                                    intent.putExtra("message", message);
                                    startActivity(intent);
                                    break;

                                case "rejected":
                                    errorText.setText("Your registration request has been rejected. Please contact administration at 613-261-6154 if you need help with anything.");
                                    break;
                                case "pending":
                                    errorText.setText("Your registration request is currently being reviewed.");
                                    break;
                            }
                            return;
                        }
                    } else {

                        checkAdminLogin(email, password);
                    }

                });

    }

    private void checkAdminLogin(String email, String password) {
        db.collection("admins")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        Administrator admin = document.toObject(Administrator.class);
                        String message = "Welcome! You are registered as an Administrator" + "\n\nWelcome, " + admin.getFirstName() + " " + admin.getLastName() + "!";
                        Intent intent = new Intent(LogInActivity.this, Welcome.class);
                        intent.putExtra("message", message);
                        startActivity(intent);
                    } else {
                        errorText.setText("Invalid email or password");

                    }
                });
    }


    public void backToMainHandler(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    //adding methods linked to the firebase

}
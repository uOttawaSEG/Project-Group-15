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

    Student user1 = new Student("Bob", "Smith", "bob.smith@hotmail.com", "1111", "123-456-7890", "Computer Science");
    Tutor tutor1 = new Tutor("Alice", "Borderland", "aliceintheborderlands@gmail.com", "2222", "123-456-7890", "PHD", new ArrayList<String>(Arrays.asList("Math", "Science")));
    Administrator admin1 = new Administrator("Micheal", "Jordan", "michealjordan@gmail.com", "3333", "647-888-9999");

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


    public void welcomeHandler(View view){

        User loggedUser;
        TextView errorText = findViewById(R.id.errorText);
        errorText.setText("");
        EditText emailText = findViewById(R.id.email);
        EditText passwordText = findViewById(R.id.password);

        String email = emailText.getText().toString().trim().toLowerCase();
        String password = passwordText.getText().toString().trim();

        if (email.equals(admin1.getFirstName().toLowerCase()) && password.equals(admin1.getPassword())) {
            String message = "Welcome! You are registered as an Administrator";
            message += "\n\nWelcome, " + admin1.getFirstName() + " " + admin1.getLastName() + "!";

            Intent intent = new Intent(this, Welcome.class);
            intent.putExtra("message", message);
            startActivity(intent);
        } else {
            FirestoreHelper db = new FirestoreHelper();
            db.checkLogin("students", email, password,  this);
            db.checkLogin("tutors", email, password, this);
        }

    }

    public void backToMainHandler(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    //adding methods linked to the firebase

}
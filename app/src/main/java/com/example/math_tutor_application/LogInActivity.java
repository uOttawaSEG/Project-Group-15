package com.example.math_tutor_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
        EditText nameText = findViewById(R.id.name);
        EditText passwordText = findViewById(R.id.password);

        String name = nameText.getText().toString().trim().toLowerCase();
        String password = passwordText.getText().toString().trim();

        if (name.equals(user1.getFirstName().toLowerCase()) && password.equals(user1.getPassword())) {
            loggedUser = user1;
        } else if (name.equals(tutor1.getFirstName().toLowerCase()) && password.equals(tutor1.getPassword())) {
            loggedUser = tutor1;
        } else if (name.equals(admin1.getFirstName().toLowerCase()) && password.equals(admin1.getPassword())) {
            loggedUser = admin1;
        } else {
            errorText.setText("Invalid username or password");
            return;
        }

        String message = "Welcome! You are registered as a " + loggedUser.getClass().getSimpleName();
        message += "\n\nWelcome, " + loggedUser.getFirstName() + " " + loggedUser.getLastName() + "!";


        Intent intent = new Intent(this, Welcome.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }

    public void backToMainHandler(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    //adding methods linked to the firebase

}
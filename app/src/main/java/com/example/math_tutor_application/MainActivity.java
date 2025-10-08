package com.example.math_tutor_application;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    //creates a list of user. Should be on firebase later
    User user1 = new User("user1", "password1", "Alice", "student");
    User user2 = new User("user2", "password2", "Bob", "tutor");
    User user3 = new User("user3", "password3", "Charlie", "student");
    User user4 = new User("user4", "password4", "David", "tutor");

    //adds the users to the list
    static ArrayList<User> userList = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        
    }
}
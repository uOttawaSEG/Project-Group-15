package com.example.math_tutor_application;

// imports
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.content.Context;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.content.Intent;
import android.app.Activity;
import android.widget.TextView;


public class FirestoreHelper {
    private FirebaseFirestore db;

    public FirestoreHelper() { db = FirebaseFirestore.getInstance(); }

    public void uploadAdmin() {} // to be implemented upon creation of Administrator class





    //new version that will store the user in the database
    public void uploadUser(User user) {
        db.collection("User")
                .add(user)
                .addOnSuccessListener(documentRef -> {
                    String generatedId = documentRef.getId();
                    user.setDocumentId(generatedId);
                    documentRef.update("documentId", generatedId);
                });

    }



    //Helper function for login activity
    public void checkLogin(String collectionName, String email, String password, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collectionName)
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String firstName = doc.getString("firstName");
                            String lastName = doc.getString("lastName");

                            String message = "Welcome back " + firstName + " " + lastName;

                            Intent intent = new Intent(context, Welcome.class);
                            intent.putExtra("message", message);
                            context.startActivity(intent);
                            return;
                        }
                    } else {
                        if (context instanceof Activity) {
                            TextView errorText = ((Activity) context).findViewById(R.id.errorText);
                            errorText.setText("Invalid username or password");
                        }
                    }
                });
    }



}

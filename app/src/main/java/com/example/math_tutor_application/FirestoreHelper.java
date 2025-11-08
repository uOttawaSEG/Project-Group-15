package com.example.math_tutor_application;

// imports

import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Context;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.content.Intent;
import android.app.Activity;
import android.widget.TextView;


public class FirestoreHelper {
    private FirebaseFirestore db;

    public FirestoreHelper() { db = FirebaseFirestore.getInstance(); }



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

    public void uploadStudent(Student student) {
        db.collection("Students")
                .add(student)
                .addOnSuccessListener(documentRef -> {
                    String generatedId = documentRef.getId();
                    student.setDocumentId(generatedId);
                    documentRef.update("documentId", generatedId);
                });

    }

    public void uploadTutor(Tutor tutor) {
        db.collection("Tutors")
                .add(tutor)
                .addOnSuccessListener(documentRef -> {
                    String generatedId = documentRef.getId();
                    tutor.setDocumentId(generatedId);
                    documentRef.update("documentId", generatedId);
                });

    }

    public void uploadAdmin(User tutor) {
        db.collection("Admins")
                .add(tutor)
                .addOnSuccessListener(documentRef -> {
                    String generatedId = documentRef.getId();
                    tutor.setDocumentId(generatedId);
                    documentRef.update("documentId", generatedId);
                });

    }

    public void uploadApprovedTutor(ApprovedTutor approvedTutor) {
        db.collection("ApprovedTutors")
                .add(approvedTutor)
                .addOnSuccessListener(documentRef -> {
                    String generatedId = documentRef.getId();
                    approvedTutor.setDocumentId(generatedId);
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

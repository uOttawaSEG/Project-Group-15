package com.example.math_tutor_application;

// imports
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class FirestoreHelper {
    private FirebaseFirestore db;

    public FirestoreHelper() { db = FirebaseFirestore.getInstance(); }

    // upload methods for firebase\
    public void uploadStudent(Student s) {
        db.collection("students")
                .document().set(s)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "Student successfully uploaded.");

                        // success handling
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Failed to upload student.");

                        // failure handling
                    }
                });
    }

    public void uploadTutor(Tutor t) {
        db.collection("tutors")
                .document().set(t)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "Tutor successfully uploaded.");

                        // success handling
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Failed to upload tutor.");

                        // failure handling
                    }
                });
    }

    public void uploadAdmin() {} // to be implemented upon creation of Administrator class
}

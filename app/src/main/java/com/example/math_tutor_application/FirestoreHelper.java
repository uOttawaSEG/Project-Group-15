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
                .document(s.getId()).set(s)
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
                .document(t.getId()).set(t)
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

    public void uploadRegistrationRequest(RegistrationRequest request) {
        // Access the "registration_requests" collection in Firestore
        db.collection("registration_requests")

                // Add the request object as a new document (Firestore auto-generates the document ID)
                .add(request)

                // If the upload is successful, log a confirmation message
                .addOnSuccessListener(unused -> Log.d("Firestore", "Registration request saved"))

                // If the upload fails, log an error message with the exception
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to save registration request", e));
    }

}

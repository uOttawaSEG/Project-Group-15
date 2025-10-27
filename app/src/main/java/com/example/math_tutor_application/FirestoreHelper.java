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

import java.util.HashMap;
import java.util.Map;


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

    public void updateRequestStatus(String docId, String newStatus, OnSuccessListener<Void> ok, OnFailureListener fail) {
        db.collection("registration_requests")
                .document(docId)
                .update("status", newStatus)
                .addOnSuccessListener(ok)
                .addOnFailureListener(fail);
    }

    public void approveRequestAndCreateUser(String docId, RegistrationRequest req, OnSuccessListener<Void> ok, OnFailureListener fail) {


        String role = req.getRole() == null ? "": req.getRole().toLowerCase();
        String target = role.equals("student") ? "students": "tutors";

        Map<String, Object> user = new HashMap<>();
        user.put("firstName", req.getFirstName());
        user.put("lastName", req.getLastName());
        user.put("email", req.getEmail());
        user.put("phoneNumber", req.getPhoneNumber());
        user.put("isapproved", true);

        if ("students".equals(target)){
            user.put("programOfStudy", req.getProgramOfStudy());
        } else{
            user.put("coursesOffered", req.getCoursesOffered());
        }

        db.collection(target).document()
                .set(user)
                .addOnSuccessListener(v ->
                        db.collection("registration_requests")
                                .document(docId)
                                .update("status", "approved")
                                .addOnSuccessListener(ok)
                                .addOnFailureListener(fail)
                )
                .addOnFailureListener(fail);




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

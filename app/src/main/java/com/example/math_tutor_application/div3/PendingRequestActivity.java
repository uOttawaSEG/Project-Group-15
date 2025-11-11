package com.example.math_tutor_application.div3;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.uml_classes.ApprovedTutor;
import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.example.math_tutor_application.uml_classes.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PendingRequestActivity extends AppCompatActivity {
    //uses recycle view -> Session Request Adapter

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private SessionRequestAdapter adapter;
    private List<RegisteredSessions> requestList = new ArrayList<>();
    private ApprovedTutor approvedTutor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request);

        //ApprovedTutor info - passed on from dashboard no need for firebase
        String approvedTutorDocId = getIntent().getStringExtra("docID");
        String email = getIntent().getStringExtra("email");
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        approvedTutor = new ApprovedTutor();
        approvedTutor.setDocumentId(approvedTutorDocId);
        approvedTutor.setEmail(email);
        approvedTutor.setFirstName(firstName);
        approvedTutor.setLastName(lastName);


        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SessionRequestAdapter(requestList, new SessionRequestAdapter.OnRequestActionListener() {
            @Override
            public void onApprove(RegisteredSessions request) {

                if (!request.getstatus().equals("approved")) {
                    request.setStatus("approved");
                    db.collection("RegisteredSessions").document(request.getDocumentId())
                            .update("status", "approved")
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(PendingRequestActivity.this, "Approved!", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            });
                } else {
                    request.setStatus("pending");
                    db.collection("RegisteredSessions").document(request.getDocumentId())
                            .update("status", "pending")
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(PendingRequestActivity.this, "Pending!", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            });




                }





            }

            @Override
            public void onReject(RegisteredSessions request) {
                if (!request.getstatus().equals("rejected")) {
                    request.setStatus("rejected");
                    db.collection("RegisteredSessions").document(request.getDocumentId())
                            .update("status", "rejected")
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(PendingRequestActivity.this, "Rejected!", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            });
                } else {
                    request.setStatus("pending");
                    db.collection("RegisteredSessions").document(request.getDocumentId())
                            .update("status", "pending")
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(PendingRequestActivity.this, "Pending!", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            });

                }

            }

            @Override
            public void onDisplay(Student student) {
                String studentInfo =
                        "Name: " + (student.getFirstName()) + " " + (student.getLastName()) + "\n" +
                                "Email: " + (student.getEmail()) + "\n" +
                                "Phone: " + (student.getPhoneNumber()) + "\n" +
                                "Program: " + (student.getProgramOfStudy());

                new AlertDialog.Builder(PendingRequestActivity.this)
                        .setTitle("Student Information")
                        .setMessage(studentInfo)
                        .setPositiveButton("OK", null)
                        .show();

            }

        });

        recyclerView.setAdapter(adapter);
        fetchSessionRequests();
    }

    private void fetchSessionRequests() {

        Log.d("Firestore", "Fetching pending sessions...");
        db.collection("RegisteredSessions")
                .whereEqualTo("approvedTutorId", approvedTutor.getDocumentId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        requestList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Log.d("Firestore", "Document: " + doc.getData().toString());
                            RegisteredSessions request = doc.toObject(RegisteredSessions.class);
                            request.setDocumentId(doc.getId());
                            request.setApprovedTutor(approvedTutor);

                            //fetch student info
                            if(request.getApprovedStudentID() == null) continue;
                            String studentId = request.getApprovedStudentID();
                            db.collection("Students").document(studentId).get().addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    Student student = task2.getResult().toObject(Student.class);
                                    request.setStudent(student);
                                    requestList.add(request);
                                    adapter.notifyDataSetChanged();

                                }
                            });

                        }
                    } else {
                        Log.e("Firestore", "Error fetching documents", task.getException());
                        Toast.makeText(this, "Failed to load requests", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(task3 -> {
                    //Tells the user if the list is empty so it doesn't look like a bug
                    if (requestList.isEmpty()) {
                        Toast.makeText(this, "No pending requests", Toast.LENGTH_SHORT).show();
                    }

                });
    }

}

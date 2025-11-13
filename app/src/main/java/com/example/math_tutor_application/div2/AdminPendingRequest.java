package com.example.math_tutor_application.div2;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.ApprovedStudent;
import com.example.math_tutor_application.uml_classes.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;


public class AdminPendingRequest extends AppCompatActivity {



    private FirebaseFirestore db;


    private RecyclerView recyclerView;

    private PendingRequestAdaptor adapter;


    private ArrayList<Student> pendingRequests = new ArrayList<>();








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.div2_admin_pending_student);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PendingRequestAdaptor(pendingRequests, new PendingRequestAdaptor.OnRequestActionListener() {
            @Override
            public void onApprove(Student request) {

                request.setStatus("approved");
                db.collection("Students")
                        .document(request.getDocumentId())
                        .set(request).addOnSuccessListener(aVoid -> {
                            ApprovedStudent approvedStudent = new ApprovedStudent(request);
                            approvedStudent.setDocumentId(request.getDocumentId());
                            db.collection("ApprovedStudents").document(approvedStudent.getDocumentId()).set(approvedStudent);
                            pendingRequests.remove(request);
                            adapter.notifyDataSetChanged();
                        });

                sendSMS(request.getPhoneNumber(), true);
            }

            @Override
            public void onReject(Student request) {

                request.setStatus("rejected");
                db.collection("Students")
                        .document(request.getDocumentId())
                        .set(request).addOnSuccessListener(aVoid -> {
                            pendingRequests.remove(request);
                            adapter.notifyDataSetChanged();
                        });

                sendSMS(request.getPhoneNumber(), true);
            }
            @Override
            public void onDisplay(Student request) {
                String studentInfo =
                        "Name: " + (request.getFirstName()) + " " + (request.getLastName()) + "\n" +
                                "Email: " + (request.getEmail()) + "\n" +
                                "Phone: " + (request.getPhoneNumber()) + "\n" +
                                "Program: " + (request.getProgramOfStudy());

                new AlertDialog.Builder(AdminPendingRequest.this)
                        .setTitle("Student Information")
                        .setMessage(studentInfo)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);

        db.collection("Students")
                .whereEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        pendingRequests.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Student student = document.toObject(Student.class);
                            student.setDocumentId(document.getId());
                            pendingRequests.add(student);
                        }
                        adapter.notifyDataSetChanged();

                    }
                });
    }

    public void sendSMS(String phoneNumber, boolean approved) {
        if (ContextCompat.checkSelfPermission(AdminPendingRequest.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();

            String msg;

            if (approved) { msg = "Your registration request has been approved"; }
            else { msg = "Your registration request has been denied"; }

            smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
            Toast.makeText(this, "SMS Sent!", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(AdminPendingRequest.this, new String[]{Manifest.permission.SEND_SMS}, 100);
        }
    }
}
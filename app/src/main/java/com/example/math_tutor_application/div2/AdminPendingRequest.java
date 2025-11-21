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
import com.example.math_tutor_application.uml_classes.Tutor;
import com.example.math_tutor_application.uml_classes.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;


public class AdminPendingRequest extends AppCompatActivity {



    private FirebaseFirestore db;


    private RecyclerView recyclerView;

    private PendingRequestAdaptor adapter;




    private ArrayList<User> pendingRequestsUser = new ArrayList<>();









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.div2_admin_pending_student);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PendingRequestAdaptor(pendingRequestsUser, new PendingRequestAdaptor.OnRequestActionListener() {
            @Override
            public void onApprove(User request) {

                if (request instanceof Student) {
                    Student student = (Student) request;
                    student.setDocumentId(request.getDocumentId());
                request.setStatus("approved");
                db.collection("Students")
                        .document(student.getDocumentId())
                        .set(student).addOnSuccessListener(aVoid -> {
                            ApprovedStudent approvedStudent = new ApprovedStudent(student);
                            approvedStudent.setDocumentId(student.getDocumentId());
                            db.collection("ApprovedStudents").document(approvedStudent.getDocumentId()).set(approvedStudent);
                            pendingRequestsUser.remove(request);
                            adapter.notifyDataSetChanged();
                        });
                }


                sendSMS(request.getPhoneNumber(), true);
            }

            @Override
            public void onReject(User request) {

                request.setStatus("rejected");
                db.collection("Students")
                        .document(request.getDocumentId())
                        .set(request).addOnSuccessListener(aVoid -> {
                            pendingRequestsUser.remove(request);
                            adapter.notifyDataSetChanged();
                        });

                sendSMS(request.getPhoneNumber(), true);
            }
            @Override
            public void onDisplay(User request) {

                if (request instanceof Student) {
                    Student student = (Student) request;
                    student.setDocumentId(request.getDocumentId());
                String studentInfo =
                        "Name: " + (request.getFirstName()) + " " + (request.getLastName()) + "\n" +
                                "Email: " + (request.getEmail()) + "\n" +
                                "Phone: " + (request.getPhoneNumber()) + "\n" +
                                "Program: " + (student.getProgramOfStudy());

                new AlertDialog.Builder(AdminPendingRequest.this)
                        .setTitle("Student Information")
                        .setMessage(studentInfo)
                        .setPositiveButton("OK", null)
                        .show();
                }


            }
        });

        recyclerView.setAdapter(adapter);

        db.collection("Students")
                .whereEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        pendingRequestsUser.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Student student = document.toObject(Student.class);
                            student.setDocumentId(document.getId());
                            pendingRequestsUser.add(student);
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
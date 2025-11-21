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
import com.example.math_tutor_application.uml_classes.ApprovedTutor;
import com.example.math_tutor_application.uml_classes.Student;
import com.example.math_tutor_application.uml_classes.Tutor;
import com.example.math_tutor_application.uml_classes.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


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
                } else if (request instanceof Tutor) {
                    Tutor tutor = (Tutor) request;
                    tutor.setDocumentId(request.getDocumentId());
                    request.setStatus("approved");
                    db.collection("Tutors")
                            .document(tutor.getDocumentId())
                            .set(tutor).addOnSuccessListener(aVoid -> {
                                ApprovedTutor approvedTutor = new ApprovedTutor(tutor);
                                approvedTutor.setDocumentId(tutor.getDocumentId());
                                db.collection("ApprovedTutors").document(approvedTutor.getDocumentId()).set(approvedTutor);
                                pendingRequestsUser.remove(request);
                                adapter.notifyDataSetChanged();
                            });

                }


                sendSMS(request.getPhoneNumber(), true);
            }

            @Override
            public void onReject(User request) {

                if (request instanceof Student) {
                    Student student = (Student) request;
                    student.setDocumentId(request.getDocumentId());
                    request.setStatus("rejected");
                    db.collection("Students")
                            .document(request.getDocumentId())
                            .set(student).addOnSuccessListener(aVoid -> {
                                pendingRequestsUser.remove(request);
                                adapter.notifyDataSetChanged();
                            });
                } else if (request instanceof Tutor) {
                    Tutor tutor = (Tutor) request;
                    tutor.setDocumentId(request.getDocumentId());
                    request.setStatus("rejected");
                    db.collection("Tutors")
                            .document(request.getDocumentId())
                            .set(tutor).addOnSuccessListener(aVoid -> {
                                pendingRequestsUser.remove(request);
                                adapter.notifyDataSetChanged();
                            });

                }

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
                } else if (request instanceof Tutor) {
                    Tutor tutor = (Tutor) request;
                    tutor.setDocumentId(request.getDocumentId());
                    String tutorInfo =
                            "Name: " + (request.getFirstName()) + " " + (request.getLastName()) + "\n" +
                                    "Email: " + (request.getEmail()) + "\n" +
                                    "Phone: " + (request.getPhoneNumber()) + "\n" +
                                    "Courses: " + (tutor.getCoursesOffered().toString());
                    new AlertDialog.Builder(AdminPendingRequest.this)
                            .setTitle("Tutor Information")
                            .setMessage(tutorInfo)
                            .setPositiveButton("OK", null)
                            .show();


                }


            }
        });

        recyclerView.setAdapter(adapter);




        Task<QuerySnapshot> studentsTask = db.collection("Students").whereEqualTo("status", "pending").get();
        Task<QuerySnapshot> tutorsTask = db.collection("Tutors").whereEqualTo("status", "pending").get();


        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(studentsTask, tutorsTask);

        allTasks.addOnSuccessListener(results -> {
            QuerySnapshot studentSnapshots = results.get(0);
            QuerySnapshot tutorSnapshots = results.get(1);

            pendingRequestsUser.clear();

            for (QueryDocumentSnapshot document : studentSnapshots) {
                Student student = document.toObject(Student.class);
                student.setDocumentId(document.getId());
                pendingRequestsUser.add(student);
            }

            for (QueryDocumentSnapshot document : tutorSnapshots) {
                Tutor tutor = document.toObject(Tutor.class);
                tutor.setDocumentId(document.getId());
                pendingRequestsUser.add(tutor);
            }

            adapter.notifyDataSetChanged();
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
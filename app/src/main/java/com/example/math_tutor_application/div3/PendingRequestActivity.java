package com.example.math_tutor_application.div3;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.uml_classes.ApprovedStudent;
import com.example.math_tutor_application.uml_classes.ApprovedTutor;
import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.Notification;
import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.example.math_tutor_application.uml_classes.Student;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
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

    String approvedTutorDocId, email, firstName, lastName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.div3_pending_request);

        //ApprovedTutor info - passed on from dashboard no need for firebase
         approvedTutorDocId = getIntent().getStringExtra("docID");
         email = getIntent().getStringExtra("email");
         firstName = getIntent().getStringExtra("firstName");
         lastName = getIntent().getStringExtra("lastName");
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

                if (!request.getStatus().equals("approved")) {
                    request.setStatus("approved");
                    db.collection("RegisteredSessions").document(request.getDocumentId())
                            .update("status", "approved")
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(PendingRequestActivity.this, "Approved!", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();

                                //send notification to student
                                Student student = request.getStudent();
                                String message = "Your session request has been approved for " + request.getCourse() + " by " + firstName + " " + lastName;
                                Notification notification = new Notification();
                                notification.setMsg(message);
                                notification.setReceiver(student.getDocumentId());
                                notification.setSender(approvedTutor.getDocumentId());
                                notification.setTimestamp(Timestamp.now());
                                db.collection("Notifications").add(notification);




                            });
                }





            }

            @Override
            public void onReject(RegisteredSessions request) {
                if (!request.getStatus().equals("rejected")) {
                    request.setStatus("rejected");
                    db.collection("RegisteredSessions").document(request.getDocumentId())
                            .update("status", "rejected")
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(PendingRequestActivity.this, "Rejected!", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();

                                //send notification to student
                                Student student = request.getStudent();
                                String message = "Your session request has been rejected for " + request.getCourse() + " by " + firstName + " " + lastName;
                                Notification notification = new Notification();
                                notification.setMsg(message);
                                notification.setReceiver(student.getDocumentId());
                                notification.setSender(approvedTutor.getDocumentId());
                                notification.setTimestamp(Timestamp.now());
                                db.collection("Notifications").add(notification);


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

            @Override
            public void onCancel(RegisteredSessions request) {
                cancel(request, request.getStatus(), firstName, lastName);

            }

        });

        recyclerView.setAdapter(adapter);
        fetchSessionRequests();
    }

    private void fetchSessionRequests() {

        //better and more robust way using Task to handle both student and session data

        db.collection("RegisteredSessions")
                .whereEqualTo("approvedTutorId", approvedTutor.getDocumentId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        requestList.clear();
                        List<Task<Student>> studentTasks = new ArrayList<>();
                        List<RegisteredSessions> sessionsFromDB = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            RegisteredSessions request = doc.toObject(RegisteredSessions.class);
                            request.setDocumentId(doc.getId());
                            request.setApprovedTutor(approvedTutor);
                            sessionsFromDB.add(request);

                            if (request.getApprovedStudentID() != null) {
                                Task<Student> studentTask = db.collection("Students")
                                        .document(request.getApprovedStudentID())
                                        .get()
                                        .continueWith(studentResultTask -> studentResultTask.getResult().toObject(Student.class));
                                studentTasks.add(studentTask);
                            }
                        }

                        if (sessionsFromDB.isEmpty()) {
                            Toast.makeText(this, "No pending requests", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            return;
                        }

                        Tasks.whenAllSuccess(studentTasks).addOnSuccessListener(students -> {

                            for (int i = 0; i < students.size(); i++) {
                                RegisteredSessions session = sessionsFromDB.get(i);
                                Student student = (Student) students.get(i);
                                if (student != null) {
                                    session.setStudent(student);
                                    requestList.add(session);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        });

                    } else {
                        Toast.makeText(this, "Failed to load requests", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void cancel(RegisteredSessions request, String msgStatus, String firstName, String lastName) {
        request.setStatus("pending");
        db.collection("RegisteredSessions").document(request.getDocumentId())
                .update("status", "pending")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(PendingRequestActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();

                    //send notification to student
                    Student student = request.getStudent();
                    String message = "Your session request has been cancelled to Pending from " + msgStatus + " for " + request.getCourse() + " by " + firstName + " " + lastName;
                    Notification notification = new Notification();
                    notification.setMsg(message);
                    notification.setReceiver(student.getDocumentId());
                    notification.setSender(approvedTutor.getDocumentId());
                    notification.setTimestamp(Timestamp.now());
                    db.collection("Notifications").add(notification);


                });

    }

}

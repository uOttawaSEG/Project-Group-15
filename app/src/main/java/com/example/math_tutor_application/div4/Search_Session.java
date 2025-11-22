package com.example.math_tutor_application.div4;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.ApprovedStudent;
import com.example.math_tutor_application.uml_classes.ApprovedTutor;
import com.example.math_tutor_application.uml_classes.Notification;
import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.example.math_tutor_application.uml_classes.Sessions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.*;

public class Search_Session extends AppCompatActivity {

    String studentDocId;



    ArrayList<Sessions> sessions = new ArrayList<>();
    ArrayList<String> courses = new ArrayList<>();

    ArrayList<Sessions> sessionsArrayList = new ArrayList<>();

    ArrayList<RegisteredSessions> registeredSessions = new ArrayList<>();


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Set<String> uniqueCourses = new HashSet<>();
    ArrayAdapter<String> adapter;
    private SearchSessionAdaptor adapter2;
    private Spinner courseSpinner;
    private RecyclerView recyclerView;

    ApprovedStudent approvedStudent;
    String selectedCourse;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.div4_search_session);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        studentDocId = getIntent().getStringExtra("docId");






        courseSpinner = findViewById(R.id.courseSpinner);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                courses
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);


        //sets up recycle view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter2 = new SearchSessionAdaptor(sessions, new SearchSessionAdaptor.OnRequestActionListener() {
            @Override
            public void onApprove(Sessions request) {

                db.collection("RegisteredSessions").whereEqualTo("approvedStudentID", studentDocId)
                        .get().addOnCompleteListener(registeredSessionsTask -> {

                            if (!registeredSessionsTask.isSuccessful() || registeredSessionsTask.getResult() == null) {
                                Toast.makeText(Search_Session.this, "Error: Could not verify student sessions.", Toast.LENGTH_SHORT).show();

                            }

                            for (QueryDocumentSnapshot doc : registeredSessionsTask.getResult()) {
                                RegisteredSessions r = doc.toObject(RegisteredSessions.class);
                                r.setDocumentId(doc.getId());
                                registeredSessions.add(r);
                            }


                            db.collection("ApprovedStudents").document(studentDocId).get().addOnCompleteListener(studentTask -> {


                                if (!studentTask.isSuccessful() || studentTask.getResult() == null) {
                                    Toast.makeText(Search_Session.this, "Error: Could not verify student profile.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                approvedStudent = studentTask.getResult().toObject(ApprovedStudent.class);

                                // Check if the session is already booked
                                if (request.getIsStudentRegister()) {
                                    Toast.makeText(Search_Session.this, "Already Registered!", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (!timeConflict(request)) {
                                    return;
                                }


                                request.setIsStudentRegister(true);
                                db.collection("Sessions").document(request.getDocumentId()).set(request);
                                adapter2.notifyItemChanged(sessions.indexOf(request));
                                RegisteredSessions registeredSessions = new RegisteredSessions(request);
                                registeredSessions.setApprovedStudentID(studentDocId);
                                registeredSessions.setCourse(selectedCourse);


                                String message;
                                if (registeredSessions.getManualApproval()) {
                                    registeredSessions.setStatus("approved");
                                    Toast.makeText(Search_Session.this, "Registered! Automatic Approval", Toast.LENGTH_SHORT).show();
                                    message = "Student " + approvedStudent.getFirstName() + " " + approvedStudent.getLastName() + " has registered for your session and has been approved automatically";
                                } else {
                                    registeredSessions.setStatus("pending");
                                    Toast.makeText(Search_Session.this, "Registered! Waiting for approval", Toast.LENGTH_SHORT).show();
                                    message = "Student " + approvedStudent.getFirstName() + " " + approvedStudent.getLastName() + " has registered for your session for " + selectedCourse;
                                }

                                db.collection("RegisteredSessions").document(registeredSessions.getDocumentId()).set(registeredSessions);


                                Notification notification = new Notification();
                                notification.setMsg(message);
                                notification.setReceiver(request.getApprovedTutor().getDocumentId());
                                notification.setSender(studentDocId);
                                notification.setTimestamp(new Timestamp(new Date()));
                                db.collection("Notifications").add(notification);


                            });


                        });




            }


            @Override
            public void onDisplay(ApprovedTutor tutor) {
                StringBuilder ratingStars = new StringBuilder();
                int rating = (int) tutor.getRating();
                for (int i = 0; i < rating; i++) {
                    ratingStars.append("⭐");
                }


                String studentInfo =
                        "Name: " + (tutor.getFirstName()) + " " + (tutor.getLastName()) + "\n" +
                                "Email: " + (tutor.getEmail()) + "\n" +
                                "Phone: " + (tutor.getPhoneNumber()) + "\n" +
                                "Course Offered: " + (tutor.getCoursesOffered().toString())  + "\n"
                                + "Ratings: " + ("" + tutor.getRating() + ratingStars) + "\n"
                                + "Number of Ratings: " + (tutor.getNumberOfRating());


                new AlertDialog.Builder(Search_Session.this)
                        .setTitle("Tutor Information")
                        .setMessage(studentInfo)
                        .setPositiveButton("OK", null)
                        .show();

            }

        });

        recyclerView.setAdapter(adapter2);







        //fetches all the courses for the spinner
        db.collection("ApprovedTutors").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful() || task.getResult() == null) {
                Toast.makeText(Search_Session.this, "Failed to fetch courses", Toast.LENGTH_SHORT).show();
                return;
            }

            for (QueryDocumentSnapshot doc : task.getResult()) {
                ApprovedTutor t = doc.toObject(ApprovedTutor.class);
                t.setDocumentId(doc.getId());
                uniqueCourses.addAll(t.getCoursesOffered());

            }
            courses.clear();
            courses.add("Select Course");
            courses.addAll(uniqueCourses);
            adapter.notifyDataSetChanged();

        });


        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourse = parent.getItemAtPosition(position).toString();

                if (!selectedCourse.equals("Select Course")) {
                    Toast.makeText(Search_Session.this, "Selected Course: " + selectedCourse, Toast.LENGTH_SHORT).show();

                }

                fetchAndDisplay(selectedCourse);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void fetchAndDisplay(String course) {

        db.collection("Sessions").orderBy("startDate", Query.Direction.DESCENDING   )
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) {
                        return;
                    }

                    sessions.clear();

                    List<Task<DocumentSnapshot>> tutorTasks = new ArrayList<>();
                    List<Sessions> sessionsFromDB = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Sessions s = doc.toObject(Sessions.class);
                        s.setDocumentId(doc.getId());
                        sessionsFromDB.add(s);

                        if (s.getApprovedTutorId() != null) {
                            tutorTasks.add(db.collection("ApprovedTutors").document(s.getApprovedTutorId()).get());
                        }
                    }

                    Tasks.whenAllSuccess(tutorTasks).addOnSuccessListener(results -> {
                        for (int i = 0; i < results.size(); i++) {
                            DocumentSnapshot tutorSnapshot = (DocumentSnapshot) results.get(i);
                            if (tutorSnapshot.exists()) {
                                ApprovedTutor t = tutorSnapshot.toObject(ApprovedTutor.class);
                                for (Sessions session : sessionsFromDB) {
                                    if (tutorSnapshot.getId().equals(session.getApprovedTutorId())) {
                                        session.setApprovedTutor(t);
                                    }
                                }
                            }
                        }
                        sessionsArrayList.clear();
                        filterSessions(course, sessionsFromDB);
                        adapter2.notifyDataSetChanged();
                    });
                });
    }





    public void filterSessions(String course,  List<Sessions> unfilteredSessions) {

        sessions.clear();

        for (Sessions session : unfilteredSessions) {
            if (session.getApprovedTutor() != null && session.getApprovedTutor().getCoursesOffered().contains(course)) {
                sessions.add(session);
            }



        }

    }

    public boolean timeConflict(Sessions request) {

        boolean checkOverlap = true;

        Timestamp calendarStart = request.getStartDate();
        Timestamp calendarEnd = request.getEndDate();


        for (Sessions s : registeredSessions) {

            //no overlap start1 <= end2 and start2 <= end1
            if (calendarStart.toDate().before(s.getEndDate().toDate()) && calendarEnd.toDate().after(s.getStartDate().toDate())) {

                //no identical sessions
                if (calendarStart.toDate().equals(s.getStartDate().toDate()) || calendarEnd.toDate().equals(s.getEndDate().toDate())) {
                    checkOverlap = false;
                    Toast.makeText(Search_Session.this, "Time Conflict! with tutor: " + s.getApprovedTutor().getFirstName(), Toast.LENGTH_SHORT).show();
                    break;
                }


            }
        }

        return checkOverlap;
    }

}
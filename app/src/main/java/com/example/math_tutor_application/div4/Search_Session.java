package com.example.math_tutor_application.div4;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.ApprovedTutor;
import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.example.math_tutor_application.uml_classes.Sessions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.*;

public class Search_Session extends AppCompatActivity {

    String studentDocId;



    ArrayList<Sessions> sessions = new ArrayList<>();
    ArrayList<String> courses = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Set<String> uniqueCourses = new HashSet<>();
    ArrayAdapter<String> adapter;
    private SearchSessionAdaptor adapter2;
    private Spinner courseSpinner;
    private RecyclerView recyclerView;



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

                request.setStudentRegister(true);

                db.collection("Sessions").document(request.getDocumentId())
                        .update("studentRegister", true).addOnCompleteListener(
                                task -> {
                                    adapter2.notifyDataSetChanged();
                                });

                RegisteredSessions registeredSessions = new RegisteredSessions(request);

                registeredSessions.setApprovedStudentID(studentDocId);

                db.collection("RegisteredSessions").add(registeredSessions);

                Toast.makeText(Search_Session.this, "Registered!", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onReject(Sessions request) {

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







        //fetches all the courses
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
                String selectedCourse = parent.getItemAtPosition(position).toString();

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
        db.collection("Sessions").orderBy("startDate").get().addOnCompleteListener(task -> {
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
                filterSessions(course, sessionsFromDB);
                adapter2.notifyDataSetChanged();
            });
        });
    }



    public void filterSessions(String course, List<Sessions> unfilteredSessions) {

        sessions.clear();

        for (Sessions session : unfilteredSessions) {
            if (session.getApprovedTutor() != null && session.getApprovedTutor().getCoursesOffered().contains(course)) {
                sessions.add(session);
            }

        }

    }

}
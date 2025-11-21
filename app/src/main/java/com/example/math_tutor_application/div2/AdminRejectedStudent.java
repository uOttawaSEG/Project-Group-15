package com.example.math_tutor_application.div2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
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

public class AdminRejectedStudent extends AppCompatActivity {

    private FirebaseFirestore db;


    private RecyclerView recyclerView;

    private AcceptedRequestAdaptor adapter;




    private ArrayList<User> pendingRequestsUser = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.div2_admin_rejected_student1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AcceptedRequestAdaptor(pendingRequestsUser, new AcceptedRequestAdaptor.OnRequestActionListener() {

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

                    new AlertDialog.Builder(AdminRejectedStudent.this)
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
                    new AlertDialog.Builder(AdminRejectedStudent.this)
                            .setTitle("Tutor Information")
                            .setMessage(tutorInfo)
                            .setPositiveButton("OK", null)
                            .show();


                }


            }
        });

        recyclerView.setAdapter(adapter);

        Task<QuerySnapshot> studentsTask = db.collection("Students").whereEqualTo("status", "rejected").get();
        Task<QuerySnapshot> tutorsTask = db.collection("Tutors").whereEqualTo("status", "rejected").get();


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



}
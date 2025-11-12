package com.example.math_tutor_application.div2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminRejectedStudent extends AppCompatActivity {

    private FirebaseFirestore db;


    private List<Student> pendingRequests = new ArrayList<>();

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


        fetchAndDisplayPendingRequests();
    }

    private void fetchAndDisplayPendingRequests() {
        db.collection("Students")
                .whereEqualTo("status", "rejected")
                .limit(5)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        pendingRequests.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Student request = document.toObject(Student.class);
                            request.setDocumentId(document.getId());
                            pendingRequests.add(request);
                        }
                        updateUiViews();
                    }
                });
    }

    private void updateUiViews() {

        //1
        if (pendingRequests.size() <= 0) return;
        Student request = pendingRequests.get(0);
        TextView nameText = findViewById(R.id.student1);
        nameText.setText(request.getFirstName() + " " + request.getLastName() + " (" + request.getRole() + ")");
        TextView emailText = findViewById(R.id.studentEmailTextView1);
        String message = request.getEmail() + " " + request.getPhoneNumber();
        emailText.setText(message);

        //2
        if (pendingRequests.size() <= 1) return;
        Student request2 = pendingRequests.get(1);
        TextView nameText2 = findViewById(R.id.student2);
        nameText2.setText(request2.getFirstName() + " " + request2.getLastName() + " (" + request2.getRole() + ")");
        TextView emailText2 = findViewById(R.id.studentEmailTextView2);
        String message2 = request2.getEmail() + " " + request2.getPhoneNumber();
        emailText2.setText(message2);

        //3
        if (pendingRequests.size() <= 2) return;
        Student request3 = pendingRequests.get(2);
        TextView nameText3 = findViewById(R.id.student3);
        nameText3.setText(request3.getFirstName() + " " + request3.getLastName() + " (" + request3.getRole() + ")");
        TextView emailText3 = findViewById(R.id.studentEmailTextView3);
        String message3 = request3.getEmail() + " " + request3.getPhoneNumber();
        emailText3.setText(message3);

        //4
        if (pendingRequests.size() <= 3) return;
        Student request4 = pendingRequests.get(3);
        TextView nameText4 = findViewById(R.id.student4);
        nameText4.setText(request4.getFirstName() + " " + request4.getLastName() + " (" + request4.getRole() + ")");
        TextView emailText4 = findViewById(R.id.studentEmailTextView4);
        String message4 = request4.getEmail() + " " + request4.getPhoneNumber();
        emailText4.setText(message4);

        //5
        if (pendingRequests.size() <= 4) return;
        Student request5 = pendingRequests.get(4);
        TextView nameText5 = findViewById(R.id.student5);
        nameText5.setText(request5.getFirstName() + " " + request5.getLastName() + " (" + request5.getRole() + ")");
        TextView emailText5 = findViewById(R.id.studentEmailTextView5);
        String message5 = request5.getEmail() + " " + request5.getPhoneNumber();
        emailText5.setText(message5);

    }


}
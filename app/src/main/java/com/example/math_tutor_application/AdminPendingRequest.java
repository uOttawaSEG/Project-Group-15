package com.example.math_tutor_application;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;



public class AdminPendingRequest extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private List<RegistrationRequest> pendingRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pending_request);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        fetchAndDisplayPendingRequests();
    }


    private void fetchAndDisplayPendingRequests() {
        db.collection("registration_requests")
                .whereEqualTo("status", "pending")
                .limit(5)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        pendingRequests.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            RegistrationRequest request = document.toObject(RegistrationRequest.class);
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
        RegistrationRequest request = pendingRequests.get(0);
        TextView nameText = findViewById(R.id.student1);
        nameText.setText(request.getFirstName() + " " + request.getLastName() + " (" + request.getRole() + ")");
        TextView emailText = findViewById(R.id.studentEmailTextView1);
        String message = request.getEmail() + " " + request.getPhoneNumber();
        emailText.setText(message);

        //2
        if (pendingRequests.size() <= 1) return;
        RegistrationRequest request2 = pendingRequests.get(1);
        TextView nameText2 = findViewById(R.id.student2);
        nameText2.setText(request2.getFirstName() + " " + request2.getLastName() + " (" + request2.getRole() + ")");
        TextView emailText2 = findViewById(R.id.studentEmailTextView2);
        String message2 = request2.getEmail() + " " + request2.getPhoneNumber();
        emailText2.setText(message2);

        //3
        if (pendingRequests.size() <= 2) return;
        RegistrationRequest request3 = pendingRequests.get(2);
        TextView nameText3 = findViewById(R.id.student3);
        nameText3.setText(request3.getFirstName() + " " + request3.getLastName() + " (" + request3.getRole() + ")");
        TextView emailText3 = findViewById(R.id.studentEmailTextView3);
        String message3 = request3.getEmail() + " " + request3.getPhoneNumber();
        emailText3.setText(message3);

        //4
        if (pendingRequests.size() <= 3) return;
        RegistrationRequest request4 = pendingRequests.get(3);
        TextView nameText4 = findViewById(R.id.student4);
        nameText4.setText(request4.getFirstName() + " " + request4.getLastName() + " (" + request4.getRole() + ")");
        TextView emailText4 = findViewById(R.id.studentEmailTextView4);
        String message4 = request4.getEmail() + " " + request4.getPhoneNumber();
        emailText4.setText(message4);

        //5
        if (pendingRequests.size() <= 4) return;
        RegistrationRequest request5 = pendingRequests.get(4);
        TextView nameText5 = findViewById(R.id.student5);
        nameText5.setText(request5.getFirstName() + " " + request5.getLastName() + " (" + request5.getRole() + ")");
        TextView emailText5 = findViewById(R.id.studentEmailTextView5);
        String message5 = request5.getEmail() + " " + request5.getPhoneNumber();
        emailText5.setText(message5);

    }




    public void logoutHandler(View view) {
        mAuth.signOut();
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    //0

    public void approveStudent1(View view) {
        RegistrationRequest request = pendingRequests.get(0);
        request.setStatus("approved");
        db.collection("registration_requests")
                .document(request.getDocumentId())
                .set(request);

        sendSMS(request.getPhoneNumber(), true);
    }

    public void rejectStudent1(View view) {
        RegistrationRequest request = pendingRequests.get(0);
        request.setStatus("rejected");
        db.collection("registration_requests")
                .document(request.getDocumentId())
                .set(request);

        sendSMS(request.getPhoneNumber(), false);
    }


    //1
    public void approveStudent2(View view) {
        RegistrationRequest request = pendingRequests.get(1);
        request.setStatus("approved");
        db.collection("registration_requests")
                .document(request.getDocumentId())
                .set(request);

        sendSMS(request.getPhoneNumber(), true);
    }

    public void rejectStudent2(View view) {
        RegistrationRequest request = pendingRequests.get(1);
        request.setStatus("rejected");
        db.collection("registration_requests")
                .document(request.getDocumentId())
                .set(request);

        sendSMS(request.getPhoneNumber(), false);
    }



    //2
    public void approveStudent3(View view) {
        RegistrationRequest request = pendingRequests.get(2);
        request.setStatus("approved");
        db.collection("registration_requests")
                .document(request.getDocumentId())
                .set(request);

        sendSMS(request.getPhoneNumber(), true);
    }

    //3
    public void rejectStudent3(View view) {
        RegistrationRequest request = pendingRequests.get(2);
        request.setStatus("rejected");
        db.collection("registration_requests")
                .document(request.getDocumentId())
                .set(request);

        sendSMS(request.getPhoneNumber(), false);
    }


    //3
    public void approveStudent4(View view) {
        RegistrationRequest request = pendingRequests.get(3);
        request.setStatus("approved");
        db.collection("registration_requests")
                .document(request.getDocumentId())
                .set(request);

        sendSMS(request.getPhoneNumber(), true);
    }

    //4
    public void rejectStudent4(View view) {
        RegistrationRequest request = pendingRequests.get(3);
        request.setStatus("rejected");
        db.collection("registration_requests")
                .document(request.getDocumentId())
                .set(request);
        sendSMS(request.getPhoneNumber(), false);
    }

    //5
    public void approveStudent5(View view) {
        RegistrationRequest request = pendingRequests.get(4);
        request.setStatus("approved");
        db.collection("registration_requests")
                .document(request.getDocumentId())
                .set(request);

        sendSMS(request.getPhoneNumber(), true);
    }
    public void rejectStudent5(View view) {
        RegistrationRequest request = pendingRequests.get(4);
        request.setStatus("rejected");
        db.collection("registration_requests")
                .document(request.getDocumentId())
                .set(request);

        sendSMS(request.getPhoneNumber(), false);
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
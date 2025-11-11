package com.example.math_tutor_application;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.example.math_tutor_application.uml_classes.Sessions;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class SlotCreationTutor extends AppCompatActivity {

    Button btnSelectDate, btnSelectTime, btnSelectDateEnd, btnSelectTimeEnd;
    TextView tvSelected, tvSelectedEnd, errorText;

    Calendar calendarStart = Calendar.getInstance();
    Calendar calendarEnd = Calendar.getInstance();
    SwitchMaterial switchManualApproval;
    boolean requiresManualApproval = false;

    private FirebaseFirestore db;

    private String docID;

    ArrayList<Sessions> sessionsArrayList;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_slot_creation_tutor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        errorText = findViewById(R.id.errorText);
        Intent intent = getIntent();
        docID = intent.getStringExtra("docID");


        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        tvSelected = findViewById(R.id.tvSelected);
        errorText = findViewById(R.id.errorText);

        btnSelectDateEnd = findViewById(R.id.btnSelectDateEnd);
        btnSelectTimeEnd = findViewById(R.id.btnSelectTimeEnd);
        tvSelectedEnd = findViewById(R.id.tvSelectedEnd);

        btnSelectDate.setOnClickListener(v -> showDatePicker(false));
        btnSelectTime.setOnClickListener(v -> showTimePicker(false));
        btnSelectDateEnd.setOnClickListener(v -> showDatePicker(true));
        btnSelectTimeEnd.setOnClickListener(v -> showTimePicker(true));

        switchManualApproval = findViewById(R.id.switchManualApproval);
        switchManualApproval.setOnCheckedChangeListener((buttonView, isChecked) -> {
            requiresManualApproval = isChecked;
        });



        sessionsArrayList = new ArrayList<>();

        //get sessions from firebase, done at creation to avoid multiple async calls
        db.collection("ApprovedTutors")
                .document(docID)
                .collection("sessionsArrayList")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot) {
                        Sessions session = doc.toObject(Sessions.class);

                        sessionsArrayList.add(session);

                    }
                });

    }


    private void showDatePicker(Boolean isEnd) {

        DatePickerDialog datePickerDialog;
        if (!isEnd) {
            datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        calendarStart.set(Calendar.YEAR, selectedYear);
                        calendarStart.set(Calendar.MONTH, selectedMonth);
                        calendarStart.set(Calendar.DAY_OF_MONTH, selectedDay);
                        updateText();
                    }, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH), calendarStart.get(Calendar.DAY_OF_MONTH));
        } else {
            datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        calendarEnd.set(Calendar.YEAR, selectedYear);
                        calendarEnd.set(Calendar.MONTH, selectedMonth);
                        calendarEnd.set(Calendar.DAY_OF_MONTH, selectedDay);
                        updateTextEnd();
                    }, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH), calendarEnd.get(Calendar.DAY_OF_MONTH));

        }


        if (!isEnd) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        } else {
            datePickerDialog.getDatePicker().setMinDate(calendarStart.getTimeInMillis());
        }
        datePickerDialog.show();


    }

    private void showTimePicker(boolean isEnd) {

        TimePickerDialog timePickerDialog;
        if (!isEnd) {
            timePickerDialog = new TimePickerDialog(this,
                    (view, selectedHour, selectedMinute) -> {
                        calendarStart.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendarStart.set(Calendar.MINUTE, selectedMinute);
                        updateText();
                    }, calendarStart.get(Calendar.HOUR_OF_DAY), calendarStart.get(Calendar.MINUTE), true);

        } else {
            timePickerDialog = new TimePickerDialog(this,
                    (view, selectedHour, selectedMinute) -> {
                        calendarEnd.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendarEnd.set(Calendar.MINUTE, selectedMinute);
                        updateTextEnd();
                    }, calendarEnd.get(Calendar.HOUR_OF_DAY), calendarEnd.get(Calendar.MINUTE), true);

        }
        timePickerDialog.show();

    }

    private void updateText() {

        String message = String.format("Start Time %d-%02d-%02d %02d:%02d", calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH) + 1, calendarStart.get(Calendar.DAY_OF_MONTH), calendarStart.get(Calendar.HOUR_OF_DAY), calendarStart.get(Calendar.MINUTE));
        tvSelected.setText(message);
    }

    private void updateTextEnd() {
        String message = String.format("End Time %d-%02d-%02d %02d:%02d", calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH) + 1, calendarEnd.get(Calendar.DAY_OF_MONTH), calendarEnd.get(Calendar.HOUR_OF_DAY), calendarEnd.get(Calendar.MINUTE));
        tvSelectedEnd.setText(message);
    }


    public void submitHandler(View view) {

        //start > end
        boolean checkYear = calendarStart.get(Calendar.YEAR) < calendarEnd.get(Calendar.YEAR);
        boolean checkMonth = calendarStart.get(Calendar.MONTH) < calendarEnd.get(Calendar.MONTH);
        boolean checkDay = calendarStart.get(Calendar.DAY_OF_MONTH) < calendarEnd.get(Calendar.DAY_OF_MONTH);
        boolean checkHour = calendarStart.get(Calendar.HOUR_OF_DAY) < calendarEnd.get(Calendar.HOUR_OF_DAY);
        boolean checkMinute = calendarStart.get(Calendar.MINUTE) < calendarEnd.get(Calendar.MINUTE);


        //start and end minutes equal == 30 or 00
        boolean checkMinuteStart = calendarStart.get(Calendar.MINUTE) == 0 || calendarStart.get(Calendar.MINUTE) == 30;
        boolean checkMinuteEnd = calendarEnd.get(Calendar.MINUTE) == 0 || calendarEnd.get(Calendar.MINUTE) == 30;



        //no overlap start1 <= end2 and start2 <= end1
        boolean checkOverlap = true;
        for (Sessions s : sessionsArrayList) {

            if (calendarStart.getTime().before(s.getEndDate().toDate()) && calendarEnd.getTime().after(s.getStartDate().toDate())) {
                checkOverlap = false;
                break;
            }
        }


        if (!checkYear && !checkMonth && !checkDay && !checkHour && !checkMinute && !checkMinuteStart && !checkMinuteEnd) {
            errorText.setText("End time must be after start time");
        } else if (!checkMinuteStart || !checkMinuteEnd) {
            errorText.setText("Minuites must be in 30 or 00");

        } else if (!checkOverlap) {
            errorText.setText("Time slots overlap");

        } else {
            Sessions session = new Sessions(new Timestamp(calendarStart.getTime()), new Timestamp(calendarEnd.getTime()), requiresManualApproval);
            session.setApprovedTutorId(docID);

            //adds it to the tutor
            db.collection("ApprovedTutors")
                    .document(docID)
                    .collection("sessionsArrayList")
                    .add(session)
                    .addOnSuccessListener(documentRef -> {
                        String generatedId = documentRef.getId();
                        documentRef.update("documentId", generatedId);
                        session.setDocumentId(generatedId);
                        sessionsArrayList.add(session);
                    }).addOnCompleteListener(aVoid -> {

                        //adds it for the public
                        db.collection("Sessions").document(session.getDocumentId()).set(session);
                        Toast.makeText(this, "Slot created Successfully", Toast.LENGTH_SHORT).show();

                        //temporally - creates a bunch of RegisteredSession as the logic is not implemented yet till div 4
/*                         RegisteredSessions registeredSessions = new RegisteredSessions(session);
                         registeredSessions.setApprovedTutorId(docID);
                         registeredSessions.setDocumentId(session.getDocumentId());
                         db.collection("RegisteredSessions").document(session.getDocumentId()).set(registeredSessions);*/


                    });

            errorText.setText("");



        }




    }
}
package com.example.math_tutor_application;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Date;

public class SlotCreationTutor extends AppCompatActivity {

    Button btnSelectDate, btnSelectTime, btnSelectDateEnd, btnSelectTimeEnd;
    TextView tvSelected, tvSelectedEnd;
    Calendar calendar;

    int year, month, day, hour, minute, yearEnd, monthEnd, dayEnd, hourEnd, minuteEnd;

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

        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        tvSelected = findViewById(R.id.tvSelected);

        btnSelectDateEnd = findViewById(R.id.btnSelectDateEnd);
        btnSelectTimeEnd = findViewById(R.id.btnSelectTimeEnd);
        tvSelectedEnd = findViewById(R.id.tvSelectedEnd);

        calendar = Calendar.getInstance();

        // Default values
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        yearEnd = year;
        monthEnd = month;
        dayEnd = day;
        hourEnd = hour;
        minuteEnd = minute;

        btnSelectDate.setOnClickListener(v -> showDatePicker(false));
        btnSelectTime.setOnClickListener(v -> showTimePicker());
        btnSelectDateEnd.setOnClickListener(v -> showDatePicker(true));
        btnSelectTimeEnd.setOnClickListener(v -> showTimePicker());

    }

    //start time
    private void showDatePicker(Boolean isEnd) {

        DatePickerDialog datePickerDialog;
        if (!isEnd) {
            datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDay;
                        updateText();
                    }, year, month, day);
        } else {
            datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        yearEnd = selectedYear;
                        monthEnd = selectedMonth;
                        dayEnd = selectedDay;
                        updateTextEnd();
                    }, yearEnd, monthEnd, dayEnd);

        }
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();


    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    hour = selectedHour;
                    minute = selectedMinute;
                    updateText();
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void updateText() {
        Date date = new Date(year - 1900, month +1, day, hour, minute);
        tvSelected.setText("Start Time: " + date.toString());
    }

    private void updateTextEnd() {
        Date date = new Date(year - 1900, month +1, day, hour, minute);
        tvSelectedEnd.setText("End Time: " + date.toString());
    }




}
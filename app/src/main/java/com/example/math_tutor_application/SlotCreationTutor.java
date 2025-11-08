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
    Calendar calendarStart = Calendar.getInstance();
    Calendar calendarEnd = Calendar.getInstance();




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




        btnSelectDate.setOnClickListener(v -> showDatePicker(false));
        btnSelectTime.setOnClickListener(v -> showTimePicker(false));
        btnSelectDateEnd.setOnClickListener(v -> showDatePicker(true));
        btnSelectTimeEnd.setOnClickListener(v -> showTimePicker(true));

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
        Date date = new Date(calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH), calendarStart.get(Calendar.DAY_OF_MONTH), calendarStart.get(Calendar.HOUR_OF_DAY), calendarStart.get(Calendar.MINUTE));
        tvSelected.setText("Start Time: " + date.toString());
    }

    private void updateTextEnd() {
        Date date = new Date(calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH), calendarEnd.get(Calendar.DAY_OF_MONTH), calendarEnd.get(Calendar.HOUR_OF_DAY), calendarEnd.get(Calendar.MINUTE));
        tvSelectedEnd.setText("End Time: " + date.toString());
    }


}
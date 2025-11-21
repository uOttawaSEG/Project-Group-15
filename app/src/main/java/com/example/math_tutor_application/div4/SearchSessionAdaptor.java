package com.example.math_tutor_application.div4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.ApprovedTutor;
import com.example.math_tutor_application.uml_classes.Sessions;
import com.example.math_tutor_application.uml_classes.Student;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchSessionAdaptor extends RecyclerView.Adapter<SearchSessionAdaptor.ViewHolder> {

    private List<Sessions> sessionsList;
    private OnRequestActionListener listener;



    // Interface to communicate button actions back to the activity
    public interface OnRequestActionListener {
        void onApprove(Sessions request);
        void onReject(Sessions request);

        void onDisplay(ApprovedTutor tutor);
    }

    public SearchSessionAdaptor(List<Sessions> sessions, OnRequestActionListener listener) {
        this.sessionsList = sessions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.div4_item_search_session_adaptor, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sessions sessions = sessionsList.get(position);



        //AVOIDS A NULL POINTER ERROR
        if (sessions.getApprovedTutor() != null) {
            holder.studentName.setText("Tutor: " + sessions.getApprovedTutor().getFirstName() + " " + sessions.getApprovedTutor().getLastName());
            holder.course.setText("Course: " + sessions.getApprovedTutor().getCoursesOffered().toString());
            String staus = sessions.getManualApproval() ? "YES" : "NO";
            holder.status.setText("Automatic Approval: " + staus);
            holder.studentRegistration.setText("Student Registration: " + (sessions.isStudentRegister() ? "Booked" : "Open"));




        }





        String timeText = DateFormat.getDateTimeInstance().format(sessions.getStartDate().toDate())
                + " - " + DateFormat.getDateTimeInstance().format(sessions.getEndDate().toDate());
        holder.time.setText("Time: " + timeText);

        holder.approveBtn.setOnClickListener(v -> listener.onApprove(sessions));
        holder.rejectBtn.setOnClickListener(v -> listener.onReject(sessions));
        holder.studentName.setOnClickListener(v-> listener.onDisplay(sessions.getApprovedTutor()));
    }



    @Override
    public int getItemCount() {
        return sessionsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName,  course, time, status, studentRegistration;
        Button approveBtn, rejectBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
            course = itemView.findViewById(R.id.course);
            time = itemView.findViewById(R.id.time);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);
            status = itemView.findViewById(R.id.status);
            studentRegistration = itemView.findViewById(R.id.studentRegistration);
        }
    }
}

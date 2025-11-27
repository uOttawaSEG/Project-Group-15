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
import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.example.math_tutor_application.uml_classes.Sessions;

import java.text.DateFormat;
import java.util.List;

public class PastSessionsAdaptor extends RecyclerView.Adapter<PastSessionsAdaptor.ViewHolder> {

    private List<RegisteredSessions> sessionsList;
    private PastSessionsAdaptor.OnRequestActionListener listener;



    // Interface to communicate button actions back to the activity
    public interface OnRequestActionListener {
        void onRateTutor(RegisteredSessions sessions);


        void onDisplay(ApprovedTutor tutor);
    }

    public PastSessionsAdaptor(List<RegisteredSessions> sessions, PastSessionsAdaptor.OnRequestActionListener listener) {
        this.sessionsList = sessions;
        this.listener = listener;
    }

    private PastSessionsAdaptor() {}; //default no arg constructor for circleCI



    @NonNull
    @Override
    public PastSessionsAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.div4_item_past_session_adaptor, parent, false);
        return new PastSessionsAdaptor.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PastSessionsAdaptor.ViewHolder holder, int position) {
        RegisteredSessions sessions = sessionsList.get(position);
        String fullName = "John Doe";
        boolean isComplete = sessions.getStatus().equals("approved") && sessions.getIsStudentRegister();
        String complete = isComplete ? "Completed" : "Incomplete";
        if (!isComplete) {
            holder.rateTutorBtn.setVisibility(View.GONE);
            if (sessions.getStatus().equals("rejected")) {
                complete += ": Tutor Rejected the session";
            } else if (sessions.getStatus().equals("pending")) {
                complete += ": Tutor did not approve the session ";
            }
        }









        //AVOIDS A NULL POINTER ERROR
        if (sessions.getApprovedTutor() != null) {
            fullName = sessions.getApprovedTutor().getFirstName() + " " + sessions.getApprovedTutor().getLastName();
        }

        holder.tutorName.setText("Tutor: " + fullName);
        holder.course.setText("Course: " + sessions.getCourse());
        holder.status.setText("Status: " + complete);
        String timeText = DateFormat.getDateTimeInstance().format(sessions.getStartDate().toDate())
                + " - " + DateFormat.getDateTimeInstance().format(sessions.getEndDate().toDate());
        holder.time.setText("Time: " + timeText);

        holder.rateTutorBtn.setOnClickListener(v -> listener.onRateTutor(sessions));
        holder.tutorName.setOnClickListener(v-> listener.onDisplay(sessions.getApprovedTutor()));
    }



    @Override
    public int getItemCount() {
        return sessionsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tutorName, course, time, status;
        Button rateTutorBtn;








        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tutorName = itemView.findViewById(R.id.tutorName);
            course = itemView.findViewById(R.id.course);
            status = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.time);
            rateTutorBtn = itemView.findViewById(R.id.rateTutorBtn);


        }
    }
}

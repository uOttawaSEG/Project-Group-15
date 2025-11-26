package com.example.math_tutor_application.div4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.example.math_tutor_application.R;

import java.text.DateFormat;
import java.util.List;

public class StudentUpcomingSessionsAdapter extends RecyclerView.Adapter<StudentUpcomingSessionsAdapter.ViewHolder> {

    // Listener interface so the Activity can handle cancel clicks
    public interface OnCancelClickListener {
        void onCancelClick(RegisteredSessions session);
    }

    private final List<RegisteredSessions> sessionsList;
    private final OnCancelClickListener listener;

    public StudentUpcomingSessionsAdapter(List<RegisteredSessions> sessionsList, OnCancelClickListener listener) {
        this.sessionsList = sessionsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_upcoming_session, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RegisteredSessions s = sessionsList.get(position);

        // Format session time
        String timeText = "Time: TBD";
        if (s.getStartDate() != null && s.getEndDate() != null) {
            timeText = DateFormat.getDateTimeInstance().format(s.getStartDate().toDate())
                    + " - " + DateFormat.getDateTimeInstance().format(s.getEndDate().toDate());
        } else if (s.getStartDate() != null) {
            timeText = DateFormat.getDateTimeInstance().format(s.getStartDate().toDate());
        }
        holder.sessionTime.setText(timeText);

        // Show status
        String status = (s.getStatus() != null && !s.getStatus().trim().isEmpty())
                ? s.getStatus()
                : "approved";
        holder.sessionStatus.setText("Status: " + status);

        // Wire up cancel button
        holder.cancelButton.setOnClickListener(v -> {
            if (listener != null) listener.onCancelClick(s);
        });
    }

    @Override
    public int getItemCount() {
        return sessionsList == null ? 0 : sessionsList.size();
    }

    // ViewHolder holds references to the item layout views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView sessionTime;
        final TextView sessionStatus;
        final Button cancelButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionTime = itemView.findViewById(R.id.sessionTime);
            sessionStatus = itemView.findViewById(R.id.sessionStatus);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }
}

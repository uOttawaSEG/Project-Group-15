package com.example.math_tutor_application.div3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.RegisteredSessions;

import java.text.DateFormat;
import java.util.List;

public class UpcomingSessionsAdapter extends RecyclerView.Adapter<UpcomingSessionsAdapter.ViewHolder> {

    public interface OnSessionClickListener {
        void onSessionClick(RegisteredSessions session);
    }

    private final List<RegisteredSessions> sessionsList;
    private final OnSessionClickListener listener;

    public UpcomingSessionsAdapter(List<RegisteredSessions> sessionsList, OnSessionClickListener listener) {
        this.sessionsList = sessionsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.div3_item_upcoming_session, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingSessionsAdapter.ViewHolder holder, int position) {
        RegisteredSessions s = sessionsList.get(position);
        String timeText = "Time: TBD";
        if (s.getStartDate() != null && s.getEndDate() != null) {
            timeText = DateFormat.getDateTimeInstance().format(s.getStartDate().toDate())
                    + " - " + DateFormat.getDateTimeInstance().format(s.getEndDate().toDate());
        } else if (s.getStartDate() != null) {
            timeText = DateFormat.getDateTimeInstance().format(s.getStartDate().toDate());
        }

        holder.sessionTime.setText(timeText);

        String status = (s.getstatus() != null && !s.getstatus().trim().isEmpty())
                ? s.getstatus()
                : "approved";
        holder.sessionStatus.setText("Status: " + status);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onSessionClick(s);
        });
    }

    @Override
    public int getItemCount() {
        return sessionsList == null ? 0 : sessionsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView sessionTime;
        final TextView sessionStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionTime = itemView.findViewById(R.id.sessionTime);
            sessionStatus = itemView.findViewById(R.id.sessionStatus);
        }
    }
}

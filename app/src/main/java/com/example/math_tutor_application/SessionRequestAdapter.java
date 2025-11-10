package com.example.math_tutor_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.List;

public class SessionRequestAdapter extends RecyclerView.Adapter<SessionRequestAdapter.ViewHolder> {

    private List<RegisteredSessions> requestList;
    private OnRequestActionListener listener;

    // Interface to communicate button actions back to the activity
    public interface OnRequestActionListener {
        void onApprove(RegisteredSessions request);
        void onReject(RegisteredSessions request);
    }

    public SessionRequestAdapter(List<RegisteredSessions> requestList, OnRequestActionListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session_request, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RegisteredSessions request = requestList.get(position);

        //avoids a null pinter errror
        String studentInfo = "";
        if (request.getStudent() != null) {
            studentInfo = request.getStudent().toString();
        }

        holder.studentName.setText("Student Info: " + studentInfo);
        holder.course.setText("Tutor ID: " + request.getApprovedTutorId());

        String timeText = DateFormat.getDateTimeInstance().format(request.getStartDate().toDate())
                + " - " + DateFormat.getDateTimeInstance().format(request.getEndDate().toDate());
        holder.time.setText("Time: " + timeText);

        holder.approveBtn.setOnClickListener(v -> listener.onApprove(request));
        holder.rejectBtn.setOnClickListener(v -> listener.onReject(request));
    }



    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, tutorName, course, time;
        Button approveBtn, rejectBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
            course = itemView.findViewById(R.id.course);
            time = itemView.findViewById(R.id.time);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);
        }
    }
}

package com.example.math_tutor_application.div3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.div2.PendingRequestAdaptor;
import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.example.math_tutor_application.uml_classes.Student;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class SessionRequestAdapter extends RecyclerView.Adapter<SessionRequestAdapter.ViewHolder> {

    private List<RegisteredSessions> requestList;
    private OnRequestActionListener listener;

    public SessionRequestAdapter(ArrayList<Student> pendingRequests, PendingRequestAdaptor.OnRequestActionListener onRequestActionListener) {
    }

    // Interface to communicate button actions back to the activity
    public interface OnRequestActionListener {
        void onApprove(RegisteredSessions request);
        void onReject(RegisteredSessions request);

        void onDisplay(Student student);
    }

    public SessionRequestAdapter(List<RegisteredSessions> requestList, OnRequestActionListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.div3_item_session_request, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RegisteredSessions request = requestList.get(position);

        String studentInfo = "";
        //avoids a null pinter error
        if (request.getStudent() != null) {
            studentInfo = request.getStudent().getFirstName() + " " + request.getStudent().getLastName();
        }

        String tutorInfo = request.getApprovedTutor().getFirstName() + " " + request.getApprovedTutor().getLastName();

        holder.studentName.setText("Student Info: " + studentInfo);
        holder.course.setText("Tutor info: " + tutorInfo); //is a bit redundant since this dashboard belongs to this ApprovedTutor

        holder.status.setText("Status: " + request.getstatus());


        String timeText = DateFormat.getDateTimeInstance().format(request.getStartDate().toDate())
                + " - " + DateFormat.getDateTimeInstance().format(request.getEndDate().toDate());
        holder.time.setText("Time: " + timeText);

        holder.approveBtn.setOnClickListener(v -> listener.onApprove(request));
        holder.rejectBtn.setOnClickListener(v -> listener.onReject(request));
        holder.studentName.setOnClickListener(v-> listener.onDisplay(request.getStudent()));
    }



    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, tutorName, course, time, status;
        Button approveBtn, rejectBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
            course = itemView.findViewById(R.id.course);
            time = itemView.findViewById(R.id.time);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);
            status = itemView.findViewById(R.id.status);
        }
    }
}

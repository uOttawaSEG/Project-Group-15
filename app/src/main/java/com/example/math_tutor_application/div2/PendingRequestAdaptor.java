package com.example.math_tutor_application.div2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.Student;
import com.example.math_tutor_application.uml_classes.Tutor;

import java.util.ArrayList;
import java.util.List;

public class PendingRequestAdaptor extends RecyclerView.Adapter<PendingRequestAdaptor.ViewHolder> {

    private List<Student> requestList;

    private List<Tutor> tutorList;

    private OnRequestActionListener listener;

    // Interface to communicate button actions back to the activity
    public interface OnRequestActionListener {

        void onApprove(Student request);

        void onReject(Student request);

        void onDisplay(Student student);
    }

    public PendingRequestAdaptor(ArrayList<Student> requestList, OnRequestActionListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.div2_item_session_request, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student request = requestList.get(position);


        holder.studentName.setText(request.getFirstName() + " " + request.getLastName());
        holder.status.setText("Status : " + request.getStatus());

        holder.approveBtn.setOnClickListener(v -> listener.onApprove(request));
        holder.rejectBtn.setOnClickListener(v -> listener.onReject(request));
        holder.studentName.setOnClickListener(v-> listener.onDisplay(request));
    }



    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, course, status;
        Button approveBtn, rejectBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);
            status = itemView.findViewById(R.id.status);
        }
    }
}

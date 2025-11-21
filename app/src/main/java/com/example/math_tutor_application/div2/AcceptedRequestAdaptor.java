package com.example.math_tutor_application.div2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.User;

import java.util.ArrayList;
import java.util.List;

public class AcceptedRequestAdaptor extends RecyclerView.Adapter<AcceptedRequestAdaptor.ViewHolder> {
    private List<User> requestList;



    private AcceptedRequestAdaptor.OnRequestActionListener listener;

    // Interface to communicate button actions back to the activity
    public interface OnRequestActionListener {

        void onDisplay(User student);
    }

    public AcceptedRequestAdaptor(ArrayList<User> requestList, AcceptedRequestAdaptor.OnRequestActionListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AcceptedRequestAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.div2_item_accepted, parent, false);
        return new AcceptedRequestAdaptor.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AcceptedRequestAdaptor.ViewHolder holder, int position) {
        User request = requestList.get(position);


        holder.studentName.setText(request.getFirstName() + " " + request.getLastName() + " - " + request.getRole());
        holder.status.setText("Status : " + request.getStatus());
        holder.studentName.setOnClickListener(v-> listener.onDisplay(request));
    }



    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, status;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
            status = itemView.findViewById(R.id.status);
        }
    }
}

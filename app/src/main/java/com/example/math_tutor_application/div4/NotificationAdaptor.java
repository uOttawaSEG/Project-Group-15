package com.example.math_tutor_application.div4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.Notification;

import java.util.ArrayList;

public class NotificationAdaptor extends RecyclerView.Adapter<NotificationAdaptor.ViewHolder> {
    private ArrayList<String> displayNotifications = new ArrayList<>();

    public NotificationAdaptor(ArrayList<String> displayNotifications) {
        this.displayNotifications = displayNotifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.div4_item_notification_display, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String notification = displayNotifications.get(position);
        holder.msg.setText(notification);
    }

    @Override
    public int getItemCount() {
        return displayNotifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg);
        }

    }





}

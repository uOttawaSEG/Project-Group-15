package com.example.math_tutor_application.div4;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.math_tutor_application.R;
import com.example.math_tutor_application.uml_classes.Notification;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class NotificationDisplay extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private NotificationAdaptor adapter;

    private final ArrayList<Notification> notifications = new ArrayList<>();

    private final ArrayList<String> displayNotifications = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.div4_notification_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String docId = getIntent().getStringExtra("docId");


        //set up recycler view

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdaptor(displayNotifications);
        recyclerView.setAdapter(adapter);
        db.collection("Notifications").orderBy("timestamp", Query.Direction.DESCENDING)
        .get().addOnCompleteListener(task -> {


            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Notification notification = document.toObject(Notification.class);
                    notification.setDocumentId(document.getId());

                    if (notification.getReceiver().equals(docId)) {
                        notifications.add(notification);
                        displayNotifications.add(notification.getMsg());


                        //better way to update the adapter
                        int insertedPosition = displayNotifications.size() - 1;
                        adapter.notifyItemInserted(insertedPosition);


                    }


                }


            }
        });




    }

//uncomit the code after testing
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        db.collection("Notifications").whereEqualTo("receiver", docId).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
//                WriteBatch batch = db.batch();
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    batch.delete(document.getReference());
//                }
//                batch.commit();
//            }
//        });
//    }


}
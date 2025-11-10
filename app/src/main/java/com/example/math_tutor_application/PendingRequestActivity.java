package com.example.math_tutor_application;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PendingRequestActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private SessionRequestAdapter adapter;
    private List<RegisteredSessions> requestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SessionRequestAdapter(requestList, new SessionRequestAdapter.OnRequestActionListener() {
            @Override
            public void onApprove(RegisteredSessions request) {
                request.setStatus("approved");
                db.collection("RegisteredSessions").document(request.getDocumentId())
                        .update("status", "approved", "isApproval", true)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(PendingRequestActivity.this, "Approved!", Toast.LENGTH_SHORT).show();
                            requestList.remove(request);
                            adapter.notifyDataSetChanged();
                        });
            }

            @Override
            public void onReject(RegisteredSessions request) {
                request.setStatus("rejected");
                db.collection("RegisteredSessions").document(request.getDocumentId())
                        .update("status", "rejected", "isApproval", false)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(PendingRequestActivity.this, "Rejected!", Toast.LENGTH_SHORT).show();
                            requestList.remove(request);
                            adapter.notifyDataSetChanged();
                        });
            }
        });

        recyclerView.setAdapter(adapter);
        fetchSessionRequests();
    }

    private void fetchSessionRequests() {
        Log.d("Firestore", "Fetching pending sessions...");
        db.collection("RegisteredSessions")
                .whereEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        requestList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Log.d("Firestore", "Document: " + doc.getData().toString());
                            RegisteredSessions request = doc.toObject(RegisteredSessions.class);
                            request.setDocumentId(doc.getId());
                            requestList.add(request);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Error fetching documents", task.getException());
                        Toast.makeText(this, "Failed to load requests", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

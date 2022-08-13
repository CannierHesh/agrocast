package com.rp.agrocast.task_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.rp.agrocast.R;
import com.rp.agrocast.adapter.TaskAdapter;
import com.rp.agrocast.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskFeed extends AppCompatActivity {
    private FloatingActionButton addBtn;
    private FirebaseAuth firebasaeAuth;
    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private ListenerRegistration listenerRegistration;
    private String currentUserId;
    private List<Task> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_task_feed);

        addBtn = findViewById(R.id.fabAdd);
        recyclerView = findViewById(R.id.recyclerView);

        firestore = FirebaseFirestore.getInstance();
        firebasaeAuth = FirebaseAuth.getInstance();

        currentUserId = firebasaeAuth.getCurrentUser().getUid();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TaskFeed.this));

        mList = new ArrayList<>();
        adapter = new TaskAdapter(TaskFeed.this, mList);
        recyclerView.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskFeed.this, AddTask.class));
            }
        });

        if (firebasaeAuth.getCurrentUser() != null){

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isBottom = !recyclerView.canScrollVertically(1);
                    if (isBottom){
                        Toast.makeText(TaskFeed.this, "That's it for today !", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            firestore.collection("users/" + currentUserId + "/Tasks").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (DocumentChange documentChange : value.getDocumentChanges()){
                        if (documentChange.getType() == DocumentChange.Type.ADDED){
                            Task task = documentChange.getDocument().toObject(Task.class);
                            mList.add(task);
                            adapter.notifyDataSetChanged();

                        }else{
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            });

        }
    }
}
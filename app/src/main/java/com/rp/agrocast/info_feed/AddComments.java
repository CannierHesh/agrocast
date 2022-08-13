package com.rp.agrocast.info_feed;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rp.agrocast.R;
import com.rp.agrocast.adapter.CommentsAdapter;
import com.rp.agrocast.model.Comments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddComments extends AppCompatActivity {
    private EditText commentText;
    private Button postCommentBtn;
    private RecyclerView commentsRecycler;
    private String postId;
    private String currentUserId;
    private ProgressBar progressBar;
    private CommentsAdapter adapter;
    private List<Comments> mList;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_comments);

        commentText = findViewById(R.id.post_comment);
        postCommentBtn = findViewById(R.id.btn_post_comment);
        commentsRecycler = findViewById(R.id.comments_recycler);
        progressBar = findViewById(R.id.progressbar);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        postId = getIntent().getStringExtra("postId");
        currentUserId = auth.getCurrentUser().getUid();

        commentsRecycler.setHasFixedSize(true);
        commentsRecycler.setLayoutManager(new LinearLayoutManager(this));

        mList = new ArrayList<>();
        adapter = new CommentsAdapter(AddComments.this, mList);
        commentsRecycler.setAdapter(adapter);

        firestore.collection("Posts/" + postId + "/Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        Comments comments = documentChange.getDocument().toObject(Comments.class);
                        mList.add(comments);
                        adapter.notifyDataSetChanged();

                    }else{
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        postCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String comment = commentText.getText().toString();
                if (!comment.isEmpty()){
                    Map<String, Object> commentsMap = new HashMap<>();
                    commentsMap.put("comment", comment);
                    commentsMap.put("time", FieldValue.serverTimestamp());
                    commentsMap.put("user", currentUserId);

                    firestore.collection("Posts/" + postId + "/Comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AddComments.this, "Comment added..", Toast.LENGTH_SHORT).show();
                                commentText.setText("");
                                progressBar.setVisibility(View.INVISIBLE);
                            }else{
                                Toast.makeText(AddComments.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }else{
                    Toast.makeText(AddComments.this, "Please add a comment.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
}
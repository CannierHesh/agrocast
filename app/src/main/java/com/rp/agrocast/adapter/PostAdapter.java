package com.rp.agrocast.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rp.agrocast.R;
import com.rp.agrocast.info_feed.AddComments;
import com.rp.agrocast.model.Post;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> mList;
    private Activity context;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public PostAdapter(Activity context, List<Post> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_post, parent, false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = mList.get(position);
        holder.setPostPic(post.getImage());
        holder.setPostTitle(post.getTitle());
        holder.setPostDescription(post.getDescription());

        long milliseconds = post.getTime().getTime();
        String date = DateFormat.format("MM/dd/yyyy 'at' HH:mm", new Date(milliseconds)).toString();
        holder.setPostDate(date);

        String userId = post.getUser();
        firestore.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String username = task.getResult().getString("name");
                    String image = task.getResult().getString("image");

                    holder.setProfilePic(image);
                    holder.setPostUsername(username);
                }else{
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //like button function
        String postId = post.PostId;
        String currentUserId = auth.getCurrentUser().getUid();
        holder.likeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()){
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());
                            firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).set(likesMap);
                        }else{
                            firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).delete();
                        }
                    }
                });
            }
        });

        //like button colour
        firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    if (value.exists()){
                        holder.likeImg.setImageDrawable(context.getDrawable(R.drawable.after_liked));
                    }else{
                        holder.likeImg.setImageDrawable(context.getDrawable(R.drawable.before_liked));
                    }
                }
            }
        });

        //likes count
        firestore.collection("Posts/" + postId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                     if (error == null){
                         if (!value.isEmpty()){
                             int count = value.size();
                             holder.setPostLikes(count);
                         }else{
                            holder.setPostLikes(0);
                         }
                     }
            }
        });

        //comments function
        holder.commentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentsIntent = new Intent(context, AddComments.class);
                commentsIntent.putExtra("postId", postId);
                context.startActivity(commentsIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{

        ImageView postImg, commentImg, likeImg;
        RoundedImageView profileImg;
        TextView postUsername, postDate, postTitle, postDescription, postLikes;
        View mView;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            likeImg = mView.findViewById(R.id.like_btn);
            commentImg = mView.findViewById(R.id.comment_btn);
        }

        public void setPostLikes(int count){
            postLikes = mView.findViewById(R.id.tv_like_count);
            postLikes.setText(count + " Likes");
        }

        public void setPostPic(String urlPost){
            postImg = mView.findViewById(R.id.postImg);
            Glide.with(context).load(urlPost).into(postImg);
        }

        public void setProfilePic(String urlProfile){
            profileImg = mView.findViewById(R.id.imageProfile);
            //Glide.with(context).load(urlProfile).into(profileImg);
            profileImg.setImageBitmap(getBitmapFromEncodedString(urlProfile));
        }

        public void setPostUsername(String username){
            postUsername = mView.findViewById(R.id.tv_username);
            postUsername.setText(username);
        }

        public void setPostDate(String date){
            postDate = mView.findViewById(R.id.tv_date);
            postDate.setText(date);
        }

        public void setPostTitle(String title){
            postTitle = mView.findViewById(R.id.tv_title);
            postTitle.setText(title);
        }

        public void setPostDescription(String description){
            postDescription =  mView.findViewById(R.id.tv_description);
            postDescription.setText(description);
        }
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        if(encodedImage != null){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else {
            return null;
        }
    }
}

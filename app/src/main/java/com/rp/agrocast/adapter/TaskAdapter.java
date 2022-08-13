package com.rp.agrocast.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rp.agrocast.R;
import com.rp.agrocast.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> mList;
    private Activity context;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public TaskAdapter(Activity context, List<Task> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_task, parent, false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = mList.get(position);
        holder.setTaskTitle(task.getTask_title());
        holder.setTaskDescription(task.getTask_description());
        holder.setTaskStartDate(task.getStart_date());
        holder.setTaskEndDate(task.getEnd_date());
        holder.setTaskAssignedUser(task.getUser());
        holder.setTaskStatus("InProgress");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView taskTitle, taskDescription, startDate, endDate, assignedUser;
        View mView;
        Spinner spinner;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTaskTitle(String title){
            taskTitle = mView.findViewById(R.id.tv_title);
            taskTitle.setText(title);
        }

        public void setTaskDescription(String description){
            taskDescription = mView.findViewById(R.id.tv_description);
            taskDescription.setText(description);
        }

        public void setTaskStartDate(String date){
            startDate = mView.findViewById(R.id.tv_start_date);
            startDate.setText(date);
        }

        public void setTaskEndDate(String date){
            endDate = mView.findViewById(R.id.tv_end_date);
            endDate.setText(date);
        }

        public void setTaskAssignedUser(String name){
            assignedUser = mView.findViewById(R.id.tv_asigned_user);
            assignedUser.setText(name);
        }

        public void setTaskStatus(String status){
            spinner = mView.findViewById(R.id.status);
            spinner.setPrompt(status);
        }
    }
}

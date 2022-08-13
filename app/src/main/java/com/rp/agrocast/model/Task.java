package com.rp.agrocast.model;

import java.util.Date;

public class Task extends TaskId {

    private String task_title, task_description, start_date, end_date, user;
    private Date time;

    public String getTask_title() {
        return task_title;
    }

    public String getTask_description() {
        return task_description;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getUser() {
        return user;
    }

    public Date getTime() {
        return time;
    }
}

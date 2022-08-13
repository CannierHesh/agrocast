package com.rp.agrocast.model;

import java.util.Date;

public class Post extends PostId{

    private String image, user, title, description;
    private Date time;

    public String getImage() {
        return image;
    }

    public String getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getTime() {
        return time;
    }
}

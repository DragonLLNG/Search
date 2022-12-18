package com.example.finalexam;

import java.io.Serializable;

public class Image implements Serializable {
    String id, url, description, date, userName, userIcon, userLink;
    boolean liked;

    public Image() {
    }

    public Image(String id, String url, String description, String date, String userName, String userIcon, String userLink, boolean liked) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.date = date;
        this.userName = userName;
        this.userIcon = userIcon;
        this.userLink = userLink;
        this.liked = liked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserLink() {
        return userLink;
    }

    public void setUserLink(String userLink) {
        this.userLink = userLink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}

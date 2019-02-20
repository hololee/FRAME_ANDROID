package com.m.m.moment.memorize.frame.item;

import android.net.Uri;

public class cardListItem {

    long time_id;
    Uri img;
    String title;
    long date;
    String content;
    String location;

    public cardListItem(long time_id, Uri img, String title, long date, String content, String location) {
        this.time_id = time_id;
        this.img = img;
        this.title = title;
        this.date = date;
        this.content = content;
        this.location = location;
    }


    public long getTime_id() {
        return time_id;
    }

    public void setTime_id(long time_id) {
        this.time_id = time_id;
    }

    public Uri getImg() {
        return img;
    }

    public void setImg(Uri img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

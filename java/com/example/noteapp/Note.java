package com.example.noteapp;

import android.net.Uri;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.Timestamp;

public class Note {
    String Title;
    Uri uri;
    int likeval;
    String Imageurlstr,dateday,time;
    private static String imageUrl;
    String Content;
    boolean li;
    MaterialCheckBox Like;
    Timestamp timestamp;
    String h;

    public Note() {

    }

    public int getLikeval() {
        return likeval;
    }

    public void setLikeval(int likeval) {
        this.likeval = likeval;
    }

    public String getDateday() {
        return dateday;
    }

    public void setDateday(String dateday) {
        this.dateday = dateday;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MaterialCheckBox getLike() {
        return Like;
    }

    public void setLike(MaterialCheckBox like) {
        this.Like = like;
       // setLikeboo(Like.isChecked());
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean setLikeboo(boolean checked) {
        return checked;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) { uri = uri;
    }

    public void setImagePath(String imageurlstr) {
        Imageurlstr = imageurlstr;
    }
    public String getImagePath(){
        return Imageurlstr;
    }

    public String get(String j) {
        h=getTitle()+".jpg";
        return h;
    }

        public Object Model(String imageUrl)
        {
            this.imageUrl=imageUrl;
            setImageUrl(imageUrl);
            getImageUrl();

            return imageUrl;
        }

        public static String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }


}

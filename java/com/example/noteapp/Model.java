package com.example.noteapp;

public class Model {

    private static String imageUrl;

    public Model(String imageUrl)
    {
        this.imageUrl=imageUrl;
        setImageUrl(imageUrl);
        getImageUrl();

    }

    public static String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

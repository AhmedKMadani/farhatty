package com.farhatty.user.model;

/**
 * Created by AhmedKamal on 8/23/2017.
 */
public class service {
    private String name;
    private int thumbnail;

    public service() {
    }

    public service(String name,  int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
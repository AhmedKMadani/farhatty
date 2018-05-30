package com.farhatty.user.model;

/**
 * Created by user on 12/5/2017.
 */

public class Contact {
    String id;
    String phone;
    String email;
    String lat;
    String lng;
    String location;
    public Contact() {
    }

    public Contact( String id , String name, String email
            , String lat , String lng , String location) {
        super();

        this.id = id;
        this.email = email;
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
        this.location = location;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLat(String lat) {
        this.lat =  lat;
    }

    public void setlng(String lng) {
        this.lng = lng;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getLat() {
        return lat;
    }

    public String getlng() {
        return lng;
    }

    public String getLocation() {
        return location;
    }
}





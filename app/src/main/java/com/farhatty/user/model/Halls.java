package com.farhatty.user.model;

/**
 * Created by user on 16/11/17.
 */

public class Halls {
    String id;
    String name;
    String image;
    String price;
    String size;
    String lat;
    String lng;
    String location;
    String desp;
    public Halls() {
    }


    public Halls( String id , String name, String image, String price,
                    String size, String lat , String lng , String location , String  desp) {
        super();

        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.lat = lat;
        this.lng = lng;
        this.location = location;
        this.desp = desp ;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSize(String size) {
        this.size = size;
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

    public void setDesp(String desp) {
        this.desp = desp;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() {
        return size;
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


    public String getDesp() {
        return desp;
    }
}
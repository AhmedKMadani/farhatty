package com.farhatty.user.model;

/**
 * Created by user on 16/11/17.
 */

public class Offers {
    String id;
    String date;
    String details_en;
    String details_ar;
    String title_en;
    String title_ar;
    String image;

    public Offers() {
    }


    public Offers( String id , String date, String image, String details_ar,
                  String details_en, String title_en , String title_ar ) {
        super();

        this.id = id;
        this.date = date;
        this.image = image;
        this.details_ar = details_ar;
        this.details_en = details_en;
        this.title_ar = title_ar;
        this.title_en = title_en;

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDetails_en(String details_en) {
        this.details_en = details_en;
    }

    public void setDetails_ar(String details_ar) {
        this.details_ar = details_ar;
    }

    public void setTitle_en(String title_en) {
        this.title_en =  title_en;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }



    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getDetails_en() {
        return details_en;
    }

    public String getDetails_ar() {
        return details_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getTitle_ar() {
        return title_ar;
    }


}
package com.farhatty.user.model;

/**
 * Created by user on 2/13/2018.
 */

public class ServiceMen {
    String price;
    String title_ar;
    String title_en;
    String details_ar;
    String details_en;
    private boolean isSelected;

    public ServiceMen() {
    }

    public ServiceMen(String price, String title_ar, String title_en
            , String details_ar, String details_en , boolean isSelected) {
        super ();

        this.price = price;
        this.title_ar = title_ar;
        this.title_en = title_en;
        this.details_ar = details_ar;
        this.details_en = details_en;
        this.isSelected = isSelected;


    }


    public void setPrice(String price) {
        this.price = price;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public void setDetails_ar(String details_ar) {
        this.details_ar = details_ar;
    }

    public void setDetails_en(String details_en) {
        this.details_en = details_en;
    }


    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public String getPrice() {
        return price;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getDetails_ar() {
        return details_ar;
    }

    public String getDetails_en() {
        return details_en;
    }

}





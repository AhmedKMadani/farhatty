package com.farhatty.user.model;

/**
 * Created by user on 2/13/2018.
 */

public class OrdersA {
    String price;
    String title;
    String service_price;


    public OrdersA() {
    }

    public OrdersA(String price, String title, String service_price
    ) {
        super ();

        this.price = price;
        this.title = title;
        this.service_price = service_price;


    }


    public void setPrice(String price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setService_Price(String service_price) {
        this.service_price = service_price;
    }



    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getService_Price() {
        return service_price;
    }



}





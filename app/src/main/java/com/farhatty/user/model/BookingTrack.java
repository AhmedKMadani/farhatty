package com.farhatty.user.model;

/**
 * Created by user on 16/11/17.
 */

public class BookingTrack {
    String id;
    String name;
    String status;
    String bookingdate;
    String date;
    String total;
    String phone;

    public BookingTrack() {
    }


    public BookingTrack( String id , String name, String status, String bookingdate,
                  String date, String total , String phone ) {
        super();

        this.id = id;
        this.name = name;
        this.status = status;
        this.bookingdate = bookingdate;
        this.date = date;
        this.total = total;
        this.phone = phone;

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBookingDate(String bookingdate) {
        this.bookingdate = bookingdate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotal(String total) {
        this.total =  total;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }




    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getBookingDate() {
        return bookingdate;
    }

    public String getDate() {
        return date;
    }

    public String getTotal() {
        return total;
    }

    public String getPhone() {
        return phone;
    }


}
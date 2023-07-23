package com.parshuram.orderplacingapp.models;


import java.text.DecimalFormat;

public class RestaurantData {

    private int id;
    private String name;
    private String tags;
    private double rating;
    private int discount;
    private String primary_image;
    private double distance;

    public RestaurantData(int id, String name, String tags, double rating, int discount, String primary_image, double distance) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.rating = rating;
        this.discount = discount;
        this.primary_image = primary_image;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getPrimary_image() {
        return primary_image;
    }

    public void setPrimary_image(String primary_image) {
        this.primary_image = primary_image;
    }

    public double getDistance() {

//        returning the distance as killometer and with limiting after decimal upto two digit

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double number = distance/1000;
        String result = decimalFormat.format(number);
        return Double.parseDouble(result);
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

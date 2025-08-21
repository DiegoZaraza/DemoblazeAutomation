package com.demoblaze.model;

public class PurchaseData {
    private String name;
    private String country;
    private String city;
    private String card;
    private String month;
    private String year;

    public PurchaseData(String name, String country, String city, String card, String month, String year) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.card = card;
        this.month = month;
        this.year = year;
    }

    // Getters
    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getCity() { return city; }
    public String getCard() { return card; }
    public String getMonth() { return month; }
    public String getYear() { return year; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setCountry(String country) { this.country = country; }
    public void setCity(String city) { this.city = city; }
    public void setCard(String card) { this.card = card; }
    public void setMonth(String month) { this.month = month; }
    public void setYear(String year) { this.year = year; }

    @Override
    public String toString() {
        return "PurchaseData{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", card='" + card + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
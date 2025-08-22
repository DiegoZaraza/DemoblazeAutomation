package com.demoblaze.model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class PurchaseData {
    private String name;
    private String country;
    private String city;
    private String card;
    private String month;
    private String year;
}
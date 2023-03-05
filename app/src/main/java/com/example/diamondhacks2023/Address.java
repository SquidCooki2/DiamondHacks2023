package com.example.diamondhacks2023;

import androidx.annotation.NonNull;

public class Address {
    String street;
    String city;
    String stateName;
    String zip;

    public Address(String s, String c, String stAbbrev, String z){
        street = s;
        city = c;
        stateName = stAbbrev;
        zip = z;
    }

    public String toString() {
        return street + ", " + city + ", " + stateName + " " + zip;
    }
}

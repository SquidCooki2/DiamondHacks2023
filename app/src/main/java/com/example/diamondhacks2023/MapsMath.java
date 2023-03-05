package com.example.diamondhacks2023;

import com.google.android.gms.maps.model.LatLng;

public class MapsMath {
    public static double distLatLng(LatLng latLng1, LatLng latLng2) {
        double lat1 = Math.toRadians(latLng1.latitude);
        double lat2 = Math.toRadians(latLng2.latitude);
        double lng1 = Math.toRadians(latLng1.longitude);
        double lng2 = Math.toRadians(latLng2.longitude);

        return Math.pow(Math.sin((lat2 - lat1) / 2), 2) +
                        Math.cos(lat1) * Math.cos(lat2) *
                        Math.pow(Math.sin((lng2 - lng1) / 2), 2);
    }
}

package com.starlead.starleadhprecision.util;

import com.amap.api.maps.model.LatLng;

public class GPSDataUtils {

    public static LatLng GPSDataDispose(String Latitude, String Longitude) {
        LatLng latLng = null;
        double longitude1 = Double.parseDouble(Latitude.substring(0, 2));
        double longitude2 = Double.parseDouble(Latitude.substring(2));
        Latitude = String.valueOf(longitude1 + longitude2 / 60.0);

        double latitude1 = Double.parseDouble(Longitude.substring(0, 3));
        double latitude2 = Double.parseDouble(Longitude.substring(3));
        Longitude = String.valueOf(latitude1 + latitude2 / 60.0);
        latLng = new LatLng(retain8(Double.parseDouble(Latitude)), retain8(Double.parseDouble(Longitude)));
        return latLng;
    }

    private static double retain8(double num) {
        String result = String.format("%.8f", num);
        return Double.valueOf(result);
    }
}

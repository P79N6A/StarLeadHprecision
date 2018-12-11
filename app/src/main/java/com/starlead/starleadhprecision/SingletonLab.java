package com.starlead.starleadhprecision;

import com.starlead.starleadhprecision.entity.Marker;

public class SingletonLab {
    private static SingletonLab sSingletonLab;
    private Marker AimMarker;


    public static SingletonLab get() {
        if (sSingletonLab == null) {
            sSingletonLab = new SingletonLab();
        }
        return sSingletonLab;
    }

    private SingletonLab() {

    }

    public Marker getAimMarker() {
        return AimMarker;
    }

    public void setAimMarker(Marker aimMarker) {
        AimMarker = aimMarker;
    }
}

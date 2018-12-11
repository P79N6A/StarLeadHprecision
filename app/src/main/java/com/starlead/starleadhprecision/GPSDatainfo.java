package com.starlead.starleadhprecision;

import com.amap.api.maps.model.LatLng;
import com.starlead.starleadhprecision.Service.BluetoothService;

public interface GPSDatainfo {
    void sendGPSData(LatLng latLng);
}

package com.starlead.starleadhprecision.entity;

import java.util.Objects;

public class DevicesInfo {
    private String mDeviceName;
    private String mDevicetAddress;

    public DevicesInfo(String deviceName, String devicetAddress) {
        mDeviceName = deviceName;
        mDevicetAddress = devicetAddress;
    }

    public String getDeviceName() {
        return mDeviceName;
    }


    public String getDevicetAddress() {
        return mDevicetAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevicesInfo that = (DevicesInfo) o;
        return Objects.equals(mDevicetAddress, that.mDevicetAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mDevicetAddress);
    }
}

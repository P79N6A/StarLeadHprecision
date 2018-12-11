package com.starlead.starleadhprecision.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPSData {

    private static final String TAG = "GPSData";
    static Pattern mPattern = Pattern.compile("(\\$GPGGA,(\\d\\d)(\\d\\d)(\\d\\d).\\d+,(\\d+.\\d+),(\\w),(\\d+.\\d+),(\\w),(\\d),(.*?),(.*?),(.*?),(\\w),(.*?),(\\w),(.*?),(.*?)\\*(\\w\\w))");
    String Data;

    String Time;
    String latitude;                            //纬度
    String longitude;                           // 经度
    int GPSstate;                               //GPS状态
    String satelliteNumber;                     //卫星数量
    String Altitude;                             //海拔高度
    String DifferentialTime;                    //差分时间
    String DifferentialStationId;               //差分站ID

    public GPSData(String data) {
        Data = data;
        disPose();
    }


    public String getData() {
        return Data;
    }

    public String getTime() {
        return Time;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getSatelliteNumber() {
        return satelliteNumber;
    }

    public String getAltitude() {
        return Altitude;
    }

    public String getDifferentialTime() {
        return DifferentialTime;
    }

    public String getDifferentialStationId() {
        return DifferentialStationId;
    }

    public int getGPSstate() {
        return GPSstate;
    }

    private void disPose() {
        Matcher matcher = mPattern.matcher(Data);
        if (matcher.find()) {
            Data = matcher.group(1);
            Time = matcher.group(2) + ":" + matcher.group(3) + ":" + matcher.group(4);
            latitude = matcher.group(5);
            longitude = matcher.group(7);
            GPSstate = Integer.parseInt(matcher.group(9));
            satelliteNumber = matcher.group(10);
            Altitude = matcher.group(12);
            try {
                float number = Float.parseFloat(Altitude);
                Altitude = String.valueOf(number);
            } catch (Exception e) {
            }
            DifferentialTime = matcher.group(16);
            DifferentialStationId = matcher.group(17);



        }
    }

}

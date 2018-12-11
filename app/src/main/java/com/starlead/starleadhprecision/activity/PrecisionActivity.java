package com.starlead.starleadhprecision.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.MapViewListener;
import com.onlylemi.mapview.library.layer.BitmapLayer;
import com.onlylemi.mapview.library.layer.LocationLayer;
import com.starlead.starleadhprecision.GPSDatainfo;
import com.starlead.starleadhprecision.R;
import com.starlead.starleadhprecision.Service.BluetoothConnect;
import com.starlead.starleadhprecision.Service.BluetoothService;
import com.starlead.starleadhprecision.entity.GPSRelativePosition;

import java.io.IOException;

public class PrecisionActivity extends AppCompatActivity implements SensorEventListener {

    private MapView mapView;
    private TextView mLineDistance;

    private LatLng mCurrentLatlng;
    private LatLng mAimLatlng;
    private BluetoothConnect mBluetoothConnect;


    private LocationLayer locationLayer;

    private SensorManager sensorManager;
    private BitmapLayer bitmapLayer;

    private float mCircleDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precision);
        mBluetoothConnect = new BluetoothConnect(this, new GPSDatainfo() {
            @Override
            public void sendGPSData(LatLng latLng) {
                setPosition(latLng);
            }
        });
        if (mBluetoothConnect.getState() != BluetoothService.STATE_CONNECTED) {
            mBluetoothConnect.showDeviceDialog();
        }

        Intent intent = getIntent();
        if (intent != null) {
            mAimLatlng = intent.getParcelableExtra("AimLatlng");
        }


        mapView = (MapView) findViewById(R.id.mapview);
        mLineDistance= findViewById(R.id.distance_aim);

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getAssets().open("map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapView.loadMap(bitmap);
        mapView.setMapViewListener(new MapViewListener() {
            @Override
            public void onMapLoadSuccess() {
                locationLayer = new LocationLayer(mapView, new PointF(0, 0));
                locationLayer.setOpenCompass(true);
                mapView.addLayer(locationLayer);

                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.mark_touch);
                bitmapLayer = new BitmapLayer(mapView, bmp);
                bitmapLayer.setLocation(new PointF(1000, 1000));
                mapView.addLayer(bitmapLayer);
                mapView.refresh();
            }

            @Override
            public void onMapLoadFail() {

            }

        });


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor
                (Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mapView.isMapLoadFinish()) {
            float mapDegree = 0; // the rotate between reality map to northern
            float degree = 0;
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                degree = event.values[0];
            }

            locationLayer.setCompassIndicatorCircleRotateDegree(mapView
                    .getCurrentRotateDegrees() + mCircleDegree);
            locationLayer.setCompassIndicatorArrowRotateDegree(mapView
                    .getCurrentRotateDegrees() + degree);
            mapView.refresh();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        mBluetoothConnect.Detach();
    }

    public void setPosition(LatLng CurrentLatlng) {
        mCurrentLatlng = CurrentLatlng;
        GPSRelativePosition position = getPGSRelativePosition(mCurrentLatlng, mAimLatlng);
        mLineDistance.setText(String.valueOf((int)position.getDistance()));

        int bearing = position.getBearing();

        PointF pointF = null;
        switch (bearing) {
            case 3:
                mCircleDegree = 180.0f + position.getDegree();
                pointF = new PointF(1000 - position.getX(), 1000 - position.getY());
                break;
            case 4:
                mCircleDegree = 180.0f - position.getDegree();
                pointF = new PointF(1000 + position.getX(), 1000 - position.getY());
                break;
            case 6:
                mCircleDegree = -position.getDegree();
                pointF = new PointF(1000 - position.getX(), 1000 + position.getY());

                break;
            case 7:
                mCircleDegree = position.getDegree();
                pointF = new PointF(1000 + position.getX(), 1000 + position.getY());
                break;
        }

        locationLayer.setCurrentPosition(pointF);
        Toast.makeText(PrecisionActivity.this, "x:" + position.getX() + "  y:" + position.getY(), Toast.LENGTH_SHORT);
        //外圈角度
        locationLayer.setCompassIndicatorCircleRotateDegree(mCircleDegree);
        mapView.refresh();

    }

    private GPSRelativePosition getPGSRelativePosition(LatLng CurrentLatlng, LatLng AimLatlng) {
        return new GPSRelativePosition(AimLatlng, CurrentLatlng, this);
    }

}

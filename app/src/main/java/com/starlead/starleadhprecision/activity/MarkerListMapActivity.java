package com.starlead.starleadhprecision.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.starlead.starleadhprecision.Loader.PatrolSchemeLoader;
import com.starlead.starleadhprecision.R;
import com.starlead.starleadhprecision.SingletonLab;
import com.starlead.starleadhprecision.entity.Marker;
import com.starlead.starleadhprecision.entity.mapper.MarkerMapper;
import com.starlead.starleadhprecision.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class MarkerListMapActivity extends BaseActivity {


    private static final int MESSAGE_MARKER_DATA = 1;

    private AlertDialog mProgressDialog;
    private MapView mMapView;
    private AMap aMap = null;
    private LatLng mCurrentLatlng;
    private LatLng mAimLatlng;
    private boolean isObtained = false;
    private float mDistance;



    private PatrolSchemeLoader mLoader;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_MARKER_DATA:
                    ArrayList<Marker> patrolSchemelist = (ArrayList<Marker>) msg.obj;
                    addMarkersInMap(patrolSchemelist);
                    hideLoading();
                    break;

            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_list_map);
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        GetPosition();

        Intent intent = getIntent();
        int PatrolSchemeId = intent.getIntExtra("PatrolSchemeId", -1);
        String url = "Marker?method=getMarkers&patrolschemeid=" + PatrolSchemeId;
        mLoader = new PatrolSchemeLoader(mHandler);
        mLoader.start();
        mLoader.getLooper();
        mLoader.queuePatrolScheme(new MarkerMapper(), url, MESSAGE_MARKER_DATA);
        showLoading();

    }


    private void addMarkersInMap(final List<Marker> markerList) {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        CoordinateConverter converter = new CoordinateConverter(this);
        converter.from(CoordinateConverter.CoordType.GPS);
        ArrayList<MarkerOptions> markerOptionsArrayList = new ArrayList<>();

        for (Marker marker : markerList) {
            converter.coord(new LatLng(marker.getMarkerLat(), marker.getMarkerLng()));
            markerOptionsArrayList.add(new MarkerOptions().position(converter.convert()).snippet("" + marker.getMarkerId()));
        }

        final ArrayList<com.amap.api.maps.model.Marker> markers = aMap.addMarkers(markerOptionsArrayList, true);
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.amap.api.maps.model.Marker marker) {
                int id = Integer.parseInt(marker.getSnippet());
                Marker m = null;
                for (Marker mymarker : markerList) {
                    if (mymarker.getMarkerId() == id) {
                        m = mymarker;
                    }
                }
                SingletonLab singletonLab = SingletonLab.get();
                singletonLab.setAimMarker(m);



                if (isObtained) {
                    Intent intent = null;

                    CoordinateConverter converter = new CoordinateConverter(MarkerListMapActivity.this);
                    converter.from(CoordinateConverter.CoordType.GPS);
                    converter.coord(new LatLng(m.getMarkerLat(), m.getMarkerLng()));
                    LatLng aimlatlng = converter.convert();
                    mDistance = AMapUtils.calculateLineDistance(mCurrentLatlng, aimlatlng);
                    if (mDistance > 50.0) {
                        intent = new Intent(MarkerListMapActivity.this, NaviActivity.class);
                        intent.putExtra("AimLatlng", aimlatlng);
                        intent.putExtra("CurrentLatlng", mCurrentLatlng);
                    }else {
                        intent = new Intent(MarkerListMapActivity.this, PrecisionActivity.class);
                        LatLng latLng = new LatLng(m.getMarkerLat(), m.getMarkerLng());
                        intent.putExtra("AimLatlng", latLng);
                    }
                    startActivity(intent);
                }

                return true;
            }
        });


    }


    private void GetPosition() {
        //定位当前位置
        AMapLocationClient locationClientSingle = new AMapLocationClient(MarkerListMapActivity.this);
        locationClientSingle.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                mCurrentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                isObtained = true;
            }
        });
        AMapLocationClientOption locationClientSingleOption = new AMapLocationClientOption();
        locationClientSingleOption.setOnceLocation(true);
        locationClientSingleOption.setLocationCacheEnable(false);
        locationClientSingle.setLocationOption(locationClientSingleOption);
        locationClientSingle.startLocation();
    }



}

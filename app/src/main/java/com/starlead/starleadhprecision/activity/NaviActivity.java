package com.starlead.starleadhprecision.activity;

import android.content.Intent;
import android.os.Bundle;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.starlead.starleadhprecision.R;
import com.starlead.starleadhprecision.SingletonLab;

public class NaviActivity extends NaviBaseActivity {

    private LatLng mAimLatlng;
    private LatLng mCurrentLatlng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        Intent intent = getIntent();
        if (intent != null) {
            mAimLatlng = intent.getParcelableExtra("AimLatlng");
            mCurrentLatlng = intent.getParcelableExtra("CurrentLatlng");
        }

        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

        AMapNaviViewOptions options = new AMapNaviViewOptions();
        options.setScreenAlwaysBright(false);
        mAMapNaviView.setViewOptions(options);
        initNaviLatlng();
    }
    private void initNaviLatlng(){
        if (mAimLatlng != null && mCurrentLatlng != null) {
            sList.clear();
            eList.clear();
            sList.add(new NaviLatLng(mCurrentLatlng.latitude, mCurrentLatlng.longitude));
            eList.add(new NaviLatLng(mAimLatlng.latitude, mAimLatlng.longitude));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }


    @Override
    public void onArriveDestination() {
        Intent intent = new Intent(NaviActivity.this, PrecisionActivity.class);
        SingletonLab singletonLab = SingletonLab.get();
        com.starlead.starleadhprecision.entity.Marker marker = singletonLab.getAimMarker();
        LatLng latLng = new LatLng(marker.getMarkerLat(), marker.getMarkerLng());
        intent.putExtra("AimLatlng", latLng);
        startActivity(intent);
    }


    @Override
    public void onInitNaviSuccess() {

        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        super.onCalculateRouteSuccess(ints);
        mAMapNavi.startNavi(NaviType.GPS);
    }
}

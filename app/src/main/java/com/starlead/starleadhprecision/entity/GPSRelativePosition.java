package com.starlead.starleadhprecision.entity;

import android.app.Activity;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;


public class GPSRelativePosition {
    private LatLng mAimLat;
    private LatLng mCurrentlyLat;
    private float mDistance;
    private float mX;
    private float mY;
    private int mBearing = -1;
    private float mDegree = 0;
    private Activity mActivity;

    public GPSRelativePosition(LatLng AimLat, LatLng CurrentlyLat, Activity activity){
        this.mAimLat = AimLat;
        this.mCurrentlyLat = CurrentlyLat;
        this.mActivity = activity;
        GPSDisposal();
    }

    private void GPSDisposal() {

        CoordinateConverter converter = new CoordinateConverter(mActivity);
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(mAimLat);
        LatLng alatLng = converter.convert();
        converter.coord(mCurrentlyLat);
        LatLng clatLng = converter.convert();
        mDistance = AMapUtils.calculateLineDistance(alatLng, clatLng);

        converter.coord(new LatLng(mCurrentlyLat.latitude, mAimLat.longitude));
        LatLng tmpl = converter.convert();
        mX = AMapUtils.calculateLineDistance(tmpl, mCurrentlyLat);
        mY = AMapUtils.calculateLineDistance(tmpl, mAimLat);

        if (mAimLat.latitude - mCurrentlyLat.latitude < 0) {
            //加3
            mBearing += 3;

        } else {
            //加5
            mBearing += 6;
        }

        if (mAimLat.longitude - mCurrentlyLat.longitude > 0) {
            //加1
            mBearing += 1;
        } else {
            //加2
            mBearing += 2;
        }

        mDegree = (float) Math.cos(mDistance / mY);
    }



    public float getDistance() {
        return mDistance;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public int getBearing() {
        return mBearing;
    }

    public float getDegree() {
        return mDegree;
    }
}

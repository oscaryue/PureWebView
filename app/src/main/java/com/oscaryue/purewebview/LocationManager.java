package com.oscaryue.purewebview;

import android.content.Context;
import android.webkit.WebView;

import com.amap.api.location.AMapLocationClient;

public class LocationManager {
    private static final LocationManager ourInstance = new LocationManager();
    private AMapLocationClient mLocationClient;

    public static LocationManager getInstance() {
        return ourInstance;
    }

    private LocationManager() {

    }

    public void start(Context context) {
        if (context != null) {
            if (mLocationClient == null) {
                mLocationClient = new AMapLocationClient(context.getApplicationContext());
            }
            mLocationClient.startAssistantLocation();
        }
    }

    public void stop() {
        if (mLocationClient != null) {
            mLocationClient.stopAssistantLocation();
            mLocationClient.onDestroy();
            mLocationClient = null;
        }

    }
}

package com.oscaryue.purewebview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

/**
 * Created by hp on 2018/6/29.
 */

public class DeviceUtils {
    public static final String DEVICE_ID_UNKNOWN = "000000";

    private static final DeviceUtils ourInstance = new DeviceUtils();

    public static DeviceUtils getInstance() {
        return ourInstance;
    }

    private DeviceUtils() {
    }

    public String getDeviceId(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return DEVICE_ID_UNKNOWN;
        }
        final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return deviceId;
    }
}

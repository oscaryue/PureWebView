package com.oscaryue.purewebview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Created by hp on 2018/6/29.
 */

public class WebClientInterface {

    private static final String TAG = "WebClientInterface";

    private Context mContext = null;
    private Handler mHandler = null;

    public WebClientInterface(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
    }

    @JavascriptInterface
    public void getDeviceID() {
        mHandler.sendEmptyMessage(0);
    }
}

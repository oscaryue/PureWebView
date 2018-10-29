package com.oscaryue.purewebview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by oscaryue on 2018/10/5.
 */

public class WeChatManager {
    private static final String TAG = "WeChatManager";
    private static final String APP_ID = "wx9afec0b668828a8e";


    private static WeChatManager sInstance = null;
    private Context mContext = null;
    private IWXAPI mApi = null;
    private WeChatManager() {

    }

    public static WeChatManager getsInstance() {
        if (sInstance == null) {
            synchronized (WeChatManager.class) {
                if (sInstance == null) {
                    sInstance = new WeChatManager();
                }
            }
        }
        return sInstance;
    }

    public void register(Context context) {
        mContext = context;
        mApi = WXAPIFactory.createWXAPI(mContext, null);

        mApi.registerApp(APP_ID);
    }

    public void shareUrl(String url, String title, String description, boolean isTimeLine) {
        if (url == null || url.length() == 0) {
            Log.e(TAG, "URL is empty!!!");
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sharing_icon);
        msg.thumbData = DeviceUtils.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = isTimeLine ?
                SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

        mApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}

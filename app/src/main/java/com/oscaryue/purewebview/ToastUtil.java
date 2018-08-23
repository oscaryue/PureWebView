package com.oscaryue.purewebview;

import android.content.Context;
import android.widget.Toast;

//import com.sdsmdg.tastytoast.TastyToast;

public class ToastUtil {
    private static Toast mToast = null;

    public static void showMessage(final Context context, final String msg) {
        showMessage(context, msg, Toast.LENGTH_SHORT);
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    public static void showMessage(final Context ctx, final String msg, final int len) {
        if (mToast != null) {
            try {
                mToast.cancel();
            } catch (Exception e) {
            }
        }
        //Toast的初始化
        mToast = Toast.makeText(ctx, msg, len);
        mToast.setDuration(len);
        mToast.show();
    }

//    public static void showTastyMessage(Context appCtx, String msg) {
//        TastyToast.makeText(appCtx, msg, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
//    }
}
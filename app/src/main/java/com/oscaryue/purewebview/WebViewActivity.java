package com.oscaryue.purewebview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends Activity {

    private static final String URL_LOGGED_IN = "http://jct.zjol.com.cn:8084"; //"http://47.98.100.193:8084";
    private static final String URL_TEST_PAGE = "file:///android_asset/testpage.html";

    private WebView mWebView = null;

    private boolean mBackKeyDown = false;
    private Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        requestPermission();
        initUI();
        loadUrl();
    }

    private boolean requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
// 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(WebViewActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            return false;
        }
        return true;
    }

    private void initUI() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    String deviceId = DeviceUtils.getInstance().getDeviceId(WebViewActivity.this);
                    Log.d("WebViewActivity", "device ID = " + deviceId);
                    if (mWebView != null) {
                        mWebView.loadUrl("javascript:javacalljswithargs(" + deviceId + ")");
                    }
                }
            }
        };

        mWebView = findViewById(R.id.webview);
        mWebView.addJavascriptInterface(new WebClientInterface(this, mHandler), "android");//添加js监听 这样html就能调用客户端
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
//webview支持js脚本
        webSettings.setJavaScriptEnabled(true);

//启用数据库
        webSettings.setDatabaseEnabled(true);

//设置定位的数据库路径
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);

//启用地理定位
        webSettings.setGeolocationEnabled(true);

//开启DomStorage缓存
        webSettings.setDomStorageEnabled(true);

//配置权限
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);

            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);

            }
        });


        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
    }

    private void loadUrl() {
        if (mWebView != null) {
            if (!AccountManager.getInstance().isLoggedIn()) {
//                mWebView.loadUrl(URL_TEST_PAGE);
                mWebView.loadUrl(URL_LOGGED_IN);
            } else {

            }
        }
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
//            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("ansen", "拦截url:" + url);
            if (url.equals("http://www.google.com/")) {
                Toast.makeText(WebViewActivity.this, "国内不能访问google,拦截该url", Toast.LENGTH_LONG).show();
                return true;//表示我已经处理过了
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {


            return super.shouldOverrideUrlLoading(webView, request);
        }
    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i("ansen", "网页标题:" + title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
//            progressBar.setProgress(newProgress);
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                if (mBackKeyDown) {
                    finish();
                } else {
                    mBackKeyDown = true;
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mBackKeyDown = false;
                        }
                    }, 1000);
                    ToastUtil.showMessage(this.getApplicationContext(), "再次点击退出");
                }

                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
//            if (mBackKeyDown) {
//                finish();
//            } else {
//                mBackKeyDown = true;
//                mHandler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        mBackKeyDown = false;
//                    }
//                }, 1000);
//                ToastUtil.showMessage(this, "再次点击退出");
//            }
//
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    private boolean handleBackKey(WebView webView) {
//        if(webView.canGoBack()) {
//            //获取webView的浏览记录
//            WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
//            //这里的判断是为了让页面在有上一个页面的情况下，跳转到上一个html页面，而不是退出当前activity
//            if (mWebBackForwardList.getCurrentIndex() > 0) {
//                String historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - 1).getUrl();
//                if (!historyUrl.equals(url)) {
//                    webView.goBack();
//                    return true;
//                }
//            }
//        } else {
//            return true;
//        }
//    }

    private boolean handleBackKey(WebView webView) {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;

        } else {
            return false;
        }
    }
}

package com.jingjiu.webviewtransparent;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import static com.jingjiu.webviewtransparent.MainActivity.mWebViewClickable;

public class JavaScriptinterface {
    private Context mContext;

    public JavaScriptinterface(Context c) {
        mContext = c;
    }

    private static final String TAG = "JavaScriptinterface";
    /**
     * 与js交互时用到的方法，在js里直接调用的
     */
    @JavascriptInterface
    public void showToast(String ssss) {
        Log.i(TAG, "showToast: ");
        mWebViewClickable = true;
    }
}

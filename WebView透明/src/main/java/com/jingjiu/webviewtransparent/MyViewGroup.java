package com.jingjiu.webviewtransparent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * author xander on  2017/9/1.
 * function
 */

public class MyViewGroup extends LinearLayout {

    MotionEvent mMotionEvent;
    private boolean stop;
    public MyViewGroup(final Context context) {
        super(context);
    }

    public MyViewGroup(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    private static final String TAG = "MyViewGroup";
    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        final WebView webView = (WebView) getChildAt(0);
        float x = ev.getX();
        float y = ev.getY();
        Log.i(TAG, "onReceiveValue: " + x);
        Log.i(TAG, "onReceiveValue: " + y);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("(")
                .append("document.elementFromPoint(")
                .append(x)
                .append(",")
                .append(y)
                .append(").click !== null")
                .append(").toString()");
        String jsStr =  stringBuilder.toString();
        webView.evaluateJavascript(jsStr, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(final String value) {
                Log.i(TAG, "onReceiveValue: " + Thread.currentThread().getName());
                if (value.equals("\"true\"")&&!stop) {
                    mMotionEvent = ev;
//                    imitateTouchEvent(mMotionEvent);
                    stop = true;
                }
            }
        });
        Log.i(TAG, "onTouchEvent: jsStr = "+jsStr);
        return false;
    }
}

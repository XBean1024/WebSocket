package com.jingjiu.webviewtransparent;

import android.content.Context;
import android.os.Handler;
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


        return super.dispatchTouchEvent(ev);
    }
}

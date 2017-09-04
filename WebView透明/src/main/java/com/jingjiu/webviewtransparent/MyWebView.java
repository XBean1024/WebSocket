package com.jingjiu.webviewtransparent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.ValueCallback;
import android.webkit.WebView;

public class MyWebView extends WebView {
    private static final String TAG = "MyWebView";
    MotionEvent mMotionEvent;
    private boolean stop;

    public MyWebView(Context context, AttributeSet attrs, int defStyle,
                     boolean privateBrowsing) {
        super(context, attrs, defStyle, privateBrowsing);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        super.onTouchEvent(ev);
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
                        .append(").hasAttribute('onclick')")
                .append(").toString()");
        String jsStr =  stringBuilder.toString();
        Log.i(TAG, "onTouchEvent: jsStr = "+jsStr);
        evaluateJavascript(jsStr, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(final String value) {
                Log.i(TAG, "onReceiveValue: " + value);
                if (value.equals("\"true\"")&&!stop) {
                    mMotionEvent = ev;
                    imitateTouchEvent(mMotionEvent);
                    stop = true;
                }
            }
        });
        return false;//不处理点击事件
    }

    private void imitateTouchEvent(final MotionEvent motionEvent) {
        Log.i(TAG, "imitateTouchEvent: ");
        super.dispatchTouchEvent(motionEvent);
    }

//	@Override
//	public boolean onTouch(final View v, final MotionEvent event) {
//		Log.i(TAG, "onTouch: ");
//		switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//			case MotionEvent.ACTION_UP:
//				if (!v.hasFocus()) {
//					v.requestFocus();
//				}
//				break;
//		}
//		return false;
//	}
}

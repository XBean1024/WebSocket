package com.jingjiu.webviewtransparent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.ValueCallback;
import android.webkit.WebView;

public class MyWebView extends WebView {
    private static final String TAG = "MyWebView";
    private final String mClick = "mClick";
    private final String mImitate_web = "web";
    private final String mImitate_video = "video";
    private String mTag = mClick;
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

    public boolean onTouchEvent1(final MotionEvent ev) {
        return false;
    }

//    @Override
    public boolean onTouchEvent(final MotionEvent ev) {

        float x = ev.getX();
        float y = ev.getY();
        if (mTag.equals(mImitate_video)) {
            Log.i(TAG, "模拟 video 事件： "+ev.toString() );
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                mTag = mClick;
            }

            return false;
        }else if (mTag.equals(mImitate_web)) {
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                mTag = mClick;
            }
            Log.i(TAG, "模拟 web 事件： "+ev.toString() );
            super.onTouchEvent(ev);

            return false;
        }else {
            String jsStr3 = "document.elementFromPoint(" + x + "," + y + ").hasAttribute('onclick');";
            evaluateJavascript(jsStr3, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(final String value) {
                    Log.i(TAG, "返回值: "+value);
                    if (!value.equals("true")) {
                        mTag = mImitate_video;
                    }else {
                        mTag = mImitate_web;
                    }
                    imitateTouchEvent(ev);
                }
            });
            return true;
        }

    }

    private void imitateTouchEvent(final MotionEvent motionEvent) {
        Log.i(TAG, "准备模拟 "+mTag+" 事件: ");
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

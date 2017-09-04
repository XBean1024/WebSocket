package com.jingjiu.webviewtransparent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class MyWebView extends WebView {
	private static final String TAG = "MyWebView";
	public MyWebView(Context context, AttributeSet attrs, int defStyle,
					 boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
		// TODO Auto-generated constructor stub
	}

	public MyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
		return super.onTouchEvent(ev);


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

package com.jingjiu.webviewtransparent;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.VideoView;

import com.jingjiu.webviewtransparent.websocket.WebSocketClient;

import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    private VideoView mVideoView;
    private WebView mWebView;
    private String path ="http://7xr5j6.com1.z0.glb.clouddn.com/hunantv0129.mp4?v=1021";
    public static boolean mWebViewClickable;//不消费
    private static final String TAG = "MainActivity";

    WebSocketClient client;
    private Handler mHandler = new Handler();
    private String html ="<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <script>\n" +
            "        function myFunction()\n" +
            "        {\n" +
            "            android.showToast(\"哈哈啊哈 \");\n" +
            "            alert(\"My First JavaScript Function\");\n" +
            "        }\n" +
            "    </script>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "<button type=\"button\" onclick=\"myFunction()\">点击这里</button>\n" +
            "</body>\n" +
            "</html>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start();
        mVideoView = (VideoView) findViewById(R.id.video_view);

        mVideoView.setVideoURI(Uri.parse(path));
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.start();

        mWebView = (WebView) findViewById(R.id.web_view);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8") ;
        mWebView.setBackgroundColor(Color.TRANSPARENT); // 设置背景色
        mWebView.addJavascriptInterface(new JavaScriptinterface(this),
                "android");
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (v.hasFocus()) {
                            Log.i(TAG, "onTouch: " +v.getWidth());
                        }
                        break;
                }
                return false;
            }
        });
        mWebView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(final View parent, final View child) {
                Log.i(TAG, "child: "+child.getWidth());
                Log.i(TAG, "parent: "+parent.getWidth());
            }

            @Override
            public void onChildViewRemoved(final View parent, final View child) {

            }
        });
        mWebView.setFindListener(new WebView.FindListener() {
            @Override
            public void onFindResultReceived(final int i, final int i1, final boolean b) {
                Log.i(TAG, "onFindResultReceived: "+i);
            }
        });
        mWebView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                Log.i(TAG, "onFocusChange: "+v.getWidth());
            }
        });
        mWebView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(final View v, final MotionEvent event) {
                Log.i(TAG, "onGenericMotion: "+v.getWidth());
                return false;
            }
        });

        mWebView.getBackground().setAlpha(180); // 设置填充透明度 范围：0-255

//        mWebView.setVisibility(View.GONE); // 加载完之后进行设置显示，以免加载时初始化效果不好看
    }
    private void start() {
        List<BasicNameValuePair> extraHeaders = Arrays.asList(
                new BasicNameValuePair("Cookie", "session=abcd")
        );

        client = new WebSocketClient(URI.create("ws://echo.websocket.org"), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
                Log.d(TAG, "Connected!");
            }

            @Override
            public void onMessage(final String message) {
                Log.d(TAG, String.format("Got string message! %s", message));
             mHandler.post(new Runnable() {
                 @Override
                 public void run() {
                     mWebView.loadDataWithBaseURL(null, message, "text/html", "utf-8",null);
                     mWebView.setWebViewClient(new WebViewClient(){
                         @Override
                         public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                             if (view.hasFocus()) {
                                 Log.i(TAG, "shouldOverrideUrlLoading: "+view.getWidth());
                             }
                             return super.shouldOverrideUrlLoading(view, url);
                         }
                     });
                     mWebView.setWebChromeClient(new WebChromeClient());
                 }
             });
            }

            @Override
            public void onMessage(byte[] data) {
                Log.d(TAG, String.format("Got binary message! %s", new String(data)));
            }

            @Override
            public void onDisconnect(int code, String reason) {
                Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
            }

            @Override
            public void onError(Exception error) {
                Log.e(TAG, "Error!", error);
            }
        }, extraHeaders);

        client.connect();

// Later…

//

    }

    public void close(View view) {
        client.disconnect();
    }

    public void connect(View view) {
        client.send(html);
    }

    public void start(View view) {
    }
}

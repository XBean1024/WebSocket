package com.jingjiu.webviewtransparent;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
            "\n" +
            "        //获取控件左绝对位置\n" +
            "\n" +
            "        function getAbsoluteLeft(objectId) {\n" +
            "            o = document.getElementById(objectId)\n" +
            "            oLeft = o.offsetLeft\n" +
            "            while(o.offsetParent!=null) {\n" +
            "                oParent = o.offsetParent\n" +
            "                oLeft += oParent.offsetLeft\n" +
            "                o = oParent\n" +
            "            }\n" +
            "            return oLeft\n" +
            "        }\n" +
            "        //获取控件上绝对位置\n" +
            "        function getAbsoluteTop(objectId) {\n" +
            "            o = document.getElementById(objectId);\n" +
            "            oTop = o.offsetTop;\n" +
            "            while(o.offsetParent!=null)\n" +
            "            {\n" +
            "                oParent = o.offsetParent\n" +
            "                oTop += oParent.offsetTop  // Add parent top position\n" +
            "                o = oParent\n" +
            "            }\n" +
            "            return oTop\n" +
            "        }\n" +
            "\n" +
            "        //获取控件宽度\n" +
            "\n" +
            "        function getElementWidth(objectId) {\n" +
            "            x = document.getElementById(objectId);\n" +
            "            return x.offsetWidth;\n" +
            "        }\n" +
            "    </script>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "\n" +
            "<h1 style=\"color:white;\">My Web Page</h1>\n" +
            "\n" +
            "<p id=\"demo\" style=\"color:white;\">A Paragraph.</p>\n" +
            "\n" +
            "<button type=\"button\" id=\"button\" onclick=\"getElementWidth(button)\">点击这里</button>\n" +
            "\n" +
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
                if (!v.hasFocus()) {
                    v.requestFocus();
                    Log.i(TAG, "onTouch: aaaaaaaaaa" +v.getAlpha());
                }
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

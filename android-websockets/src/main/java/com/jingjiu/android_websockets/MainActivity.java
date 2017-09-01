package com.jingjiu.android_websockets;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jingjiu.android_websockets.websocket.WebSocketClient;

import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    WebSocketClient client;
    String html = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Title</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<form name=\"atool_alipay_img_form\" style=\"padding-bottom: 0;border:none;\" method=\"post\"\n" +
            "      action=\"https://shenghuo.alipay.com/send/payment/fill.htm\" target=\"_blank\" accept-charset=\"GBK\"\n" +
            "      onsubmit=\"document.charset='gbk';\">\n" +
            "    <input type=\"hidden\" value=\"18221869775\" name=\"optEmail\">\n" +
            "    <input type=\"hidden\" name=\"title\" placeholder=\"付款说明\" value=\"\">\n" +
            "    <input type=\"image\" value=\"支付宝收款\" src=\"http://www.atool.org/res/alipay_1.png\" name=\"pay\">\n" +
            "</form>\n" +
            "<form name=\"atool_alipay_text_form\" style=\"padding-bottom: 0;border:none;\" method=\"post\"\n" +
            "      action=\"https://shenghuo.alipay.com/send/payment/fill.htm\" target=\"_blank\" accept-charset=\"GBK\"\n" +
            "      onsubmit=\"document.charset='gbk';\">\n" +
            "    <input type=\"hidden\" value=\"18221869775\" name=\"optEmail\"/>\n" +
            "    <input type=\"hidden\" value=\"1000\"name=\"payAmount\"/>\n" +
            "    <input type=\"hidden\" name=\"title\" placeholder=\"付款说明\" value=\"\"/>\n" +
            "    <a href=\"javascript:javascript:document.atool_alipay_text_form.submit();\" title=\"支付宝收款\" value=\"支付宝收款\" name=\"pay\">向我捐款</a>\n" +
            "</form>\n" +
            "</body>\n" +
            "</html>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {
        List<BasicNameValuePair> extraHeaders = Arrays.asList(
                new BasicNameValuePair("Cookie", "session=abcd")
        );

        client = new WebSocketClient(URI.create("ws://192.168.200.162:8181/"), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
                Log.d(TAG, "Connected!");
            }

            @Override
            public void onMessage(String message) {
                Log.d(TAG, String.format("Got string message! %s", message));
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
}

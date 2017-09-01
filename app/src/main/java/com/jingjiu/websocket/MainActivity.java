package com.jingjiu.websocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class MainActivity extends AppCompatActivity {
    private Button mStart;
    private TextView mOuput;
    private OkHttpClient mOkHttpClient;

    private final class EchoWebsocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        /*
        * 建立链接时触发*/
        @Override
        public void onOpen(final WebSocket webSocket, final Response response) {
            webSocket.send("Hello it's");
            webSocket.send("hhhhh");
            webSocket.send(ByteString.decodeHex("deadbeef"));
            webSocket.close(NORMAL_CLOSURE_STATUS, "good bey");
        }

        /*
        * 接受到消息时候的回调函数
        * */
        @Override
        public void onMessage(final WebSocket webSocket, final ByteString bytes) {
            output("recrive msg :" + bytes.hex());
        }

        @Override
        public void onMessage(final WebSocket webSocket, final String text) {
            output("recrive msg :" + text);
        }

        @Override
        public void onClosed(final WebSocket webSocket, final int code, final String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onClosing(final WebSocket webSocket, final int code, final String reason) {
            output("onClosing code:" + code+" / " +reason);
        }

        @Override
        public void onFailure(final WebSocket webSocket, final Throwable t, final Response response) {
            output("onFailure" + t.getMessage());
        }
    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mOuput.setText(mOuput.getText().toString()+"\n\n"+txt);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStart = (Button) findViewById(R.id.start);
        mOuput = (TextView) findViewById(R.id.output);
        mOkHttpClient = new OkHttpClient();
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                start();
            }
        });
    }

    private void start() {
        Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        EchoWebsocketListener listener = new EchoWebsocketListener();
        WebSocket webSocket = mOkHttpClient.newWebSocket(request,listener);
        mOkHttpClient.dispatcher().executorService().shutdown();
    }
}

# WebSocket
First step is to add the OkHttp dependency in your Gradle build file :

	compile 'com.squareup.okhttp3:okhttp:3.6.0'

Don’t forget to add the Internet permission in your Android manifest since the application will use the network to create a WebSocket connection to the Echo WebSocket server. For this tutorial, we will need a simple layout with a Button to start the connection and the exchange with the server and a TextView which will be used as a console output for the messages received from the server :

	<?xml version="1.0" encoding="utf-8"?>
	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
	  xmlns:tools="http://schemas.android.com/tools"
	  android:id="@+id/activity_main"
	  android:layout_width="match_parent"
	  android:layout_height="match_parent"
	  android:paddingBottom="@dimen/activity_vertical_margin"
	  android:paddingLeft="@dimen/activity_horizontal_margin"
	  android:paddingRight="@dimen/activity_horizontal_margin"
	  android:paddingTop="@dimen/activity_vertical_margin"
	  tools:context="com.ssaurel.websocket.MainActivity">

	  <Button
	    android:id="@+id/start"
	    android:layout_width="wrap_content"
	    android:layout_height="wrap_content"
	    android:layout_centerHorizontal="true"
	    android:text="Start !"
	    android:layout_marginTop="40dp"
	    android:textSize="17sp"/>

	  <TextView
	    android:id="@+id/output"
	    android:layout_width="wrap_content"
	    android:layout_height="wrap_content"
	    android:layout_below="@id/start"
	    android:layout_centerHorizontal="true"
	    android:textSize="16sp"
	    android:layout_marginTop="30dp"/>

	</RelativeLayout>

Then, we can write the Java code of the application. The main part will be the method used to create the connection to the WebSocket connection and the WebSocketListener object used to exchange with the server :

	private final class EchoWebSocketListener extends WebSocketListener {
	  private static final int NORMAL_CLOSURE_STATUS = 1000;

	  @Override
	  public void onOpen(WebSocket webSocket, Response response) {
	    webSocket.send("Hello, it's SSaurel !");
	    webSocket.send("What's up ?");
	    webSocket.send(ByteString.decodeHex("deadbeef"));
	    webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
	  }

	  @Override
	  public void onMessage(WebSocket webSocket, String text) {
	    output("Receiving : " + text);
	  }

	  @Override
	  public void onMessage(WebSocket webSocket, ByteString bytes) {
	    output("Receiving bytes : " + bytes.hex());
	  }

	  @Override
	  public void onClosing(WebSocket webSocket, int code, String reason) {
	    webSocket.close(NORMAL_CLOSURE_STATUS, null);
	    output("Closing : " + code + " / " + reason);
	  }

	  @Override
	  public void onFailure(WebSocket webSocket, Throwable t, Response response) {
	    output("Error : " + t.getMessage());
	  }
	}

We send messages to the server in the onOpen method. The messages received from the Echo WebSocket server are displayed inside the onMessage method. Note that you can send text or hexadecimal messages. Lastly, we close the connection by using the close method of the WebSocket object. To create the WebSocket connection with OkHttp, we need to build a Request object with the URL of the Echo WebSocket server in parameter, then calling the newWebSocket method of the OkHttpClient object.

The code will have the following form :

	package com.ssaurel.websocket;

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

	  private Button start;
	  private TextView output;
	  private OkHttpClient client;

	  private final class EchoWebSocketListener extends WebSocketListener {
	    private static final int NORMAL_CLOSURE_STATUS = 1000;

	    @Override
	    public void onOpen(WebSocket webSocket, Response response) {
	      webSocket.send("Hello, it's SSaurel !");
	      webSocket.send("What's up ?");
	      webSocket.send(ByteString.decodeHex("deadbeef"));
	      webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
	    }

	    @Override
	    public void onMessage(WebSocket webSocket, String text) {
	      output("Receiving : " + text);
	    }

	    @Override
	    public void onMessage(WebSocket webSocket, ByteString bytes) {
	      output("Receiving bytes : " + bytes.hex());
	    }

	    @Override
	    public void onClosing(WebSocket webSocket, int code, String reason) {
	      webSocket.close(NORMAL_CLOSURE_STATUS, null);
	      output("Closing : " + code + " / " + reason);
	    }

	    @Override
	    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
	      output("Error : " + t.getMessage());
	    }
	  }

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    start = (Button) findViewById(R.id.start);
	    output = (TextView) findViewById(R.id.output);
	    client = new OkHttpClient();

	    start.setOnClickListener(new View.OnClickListener() {
	      @Override
	      public void onClick(View view) {
	        start();
	      }
	    });

	  }

	  private void start() {
	    Request request = new Request.Builder().url("ws://echo.websocket.org").build();
	    EchoWebSocketListener listener = new EchoWebSocketListener();
	    WebSocket ws = client.newWebSocket(request, listener);

	    client.dispatcher().executorService().shutdown();
	  }

	  private void output(final String txt) {
	    runOnUiThread(new Runnable() {
	      @Override
	      public void run() {
	        output.setText(output.getText().toString() + "\n\n" + txt);
	      }
	    });
	  }
	}

Finally, you have just to run your application and enjoy the result :

![](https://github.com/XBean1024/WebSocket/blob/master/video/1.png)

 To discover more tutorials on Android Development, don’t hesitate to visit the SSaurel’s blog : https://www.ssaurel.com/blog/

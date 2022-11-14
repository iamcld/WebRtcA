package com.example.webrtca;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

// 音视频通话客户端
public class SocketLiveClient {
    private static final String TAG = SocketLiveClient.class.getSimpleName();
    private SocketCallback socketCallback;
    private MyWebSocketClient myWebSocketClient;

    public SocketLiveClient(SocketCallback socketCallback) {
        this.socketCallback = socketCallback;

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void start() {
        // 此地址为手机的ip地址。2台设备需要连接同一个wifi，保持同一个网段
        try {
            URI url = new URI("ws://192.168.3.186:9007");
            myWebSocketClient = new MyWebSocketClient(url);
            myWebSocketClient.connect();
            Log.d(TAG, "connect success" );
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "connect fail" );
        }
    }

    public void sendData(byte[] bytes) {
        if (myWebSocketClient != null && myWebSocketClient.isOpen()) {
            myWebSocketClient.send(bytes);
        }
    }


    // 客户端，负责接收服务端A设备上发送的投屏的数据。B设备为客户端，可以看到A设备的投屏画面

    private class MyWebSocketClient extends WebSocketClient {

        public MyWebSocketClient(URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            Log.d(TAG, "onOpen() ---->" );

        }

        @Override
        public void onMessage(String message) {
            Log.d(TAG, "onMessage() ---->" );
        }

        // 其他设备发消音视频数据过来
        @Override
        public void onMessage(ByteBuffer bytes) {
            Log.d(TAG, "消息长度：" + bytes.remaining());
            byte[] buf = new byte[bytes.remaining()];
            socketCallback.callBack(buf);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.d(TAG, "onClose() ---->" );

        }

        @Override
        public void onError(Exception ex) {

        }
    }

    public interface SocketCallback {
        void callBack(byte[] data);
    }
}

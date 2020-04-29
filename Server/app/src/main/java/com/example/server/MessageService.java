package com.example.server;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author LessHair
 * @Date 2020/04/29
 * @Description:
 */
public class MessageService extends Service {
    private static final String TAG = "MessageService";
    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.i("TAG","we have message!");
            switch (msg.what){
                case 1:
                    Log.i(TAG,"receive message from client : " + msg.getData().getString("msg"));
                    break;
                case 2:
                    Log.i(TAG,"new message from client : "+ msg.getData().getString("new"));
                    break;
                default:
                    super.handleMessage(msg);
            }

            Messenger client = msg.replyTo;
            Message replyMessage = Message.obtain(null,3);
            Bundle bundle = new Bundle();
            bundle.putString("reply","this is the reply from server!I got the message!");
            replyMessage.setData(bundle);
            try {
                client.send(replyMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private Messenger mMessenger = new Messenger(new MessengerHandler());
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}

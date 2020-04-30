package com.example.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.client.bean.Book;
import com.example.client.databinding.ActivityMainBinding;
import com.example.server.IEasyService;
import com.example.server.IOnNewBookArrivedListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ActivityMainBinding binding;
    private static final String ACTION = "com.example.IEasyService";
    //服务端包名
    private static final String PACKAGE = "com.example.server";
    private IEasyService mIeasyService;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            switch (msg.what){
                case 4:
                    Log.d(TAG,"receive new Book : "+msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    /**
     * 获取AIDL接口实例
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        /**
         * 服务连接时初始化IEasyService对象
         * @param name
         * @param service 这个就是Binder对象，是Server端传过来的
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            /**
             * 通过AsInterface判断是否在同一进程，如果不在同一进程就将Binder对象包装成Proxy。
             * 这其实是一种设计模式叫做代理-桩模式。
             * Stub是服务器的代理，Proxy是Stub的代理。
             */
            mIeasyService = IEasyService.Stub.asInterface(service);
            try {
                mIeasyService.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            //给service也就是Binder对象设置死亡代理
            try {
                service.linkToDeath(mDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIeasyService = null;
        }
    };

    private IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(4,newBook).sendToTarget();
        }
    };

    /**
     * 初始化远程Service的死亡代理
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if(mIeasyService==null){
                return;
            }

            mIeasyService.asBinder().unlinkToDeath(mDeathRecipient,0);
            mIeasyService = null;

            //重新绑定远程Service
            Intent intent = new Intent(ACTION);
            //这的PACKAGE是服务端的包名，android5.0之后不允许匿名启动Service
            intent.setPackage(PACKAGE);
            bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    };

    private Messenger mService;
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());
    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 3:
                    Log.i(TAG,"receive message from server! "+ msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);

            }
        }
    };
    private ServiceConnection msgServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain(null,1);
            Bundle data = new Bundle();
            data.putString("msg","hello this is the message from the client.");
            msg.setData(data);
            msg.replyTo = mGetReplyMessenger;

            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        init();
    }

    @Override
    protected void onDestroy() {
       if(mIeasyService!=null && mIeasyService.asBinder().isBinderAlive()){
           Log.i(TAG,"unregister listener : "+listener);
           try {
               mIeasyService.unRegisterListener(listener);
           } catch (RemoteException e) {
               e.printStackTrace();
           }
       }
       unbindService(msgServiceConnection);
       unbindService(mServiceConnection);
       super.onDestroy();
    }

    private void init() {
        binding.btnStartService.setOnClickListener(view->{
            Log.i(TAG,"onButtonClick :  btn_start_service ");
            //ACTION为server端在manifest中注册时的ACTION
            Intent intent = new Intent(ACTION);
            //这的PACKAGE是服务端的包名，android5.0之后不允许匿名启动Service
            intent.setPackage(PACKAGE);
            bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
        });

        binding.btnContact.setOnClickListener(view->{
            Log.i(TAG,"onButtonClick : btn_contact = ");
            if(mIeasyService!=null){
                try {
                    String result = mIeasyService.getVal();
                    result += " " + mIeasyService.getBookName();
                    Log.i(TAG,result);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.btnBindMessenger.setOnClickListener(view->{
            String ACTION = "com.example.MessageService";
            //服务端包名
            String PACKAGE = "com.example.server";
            Intent intent = new Intent(ACTION);
            intent.setPackage(PACKAGE);
            bindService(intent,msgServiceConnection,Context.BIND_AUTO_CREATE);
        });

        binding.btnSendMessage.setOnClickListener(view->{
            Message message = Message.obtain(null,2);
            Bundle bundle = new Bundle();
            bundle.putString("new","hello again!");
            message.setData(bundle);
            message.replyTo = mGetReplyMessenger;

            try {
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}

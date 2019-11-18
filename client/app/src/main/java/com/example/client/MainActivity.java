package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.client.databinding.ActivityMainBinding;
import com.example.server.IEasyService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ActivityMainBinding binding;
    private static final String ACTION = "com.example.IEasyService";
    //服务端包名
    private static final String PACKAGE = "com.example.server";
    private IEasyService mIeasyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        init();
    }

    private void init() {
        /**
         * 获取AIDL接口实例
         */
        ServiceConnection mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mIeasyService = IEasyService.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIeasyService = null;
            }
        };

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
                    Log.i(TAG,result);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

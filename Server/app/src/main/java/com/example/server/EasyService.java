package com.example.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

public class EasyService extends Service {

    private static final String TAG = EasyService.class.getName();

    IEasyService.Stub mIBinder = new IEasyService.Stub() {
        @Override
        public String getVal() throws RemoteException {
            return "testService";
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind: intent = " + intent.toString());
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG,"onUnBind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy: ");
        super.onDestroy();
    }
}
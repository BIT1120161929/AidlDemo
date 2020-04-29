package com.example.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.server.bean.Book;

public class EasyService extends Service {

    private static final String TAG = EasyService.class.getName();

    private Book book = new Book("test_Name");
    /**
     * 完全可以不使用AIDL而自己手写对应的接口然后在onBind中返回即可
     */
    IEasyService.Stub mIBinder = new IEasyService.Stub() {
        @Override
        public String getVal() throws RemoteException {
            return "testService";
        }

        @Override
        public String getBookName() throws RemoteException {
            return book.getName();
        }

        @Override
        public void setBookName() throws RemoteException {

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

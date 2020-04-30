package com.example.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.server.bean.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EasyService extends Service {

    private static final String TAG = EasyService.class.getName();

    private Book book = new Book("test_Name");

    List<Book> bookList = new CopyOnWriteArrayList<>();
    List<IOnNewBookArrivedListener> listeners = new CopyOnWriteArrayList<>();
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
            book.setName("changeName");
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if(!listeners.contains(listener)){
                listeners.add(listener);
            }else{
                Log.d(TAG,"already exist!");
            }

            Log.d(TAG,"register, registerListener size : "+listeners.size());
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if(listeners.contains(listener)){
                listeners.remove(listener);
            }else{
                Log.d(TAG,"not found!");
            }

            Log.d(TAG,"unregister, registerListener size : "+listeners.size());
        }


    };

    @Override
    public void onCreate() {
        super.onCreate();
        bookList.add(new Book("Android"));
        bookList.add(new Book("IOS"));
        new Thread(new ServiceWorker()).start();
    }

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


    private void onNewBookArrived(Book book) throws RemoteException{
        bookList.add(book);
        Log.d(TAG,"onNewBookArrived, notify listeners : "+listeners.size());

        for (IOnNewBookArrivedListener listener : listeners) {
            listener.onNewBookArrived(book);
            Log.d(TAG,"onNewBookArrived, notify listeners : "+listener);
        }
    }

    private class ServiceWorker implements Runnable{

        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Book newBook = new Book("book#"+(bookList.size()+1));
                Log.i(TAG,"add a new book : "+newBook);
                try {
                    onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

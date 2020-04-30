// IOnNewBookArrivedListener.aidl
package com.example.server;

import com.example.server.bean.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}

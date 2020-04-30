// IEasyService.aidl
package com.example.server;

// Declare any non-default types here with import statements

import com.example.server.IOnNewBookArrivedListener;
import com.example.server.bean.Book;

interface IEasyService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getVal();

    String getBookName();

    void setBookName();

    void addBook(in Book book);

    void registerListener(IOnNewBookArrivedListener listener);

    void unRegisterListener(IOnNewBookArrivedListener listener);
    }

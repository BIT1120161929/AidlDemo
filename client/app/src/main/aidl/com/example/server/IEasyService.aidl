// IEasyService.aidl
package com.example.server;

// Declare any non-default types here with import statements

interface IEasyService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getVal();

    String getBookName();

    void setBookName();
    }

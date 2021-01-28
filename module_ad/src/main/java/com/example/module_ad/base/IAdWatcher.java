package com.example.module_ad.base;

public interface IAdWatcher<T> {

     void showAd();

    void releaseAd();

    void setOnShowError(T t);

}

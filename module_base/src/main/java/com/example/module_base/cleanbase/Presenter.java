package com.example.module_base.cleanbase;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}

package com.example.module_base.cleanbase;

import java.lang.ref.WeakReference;


/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 */

public abstract class BasePresenter<V> implements Presenter<V> {

    protected WeakReference<V> mMvpView;

    @Override
    public void attachView(V mvpView) {
        this.mMvpView = new WeakReference<>(mvpView);
    }


    protected V getView() {
        return mMvpView.get();
    }


    @Override
    public void detachView() {
        if (mMvpView != null) {
            mMvpView.clear();
            mMvpView = null;
        }
    }
}
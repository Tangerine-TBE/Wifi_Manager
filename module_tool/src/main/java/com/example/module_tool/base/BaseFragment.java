package com.example.module_tool.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.module_tool.R;
import com.example.module_tool.widget.loaddialog.LoadingDialog;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 * 普通fragment
 */
public abstract class BaseFragment extends RxFragment {
    public Activity mActivity;
    protected View rootView;

    protected abstract int getLayoutId();

    protected LoadingDialog loadingDialog;//正在加载

//    protected DaoSession daoSession;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_base, container, false);
        ((ViewGroup) rootView.findViewById(R.id.fl_content)).addView(getLayoutInflater().inflate(getLayoutId(), null));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
        loadingDialog = new LoadingDialog(mActivity);
//        daoSession = BaseApplication.getDaoSession();

        initView();
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getEvent() {
//
//    }

    /**
     * 初始化View
     */
    protected void initView() {
    }

    public void showLoading() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void visible(View... views) {
        for (View view : views) {
            if (view != null)
                view.setVisibility(View.VISIBLE);
        }
    }

    public void gone(View... views) {
        for (View view : views) {
            if (view != null)
                view.setVisibility(View.GONE);
        }
    }

    public void invisible(View... views) {
        for (View view : views) {
            if (view != null)
                view.setVisibility(View.INVISIBLE);
        }
    }
}

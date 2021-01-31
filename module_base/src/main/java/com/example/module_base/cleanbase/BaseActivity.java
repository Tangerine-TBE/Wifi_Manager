package com.example.module_base.cleanbase;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentActivity;

import com.example.module_base.R;
import com.example.module_base.widget.LoadingDialog;
import com.gyf.immersionbar.ImmersionBar;


/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 * 普通activity
 * 需要actionBar时，返回true，默认false
 */
public abstract class BaseActivity extends FragmentActivity {
    public Activity mContext;

    protected ImmersionBar mImmersionBar;
    //    protected DaoSession daoSession;
    protected LoadingDialog loadingDialog;//正在加载
    protected ActionBar actionBar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mContext = this;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setCancelable(cancelDialog());
        if (isActionBar()) {
            setContentView(R.layout.activity_base);
            ((ViewGroup) findViewById(R.id.fl_content)).addView(getLayoutInflater().inflate(getLayoutId(), null));
            actionBar = findViewById(R.id.actionbar);
            actionBar.setVisibility(View.VISIBLE);
        } else {
           setContentView(getLayoutId());
        }
        //沉浸式状态栏
        initImmersionBar(getStatusBarColor());
        initView();
        initData();
//        daoSession = BaseApplication.getDaoSession();


    }

    protected int getStatusBarColor() {
        return android.R.color.transparent;
    }

    /**
     * 沉浸栏颜色
     */
    protected void initImmersionBar(int color) {
        mImmersionBar = ImmersionBar.with(this);
        if (color != 0) {
            mImmersionBar.statusBarColor(color);
        }
        mImmersionBar.statusBarDarkFont(isStatusBarDarkFont());
        mImmersionBar.init();
    }

    protected boolean isStatusBarDarkFont(){
        return false;
    }

    protected abstract int getLayoutId();


    public void showLoading() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing())
                loadingDialog.dismiss();
        }
    }

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 是否需要ActionBar
     */
    protected boolean isActionBar() {
        return false;
    }

    protected boolean cancelDialog() {
        return false;
    }

    public void visible(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void invisible(View... views) {
        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void receive(Event event) {
//
//    }

    protected void setTitleText(String title) {
        if (actionBar != null) {
            actionBar.setCenterText(title);
        }
    }

    protected void setTransparentActionBar() {
        if (actionBar != null) {
            actionBar.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }
}
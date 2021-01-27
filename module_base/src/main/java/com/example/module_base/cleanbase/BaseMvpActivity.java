package com.example.module_base.cleanbase;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentActivity;

import com.example.module_base.R;
import com.example.module_base.utils.LogUtils;
import com.example.module_base.widget.LoadingDialog;
import com.gyf.immersionbar.ImmersionBar;


/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 * 需要网络请求的activity
 * 需要actionBar时，返回true，默认false
 */
public abstract class BaseMvpActivity<V, P extends BasePresenter> extends FragmentActivity {
    public Activity mContext;
    public P mPresenter;

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
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setCancelable(cancelDialog());
//        daoSession = BaseApplication.getDaoSession();
        if (isActionBar()) {
            setContentView(R.layout.activity_base);
            ((ViewGroup) findViewById(R.id.fl_content)).addView(getLayoutInflater().inflate(getLayoutId(), null));
            actionBar = findViewById(R.id.actionbar);
            actionBar.setVisibility(View.VISIBLE);
        } else {
//            setContentView(getLayoutId());
            if (Build.VERSION.SDK_INT<=22) {
                setContentView(R.layout.activity_base);
                ((ViewGroup) findViewById(R.id.fl_content)).addView(getLayoutInflater().inflate(getLayoutId(), null));
            }else {
                setContentView(getLayoutId());
            }
        }
        //沉浸式状态栏
        initImmersionBar(getStatusBarColor());
        initView();
        initData();

    }

    protected int getStatusBarColor() {
        return R.color.themeColor;
    }

    /**
     * 沉浸栏颜色
     */
    protected void initImmersionBar(int color) {
        mImmersionBar = ImmersionBar.with(this).statusBarDarkFont(isStatusBarDarkFont());
        if (color != 0) {
            mImmersionBar.statusBarColor(color);
        }
        mImmersionBar.init();
    }

    protected boolean isStatusBarDarkFont(){
        return false;
    }

    protected abstract int getLayoutId();

    protected abstract P createPresenter();


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
    protected void initView() {
    }

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
        return true;
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
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        isStartAct=false;
    }

    private boolean isStartAct=false;
    @Override
    public void startActivity(Intent intent) {
        if (isStartAct)
            return;
        isStartAct=true;
        super.startActivity(intent);
    }
}
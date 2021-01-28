package com.example.module_ad.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.module_ad.R;
import com.example.module_ad.advertisement.TTSplashAd;
import com.example.module_ad.advertisement.TXSplashAd;
import com.example.module_ad.base.IShowAdCallback;
import com.example.module_ad.bean.AdBean;
import com.example.module_ad.utils.AdProbabilityUtil;
import com.example.module_ad.utils.AdMsgUtil;
import com.example.module_ad.utils.Contents;
import com.example.module_base.utils.MyStatusBarUtil;
import com.example.module_base.utils.SPUtil;

import java.util.Map;



/**
 * @author: Administrator
 * @date: 2020/7/16 0016
 */
public class BackActivity extends AppCompatActivity {


    FrameLayout mAdContainer;
    private TTSplashAd mTtSplashAd;
    private TXSplashAd mTxSplashAd;

    private boolean mAddToutiaoAdError=false;
    private boolean mAddTengxunAdError=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
        mAdContainer= findViewById(R.id.ad_container);
        intView();


    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        MyStatusBarUtil.fullScreenWindow(hasFocus,this);
    }


    protected void intView() {
        SPUtil.getInstance().putBoolean(Contents.NO_BACK,true);
        mAdContainer.setVisibility(View.VISIBLE);
        AdBean.DataBean adState = AdMsgUtil.getAdState();
        Map<String, String> adKey = AdMsgUtil.getADKey();
        if (adKey != null&adState!=null) {
            AdBean.DataBean.StartPageBean.SpreadScreenBean spread_screen = adState.getStart_page().getSpread_screen();
            String ad_percent = spread_screen.getAd_percent();
            double probability = AdProbabilityUtil.showAdProbability(ad_percent);
            if (spread_screen.isStatus()) {
                double random = Math.random();
                if (random > probability) {

                    showBackTTAd();

                } else {

                    showTXBackAd();
                }
            }
        }

    }

    private void showTXBackAd() {
        mTxSplashAd = new TXSplashAd(this, mAdContainer,false,BackActivity.class);
        mTxSplashAd.showAd();
        mTxSplashAd.setOnShowError(new IShowAdCallback(){
            @Override
            public void onShowError() {
                if (!mAddTengxunAdError) {
                    showBackTTAd();
                }
                mAddTengxunAdError=true;
                showADError();
            }

            @Override
            public void onShowSuccess() {

            }
        });
    }

    private void showBackTTAd() {
        mTtSplashAd = new TTSplashAd(this, mAdContainer,false,BackActivity.class);
        mTtSplashAd.showAd();
        mTtSplashAd.setOnShowError(new IShowAdCallback() {
            @Override
            public void onShowError() {
                if (!mAddToutiaoAdError) {
                    showTXBackAd();
                }
                mAddToutiaoAdError=true;
                showADError();
            }

            @Override
            public void onShowSuccess() {

            }
        });
    }

    private  void showADError() {
        if (mAddTengxunAdError&mAddToutiaoAdError) {
            finish();
        }
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtil.getInstance().putBoolean(Contents.NO_BACK,false);
    }
}

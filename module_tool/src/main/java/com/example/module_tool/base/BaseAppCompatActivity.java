package com.example.module_tool.base;

import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class BaseAppCompatActivity extends FragmentActivity {

    private boolean isShow=false;
    private CountDownTimer mStart;
    private int mShowTime=1000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
      //  BaseBackstage.setStop(this);

    }


    @Override
    protected void onPause() {
        super.onPause();
        //    MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  MobclickAgent.onResume(this);
    //    BaseBackstage.setBackstage(this);

    }


}

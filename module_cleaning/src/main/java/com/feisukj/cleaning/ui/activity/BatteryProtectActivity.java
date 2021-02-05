package com.feisukj.cleaning.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.module_base.utils.LogUtils;
import com.feisu.module_battery.ui.fragment.ChargeFragment;
import com.feisukj.cleaning.R;
import com.gyf.immersionbar.ImmersionBar;

public class BatteryProtectActivity extends AppCompatActivity {

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_protect);
        ImmersionBar.with(this).statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init();
        initView();
        LogUtils.i("-----------BatteryProtectActivity------------------");
    }

    private void initView() {
        back = findViewById(R.id.back);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_root, new ChargeFragment());
        transaction.commit();

    }


}
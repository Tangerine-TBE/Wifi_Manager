package com.feisukj.cleaning.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.advertisement.FeedHelper
import com.example.module_ad.advertisement.InsertHelper

import com.feisukj.cleaning.R
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_antivirus_complete.*
import kotlinx.android.synthetic.main.activity_antivirus_complete.frameLayout
import kotlinx.android.synthetic.main.activity_antivirus_complete.leftBack
import kotlinx.android.synthetic.main.activity_strong_accelerate_complete.*

class AntivirusCompleteActivity : FragmentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()
        setContentView(R.layout.activity_antivirus_complete)
        leftBack.setOnClickListener {
            finish()
        }


        feedHelper.showAd(AdType.CLEAN_FINISHED)
        insertHelper.showAd(AdType.CLEAN_FINISHED)
    }

    private val feedHelper by lazy {
        FeedHelper(this,frameLayout)
    }

    private val insertHelper by lazy {
        InsertHelper(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        feedHelper.releaseAd()
        insertHelper.releaseAd()
    }
}
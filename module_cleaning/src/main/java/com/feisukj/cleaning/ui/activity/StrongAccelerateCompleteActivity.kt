package com.feisukj.cleaning.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.advertisement.FeedHelper
import com.example.module_ad.advertisement.InsertHelper


import com.feisukj.cleaning.R
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_strong_accelerate_complete.*

class StrongAccelerateCompleteActivity : FragmentActivity(){
    companion object{
        private const val TIP_TEXT_KEY="accelerate_tip_key"
        fun getIntent(context: Context, tip:String): Intent {
            val intent= Intent(context,StrongAccelerateCompleteActivity::class.java)
            intent.putExtra(TIP_TEXT_KEY,tip)
            return intent
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()
        setContentView(R.layout.activity_strong_accelerate_complete)
        intent?.getStringExtra(TIP_TEXT_KEY)?.let {
            completeTitle.text=it
        }
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
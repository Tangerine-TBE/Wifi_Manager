package com.feisukj.cleaning.ui.activity

import com.example.module_ad.advertisement.AdType
import com.example.module_ad.advertisement.FeedHelper
import com.example.module_ad.advertisement.InsertHelper
import com.example.module_base.cleanbase.BaseActivity


import com.feisukj.cleaning.R
import com.feisukj.cleaning.manager.FloatBallManager
import com.feisukj.cleaning.utils.getSizeString
import kotlinx.android.synthetic.main.act_complete_clean.*
import kotlinx.android.synthetic.main.act_complete_clean.frameLayout
import kotlinx.android.synthetic.main.act_complete_clean.leftBack
import kotlinx.android.synthetic.main.activity_strong_accelerate_complete.*

class CompleteActivity: BaseActivity() {
    companion object{
        const val SIZE_KEY="size_key"
        const val IMAGE_COUNT_KEY="image_count_k"
    }
    private var garbageSize=0L
    private var imageCount=0
    override fun getLayoutId()= R.layout.act_complete_clean

    override fun initView() {
        garbageSize=intent.getLongExtra(SIZE_KEY,0)
        imageCount=intent.getIntExtra(IMAGE_COUNT_KEY,0)
        if (garbageSize!=0L){
            completeTip.text=String.format(resources.getString(R.string.SuccessfulClean),getSizeString(garbageSize))
        }else{
            completeTip.text=String.format(resources.getString(R.string.successfulCleanPhoto),imageCount)
        }
        initClick()


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
    private fun initClick(){
        leftBack.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        if (FloatBallManager.instance.floatBallSwitch){
            Thread {
                kotlin.run {
                    FloatBallManager.instance.updateFloatBallView()
                }
            }.start()
        }
    }
}
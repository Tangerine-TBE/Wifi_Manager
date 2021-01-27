package com.feisukj.cleaning.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

import com.feisukj.cleaning.R
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_antivirus_complete.*

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

    }
}
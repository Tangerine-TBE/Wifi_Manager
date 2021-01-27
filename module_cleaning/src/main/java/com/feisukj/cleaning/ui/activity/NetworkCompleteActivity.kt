package com.feisukj.cleaning.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.FragmentActivity

import com.feisukj.cleaning.R
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_network_complete.*

class NetworkCompleteActivity :FragmentActivity(){
    companion object{
        private const val TIP_TEXT_KEY="network_tip_key"
        fun getIntent(context: Context, tip:String): Intent {
            val intent= Intent(context,NetworkCompleteActivity::class.java)
            intent.putExtra(TIP_TEXT_KEY,tip)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()
        setContentView(R.layout.activity_network_complete)
        intent?.getStringExtra(TIP_TEXT_KEY)?.let {
            completeTitle.text=it
        }
        leftBack.setOnClickListener {
            finish()
        }

    }
}
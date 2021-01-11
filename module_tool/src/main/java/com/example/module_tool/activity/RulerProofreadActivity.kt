package com.example.module_tool.activity

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.module_tool.R
import com.example.module_tool.base.BaseAppCompatActivity
import kotlinx.android.synthetic.main.activity_ruler_proofread_cjy.*

class RulerProofreadActivity: BaseAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ruler_proofread_cjy)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        initClick()
    }
    private fun initClick(){
        mCancel.setOnClickListener {

            finish()
        }
        ok.setOnClickListener {
            val intent=Intent()
            intent.putExtra(RulerActivity2.proofreadResultKey,rulerProofread.getUnitCMLength())
            setResult(0, intent)
            finish()
        }
    }
}
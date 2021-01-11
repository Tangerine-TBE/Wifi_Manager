package com.example.module_tool.activity

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.module_tool.R
import com.example.module_tool.base.BaseAppCompatActivity
import kotlinx.android.synthetic.main.activity_bank_proofread_cjy.*

class BankProofreadActivity : BaseAppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bank_proofread_cjy)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        bankCardWidth.isSelected=true
        initClick()
    }

    private fun initClick(){
        bankCardWidth.setOnClickListener {
            it.isSelected=!it.isSelected
            bankCardHeight.isSelected=!it.isSelected
            bankProofread.isWidth=true
        }
        bankCardHeight.setOnClickListener {
            it.isSelected=!it.isSelected
            bankCardWidth.isSelected=!it.isSelected
            bankProofread.isWidth=false
        }
        mCancel.setOnClickListener {
            finish()
        }
        ok.setOnClickListener {
            val intent= Intent()
                    intent.putExtra(RulerActivity2.proofreadResultKey,bankProofread.getUnitCMLength())
            setResult(0, intent)
            finish()
        }
    }
}
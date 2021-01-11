package com.example.module_tool.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle

import androidx.fragment.app.FragmentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.module_tool.R
import com.gyf.barlibrary.ImmersionBar
import kotlinx.android.synthetic.main.activity_proofread_cjy.*

import java.text.DecimalFormat

class ProofreadActivity :FragmentActivity() {
    companion object{
        val MFI_KEY="mfi_k"
        val PROOFREAD_BR_ACTION="action_br_proofread"
    }
    private val proofreadFieldBR:ProofreadFieldBR by lazy { ProofreadFieldBR() }
    private lateinit var localBroadcastManager : LocalBroadcastManager
    private val df by lazy { DecimalFormat("0.00") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proofread_cjy)
        ImmersionBar.with(this)
                .transparentStatusBar()
                .init()
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        val intentFilter = IntentFilter(PROOFREAD_BR_ACTION)
        localBroadcastManager.registerReceiver(proofreadFieldBR,intentFilter)
        Glide.with(this).load(R.drawable.jiaozhun).into(iv1)
    }
    private inner class ProofreadFieldBR : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            intent?.getFloatExtra(MFI_KEY,0f)?.also {
                //磁场强度
                tv5.text=df.format(it)
                //准确度
                tv3.text=if (it<70/2){
                    getString(R.string.hao)
                }else if (it<70){
                    getString(R.string.lianghao)
                }else{
                    getString(R.string.cha)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastManager.unregisterReceiver(proofreadFieldBR)
    }
}
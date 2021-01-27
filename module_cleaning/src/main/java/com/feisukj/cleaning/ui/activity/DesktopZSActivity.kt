package com.feisukj.cleaning.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.module_base.cleanbase.BaseActivity2

import com.feisukj.cleaning.R
import com.feisukj.cleaning.manager.FloatBallManager
import com.feisukj.cleaning.ui.MANAGE_OVERLAY_PERMISSION_CODE
import com.feisukj.cleaning.ui.USAGE_ACCESS_SETTINGS_PERMISSION_CODE
import com.feisukj.cleaning.utils.UsageAccessHelp
import com.feisukj.cleaning.view.OpenDesktopViewTipDialog
import kotlinx.android.synthetic.main.act_desktop_zs_clean.*

class DesktopZSActivity: BaseActivity2() {
    private val floatBallSwitchManager by lazy { FloatBallManager.instance }

    override fun getLayoutId()=R.layout.act_desktop_zs_clean

    override fun initView() {
        setContentText(R.string.zuomian)
        image1.isSelected=floatBallSwitchManager.floatBallSwitch
        image2.isSelected=floatBallSwitchManager.userAccessSwitch
    }

    override fun initListener() {
        image1.setOnClickListener { image ->
            image.isSelected=!image.isSelected
            val gainPermission=checkFloatPermission()
            if (gainPermission){
                floatBallSwitchManager.floatBallSwitch=image.isSelected
            }
        }
        image2.setOnClickListener {
            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
                Toast.makeText(this,R.string.userTip,Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!image1.isSelected){
                return@setOnClickListener
            }
            it.isSelected=!it.isSelected
            val haveUser=checkUserPermission()
            if (it.isSelected){
                if (!haveUser){
                    AlertDialog.Builder(this)
                            .setTitle(R.string.tips)
                            .setMessage(R.string.seeUsePermission)
                            .setPositiveButton(R.string.yes){dialogInterface, i ->
                                startActivityForResult(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), USAGE_ACCESS_SETTINGS_PERMISSION_CODE)
                            }
                            .setNegativeButton(R.string.no){dialogInterface, i ->
                                it.isSelected=false
                            }
                            .show()
                }
            }
            if (haveUser)
                floatBallSwitchManager.userAccessSwitch=it.isSelected
        }
    }

    /**
     * 如果有权限返回true 没有则申请权限，返回false
     */
    private fun checkFloatPermission():Boolean{
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M||Settings.canDrawOverlays(this)){
            return true
        }else{
            OpenDesktopViewTipDialog(this).apply {
                openOnClick= View.OnClickListener {
                    if (isFinishing){
                        return@OnClickListener
                    }
                    val intent= Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    intent.data = Uri.parse("package:${packageName}")
                    this@DesktopZSActivity.startActivityForResult(intent, MANAGE_OVERLAY_PERMISSION_CODE)
                    dismiss()
                }
                noOpenOnClick=View.OnClickListener {
                    if (isFinishing){
                        return@OnClickListener
                    }
                    this@DesktopZSActivity.image1.isSelected=false
                    dismiss()
                }
                show()
            }
        }
        return false
    }

    private fun checkUserPermission():Boolean{
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            val have=UsageAccessHelp.instance.isNoOption()
            if (have){
                val isOpen=UsageAccessHelp.instance.isNoSwitch()
                if (isOpen){
                    return true
                }
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            MANAGE_OVERLAY_PERMISSION_CODE ->{
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O&&Settings.canDrawOverlays(this)){
                    FloatBallManager.instance.floatBallSwitch=true
                }else{
                    image1.isSelected=false
                }
            }
            USAGE_ACCESS_SETTINGS_PERMISSION_CODE ->{
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP&&UsageAccessHelp.instance.isNoSwitch()) {
                    FloatBallManager.instance.userAccessSwitch=true
                }else{
                    image2.isSelected=false
                }
            }

        }

    }
}
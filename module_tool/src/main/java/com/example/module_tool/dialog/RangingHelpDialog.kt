package com.example.module_tool.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.module_tool.R
import com.example.module_tool.fragment.RangingHelpFragment1
import com.example.module_tool.fragment.RangingHelpFragment2
import kotlinx.android.synthetic.main.dialog_ranging_help.*

class RangingHelpDialog(val mContext: Context):Dialog(mContext, R.style.MyDialog) {

    private val fragment1: RangingHelpFragment1 by lazy {
        RangingHelpFragment1()
    }
    private val fragment2: RangingHelpFragment2 by lazy {
        RangingHelpFragment2()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_ranging_help)
        val a = window?.attributes
        a?.width = WindowManager.LayoutParams.MATCH_PARENT
        a?.width = WindowManager.LayoutParams.MATCH_PARENT

        fragment1.toDo = {
            dismiss()
        }
        fragment2.toDo = {
            dismiss()
        }
        val list: List<Fragment> = listOf(fragment1,fragment2)
        viewPage.adapter = object :FragmentStateAdapter(mContext as FragmentActivity){
            override fun getItemCount(): Int {
                return list.size
            }

            override fun createFragment(position: Int): Fragment {
                return list[position]
            }

        }
        viewPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0){
                    iv1.setImageResource(R.drawable.shape_ranging_help_dot_y)
                    iv2.setImageResource(R.drawable.shape_ranging_help_dot_n)
                }else{
                    iv1.setImageResource(R.drawable.shape_ranging_help_dot_n)
                    iv2.setImageResource(R.drawable.shape_ranging_help_dot_y)
                }
            }
        })
        viewPage.offscreenPageLimit = list.size
//        val r = (viewPage.getChildAt(0)) as RecyclerView
//        r.setPadding(DeviceUtils.dp2px(context,10f),0,DeviceUtils.dp2px(context,10f),0)
//        r.clipToPadding = false

    }
}
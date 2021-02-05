package com.example.wifi_manager.ui.popup


import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.advertisement.FeedHelper
import com.example.module_base.base.BasePopup
import com.example.module_base.utils.MyActivityManager
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.DiyExitPopupWindowBinding


class ExitPoPupWindow(mActivity: FragmentActivity) : BasePopup<DiyExitPopupWindowBinding>(
    mActivity,
    R.layout.diy_exit_popup_window,
    ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT
) {


    override fun initEvent() {
        mView?.apply {
            mCancel.setOnClickListener {
                mOutValueAnimator?.start()
                dismiss()
                exitAdContainer.removeAllViews()
            }
            mSure.setOnClickListener {
                mOutValueAnimator?.start()
                dismiss()
                MyActivityManager.removeAllActivity()
            }

        }
        setOnDismissListener {
            if (mFeedHelper != null) {
                mFeedHelper?.releaseAd()
                mFeedHelper = null
            }
        }

    }

    private var mFeedHelper: FeedHelper? = null
    private fun popupShowAd() {
        mView?.apply {
            mFeedHelper = FeedHelper(activity, exitAdContainer)
            mFeedHelper?.showAd(AdType.EXIT_PAGE)
        }
    }

    override fun showPopupView(view: View, gravity: Int, x: Int, y: Int) {
        popupShowAd()
        super.showPopupView(view, gravity, x, y)

    }
}
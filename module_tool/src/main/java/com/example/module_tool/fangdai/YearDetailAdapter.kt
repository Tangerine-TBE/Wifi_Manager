package com.example.module_tool.fangdai

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


import com.example.module_tool.R

import com.feisukj.main.bean.RefundYearDetail
import java.text.DecimalFormat

/**
 * Author : Gupingping
 * Date : 2019/2/25
 * QQ : 464955343
 */
class YearDetailAdapter
    : BaseQuickAdapter<RefundYearDetail, BaseViewHolder>(R.layout.item_year_detail) {

    override fun convert(helper: BaseViewHolder, item: RefundYearDetail) {
        val df = DecimalFormat("#,###,###.##")       //保留两位小数
        val checkBox = helper?.getView<View>(R.id.refundDetailArrowIv) as CheckBox
        helper.setText(R.id.refundDetailYearTv, item?.year)
        helper.setText(R.id.refundDetailYearMoneyTv, df.format(item?.yearRefundMoney))
        helper.setText(R.id.refundDetailYearInterestTv, df.format(item?.yearRefundInterest))

        val monthRecycler = helper.getView<RecyclerView>(R.id.monthDetailRecycler)
        monthRecycler?.layoutManager = LinearLayoutManager(helper.itemView.context)

        val monthAdapter = MonthDetailAdapter()
        val header = LayoutInflater.from(helper.itemView.context).inflate(R.layout.item_month_detail, null, false)
        monthAdapter.addHeaderView(header)
        monthRecycler?.adapter = monthAdapter
        monthAdapter.setNewData(item?.monthList)

        helper.getView<RelativeLayout>(R.id.llYearDetail)?.setOnClickListener {
            //            LogUtils.e("llYearDetail setOnClickListener")
            checkBox.isChecked = !checkBox.isChecked
        }

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.setText(R.string.close)
                monthRecycler?.visibility = View.VISIBLE
            } else {
                buttonView.setText(R.string.open)
                monthRecycler?.visibility = View.GONE
            }
        }
    }
}
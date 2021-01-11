package com.example.module_tool.fangdai

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.example.module_tool.R
import com.feisukj.main.bean.RefundMonthDetail

/**
 * Author : Gupingping
 * Date : 2019/2/26
 * QQ : 464955343
 */
class MonthDetailAdapter : BaseQuickAdapter<RefundMonthDetail, BaseViewHolder>(R.layout.item_month_detail) {
    override fun convert(helper: BaseViewHolder, item: RefundMonthDetail) {
        helper?.setText(R.id.monthDetailTv, item?.month)
        helper?.setText(R.id.monthRefundDetailTv, item?.refundMoney)
        helper?.setText(R.id.monthCapitalDetailTv, item?.refundCapital)
        helper?.setText(R.id.monthInterestDetailTv, item?.refundInterest)
    }
}
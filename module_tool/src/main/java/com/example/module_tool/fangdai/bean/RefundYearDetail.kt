package com.feisukj.main.bean

import com.example.module_tool.fangdai.bean.BaseBean


/**
 * Author : Gupingping
 * Date : 2019/2/25
 * QQ : 464955343
 * 还款每年详情
 */
class RefundYearDetail : BaseBean() {
    var year: String? = null
    //本年还款
    var yearRefundMoney: Double =0.0
    //本年还利息
    var yearRefundInterest: Double =0.0
    //每月详情
    var monthList = ArrayList<RefundMonthDetail>()

    override fun toString(): String {
        return "RefundYearDetail(yearUnit=$year, 本年还款=$yearRefundMoney, 本年还利息=$yearRefundInterest)"
    }

}
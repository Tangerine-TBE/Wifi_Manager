package com.feisukj.main.bean

import com.example.module_tool.fangdai.bean.BaseBean


/**
 * Author : Gupingping
 * Date : 2019/2/25
 * QQ : 464955343
 * 还款每月详情
 */
class RefundMonthDetail : BaseBean() {
    //月份
    var month: String? = null
    //月供
    var refundMoney : String? = null
    //本期本金
    var refundCapital : String? = null
    //本期利息
    var refundInterest : String? = null
    //本期剩余
    var refundRemain : String? = null
}
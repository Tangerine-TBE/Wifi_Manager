package com.feisukj.main.bean

import com.example.module_tool.fangdai.bean.BaseBean


/**
 * Author : Gupingping
 * Date : 2019/2/25
 * QQ : 464955343
 * 还款简介
 */
class RefundDesc : BaseBean() {
    //还款年数
    var years: String? = null
    //月供
    var monthPay: String? = null
    //还款总额
    var sumPay: String? = null
    //支付利息
    var interestPay: String? = null
    //每月递减
    var down: String? = null
    //商业还款总额
    var businessSum: String? = null
    //公积金还款总额
    var fundSum: String? = null
    //商业还利息
    var businessInterest: String? = null
    //公积金还利息
    var fundInterest: String? = null

    override fun toString(): String {
        return "RefundDesc(还款年数=$years, 月供=$monthPay, 还款总额=$sumPay, 支付利息=$interestPay)"
    }


}
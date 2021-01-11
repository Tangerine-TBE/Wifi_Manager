package com.feisukj.main.bean

import com.example.module_tool.fangdai.bean.BaseBean

import java.util.*

/**
 * Author : Gupingping
 * Date : 2019/2/21
 * QQ : 464955343
 * 商业贷款或者公积金贷款信息
 */
class LoanSingleInfo : BaseBean() {
    var loanType: LoanType = LoanType.BUSINESS
    var money: Double = 0.0
    var refundMethod: RefundMethod = RefundMethod.INTEREST
    var refundYears: Int = 0
    var firstRefundDate: String = "2019年2月25日"
    var rate: Double = 0.00
    override fun toString(): String {
        return "商业贷款或者公积金贷款信息(loanType=$loanType, money=$money, refundMethod=$refundMethod, refundYears=$refundYears, firstRefundDate=$firstRefundDate, rate=$rate)"
    }

}
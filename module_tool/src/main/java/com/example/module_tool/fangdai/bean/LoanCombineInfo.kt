package com.feisukj.main.bean

import com.example.module_tool.fangdai.bean.BaseBean

import java.util.*

/**
 * Author : Gupingping
 * Date : 2019/2/21
 * QQ : 464955343
 *  组合贷款信息
 */
class LoanCombineInfo : BaseBean() {
    var loanType: LoanType = LoanType.COMBINE
    //商业贷款金额
    var businessMoney: Double = 0.0
    //公积金贷款金额
    var fundMoney: Double = 0.0
    //还款方式
    var refundMethod: RefundMethod = RefundMethod.INTEREST
    //还款年数
    var refundYears: Int = 0
    //首次还款日期
    var firstRefundDate: String = "2019年2月25日"
    //商业贷款利率
    var businessRate = 0.0
    //公积金贷款利率
    var fundRate = 0.0

    override fun toString(): String {
        return "组合贷款信息(loanType=$loanType, businessMoney=$businessMoney, fundMoney=$fundMoney, refundMethod=$refundMethod, refundYears=$refundYears, firstRefundDate=$firstRefundDate, businessRate=$businessRate, fundRate=$fundRate)"
    }

}
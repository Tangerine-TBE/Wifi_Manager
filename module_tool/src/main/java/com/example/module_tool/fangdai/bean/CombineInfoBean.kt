package com.feisukj.main.bean

import com.example.module_tool.fangdai.bean.BaseBean


/**
 * Author : Gupingping
 * Date : 2019/2/27
 * QQ : 464955343
 * 组合贷款计算信息bean
 */
class CombineInfoBean : BaseBean() {
    var capital: MutableList<Double>? = null
    var interest: MutableList<Double>? = null
    var pay: MutableList<Double>? = null
    var yearDetailList: ArrayList<RefundYearDetail>? = null
}
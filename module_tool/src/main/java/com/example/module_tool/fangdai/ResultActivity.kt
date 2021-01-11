package com.example.module_tool.fangdai

import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.example.module_tool.R
import com.example.module_tool.base.BaseActivity
import com.example.module_tool.fangdai.bean.BaseBean

import com.feisukj.main.bean.*
import kotlinx.android.synthetic.main.activity_result.*
import java.text.DecimalFormat

/**
 * Author : Gupingping
 * Date : 2019/2/21
 * QQ : 464955343
 */
class ResultActivity : BaseActivity() {

    val df = DecimalFormat("#,###,###.##")       //保留两位小数
    lateinit var refundDesc: RefundDesc
    lateinit var yearDetailList: ArrayList<RefundYearDetail>

    override fun getLayoutId(): Int = R.layout.activity_result

    override fun initView() {

        showLoading()
        result_toolbar.setTitle("房贷清单")
        val bean = intent?.extras!!["bean"]
        if (bean != null) {
            bean as BaseBean

            //开始计算
            calculateLoanInfo(bean)

            monthPayTv.text = refundDesc.monthPay
            refundSumTv.text = refundDesc.sumPay
            refundInterestTv.text = refundDesc.interestPay
            refundDetailMsgTv.text = String.format(resources.getString(R.string.refundDetailMsg), refundDesc.years)
            refundDesc.down?.apply {
                downTv.visibility = View.VISIBLE
                downTv.text = String.format(resources.getString(R.string.down), this)
            }
            refundDesc.businessSum?.apply {
                refundSumMsgTv.textSize = 18f
                refundSumTv.textSize = 18f
                refundInterestMsgTv.textSize = 18f
                refundInterestTv.textSize = 18f
                ll_business.visibility = View.VISIBLE
                businessSum.text = this
            }
            refundDesc.fundSum?.apply {
                ll_fund.visibility = View.VISIBLE
                fundSum.text = this
            }

            refundDesc.businessInterest?.apply {
                ll_business_interest.visibility = View.VISIBLE
                businessInterest.text = this
            }

            refundDesc.fundInterest?.apply {
                ll_fund_interest.visibility = View.VISIBLE
                fundInterest.text = this
            }
            dismissLoading()

            payRecycler.layoutManager = LinearLayoutManager(this)
            val adapter = YearDetailAdapter()
            payRecycler.adapter = adapter
            payRecycler.isNestedScrollingEnabled = false
            adapter.setNewData(yearDetailList)
            ll_detail.setOnClickListener {
                arrowIv.isSelected = !arrowIv.isSelected
                payRecycler.visibility = if (payRecycler.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }
    }

    private fun calculateLoanInfo(info: BaseBean) {
        if (info is LoanSingleInfo) {
            when (info.refundMethod) {
                RefundMethod.CAPITAL -> {
                    //等额本金
                    calculateCapital(info)
                }
                RefundMethod.INTEREST -> {
                    //等额本息
                    calculateInterest(info)
                }
            }
        } else if (info is LoanCombineInfo) {

            when (info.refundMethod) {
                RefundMethod.CAPITAL -> {
                    calculateCombineCapital(info)
                }
                RefundMethod.INTEREST -> {
                    //等额本息
                    calculateCombineInterest(info)
                }
            }
        }


    }

    override fun isActionBar(): Boolean = false

    override fun initEvent() {
        result_toolbar.finishActivity(this)
    }

    //商业或者公积金等额本息计算
    private fun calculateInterest(info: LoanSingleInfo) {


        //贷款月数转换
        val monthCounts = info.refundYears * 12
        //万元转换为元
        val money = info.money * 10000
        //年利率转换为月利率
        val montRate = info.rate / 100 / 12


        //月供
        val monthPay = money * montRate * Math.pow(1 + montRate, monthCounts.toDouble()) / (Math.pow(1 + montRate, monthCounts.toDouble()) - 1)
        //还款总额
        val sum = monthPay * monthCounts
        //还款利息总额
        val interest = monthPay * monthCounts - money

        //房贷清单页面简介bean
        refundDesc = RefundDesc()
        refundDesc.years = info.refundYears.toString()
        refundDesc.sumPay = df.format(sum)
        refundDesc.monthPay = df.format(monthPay)
        refundDesc.interestPay = df.format(interest)

        //每月本金list
        val monthCapitalList = mutableListOf<Double>()
        //每月利息list
        val monthInterestList = mutableListOf<Double>()
        //每月月供list
        val monthPayList = mutableListOf<Double>()
        //第几期list
        val datePayList = ArrayList<String>()
        for (item in 1..monthCounts) {
            //每月应还本金：每月应还本金=贷款本金×月利率×(1+月利率)^(还款月序号-1)÷〔(1+月利率)^还款月数-1〕
            val monthCapital = money * montRate * Math.pow(1 + montRate, (item - 1).toDouble()) / (Math.pow(1 + montRate, monthCounts.toDouble()) - 1)
            monthCapitalList.add(monthCapital)

            //每月应还利息：贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
            val monthInterest = money * montRate * (Math.pow(1 + montRate, monthCounts.toDouble()) - Math.pow(1 + montRate, (item - 1).toDouble())) / (Math.pow(1 + montRate, monthCounts.toDouble()) - 1)
            monthInterestList.add(monthInterest)

            //月供：每月月供额=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
            val monthSum = money * montRate * Math.pow(1 + montRate, monthCounts.toDouble()) / (Math.pow(1 + montRate, monthCounts.toDouble()) - 1)
            monthPayList.add(monthSum)

            datePayList.add("${item}${if(language== com.example.module_tool.fangdai.Language.zh_CN) "期" else " phase"}")
        }

        val firstMonth = info.firstRefundDate.let {
            if(language== com.example.module_tool.fangdai.Language.zh_CN) {
                it.slice(it.indexOf(yearUnit) + 1 until it.indexOf(monthUnit)).toInt()
            }else{
                val month=it.slice(0 until it.indexOf(" "))
                MonthIndexMap.valueOf(month).month+1
            }
        }
        val firstYear = info.firstRefundDate.let {
            if(language== com.example.module_tool.fangdai.Language.zh_CN) {
                it.slice(0 until it.indexOf(yearUnit)).toInt()
            }else{
                it.slice(it.indexOf(",")+1 until it.length).toInt()
            }
        }

        yearDetailList = ArrayList<RefundYearDetail>()//每年还款信息List


        val deltaMonth = 12 - firstMonth + 1
        val deltaYear = info.refundYears
        var max = 0

        if (deltaMonth != 12) {//开始月份不是1月
            for (item in 0..info.refundYears) {
                val refundYearDetail = RefundYearDetail()//创建每年还款对象
                refundYearDetail.year = "${firstYear + item}$yearUnit"
                yearDetailList.add(refundYearDetail)
            }

            val firstRefundYearDetail = yearDetailList[0]//取出首年还款对象
            //第一年内的还款月信息
            for (index in 0 until deltaMonth) {
                val monthDetail = RefundMonthDetail()
//                val monthCode=firstMonth + index-1
//                val month=MonthIndexMap.values().find {
//                    it.month==monthCode
//                }?.name?:""
                monthDetail.month = "${firstMonth + index}$monthUnit-${datePayList[index]}"
                monthDetail.refundCapital = df.format(monthCapitalList[index])
                monthDetail.refundInterest = df.format(monthInterestList[index])
                monthDetail.refundMoney = df.format(monthPayList[index])
                firstRefundYearDetail.yearRefundMoney += monthPayList[index]
                firstRefundYearDetail.yearRefundInterest += monthInterestList[index]
                firstRefundYearDetail.monthList.add(monthDetail)
            }
            var stepYear = 1//年增量
            var stepMonth = deltaMonth  //月增量
            max = monthCounts + (deltaYear + 1) - (deltaMonth + 1)

            //剩余年数内还款月信息
            for (item in 0 until max) {
                val index = item % 13
                if (index == 0) {//过滤所有的年
                    stepYear++
                } else {
                    val refundYearDetail = yearDetailList[stepYear - 1]//取出当年还款对象
                    val monthDetail = RefundMonthDetail()
//                    val monthCode=firstMonth + index-1
//                    val month=MonthIndexMap.values().find {
//                        it.month==monthCode
//                    }?.name?:""
                    monthDetail.month = "${index}$monthUnit-${datePayList[stepMonth]}"
                    monthDetail.refundCapital = df.format(monthCapitalList[stepMonth])
                    monthDetail.refundInterest = df.format(monthInterestList[stepMonth])
                    monthDetail.refundMoney = df.format(monthPayList[stepMonth])
                    refundYearDetail.monthList.add(monthDetail)
                    refundYearDetail.yearRefundMoney += monthPayList[stepMonth]
                    refundYearDetail.yearRefundInterest += monthInterestList[stepMonth]
                    stepMonth++
                }
            }
        } else {//开始月份是1月
            for (item in 0 until info.refundYears) {
                val refundYearDetail = RefundYearDetail()//创建每年还款对象
                refundYearDetail.year = "${firstYear + item}年"
                yearDetailList.add(refundYearDetail)
            }
            max = monthCounts + deltaYear
            var stepYear = 0//年增量
            var stepMonth = 0 //月增量
            for (item in 0 until max) {//把年和月放在一起
                val index = item % 13
                if (index == 0) {//过滤所有的年
                    stepYear++
                } else {
                    val refundYearDetail = yearDetailList[stepYear - 1]//取出当年还款对象
                    val monthDetail = RefundMonthDetail()
                    monthDetail.month = "${index}$monthUnit-${datePayList[stepMonth]}"
                    monthDetail.refundCapital = df.format(monthCapitalList[stepMonth])
                    monthDetail.refundInterest = df.format(monthInterestList[stepMonth])
                    monthDetail.refundMoney = df.format(monthPayList[stepMonth])
                    refundYearDetail.monthList.add(monthDetail)
                    refundYearDetail.yearRefundMoney += monthPayList[stepMonth]
                    refundYearDetail.yearRefundInterest += monthInterestList[stepMonth]
                    stepMonth++
                }
            }

        }
    }

    //商业或者公积金等额本金计算
    private fun calculateCapital(info: LoanSingleInfo) {


        //贷款月数转换
        val monthCounts = info.refundYears * 12
        //万元转换为元
        val money = info.money * 10000
        //年利率转换为月利率
        val montRate = info.rate / 100 / 12


        //还款总额
        val sum = monthCounts * (money * montRate - montRate * (money / monthCounts) * (monthCounts - 1).toDouble() / 2 + money / monthCounts)
        //还款利息总额
        val interest = sum - money


        //每月本金list
        val monthCapitalList = mutableListOf<Double>()
        //每月利息list
        val monthInterestList = mutableListOf<Double>()
        //每月月供list
        val monthPayList = mutableListOf<Double>()
        //第几期list
        val datePayList = ArrayList<String>()
        //已还本金金额
        var paid = 0.0


        for (item in 1..monthCounts) {
            //每月应还本金：贷款本金÷还款月数
            val monthCapital = money / monthCounts
            monthCapitalList.add(monthCapital)

            //每月应还利息：剩余本金×月利率=(贷款本金-已归还本金累计额)×月利率
            val monthInterest = (money - paid) * montRate
            monthInterestList.add(monthInterest)

            //月供：(贷款本金÷还款月数)+(贷款本金-已归还本金累计额)×月利率
            val monthSum = (money / monthCounts) + (money - paid) * montRate
            monthPayList.add(monthSum)
            //已归还本金累计额
            paid += money / monthCounts

            datePayList.add("${item}${if(language== com.example.module_tool.fangdai.Language.zh_CN)"期" else "phase"}")
        }

        //房贷清单页面简介bean
        refundDesc = RefundDesc()
        refundDesc.years = info.refundYears.toString()
        refundDesc.monthPay = df.format(monthPayList[0])
        refundDesc.sumPay = df.format(sum)
        refundDesc.interestPay = df.format(interest)
        refundDesc.down = df.format(monthPayList[0] - monthPayList[1])


        val firstMonth = info.firstRefundDate.let {
            it.slice(it.indexOf(yearUnit) + 1 until it.indexOf(monthUnit)).toInt()
        }
        val firstYear = info.firstRefundDate.let {
            it.slice(0 until it.indexOf(yearUnit)).toInt()
        }


        val deltaMonth = 12 - firstMonth + 1
        val deltaYear = info.refundYears
        var max = 0

        yearDetailList = ArrayList<RefundYearDetail>()//每年还款信息List


        if (deltaMonth != 12) {//开始月份不是1月
            for (item in 0..info.refundYears) {
                val refundYearDetail = RefundYearDetail()//创建每年还款对象
                refundYearDetail.year = "${firstYear + item}$yearUnit"
                yearDetailList.add(refundYearDetail)
            }

            max = monthCounts + deltaYear - deltaMonth

            val firstRefundYearDetail = yearDetailList[0]//取出首年还款对象
            //第一年内的还款月信息
            for (index in 0 until deltaMonth) {
                val monthDetail = RefundMonthDetail()
                monthDetail.month = "${firstMonth + index}$monthUnit-${datePayList[index]}"
                monthDetail.refundCapital = df.format(monthCapitalList[index])
                monthDetail.refundInterest = df.format(monthInterestList[index])
                monthDetail.refundMoney = df.format(monthPayList[index])
                firstRefundYearDetail.yearRefundMoney += monthPayList[index]
                firstRefundYearDetail.yearRefundInterest += monthInterestList[index]
                firstRefundYearDetail.monthList.add(monthDetail)
            }
            var stepYear = 1//年增量
            var stepMonth = deltaMonth//月增量
            //剩余年数内还款月信息
            for (item in 0 until max) {
                val index = item % 13
                if (index == 0) {//过滤所有的年
                    stepYear++
                } else {
                    val refundYearDetail = yearDetailList[stepYear - 1]//取出当年还款对象
                    val monthDetail = RefundMonthDetail()
                    monthDetail.month = "${index}$monthUnit-${datePayList[stepMonth]}"
                    monthDetail.refundCapital = df.format(monthCapitalList[stepMonth])
                    monthDetail.refundInterest = df.format(monthInterestList[stepMonth])
                    monthDetail.refundMoney = df.format(monthPayList[stepMonth])
                    refundYearDetail.monthList.add(monthDetail)
                    refundYearDetail.yearRefundMoney += monthPayList[stepMonth]
                    refundYearDetail.yearRefundInterest += monthInterestList[stepMonth]
                    stepMonth++
                }
            }
        } else { //开始月份是1月
            for (item in 0 until info.refundYears) {
                val refundYearDetail = RefundYearDetail()//创建每年还款对象
                refundYearDetail.year = "${firstYear + item}$yearUnit"
                yearDetailList.add(refundYearDetail)
            }

            max = monthCounts + deltaYear
            var stepYear = 0//年增量
            var stepMonth = 0//月增量
            for (item in 0 until max) {//把年和月放在一起
                val index = item % 13
                if (index == 0) {//过滤所有的年
                    stepYear++
                } else {
                    val refundYearDetail = yearDetailList[stepYear - 1]//取出当年还款对象
                    val monthDetail = RefundMonthDetail()
                    monthDetail.month = "${index}$monthUnit-${datePayList[stepMonth]}"
                    monthDetail.refundCapital = df.format(monthCapitalList[stepMonth])
                    monthDetail.refundInterest = df.format(monthInterestList[stepMonth])
                    monthDetail.refundMoney = df.format(monthPayList[stepMonth])
                    refundYearDetail.monthList.add(monthDetail)
                    refundYearDetail.yearRefundMoney += monthPayList[stepMonth]
                    refundYearDetail.yearRefundInterest += monthInterestList[stepMonth]
                    stepMonth++
                }
            }

        }
    }

    //组合贷款等额本息计算
    private fun calculateCombineInterest(info: LoanCombineInfo) {
        //贷款月数转换
        val monthCounts = info.refundYears * 12
        //万元转换为元
        val businessMoney = info.businessMoney * 10000
        //年利率转换为月利率
        val businessMontRate = info.businessRate / 100 / 12

        //万元转换为元
        val fundMoney = info.fundMoney * 10000
        //年利率转换为月利率
        val fundMontRate = info.fundRate / 100 / 12

        val firstMonth = info.firstRefundDate.let {
            if(language== com.example.module_tool.fangdai.Language.zh_CN) {
                it.slice(it.indexOf(yearUnit) + 1 until it.indexOf(monthUnit)).toInt()
            }else{
                val month=it.slice(0 until it.indexOf(" "))
                MonthIndexMap.valueOf(month).month+1
            }
        }
        val firstYear = info.firstRefundDate.let {
            if(language== com.example.module_tool.fangdai.Language.zh_CN) {
                it.slice(0 until it.indexOf(yearUnit)).toInt()
            }else{
                it.slice(it.indexOf(",")+1 until it.length).toInt()
            }
        }

        //每月本金list
        val monthCapitalList = mutableListOf<Double>()
        //每月利息list
        val monthInterestList = mutableListOf<Double>()
        //每月月供list
        val monthPayList = mutableListOf<Double>()
        //第几期list
        val datePayList = ArrayList<String>()

        for (i in 1..monthCounts) {
            //商业每月本金
            val monthCommCapital = businessMoney * businessMontRate * Math.pow(1 + businessMontRate, (i - 1).toDouble()) / (Math.pow(1 + businessMontRate, monthCounts.toDouble()) - 1)
            //商业每月利息
            val monthCommInterest = businessMoney * businessMontRate * (Math.pow(1 + businessMontRate, monthCounts.toDouble()) - Math.pow(1 + businessMontRate, (i - 1).toDouble())) / (Math.pow(1 + businessMontRate, monthCounts.toDouble()) - 1)
            //商业每月月供
            val monthCommSum = businessMoney * businessMontRate * Math.pow(1 + businessMontRate, monthCounts.toDouble()) / (Math.pow(1 + businessMontRate, monthCounts.toDouble()) - 1)
            //公积金每月本金
            val monthFundCapital = fundMoney * fundMontRate * Math.pow(1 + fundMontRate, (i - 1).toDouble()) / (Math.pow(1 + fundMontRate, monthCounts.toDouble()) - 1)
            //公积金每月利息
            val monthFundInterest = fundMoney * fundMontRate * (Math.pow(1 + fundMontRate, monthCounts.toDouble()) - Math.pow(1 + fundMontRate, (i - 1).toDouble())) / (Math.pow(1 + fundMontRate, monthCounts.toDouble()) - 1)
            //公积金每月月供
            val monthFundSum = fundMoney * fundMontRate * Math.pow(1 + fundMontRate, monthCounts.toDouble()) / (Math.pow(1 + fundMontRate, monthCounts.toDouble()) - 1)

            monthCapitalList.add(monthCommCapital + monthFundCapital)
            monthInterestList.add(monthCommInterest + monthFundInterest)
            monthPayList.add(monthCommSum + monthFundSum)
            datePayList.add("${i}${if(language== com.example.module_tool.fangdai.Language.zh_CN) "期" else "phase"}")
        }
        //商业贷款月供
        val monthCommPay = businessMoney * businessMontRate * Math.pow(1 + businessMontRate, monthCounts.toDouble()) / (Math.pow(1 + businessMontRate, monthCounts.toDouble()) - 1)
        //商业还款总额
        val sumComm = monthCommPay * monthCounts
        //商业还款利息
        val interestComm = monthCommPay * monthCounts - businessMoney

        //公积金贷款月供
        val monthFundPay = fundMoney * fundMontRate * Math.pow(1 + fundMontRate, monthCounts.toDouble()) / (Math.pow(1 + fundMontRate, monthCounts.toDouble()) - 1)
        //公积金还款总额
        val sumFund = monthFundPay * monthCounts
        //公积金还款利息
        val interestFund = monthFundPay * monthCounts - fundMoney

        yearDetailList = ArrayList<RefundYearDetail>()//每年还款信息List

        //房贷清单页面简介bean
        refundDesc = RefundDesc()
        refundDesc.years = info.refundYears.toString()
        refundDesc.sumPay = df.format(sumComm + sumFund)
        refundDesc.monthPay = df.format(monthCommPay + monthFundPay)
        refundDesc.interestPay = df.format(interestComm + interestFund)
        refundDesc.businessSum = df.format(sumComm)
        refundDesc.fundSum = df.format(sumFund)
        refundDesc.businessInterest = df.format(interestComm)
        refundDesc.fundInterest = df.format(interestFund)

        val deltaMonth = 12 - firstMonth + 1
        val deltaYear = info.refundYears
        var max = 0

        if (deltaMonth != 12) {//开始月份不是1月
            for (item in 0..info.refundYears) {
                val refundYearDetail = RefundYearDetail()//创建每年还款对象
                refundYearDetail.year = "${firstYear + item}$yearUnit"
                yearDetailList.add(refundYearDetail)
            }

            val firstRefundYearDetail = yearDetailList[0]//取出首年还款对象
            //第一年内的还款月信息
            for (index in 0 until deltaMonth) {
                val monthDetail = RefundMonthDetail()
                monthDetail.month = "${firstMonth + index}$monthUnit-${datePayList[index]}"
                monthDetail.refundCapital = df.format(monthCapitalList[index])
                monthDetail.refundInterest = df.format(monthInterestList[index])
                monthDetail.refundMoney = df.format(monthPayList[index])
                firstRefundYearDetail.yearRefundMoney += monthPayList[index]
                firstRefundYearDetail.yearRefundInterest += monthInterestList[index]
                firstRefundYearDetail.monthList.add(monthDetail)
            }
            var stepYear = 1//年增量
            var stepMonth = deltaMonth  //月增量
            max = monthCounts + (deltaYear + 1) - (deltaMonth + 1)

            //剩余年数内还款月信息
            for (item in 0 until max) {
                val index = item % 13
                if (index == 0) {//过滤所有的年
                    stepYear++
                } else {
                    val refundYearDetail = yearDetailList[stepYear - 1]//取出当年还款对象
                    val monthDetail = RefundMonthDetail()
                    monthDetail.month = "${index}$monthUnit-${datePayList[stepMonth]}"
                    monthDetail.refundCapital = df.format(monthCapitalList[stepMonth])
                    monthDetail.refundInterest = df.format(monthInterestList[stepMonth])
                    monthDetail.refundMoney = df.format(monthPayList[stepMonth])
                    refundYearDetail.monthList.add(monthDetail)
                    refundYearDetail.yearRefundMoney += monthPayList[stepMonth]
                    refundYearDetail.yearRefundInterest += monthInterestList[stepMonth]
                    stepMonth++
                }
            }
        } else {//开始月份是1月
            for (item in 0 until info.refundYears) {
                val refundYearDetail = RefundYearDetail()//创建每年还款对象
                refundYearDetail.year = "${firstYear + item}$yearUnit"
                yearDetailList.add(refundYearDetail)
            }
            max = monthCounts + deltaYear
            var stepYear = 0//年增量
            var stepMonth = 0 //月增量
            for (item in 0 until max) {//把年和月放在一起
                val index = item % 13
                if (index == 0) {//过滤所有的年
                    stepYear++
                } else {
                    val refundYearDetail = yearDetailList[stepYear - 1]//取出当年还款对象
                    val monthDetail = RefundMonthDetail()
                    monthDetail.month = "${index}$monthUnit-${datePayList[stepMonth]}"
                    monthDetail.refundCapital = df.format(monthCapitalList[stepMonth])
                    monthDetail.refundInterest = df.format(monthInterestList[stepMonth])
                    monthDetail.refundMoney = df.format(monthPayList[stepMonth])
                    refundYearDetail.monthList.add(monthDetail)
                    refundYearDetail.yearRefundMoney += monthPayList[stepMonth]
                    refundYearDetail.yearRefundInterest += monthInterestList[stepMonth]
                    stepMonth++
                }
            }


        }
    }

    //组合贷款等额本金计算
    private fun calculateCombineCapital(info: LoanCombineInfo) {
        //贷款月数转换
        val monthCounts = info.refundYears * 12
        //万元转换为元
        val businessMoney = info.businessMoney * 10000
        //年利率转换为月利率
        val businessMontRate = info.businessRate / 100 / 12

        //万元转换为元
        val fundMoney = info.fundMoney * 10000
        //年利率转换为月利率
        val fundMontRate = info.fundRate / 100 / 12

        val firstMonth = info.firstRefundDate.let {
            it.slice(it.indexOf(yearUnit) + 1 until it.indexOf(monthUnit)).toInt()
        }
        val firstYear = info.firstRefundDate.let {
            it.slice(0 until it.indexOf(yearUnit)).toInt()
        }

        //每月本金list
        val monthCapitalList = mutableListOf<Double>()
        //每月利息list
        val monthInterestList = mutableListOf<Double>()
        //每月月供list
        val monthPayList = mutableListOf<Double>()
        //第几期list
        val datePayList = ArrayList<String>()
        //已还商业本金金额
        var paidComm = 0.0
        //已还公积金本金金额
        var paidFund = 0.0
        for (i in 0..monthCounts) {
            val monthCommCapital = businessMoney / monthCounts
            val monthCommInterest = (businessMoney - paidComm) * businessMontRate
            val monthCommSum = businessMoney / monthCounts + (businessMoney - paidComm) * businessMontRate
            paidComm += businessMoney / monthCounts

            val monthFundCapital = fundMoney / monthCounts
            val monthFundInterest = (fundMoney - paidFund) * fundMontRate
            val monthFundSum = fundMoney / monthCounts + (fundMoney - paidFund) * fundMontRate
            paidFund += fundMoney / monthCounts

            monthCapitalList.add(monthCommCapital + monthFundCapital)
            monthInterestList.add(monthCommInterest + monthFundInterest)
            monthPayList.add(monthCommSum + monthFundSum)
            datePayList.add("${i}期")
        }
        //商业还款总额
        val sumComm = monthCounts * (businessMoney * businessMontRate - businessMontRate * (businessMoney / monthCounts) * (monthCounts - 1).toDouble() / 2 + businessMoney / monthCounts)
        //商业还款利息
        val interestComm = sumComm - businessMoney
        //公积金还款总额
        val sumFund = monthCounts * (fundMoney * fundMontRate - fundMontRate * (fundMoney / monthCounts) * (monthCounts - 1).toDouble() / 2 + fundMoney / monthCounts)
        //公积金还款西丽
        val interestFund = sumFund - fundMoney

        //房贷清单页面简介bean
        refundDesc = RefundDesc()
        refundDesc.years = info.refundYears.toString()
        refundDesc.monthPay = df.format(monthPayList[0])
        refundDesc.sumPay = df.format(sumComm + sumFund)
        refundDesc.interestPay = df.format(interestComm + interestFund)
        refundDesc.businessSum = df.format(sumComm)
        refundDesc.fundSum = df.format(sumFund)
        refundDesc.businessInterest = df.format(interestComm)
        refundDesc.fundInterest = df.format(interestFund)
        refundDesc.down = df.format(monthPayList[0] - monthPayList[1])


        val deltaMonth = 12 - firstMonth + 1
        val deltaYear = info.refundYears
        var max = 0

        yearDetailList = ArrayList<RefundYearDetail>()//每年还款信息List


        if (deltaMonth != 12) {//开始月份不是1月
            for (item in 0..info.refundYears) {
                val refundYearDetail = RefundYearDetail()//创建每年还款对象
                refundYearDetail.year = "${firstYear + item}$yearUnit"
                yearDetailList.add(refundYearDetail)
            }

            max = monthCounts + deltaYear - deltaMonth

            val firstRefundYearDetail = yearDetailList[0]//取出首年还款对象
            //第一年内的还款月信息
            for (index in 0 until deltaMonth) {
                val monthDetail = RefundMonthDetail()
                monthDetail.month = "${firstMonth + index}$monthUnit-${datePayList[index]}"
                monthDetail.refundCapital = df.format(monthCapitalList[index])
                monthDetail.refundInterest = df.format(monthInterestList[index])
                monthDetail.refundMoney = df.format(monthPayList[index])
                firstRefundYearDetail.yearRefundMoney += monthPayList[index]
                firstRefundYearDetail.yearRefundInterest += monthInterestList[index]
                firstRefundYearDetail.monthList.add(monthDetail)
            }
            var stepYear = 1//年增量
            var stepMonth = deltaMonth//月增量
            //剩余年数内还款月信息
            for (item in 0 until max) {
                val index = item % 13
                if (index == 0) {//过滤所有的年
                    stepYear++
                } else {
                    val refundYearDetail = yearDetailList[stepYear - 1]//取出当年还款对象
                    val monthDetail = RefundMonthDetail()
                    monthDetail.month = "${index}$monthUnit-${datePayList[stepMonth]}"
                    monthDetail.refundCapital = df.format(monthCapitalList[stepMonth])
                    monthDetail.refundInterest = df.format(monthInterestList[stepMonth])
                    monthDetail.refundMoney = df.format(monthPayList[stepMonth])
                    refundYearDetail.monthList.add(monthDetail)
                    refundYearDetail.yearRefundMoney += monthPayList[stepMonth]
                    refundYearDetail.yearRefundInterest += monthInterestList[stepMonth]
                    stepMonth++
                }
            }
        } else { //开始月份是1月
            for (item in 0 until info.refundYears) {
                val refundYearDetail = RefundYearDetail()//创建每年还款对象
                refundYearDetail.year = "${firstYear + item}$yearUnit"
                yearDetailList.add(refundYearDetail)
            }

            max = monthCounts + deltaYear
            var stepYear = 0//年增量
            var stepMonth = 0//月增量
            for (item in 0 until max) {//把年和月放在一起
                val index = item % 13
                if (index == 0) {//过滤所有的年
                    stepYear++
                } else {
                    val refundYearDetail = yearDetailList[stepYear - 1]//取出当年还款对象
                    val monthDetail = RefundMonthDetail()
                    monthDetail.month = "${index}$monthUnit-${datePayList[stepMonth]}"
                    monthDetail.refundCapital = df.format(monthCapitalList[stepMonth])
                    monthDetail.refundInterest = df.format(monthInterestList[stepMonth])
                    monthDetail.refundMoney = df.format(monthPayList[stepMonth])
                    refundYearDetail.monthList.add(monthDetail)
                    refundYearDetail.yearRefundMoney += monthPayList[stepMonth]
                    refundYearDetail.yearRefundInterest += monthInterestList[stepMonth]
                    stepMonth++
                }
            }

        }
    }

}
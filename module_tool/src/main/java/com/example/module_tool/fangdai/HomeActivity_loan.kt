package com.example.module_tool.fangdai

import android.annotation.SuppressLint
import android.content.Intent
import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CompoundButton
import android.widget.TextView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.example.module_tool.R

import com.example.module_tool.base.BaseActivity
import com.example.module_tool.fangdai.bean.BaseBean
import com.example.module_tool.utils.toast
import com.feisukj.main.bean.LoanCombineInfo
import com.feisukj.main.bean.LoanSingleInfo
import com.feisukj.main.bean.LoanType
import com.feisukj.main.bean.RefundMethod
import com.feisukj.main.widget.DialogItemSelectedListener
import com.feisukj.main.widget.RefundMethodDialog
import com.feisukj.main.widget.RefundMethodSelectedListener
import com.feisukj.main.widget.RefundYearsDialog


import kotlinx.android.synthetic.main.activity_house_home_loan.*
import razerdp.basepopup.QuickPopupBuilder
import razerdp.basepopup.QuickPopupConfig
import razerdp.util.SimpleAnimationUtils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Author : Gupingping
 * Date : 2019/2/19
 * QQ : 464955343
 */
class HomeActivity_loan : BaseActivity(), CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private var dialogTime: TimePickerView? = null
    //选择的贷款金额
    private var money: Double = 0.0
    //选择的商业贷款金额
    private var businessMoney: Double = 0.0
    //选择的公积金贷款金额
    private var fundMoney: Double = 0.0
    //选择的贷款类型
    private var loanType: LoanType = LoanType.BUSINESS
    //选择的还款方式
    private var refundMethod: RefundMethod = RefundMethod.INTEREST
    //选择的还款年数
    private var refundYears: Int = 30
    //选择的首次还款日期
    private var firstRefundDate: String = getTime(Date())
    //选择的贷款利率
    private var rate: Double = 4.9
    //选择的商业贷款利率
    private var businessRate: Double = 4.9
    //选择的公积金贷款利率
    private var fundRate: Double = 3.25




    override fun getLayoutId(): Int = R.layout.activity_house_home_loan

    override fun initView() {
        home_toolbar.setTitle("房贷计算")
        radioBtn_business.setOnCheckedChangeListener(this)
        radioBtn_fund.setOnCheckedChangeListener(this)
        radioBtn_combine.setOnCheckedChangeListener(this)
        rl_refund_method.setOnClickListener(this)
        rl_refund_years.setOnClickListener(this)
        rl_refund_first_date.setOnClickListener(this)
        rl_rate.setOnClickListener(this)
        rl_business_rate.setOnClickListener(this)
        rl_fund_rate.setOnClickListener(this)
        calculate.setOnClickListener(this)
        methodDesc.setOnClickListener(this)
        radioBtn_business.isChecked = true


    }

    override fun initEvent() {
        home_toolbar.finishActivity(this)
    }

    //选择贷款类型
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        resetData()
        when (buttonView?.id) {
            radioBtn_business.id,
            radioBtn_fund.id -> {
                if (isChecked) {
                    combineMoneyLL.visibility = GONE
                    rl_business_rate.visibility = GONE
                    rl_fund_rate.visibility = GONE
                    loanTypeLL.visibility = GONE

                    common_money.visibility = VISIBLE
                    rl_rate.visibility = VISIBLE
                    loanCount.visibility = VISIBLE

                    if (buttonView.id == radioBtn_business.id) {
                        //商业贷款
                        loanCount.text = "贷款总额(万)"
                        loanRate.text = "贷款利率"
                        rateTv.text = "基准利率(4.9%)"

                        loanType = LoanType.BUSINESS
                        rate = 4.9

                    } else if (buttonView.id == radioBtn_fund.id) {
                        //公积金贷款
                        loanCount.text = "公积金贷款金额(万)"
                        loanRate.text = "公积金贷款利率"
                        rateTv.text = "基准利率(3.25%)"

                        loanType = LoanType.FUND
                        rate = 3.25

                    }
                }
            }
            //组合贷款
            radioBtn_combine.id -> {
                if (isChecked) {

                    loanTypeLL.visibility = VISIBLE
                    combineMoneyLL.visibility = VISIBLE
                    rl_business_rate.visibility = VISIBLE
                    rl_fund_rate.visibility = VISIBLE

                    common_money.visibility = GONE
                    rl_rate.visibility = GONE
                    loanCount.visibility = GONE

                    loanType = LoanType.COMBINE

                    business_rate_tv.text = "基准利率4.9%"
                    fund_rate_tv.text = "基准利率3.25%"
                }
            }
        }
    }

    /**
     * 重置
     */
    private fun resetData() {
        refundMethod = RefundMethod.INTEREST
        refundYears = 30
        rate = 4.9
        businessRate = 4.9
        fundRate = 3.25
        firstRefundDate = getTime(Date())
        refundMethodTv.text = "等额本息(每月等额还款)"
        yearsTv.text = "30年(360个月)"
        dateTv.text = getTime(Date())
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v?.id) {
            //还款方式
            rl_refund_method.id -> {

                RefundMethodDialog(this, refundMethodTv.text.toString(), object : RefundMethodSelectedListener {
                    override fun refundMethod(method: RefundMethod) {
//                        toast("还款方式:${method.name}")
                        refundMethod = method
                        when (method) {
                            RefundMethod.INTEREST -> refundMethodTv.text = "等额本息(每月等额还款)"
                            RefundMethod.CAPITAL -> refundMethodTv.text = "等额本金(每月递减还款)"
                        }
                    }
                }).show()
            }
            methodDesc.id -> {
                QuickPopupBuilder.with(this)
                        .contentView(R.layout.layout_question)
                        .config(QuickPopupConfig()
                                .blurBackground(true)
                                .withShowAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(true))
                                .withDismissAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(false)))
                        .show()
            }
            //还款年数
            rl_refund_years.id -> {
                val mOptionsItems = ArrayList<Any>()
                for (item in 1..30) {
                    mOptionsItems.add(item.toString())
                }
                val selected = yearsTv.text.toString().slice(0 until yearsTv.text.indexOf("年"))
                RefundYearsDialog(this,
                        mOptionsItems,
                        selected,
                        "还款年数", 18f,
                        object : DialogItemSelectedListener {
                            @SuppressLint("SetTextI18n")
                            override fun onItemSelected(index: Int) {
                                if (index>=0){
                                toast("还款年数:index==$index,years==${mOptionsItems[index]}")
                                val month = (mOptionsItems[index].toString().toInt()) * 12
                                yearsTv.text = "${mOptionsItems[index]}年(${month}月)"
                                refundYears = mOptionsItems[index].toString().toInt()
                                }
                            }
                        }).show()
            }
            //首次还款日期
            rl_refund_first_date.id -> {
                val data = Calendar.getInstance()
                data.set(Calendar.YEAR, dateTv.text.toString().slice(0 until dateTv.text.toString().indexOf("年")).toInt())
                data.set(Calendar.MONTH, dateTv.text.toString().slice(dateTv.text.toString().indexOf("年") + 1 until dateTv.text.toString().indexOf("月")).toInt() - 1)
                data.set(Calendar.DAY_OF_MONTH, dateTv.text.toString().slice(dateTv.text.toString().indexOf("月") + 1 until dateTv.text.toString().indexOf("日")).toInt())

                dialogTime = TimePickerBuilder(this, OnTimeSelectListener { date, _ ->
                    //选中事件回调
//                    toast("首次还款日期:$date")
                    dateTv.text = getTime(date)
                    firstRefundDate = getTime(date)
                })
                        .setLayoutRes(R.layout.pickerview_custom_time) {
                            it.findViewById<TextView>(R.id.dateCancel).setOnClickListener {
                                dialogTime?.dismiss()
                            }
                            it.findViewById<TextView>(R.id.dateConfirm).setOnClickListener {
                                dialogTime?.returnData()
                                dialogTime?.dismiss()
                            }
                        }
                        .setContentTextSize(17)
                        .setDate(data)
                        .setType(booleanArrayOf(true, true, true, false, false, false))
                        .setLabel("年", "月", "日", "时", "分", "秒")
                        .setLineSpacingMultiplier(1.2f)
                        .setTextColorCenter(ContextCompat.getColor(this, R.color.them_cjy))
                        .setTextXOffset(0, 0, 0, 40, 0, -40)
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setDividerColor(ContextCompat.getColor(this, R.color.gray))
                        .build()
                dialogTime?.show()
            }
            //商业和公积金贷款利率
            rl_rate.id -> {
                val mOptionsItems = ArrayList<Any>()

                when (loanType) {
                    LoanType.BUSINESS -> {
//                        rateTv.text = "基准利率(4.9%)"
                        getBusinessRate(mOptionsItems)
                    }
                    else
//                        rateTv.text = "基准利率(3.25%)"
                    -> getFundRate(mOptionsItems)
                }

                RefundYearsDialog(this,
                        mOptionsItems,
                        rateTv.text.toString(),
                        "贷款利率",
                        15f,
                        object : DialogItemSelectedListener {
                            override fun onItemSelected(index: Int) {
                                if (index >= 0) {
//                                toast("贷款利率:index==$index,years==${mOptionsItems[index]}")
                                    rateTv.text = "${mOptionsItems[index]}"
                                    rate = setRate(mOptionsItems[index].toString())
                                }
                            }
                        }).show()

            }
            //组合贷款时 商业利率
            rl_business_rate.id -> {
                val mOptionsItems = ArrayList<Any>()
                getBusinessRate(mOptionsItems)
                RefundYearsDialog(this,
                        mOptionsItems,
                        business_rate_tv.text.toString(),
                        "商业贷款利率",
                        15f,
                        object : DialogItemSelectedListener {
                            override fun onItemSelected(index: Int) {
                                if (index >= 0) {
//                                toast("商业贷款利率:index==$index,years==${mOptionsItems[index]}")
                                    business_rate_tv.text = "${mOptionsItems[index]}"
                                    businessRate = setRate(mOptionsItems[index].toString())
                                }
                            }
                        }).show()
            }
            //组合贷款时 公积金利率
            rl_fund_rate.id -> {
                val mOptionsItems = ArrayList<Any>()
                getFundRate(mOptionsItems)
                RefundYearsDialog(this,
                        mOptionsItems,
                        fund_rate_tv.text.toString(),
                        "公积金贷款利率",
                        15f,
                        object : DialogItemSelectedListener {
                            override fun onItemSelected(index: Int) {
                                if (index >= 0) {
//                                toast("公积金贷款利率:index==$index,years==${mOptionsItems[index]}")
                                    fund_rate_tv.text = "${mOptionsItems[index]}"
                                    fundRate = setRate(mOptionsItems[index].toString())
                                }
                            }
                        }).show()
            }
            //计算
            calculate.id -> {
                var info: BaseBean
                if (checkDataNotNull()) {
                    when (loanType) {
                        LoanType.BUSINESS, LoanType.FUND -> {
                            money = common_money.text.toString().toDouble()

                            info = LoanSingleInfo()
                            info.loanType = loanType
                            info.money = money
                            info.refundMethod = refundMethod
                            info.refundYears = refundYears
                            info.firstRefundDate = firstRefundDate
                            info.rate = setRate(rateTv.text.toString())
                        }
                        LoanType.COMBINE -> {
                            businessMoney = business_money.text.toString().toDouble()
                            fundMoney = fund_money.text.toString().toDouble()

                            info = LoanCombineInfo()
                            info.loanType = LoanType.COMBINE
                            info.refundMethod = refundMethod
                            info.refundYears = refundYears
                            info.firstRefundDate = firstRefundDate
                            info.businessMoney = businessMoney
                            info.fundMoney = fundMoney
                            info.businessRate = businessRate
                            info.fundRate = fundRate
                        }
                    }
                    val intent=Intent(this, ResultActivity::class.java)
                    intent.putExtra("bean",info)
                    startActivity(intent)
//                    IntentUtils.toActivity(this, ResultActivity::class.java, mapOf("bean" to info))
                }
            }
        }
    }

    private fun setRate(rateString: String): Double {
        return rateString.let {
            val start = it.indexOf("(")
            val end = it.indexOf("%")
            val percent = it.slice(start + 1 until end)
            percent.toDouble()
        }
    }

    private fun checkDataNotNull(): Boolean {
        when (loanType) {
            LoanType.FUND, LoanType.BUSINESS -> {
                return if (TextUtils.isEmpty(common_money.text)) {
                    toast("请输入贷款金额")
                    false
                } else {
                    true
                }
            }
            LoanType.COMBINE -> {

                if (TextUtils.isEmpty(business_money.text)) {
                    toast("请输入商业贷款金额")
                    return false
                }

                if (TextUtils.isEmpty(fund_money.text)) {
                    toast("请输入公积金贷款金额")
                    return false
                }
                return true
            }
        }
    }

    private fun getFundRate(mOptionsItems: ArrayList<Any>): ArrayList<Any> {
        mOptionsItems.clear()
        mOptionsItems.add("基准利率(3.25%)")
        mOptionsItems.add("1.1倍(3.575%)")
        mOptionsItems.add("1.2倍(3.9%)")
        return mOptionsItems
    }

    private fun getBusinessRate(mOptionsItems: ArrayList<Any>): ArrayList<Any> {
        mOptionsItems.clear()
        mOptionsItems.add("7折(3.43%)")
        mOptionsItems.add("8折(3.92%)")
        mOptionsItems.add("8.3折(4.067%)")
        mOptionsItems.add("8.5折(4.165%)")
        mOptionsItems.add("8.8折(4.312%)")
        mOptionsItems.add("9折(4.41%)")
        mOptionsItems.add("9.5折(4.655%)")
        mOptionsItems.add("基准利率(4.9%)")
        mOptionsItems.add("1.05倍(5.145%)")
        mOptionsItems.add("1.1倍(5.39%)")
        mOptionsItems.add("1.15倍(5.635%)")
        mOptionsItems.add("1.2倍(5.88%)")
        mOptionsItems.add("1.25倍(6.125%)")
        mOptionsItems.add("1.3倍(6.37%)")
        mOptionsItems.add("1.35倍(6.615%)")
        mOptionsItems.add("1.4倍(6.86%)")
        return mOptionsItems
    }

    private fun getTime(date: Date): String {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.time)
        val format = SimpleDateFormat("yyyy年MM月dd日")
        return format.format(date)
    }

    override fun isActionBar(): Boolean = false

    override fun onDestroy() {
        super.onDestroy()

    }
}
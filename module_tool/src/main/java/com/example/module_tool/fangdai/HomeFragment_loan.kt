package com.example.module_tool.fangdai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
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
import com.example.module_base.base.BaseApplication
import com.example.module_tool.R
import com.example.module_tool.base.BaseFragment
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

enum class Language{
    zh_CN,en_US
}
var language=Language.zh_CN
enum class MonthIndexMap(val month:Int){
    January(0),February(1),March(2),April(3),May(4),June(5),July(6)
    ,August(7),September(8),October(9),November(10),December(11),Undecimber(-1)
}
var yearUnit="年"
var monthUnit="月"
var dayUnit_="日"
class HomeFragment_loan : BaseFragment(), CompoundButton.OnCheckedChangeListener, View.OnClickListener{
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
        //获得系统语言环境
        val locale:Locale
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
            locale=resources.configuration.locales[0]
        }else{
            locale=resources.configuration.locale
        }
        if (locale.language.contains("zh")){
            language=Language.zh_CN
            yearUnit="年"
            monthUnit="月"
            dayUnit_="日"
        }else{
            language=Language.en_US
            yearUnit=" Year"
            monthUnit=" Month"
            dayUnit_=" Day"
        }

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
                        loanCount.setText(R.string.totalUnit)
                        loanRate.setText(R.string.rate)
                        rateTv.setText(R.string.benchmark)

                        loanType = LoanType.BUSINESS
                        rate = 4.9

                    } else if (buttonView.id == radioBtn_fund.id) {
                        //公积金贷款
                        loanCount.setText(R.string.HouseT)
                        loanRate.setText(R.string.rateHousing)
                        rateTv.setText(R.string.benchmark_g)

                        loanType = LoanType.FUND
                        rate = 3.25

                    }
                }
            }
            //组合贷款
            radioBtn_combine.id -> {
                if (isChecked) {

                    loanTypeLL.visibility = View.VISIBLE
                    combineMoneyLL.visibility = View.VISIBLE
                    rl_business_rate.visibility = View.VISIBLE
                    rl_fund_rate.visibility = View.VISIBLE

                    common_money.visibility = View.GONE
                    rl_rate.visibility = View.GONE
                    loanCount.visibility = View.GONE

                    loanType = LoanType.COMBINE

                    business_rate_tv.setText(R.string.benchmark)
                    fund_rate_tv.setText(R.string.benchmark_g)
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
        refundMethodTv.setText(R.string.equivalent)
        val r=resources.getString(R.string.term)
        yearsTv.text = r.format("30","360")
        dateTv.text = getTime(Date())
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v?.id) {
            //还款方式
            rl_refund_method.id -> {

                RefundMethodDialog(context!!, refundMethodTv.text.toString(), object : RefundMethodSelectedListener {
                    override fun refundMethod(method: RefundMethod) {
//                        toast("还款方式:${method.name}")
                        refundMethod = method
                        when (method) {
                            RefundMethod.INTEREST -> refundMethodTv.setText(R.string.equivalent)
                            RefundMethod.CAPITAL -> refundMethodTv.setText(R.string.principal)
                        }
                    }
                }).show()
            }
            methodDesc.id -> {
                QuickPopupBuilder.with(context)
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
                val selected =if(language==Language.zh_CN) {
                    yearsTv.text.toString().slice(0 until yearsTv.text.indexOf(yearUnit))
                }else{
                    yearsTv.text.toString().slice(0 until yearsTv.text.indexOf(yearUnit))
                }
                RefundYearsDialog(context!!,
                        mOptionsItems,
                        selected,
                        resources.getString(R.string.repayment), 18f,
                        object : DialogItemSelectedListener {
                            @SuppressLint("SetTextI18n")
                            override fun onItemSelected(index: Int) {
//                                toast("还款年数:index==$index,years==${mOptionsItems[index]}")
                                val month = (mOptionsItems[index] as String).toInt() * 12
                                val r=resources.getString(R.string.term)
                                yearsTv.text = r.format(mOptionsItems[index].toString(),month.toString())
                                refundYears = (mOptionsItems[index] as String).toInt()
                            }
                        }).show()
            }
            //首次还款日期
            rl_refund_first_date.id -> {
                val data = Calendar.getInstance()
                val text=dateTv.text.toString()
                if (language==Language.zh_CN){
                    data.set(Calendar.YEAR, text.slice(0 until text.indexOf(yearUnit)).toInt())
                    data.set(Calendar.MONTH, text.slice(text.indexOf(yearUnit) + 1 until text.indexOf(monthUnit)).toInt() - 1)
                    data.set(Calendar.DAY_OF_MONTH, text.slice(text.indexOf(monthUnit) + 1 until text.indexOf(dayUnit_)).toInt())
                }else{
                    data.set(Calendar.YEAR, text.slice(text.indexOf(",")+1 until text.length).toInt())
                    val month=text.slice(0 until text.indexOf(" "))
                    data.set(Calendar.MONTH, MonthIndexMap.valueOf(month).month)
                    data.set(Calendar.DAY_OF_MONTH, text.slice(text.indexOf(" ")+1 until text.indexOf(",")).toInt())
                }
                val isChinese= language==Language.zh_CN
                dialogTime = TimePickerBuilder(context, OnTimeSelectListener { date, _ ->
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
                        .setLabel(if(isChinese) yearUnit else yearUnit, if(isChinese) monthUnit else monthUnit, if(isChinese) dayUnit_ else dayUnit_, "时", "分", "秒")
                        .setLineSpacingMultiplier(1.2f)
                        .setTextColorCenter(ContextCompat.getColor(context!!, R.color.them_cjy))
                        .setTextXOffset(0, 0, 0, 40, 0, -40)
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setDividerColor(ContextCompat.getColor(context!!, R.color.gray))
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

                RefundYearsDialog(context!!,
                        mOptionsItems,
                        rateTv.text.toString(),
                        resources.getString(R.string.rate),
                        15f,
                        object : DialogItemSelectedListener {
                            override fun onItemSelected(index: Int) {
//                                toast("贷款利率:index==$index,years==${mOptionsItems[index]}")
                                rateTv.text = "${mOptionsItems[index]}"
                                rate = setRate(mOptionsItems[index].toString())
                            }
                        }).show()

            }
            //组合贷款时 商业利率
            rl_business_rate.id -> {
                val mOptionsItems = ArrayList<Any>()
                getBusinessRate(mOptionsItems)
                RefundYearsDialog(context!!,
                        mOptionsItems,
                        business_rate_tv.text.toString(),
                        resources.getString(R.string.rateCom),
                        15f,
                        object : DialogItemSelectedListener {
                            override fun onItemSelected(index: Int) {
//                                toast("商业贷款利率:index==$index,years==${mOptionsItems[index]}")
                                business_rate_tv.text = "${mOptionsItems[index]}"
                                businessRate = setRate(mOptionsItems[index].toString())
                            }
                        }).show()
            }
            //组合贷款时 公积金利率
            rl_fund_rate.id -> {
                val mOptionsItems = ArrayList<Any>()
                getFundRate(mOptionsItems)
                RefundYearsDialog(context!!,
                        mOptionsItems,
                        fund_rate_tv.text.toString(),
                        resources.getString(R.string.rateHousing),
                        15f,
                        object : DialogItemSelectedListener {
                            override fun onItemSelected(index: Int) {
//                                toast("公积金贷款利率:index==$index,years==${mOptionsItems[index]}")
                                fund_rate_tv.text = "${mOptionsItems[index]}"
                                fundRate = setRate(mOptionsItems[index].toString())
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
                    val intent= Intent(context, ResultActivity::class.java)
                    intent.putExtra("bean",info)
                    startActivity(intent)
//                    IntentUtils.toActivity(context, ResultActivity::class.java, mapOf("bean" to info))
                }
            }
        }
    }

    private fun setRate(rateString: String): Double {
        return rateString.let { string ->
            val start = string.indexOf("(")
            val end = string.indexOfLast {
                '%' ==it
            }
            val percent = string.slice(start + 1 until end)
            percent.toDouble()
        }
    }

    private fun checkDataNotNull(): Boolean {
        when (loanType) {
            LoanType.FUND, LoanType.BUSINESS -> {
                return if (TextUtils.isEmpty(common_money.text)) {
                    toast(R.string.money)
                    false
                } else {
                    true
                }
            }
            LoanType.COMBINE -> {

                if (TextUtils.isEmpty(business_money.text)) {
                    toast(R.string.inputCom)
                    return false
                }

                if (TextUtils.isEmpty(fund_money.text)) {
                    toast(R.string.inputHousing)
                    return false
                }
                return true
            }
        }
    }

    private fun getFundRate(mOptionsItems: ArrayList<Any>): ArrayList<Any> {
        mOptionsItems.clear()
        resources.also {
            mOptionsItems.add(it.getString(R.string._10_0_percent_g))
            mOptionsItems.add(it.getString(R.string._1_10_times_g))
            mOptionsItems.add(it.getString(R.string._1_20_times_g))
        }
        return mOptionsItems
    }

    private fun getBusinessRate(mOptionsItems: ArrayList<Any>): ArrayList<Any> {
        mOptionsItems.clear()
        mOptionsItems.add(resources.getString(R.string._7_0_percent))
        mOptionsItems.add(resources.getString(R.string._8_0_percent))
        mOptionsItems.add(resources.getString(R.string._8_3_percent))
        mOptionsItems.add(resources.getString(R.string._8_5_percent))
        mOptionsItems.add(resources.getString(R.string._8_8_percent))
        mOptionsItems.add(resources.getString(R.string._9_0_percent))
        mOptionsItems.add(resources.getString(R.string._9_5_percent))
        mOptionsItems.add(resources.getString(R.string._10_0_percent))
        mOptionsItems.add(resources.getString(R.string._1_05_times))
        mOptionsItems.add(resources.getString(R.string._1_10_times))
        mOptionsItems.add(resources.getString(R.string._1_15_times))
        mOptionsItems.add(resources.getString(R.string._1_20_times))
        mOptionsItems.add(resources.getString(R.string._1_25_times))
        mOptionsItems.add(resources.getString(R.string._1_30_times))
        mOptionsItems.add(resources.getString(R.string._1_35_times))
        mOptionsItems.add(resources.getString(R.string._1_40_times))
        return mOptionsItems
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTime(date: Date): String {//可根据需要自行截取数据显示
        if (language==Language.zh_CN) {
            Log.d("getTime()", "choice date millis: " + date.time)
            val format = SimpleDateFormat("yyyy年MM月dd日")
            return format.format(date)
        }else{
            val cal=Calendar.getInstance()
            cal.time=date
            val r= BaseApplication.mContext.getString(R.string.firstDate)
            val month=MonthIndexMap.values().find {
                it.month==Calendar.MONTH
            }?.name?:return ""
            return r.format(cal.get(Calendar.YEAR),month,cal.get(Calendar.DAY_OF_MONTH))
        }
    }
}
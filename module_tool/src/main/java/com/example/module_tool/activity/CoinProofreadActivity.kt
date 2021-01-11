package com.example.module_tool.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.module_tool.R
import com.example.module_tool.base.BaseAppCompatActivity
import com.example.module_tool.base.BaseConstant
import com.example.module_tool.widget.CoinSize
import com.example.module_tool.widget.PopupWindowView
import kotlinx.android.synthetic.main.activity_coin_cjy.*


class CoinProofreadActivity: BaseAppCompatActivity() {
    private enum class CoinKind(val coinType:List<CoinSize>, val des:String){
        RMB(CoinSize.getCoinTypes(CoinSize.Companion.Kind.RMB), BaseConstant.application.getString(R.string.rmb)),
        USA(CoinSize.getCoinTypes(CoinSize.Companion.Kind.USA),BaseConstant.application.getString(R.string.usa))
    }
    private lateinit var coinKindWindow: PopupWindowView
    private lateinit var coinKindItemClick: PopupWindowView.ItemOnClick

    private lateinit var coinTypeWindow: PopupWindowView
    private lateinit var coinTypeItemClick: PopupWindowView.ItemOnClick

    private var currentCoinKind = CoinKind.RMB
    private var currentCoinType=currentCoinKind.coinType.find {
        it==CoinSize.RMB_1_0
    }?:currentCoinKind.coinType[0]
    set(value) {
        if (value!=field){
            field=value
            coinType.text=value.des
            coinProof.currentCoin=value
        }
    }
    private val coinTypeData by lazy {
        Array<ArrayList<String>>(1){
            currentCoinKind.coinType.map { it.des } as ArrayList<String>
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coin_cjy)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val coinKindData=Array<ArrayList<String>>(1){
            CoinKind.values().map { it.des } as ArrayList<String>
        }
        coinKindWindow=PopupWindowView(this,coinKindData,coinKind,Color.parseColor("#ffffff")).apply {
            textColor=Color.parseColor("#000000")
        }
        coinTypeWindow=PopupWindowView(this,coinTypeData,coinType,Color.parseColor("#ffffff")).apply {
            textColor=Color.parseColor("#000000")
        }
        initClick()
        coinKindWindow.itemOnClick=coinKindItemClick
        coinTypeWindow.itemOnClick=coinTypeItemClick

        coinKind.text=currentCoinKind.des
        coinType.text=currentCoinType.des
        coinProof.currentCoin=currentCoinType
    }

    private fun initClick(){
        //币种回调
        coinKindItemClick=object :PopupWindowView.ItemOnClick{
            override fun onItemClick(position: Int) {
                if (position<CoinKind.values().size){
                    CoinKind.values()[position].also { kind ->
                        if (kind!=currentCoinKind) {
                            coinKind.text=kind.des
                            currentCoinKind = kind

                            coinTypeData[0] = kind.coinType.map { it.des } as ArrayList<String>
                            currentCoinType = kind.coinType[0]
                            coinTypeWindow.adapter.notifyDataSetChanged()
                        }
                    }
                }
                coinKindWindow.dismiss()
            }
        }
        //硬币类型回调（1角、5角。。。）
        coinTypeItemClick=object :PopupWindowView.ItemOnClick{
            override fun onItemClick(position: Int) {
                if (position<coinTypeData[0].size){
//                    coinType.text=coinTypeData[0][position]
                    coinTypeData[0][position].also {
                        currentCoinType=currentCoinKind.coinType.find {coinSize->
                            coinSize.des==it
                        }?:currentCoinKind.coinType[0]
                    }
                }
                coinTypeWindow.dismiss()
            }
        }

        //选择币种
        coinKind.setOnClickListener {
            coinKindWindow.show()
        }
        //选择硬币面额
        coinType.setOnClickListener {
            coinTypeWindow.show()
        }
        ok.setOnClickListener {
            val intent=Intent()
            intent.putExtra(RulerActivity2.proofreadResultKey,coinProof.getUnitCMLength())
            setResult(0,intent)
            finish()
        }
        mCancel.setOnClickListener {
            finish()
        }
    }
}
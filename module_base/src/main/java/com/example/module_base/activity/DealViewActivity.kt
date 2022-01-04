package com.example.module_base.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import com.example.module_base.R
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.databinding.ActivityDealBinding
import com.example.module_base.utils.Constants
import com.example.module_base.utils.MyStatusBarUtil
import com.example.module_base.utils.PackageUtil
import com.example.module_base.utils.toolbarEvent
import com.example.module_base.widget.MyToolbar
import com.tamsiree.rxkit.RxKeyboardTool
import kotlinx.android.synthetic.main.activity_deal.*


class DealViewActivity : BaseViewActivity<ActivityDealBinding>()  {


    var mTitleMsg="用户协议"
    var mContent=R.string.user
    private val umUrl = "https://www.umeng.com/page/policy?spm=a211eg.10560647.0.0.547034dcafEUZJ"
    private val gdtUrl = "https://imgcache.qq.com/gdt/cdn/adn/uniondoc/ylh_sdk_privacy_statement.html"
    private val ttUrl = "https://partner.oceanengine.com/privacy"
    private val email = "2681706890@qq.com"
    private val com = "深圳市天王星互娱科技有限公司"
    private var count = 10


    override fun setFullScreenWindow() {
        MyStatusBarUtil.setColor(this, Color.TRANSPARENT)
    }
    override fun getLayoutView(): Int = R.layout.activity_deal
    override fun initView() {
        when (intent.getIntExtra(Constants.SET_DEAL1, 0)) {
            1 -> {
                mTitleMsg="用户协议"
                mContent=R.string.user
                text.text = PackageUtil.difPlatformName(this,mContent)
            }
            2-> {
                mTitleMsg="隐私协议"
                text.text = getYinsi()
                text.movementMethod = LinkMovementMethod()
            }
            3->{
                mTitleMsg="功能说明"
                mContent=R.string.shareText
                text.text = PackageUtil.difPlatformName(this,mContent)
            }

        }
        privacy_toolbar.setTitle(mTitleMsg)

    }

    override fun initEvent() {

        privacy_toolbar.toolbarEvent(this) {}

        text.setOnClickListener {
            count--
            if (count == 0){
                val cm: ClipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText("text", getYinsi().toString())) //text也可以是"null"

                if (cm.hasPrimaryClip()) {
                    cm.primaryClip?.getItemAt(0)?.text
                }
                Toast.makeText(this,"复制成功",Toast.LENGTH_SHORT).show()
                count= 5
            }
        }


    }

    private fun getYinsi(): SpannableStringBuilder {
        val stringBuilder = SpannableStringBuilder()
        val um = SpannableString(umUrl)
        val gdt = SpannableString(gdtUrl)
        val tt = SpannableString(ttUrl)

        val personalInformation1 = SpannableString("个人常用设备信息、位置信息、网络信息、个人通信信息")
        val personalInformation2 = SpannableString("个人常用设备信息、位置信息、网络信息、个人通信信息")
        val personalInformation3 = SpannableString("个人常用设备信息、位置信息、网络信息、个人通信信息")
        val personalInformation4 = SpannableString("个人常用设备信息、位置信息、网络信息、个人通信信息")

        um.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent()
                intent.setAction("android.intent.action.VIEW");
                intent.data = Uri.parse(umUrl)
                startActivity(intent)
            }
        }, 0, um.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        gdt.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent()
                intent.setAction("android.intent.action.VIEW");
                intent.data = Uri.parse(gdtUrl)
                startActivity(intent)
            }
        }, 0, gdt.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tt.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent()
                intent.setAction("android.intent.action.VIEW");
                intent.data = Uri.parse(ttUrl)
                startActivity(intent)
            }
        }, 0, tt.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        um.setSpan(ForegroundColorSpan(Color.parseColor("#09C1CE")), 0, um.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        gdt.setSpan(ForegroundColorSpan(Color.parseColor("#09C1CE")), 0, gdt.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tt.setSpan(ForegroundColorSpan(Color.parseColor("#09C1CE")), 0, tt.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        personalInformation1.setSpan(UnderlineSpan(),0,personalInformation1.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        personalInformation2.setSpan(UnderlineSpan(),0,personalInformation2.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        personalInformation3.setSpan(UnderlineSpan(),0,personalInformation3.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        personalInformation4.setSpan(UnderlineSpan(),0,personalInformation4.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        stringBuilder.append("隐私政策\n" +
                "\n" +
                "最后更新日期：2021年11月18日\n" +
                "\n"+
                "隐私政策尊重并保护所有使用隐私政策网络服务用户的个人隐私权。\n" +
                "\n" +
                "为了给您提供更准确、更有个性化的服务，我们的隐私政策涵盖我们收集，使用，披露，传输和存储您的信息的方式。但隐私政策将以高度的勤勉、审慎义务对待这些信息。隐私政策会不时更新本隐私权政策。 为了更好地改进产品体验，同时持续提供手机用户最佳的服务，在不会涉及用户个人隐私及敏感数据的前提下，我们会采用如下方式收集您的设备和其他相关信息，本协议解释了这些情况下的信息收集和使用情况。\n" +
                "       \n" +
                "信息的收集和使用\n" +
                "\n" +
                "1. 读取手机状态和身份\n" +
                "\n" +
                "友盟统计需要，获取设备唯一标识信息，用于统计用户使用情况\n" +
                "\n" +
                "2.大致位置（基于网络）\n" +
                "\n" +
                "友盟统计需要，获取用户大概位置，用于统计用户地区分布\n" +
                "\n" +
                "3.无线网络\n" +
                "\n" +
                "用于进行网络定位\n" +
                "\n" +
                "4.查看网络连接\n" +
                "\n" +
                "用于检查网络开启状态\n" +
                "\n" +
                "5.储存\n" +
                "\n" +
                "用于读取您的音视频信息\n" +
                "\n" +
                "6.IMSI、IMEI、MAC信息\n" +
                "\n" +
                "穿山甲SDK通过获取IMSI、IMEI和MAC信息来标记用户设备，为用户推送更智能合理的广告。友盟SDK通过获取IMSI、IMEI和MAC信息来标记用户设备，以此来统计用户对该应用的使用情况\n" +
                "\n" +
                "7.手机IP地址\n" +
                "\n" +
                "穿山甲SDK通过IP地址获取设备所在区域来甄别广告推送通道；友盟SDK通过IP地址获取设备所在区域，来统计用户分布情况\n" +
                "\n" +
                "8.其他\n" +
                "\n" +
                "安装或卸载快捷方式、与蓝牙设置配对、控制手机振动等等\n" +
                "\n" +
                "使用的sdk\n" +
                "\n")
            .append("1.友盟移动统计\n" +
                    "\n" +
                    "提供方名称：友盟同欣（北京）科技有限公司\n" +
                    "\n" +
                    "场景描述：针对App的各纬度的统计功能\n" +
                    "\n" +
                    "收集方式：调用系统相关接口自动采集\n" +
                    "\n" +
                    "个人信息类型：")
            .append(personalInformation1)
            .append("\n"+
                    "\n" +
                    "个人信息字段：设备信息：设备标识符（IMEI、IDFA、Android ID、MAC、OAID等相关信息）、应用信息（应用崩溃信息、通知开关状态、软件列表等相关信息）、设备参数及系统信息（设备类型、设备型号、操作系统及硬件相关信息）\n" +
                    "\n" +
                    "网络信息：IP地址，WiFi信息，基站信息等相关信息\n" +
                    "\n" +
                    "用途或目的：用于统计详细崩溃日志以及行为分析\n" +
                    "\n" +
                    "是否为必要信息：是\n" +
                    "\n" +
                    "信息处理方式：采用去标识化方式对个人信息进行脱敏展示\n" +
                    "\n" +
                    "隐私政策：")
            .append(um)
            .append("\n\n")
            .append("2.优量汇SDK\n" +
                    "\n" +
                    "提供方名称：腾讯\n" +
                    "\n" +
                    "场景描述：通过识别设备信息和地理位置和网络信息用于创建智能标签，为用户推送更智能合理的广告。\n" +
                    "\n" +
                    "收集方式：调用系统相关接口自动采集\n" +
                    "\n" +
                    "个人信息类型：")
            .append(personalInformation2)
            .append("\n"+
                    "\n" +
                    "个人信息字段：设备信息：设备标识符（IMEI、IDFA、Android ID、MAC、OAID等相关信息）、应用信息（应用崩溃信息、通知开关状态、软件列表等相关信息）、设备参数及系统信息（设备类型、设备型号、操作系统及硬件相关信息） 网络信息：IP地址，WiFi信息，基站信息等相关信息\n" +
                    "\n" +
                    "网络信息：IP地址，WiFi信息，基站信息等相关信息， 用户通讯信息\n" +
                    "\n" +
                    "用途或目的：为了实现便签云备份功能，避免用户的保存的便签丢失，和换手机之后能及时同步\n" +
                    "\n" +
                    "是否为必要信息：是\n" +
                    "\n" +
                    "信息处理方式：采用去标识化方式对个人信息进行脱敏展示\n" +
                    "\n" +
                    "隐私政策：")
            .append(gdt)
            .append("\n\n")
            .append("3.穿山甲\n" +
                    "\n" +
                    "提供方名称：今日头条\n" +
                    "\n" +
                    "场景描述：通过识别设备信息和地理位置和网络信息用于创建智能标签，为用户推送更智能合理的广告。\n" +
                    "\n" +
                    "收集方式：调用系统相关接口自动采集\n" +
                    "\n" +
                    "个人信息类型：")
            .append(personalInformation3)
            .append("\n"+
                    "\n" +
                    "个人信息字段：设备信息：设备标识符（IMEI、IDFA、Android ID、MAC、OAID等相关信息）、应用信息（应用崩溃信息、通知开关状态、软件列表等相关信息）、设备参数及系统信息（设备类型、设备型号、操作系统及硬件相关信息）\n" +
                    "\n" +
                    "网络信息：IP地址，WiFi信息，基站信息等相关信息\n" +
                    "\n" +
                    "用途或目的：展示第三方广告\n" +
                    "\n" +
                    "是否为必要信息：是\n" +
                    "\n" +
                    "信息处理方式：采用去标识化方式对个人信息进行脱敏展示\n" +
                    "\n" +
                    "隐私政策：")
            .append(tt)
            .append("\n\n")
            .append(
                "\n" +
                        "第三方帐户\n" +
                        "\n" +
                        "我们的App不存储任何其他第三方服务密码。您的帐户信息不会透露给此应用程序，您可以随时取消与此应用程序的任何社交网站连接。\n" +
                        "\n" +
                        "共享个人数据\n" +
                        "\n" +
                        "为了改进我们的产品并为您提供更好的服务，我们还与代表我们工作的供应商或代理商共享个人数据，用于本隐私政策中所述的目的。例如，我们雇用提供数据分析服务的公司可能需要收集和访问个人数据以提供这些功能。在这种情况下，这些公司必须遵守我们的数据隐私和安全要求。\n" +
                        "\n" +
                        "您的权利\n" +
                        "\n" +
                        "我们非常重视您对个人信息的管理，并尽全力保护您对于您个人信息的访问、修改（更新或更正）、删除以及撤回授权同意的权利，以使您拥有充分的能力保障您的隐私和安全。\n" +
                        "\n" +
                        "（一）管理、撤回授权您的信息\n" +
                        "\n" +
                        "您可以通过关闭功能、在设备设置中更改应用程序权限等方式撤回您的同意，改变您授权我们继续收集个人信息的范围或撤回您的授权。请您理解，特定的业务功能和服务将需要您的信息才能得以完成，当您撤回同意或授权后，我们无法继续为您提供撤回同意或授权所对应的功能和服务，也不再处理您相应的个人信息。但您撤回同意或授权的决定，不会影响此前基于您的授权而开展的个人信息处理。\n" +
                        "\n" +
                        "（二）注销账号\n" +
                        "\n" +
                        "在软件首页点击“个人”-“账号注销”即可注销账号\n" +
                        "\n" +
                        "信息保护\n" +
                        "\n" +
                        "您的信息的安全性对我们很重要。我们保持适当的技术和物理保护措施，以保护您的信息免遭意外或非法破坏或丢失，更改，未经授权的泄露或访问，使用以及我们拥有的所有其他非法形式的数据处理。我们使用与其他类似应用程序开发人员相同的技能来保护您的个人信息。但是，由于互联网不是一个完全安全的环境，我们无法保证您通过我们的应用程序传输的信息不会因违反我们的任何保护措施而被访问，披露，更改或破坏。\n" +
                        "\n" +
                        "数据处理的法律依据\n" +
                        "\n" +
                        "出于本隐私政策中规定的目的，我们处理个人数据，如上所述。我们处理个人数据的法律依据包括以下处理：与您签订合同所必需的处理（例如，为您提供您请求的服务以及识别和验证您以便您使用本网站）; 必须遵守法律要求（例如，遵守适用的会计规则并向执法部门强制披露）; 我们的合法利益所必需的（例如，管理我们与您的关系，确保我们服务的安全性，与您沟通我们的产品和服务）; 并基于我们客户的同意（例如，放置某些cookie并与第三方共享您的信息用于广告目的）。 在某些情况下，您可能需要向我们提供个人数据以进行上述处理，以便我们能够为您提供我们的所有服务，并让您使用我们应用程序的所有功能。\n" +
                        "\n" +
                        "国际个人数据传输\n" +
                        "\n" +
                        "我们的业务可能要求我们将您的个人数据传输到欧洲经济区（“EEA”）以外的国家，包括中华人民共和国或新加坡等国家。我们采取适当措施确保您的个人数据接收者必须遵守保密义务，并采取标准合同条款等措施。可以通过联系我们的帮助中心获取这些条款的副本。\n" +
                        "\n" +
                        "免责声明\n" +
                        "\n" +
                        "就下列相关事宜的发生，${packageManager.getApplicationLabel(applicationInfo)}不承担任何法律责任：\n" +
                        "\n" +
                        "1、由于您将用户密码告知他人或与他人共享注册帐户，由此导致的任何个人信息的泄露，或其他非因${
                            packageManager.getApplicationLabel(
                                applicationInfo
                            )
                        }原因导致的个人信息的泄露； \n" +
                        "\n" +
                        "2、${packageManager.getApplicationLabel(applicationInfo)}根据法律规定或政府相关政策要求提供您的个人信息； \n" +
                        "\n" +
                        "3、任何第三方根据${packageManager.getApplicationLabel(applicationInfo)}各服务条款及声明中所列明的情况使用您的个人信息，由此所产生的纠纷；\n" +
                        "\n" +
                        "4、任何由于黑客攻击、电脑病毒侵入或政府管制而造成的暂时性软件功能暂停； \n" +
                        "\n" +
                        "5、因不可抗力导致的任何后果； \n" +
                        "\n" +
                        "6、${packageManager.getApplicationLabel(applicationInfo)}在各服务条款及声明中列明的使用方式或免责情形。\n" +
                        "\n" +
                        "\n" +
                        "如果您使用了上述${packageManager.getApplicationLabel(applicationInfo)}提供的服务，则表明您理解并同意上述协议并授权${com}接收您提供的有关个人信息，及授权${com}存储备份您提供的有关个人信息。\n" +
                        "\n" +
                        "隐私政策变更\n" +
                        "\n" +
                        "本隐私政策可能会不时更新。我们将通过在此页面上发布新的隐私政策来通知您我们的隐私政策的任何变更。建议您定期查阅本隐私政策以了解任何变更。\n" +
                        "\n" +
                        "联系我们\n" +
                        "\n" +
                        "如果您在使用本应用程序时对隐私有任何疑问，或对我们的做法有疑问，请通过电子邮件联系我们\n" +
                        " \n" +
                        " 客服邮箱：${email}\n" +
                        "\n"
            )
        return stringBuilder
    }

}
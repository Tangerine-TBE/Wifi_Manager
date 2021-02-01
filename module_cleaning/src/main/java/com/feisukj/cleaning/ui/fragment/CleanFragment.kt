package com.feisukj.cleaning.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import androidx.fragment.app.Fragment
import com.example.module_base.BuildConfig
import com.example.module_base.cleanbase.BaseConstant
import com.example.module_base.cleanbase.PackageUtils
import com.example.module_base.cleanbase.SectionData
import com.example.module_base.cleanbase.toast
import com.example.module_base.utils.checkAppPermission
import com.example.module_base.utils.showToast

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.GarbageBean
import com.feisukj.cleaning.bean.GarbageItemBean
import com.feisukj.cleaning.bean.GarbageSectionData
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.file.*
import com.feisukj.cleaning.ui.activity.*
import com.feisukj.cleaning.ui.activity.BatteryInfoActivity
import com.feisukj.cleaning.utils.formatFileSize
import com.feisukj.cleaning.utils.getInstallAppPackageName
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_home_clean.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.random.Random

class CleanFragment:Fragment(R.layout.fragment_home_clean) {
    private val askStoragePermissionLis = arrayListOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )
    companion object{
        val adapterData= ArrayList<SectionData<TitleBean_Group, GarbageBean>>()
        const val TO_SEE_DETAILS_CODE=3
        const val CLEAN_ALREADY_CODE=200
        private var isFirstStart=true
        private const val TO_COMPLETE_ACTIVITY=101
        private const val RED_ICON_SP="red_ic_s"
        private const val SAVE_IDS_KEY="save_ids_k"
        private val currentDate=SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Date())
        private val redIds= arrayOf(R.id.wechat,R.id.savePower,R.id.qq,R.id.notification,R.id.picture,R.id.software,R.id.shortVideo)
        private val topResIds= arrayOf(R.id.allAntivirus,R.id.phoneCoolingDown,R.id.phoneAccelerate)


        private fun putRedIconIndex(sp:SharedPreferences, ids:List<Int>){
//            "[1, 2]"
            sp.edit().clear()
                    .apply()
            val stringBuilder=StringBuilder()
            ids.map {
                var index= redIds.indexOf(it)
                if (index==-1){
                    index= topResIds.indexOf(it)
                    if (index==-1){
                        index=0
                    }else{
                        index+= redIds.size
                    }
                }
                index.toString()
            }
            .forEach {
                stringBuilder.append("$it,")
            }
            sp.edit().putString(currentDate+ SAVE_IDS_KEY,stringBuilder.toString().removeSuffix(","))
                    .apply()
        }

        private fun getTodayRedIconIndex(sp: SharedPreferences):List<Int>{
            val old=sp.getString(currentDate+SAVE_IDS_KEY,null)
            if (old==null){
                val indexs= ArrayList<Int>()
                val ids=ArrayList<Int>()
                while (indexs.size<3){
                    val r=Random.nextInt(redIds.size)
                    if (!indexs.contains(r)){
                        indexs.add(r)
                        ids.add(redIds[r])
                    }
                }
                while (indexs.size<5){
                    val r=Random.nextInt(topResIds.size)+ redIds.size
                    if (!indexs.contains(r)){
                        indexs.add(r)
                        ids.add(topResIds[r- redIds.size])
                    }
                }

                putRedIconIndex(sp,ids)
                return indexs
            }else{
                if (old.isEmpty()){
                    return emptyList()
                }
                return old.split(",").map { it.toInt() }
            }
        }
    }

    var toSetting:(()->Unit)?=null
    private var anim1 = RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
    private var anim2 = RotateAnimation(0f,-360f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
    private var anim3 = RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
    private var anim4 = RotateAnimation(0f,-360f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
    private var anim5 = RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
    private var anim6 = ScaleAnimation(1f,1.2f,1f,1.2f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
    private enum class ScanState{
        noScan,
        runScan,
        completeScan,
        cleanComplete;
    }

    private var currentState=ScanState.noScan
        set(value) {
            if(value!=field){
                field=value
                updateUIState()
            }
        }
    private var totalGarbageSize=0L
        set(value) {
            field=value
            val p=formatFileSize(context,field)
            activity?.runOnUiThread {
                if (!isAdded){
                    return@runOnUiThread
                }
                sizeNumber.text=p.first
                sizeUnit.text=p.second
            }
        }
    private val sharedPreferences by lazy { context?.getSharedPreferences(RED_ICON_SP, Context.MODE_PRIVATE) }
    private val ids=ArrayList<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topCleanView.setPadding(0, ImmersionBar.getStatusBarHeight(this),0,0)
        initListener()
        updateUIState()
        activity?.let {

        }
        if (isFirstStart){
            startScanning()
        }
        isFirstStart=false

        sharedPreferences?.let { preferences ->
            val t=getTodayRedIconIndex(preferences).map {
                if (it>=redIds.size){
                    topResIds[it- redIds.size]
                }else{
                    redIds[it]
                }
            }
            ids.addAll(t)
            ids.forEach {
                try {
                    view.findViewById<View>(it).isSelected=true
                }catch (e:Exception){

                }
            }
        }
        try {
            FileContainer.cachePhotoCount.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            photoSize.text="发现${it}张照片"
            })
        }catch (e : Exception){
            e.printStackTrace()
        }

        initAnim()

        title_text.text ="清理"


        checkAppPermission(askStoragePermissionLis,{
            FileManager.start()
        },{
            showToast("我们将无法为您提清理服务！！！")
        },fragment = this)


    }



    private fun initAnim() {
        setAnim(anim1,1600,true,Animation.RESTART,LinearInterpolator(),-1)
        setAnim(anim2,1000,true,Animation.RESTART,LinearInterpolator(),-1)
        setAnim(anim3,2000,true,Animation.RESTART,LinearInterpolator(),-1)
        setAnim(anim4,1800,true,Animation.RESTART,LinearInterpolator(),-1)
        setAnim(anim5,2200,true,Animation.RESTART,LinearInterpolator(),-1)

        anim6.apply {
            this.duration = 900
            this.interpolator = LinearInterpolator()
            this.repeatMode = Animation.REVERSE
            this.repeatCount = -1
        }
    }

    private fun setAnim(anim : RotateAnimation, duration : Long, fillAfter : Boolean, repeatMode : Int, interpolator : LinearInterpolator, repeatCount : Int) {
        anim.duration = duration
        anim.fillAfter = fillAfter
        anim.repeatMode = repeatMode
        anim.interpolator = interpolator
        anim.repeatCount = repeatCount
    }

    override fun onResume() {
        super.onResume()
        val paths= shortVideoPath.map { it.path }
        thread {
            var count=0
            val list=LinkedList<File>(paths.map { File(it) })
            while (list.isNotEmpty()){
                val file=list.pop()?:continue
                if (file.exists()){
                    if (file.isDirectory){
                        list.addAll(file.listFiles()?:continue)
                    }else if (file.isFile){
                        count++
                    }
                }
            }

            activity?.runOnUiThread {
//                shortVideoCount?.text="发现${count}个短视频"
            }
        }
    }

    private fun initListener(){
        setting.setOnClickListener {
//            startActivity(Intent(context,SettingActivity::class.java))
            toSetting?.invoke()
        }
        val redAction={view:View->
            if (ids.contains(view.id)){
                view.isSelected=false
                ids.remove(view.id)
                sharedPreferences?.apply {
                    putRedIconIndex(this,ids)
                }
            }
        }
        //手机瘦身
        loseWeight.setOnClickListener {
            startActivity(Intent(context,PhoneLoseActivity::class.java))
        }
        //查看详情
        seeDetails.setOnClickListener {
            if (currentState==ScanState.runScan){
                toast("正在扫描中...")
                return@setOnClickListener
            }
            val intent=Intent(context,CleanActivity::class.java)
            startActivityForResult(intent,TO_SEE_DETAILS_CODE)
        }
        //一键清理
        cleanButton_bg.setOnClickListener {
            if (currentState==ScanState.noScan||currentState==ScanState.cleanComplete){
                startScanning()
            }else if (currentState==ScanState.runScan){
                toast("正在扫描中...")
                return@setOnClickListener
            }else if (currentState==ScanState.completeScan){
                currentState=ScanState.cleanComplete
                val intent=Intent(context, CleanAnimatorActivity::class.java)
                intent.putExtra(CompleteActivity.SIZE_KEY,totalGarbageSize)
                startActivityForResult(intent,TO_COMPLETE_ACTIVITY)
                Thread {
                    kotlin.run {
                        adapterData.flatMap {
                            it.getItemData()
                        }.flatMap {
                            it.getItems()
                        }.flatMap {
                            it.getFiles()
                        }.forEach {
                            if (!BuildConfig.DEBUG){
                                it.delete()
                            }
                        }

                        activity?.runOnUiThread {
                            totalGarbageSize=0L
                            adapterData.clear()
                        }
                    }
                }.start()
            }
        }
        //微信专清
        wechat.setOnClickListener {
            redAction.invoke(it)
            val intent= Intent(context, WeChatAndQQCleanActivity::class.java)

            intent.putExtra(WeChatAndQQCleanActivity.KEY, WeChatAndQQCleanActivity.WE_CHAT)
            startActivity(intent)
        }
        //QQ专清
        qq.setOnClickListener {
            redAction.invoke(it)
            val intent=Intent(context,WeChatAndQQCleanActivity::class.java)

            intent.putExtra(WeChatAndQQCleanActivity.KEY, WeChatAndQQCleanActivity.QQ)
            startActivity(intent)
        }
        //通知栏清理
        notification.setOnClickListener {
            redAction.invoke(it)

            startActivity(Intent(context, NotificationCleanActivity::class.java))
        }
        //图片专清
        picture.setOnClickListener {
            redAction.invoke(it)

            startActivity(Intent(context, PhotoCleanActivity::class.java))
        }

        if (BaseConstant.channel=="_oppo"){
            ll_.visibility = View.GONE
        }
        //软件管理
        software.setOnClickListener {
            redAction.invoke(it)

            startActivity(Intent(context, AppActivity2::class.java))
        }
        shortVideo.setOnClickListener {
            redAction.invoke(it)
            //短视频

            startActivity(Intent(context, ShortVideoDesActivity2::class.java))
        }

        //全盘杀毒
        allAntivirus.setOnClickListener {
            redAction.invoke(it)
            startActivity(Intent(context,AntivirusActivity::class.java))
        }
        //手机降温
        phoneCoolingDown.setOnClickListener {
            redAction.invoke(it)
            context?.apply {
                startActivity(CoolingActivity.getIntent(this))
            }
        }
        //强力加速
        phoneAccelerate.setOnClickListener {
            redAction.invoke(it)
            context?.apply {
                startActivity(StrongAccelerateActivity.getIntent(this))
            }
        }
        //网络加速
        network.setOnClickListener {
            context?.apply {
                startActivity(NetworkActivity.getIntent(this))
            }
        }
        //电池检测
        battery.setOnClickListener {
            startActivity(Intent(context, BatteryInfoActivity::class.java))
        }
        //一键省电
        savePower.setOnClickListener {
            redAction.invoke(it)
            context?.apply {
                startActivity(AccelerateActivity.getIntent(this))
            }
        }
        //电池保护
        batteryProtect.setOnClickListener {
            startActivity(Intent(context,BatteryProtectActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==TO_SEE_DETAILS_CODE&&resultCode==CLEAN_ALREADY_CODE){
            currentState=ScanState.cleanComplete
        }
        if (requestCode==TO_COMPLETE_ACTIVITY){

        }
    }

    private fun updateUIState(){
        val clip = cleanButton_anim?.drawable
        val time = Timer()
        when(currentState){
            ScanState.noScan -> {
                cleanButton.text="一键扫描"
//                tipText.text="你的手机垃圾较多"
            }
            ScanState.runScan -> {
                if (viewSwitcher.currentView!=runScanView){
                    viewSwitcher.showNext()
                }
                seeDetails.visibility = View.GONE
                tipText.visibility = View.VISIBLE
                cleanButton.text="扫描中..."
                if (cleanButton_anim != null)
                    cleanButton_anim.visibility = View.VISIBLE

                round_1.animation = anim1
                round_2.animation = anim2
                round_3.animation = anim3
                round_4.animation = anim4
                round_5.animation = anim5

                time.schedule(object : TimerTask(){

                    override fun run() {
                        clip?.apply {
                            activity?.runOnUiThread {
                                if (clip.level >= 10000){
                                    clip.level = 0
                                }else{
                                    clip.level += 40
                                }
                                if (currentState == ScanState.completeScan){
                                    clip.level = 0
                                    time.cancel()
                                }
                            }
                        }

                    }
                },5,5)
            }
            ScanState.completeScan -> {
                if (viewSwitcher.currentView!=runScanView){
                    viewSwitcher.showNext()
                }
                seeDetails.visibility = View.VISIBLE
                tipText.visibility = View.GONE
                cleanButton.text=""

                round_1.clearAnimation()
                round_2.clearAnimation()
                round_3.clearAnimation()
                round_4.clearAnimation()
                round_5.clearAnimation()

                cleanButton_bg.setImageResource(R.mipmap.ic_main_btn_bg_2)
                cleanButton_bg.animation = anim6
            }
            ScanState.cleanComplete ->{
                if (viewSwitcher.currentView!=scanIcon){
                    viewSwitcher.showNext()
                }
                cleanButton_bg.setImageResource(R.mipmap.ic_main_btn_bg)
                cleanButton.text="再次扫描"
                cleanButton.clearAnimation()
                cleanButton_bg.clearAnimation()
//                tipText.text="你的手机非常干净"
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun startScanning(){
        currentState=ScanState.runScan
        adapterData.clear()
        val cache= GarbageSectionData<TitleBean_Group>()
        val titleCache= TitleBean_Group().also {
            it.isCheck=true
        }
        titleCache.title=getString(R.string.cacheGarbage)
        titleCache.icon=R.mipmap.ic_clean_cache
        cache.groupData=titleCache
        adapterData.add(cache)

        val ad= GarbageSectionData<TitleBean_Group>()
        val titleAd= TitleBean_Group().also {
            it.isCheck=true
        }
        titleAd.title=getString(R.string.adGarbage)
        titleAd.icon = R.mipmap.ic_clean_ad
        ad.groupData=titleAd
        adapterData.add(ad)

        val unInstall= GarbageSectionData<TitleBean_Group>()
        val titleGarbage= TitleBean_Group().also {
            it.isCheck=true
        }
        titleGarbage.title=getString(R.string.unloadingResidue)
        titleGarbage.icon = R.mipmap.ic_clean_uninstall
        unInstall.groupData=titleGarbage
        adapterData.add(unInstall)

        val installedApk= GarbageSectionData<TitleBean_Group>()
        val installedApkTitle= TitleBean_Group().also {
            it.title=getString(R.string.installedApk)
            it.icon = R.mipmap.ic_clean_installedapk
        }
        installedApk.groupData=installedApkTitle
        adapterData.add(installedApk)

        val unInstalledApk= GarbageSectionData<TitleBean_Group>()
        val unInstalledApkTitle= TitleBean_Group().also {
            it.title=getString(R.string.unInstalledApk)
            it.icon = R.mipmap.ic_clean_uninstalledapk
        }
        unInstalledApk.groupData=unInstalledApkTitle
        adapterData.add(unInstalledApk)
        Thread{
            val packageNames= getInstallAppPackageName()
            val garbageItems=applicationGarbagePath.toList().groupBy {
                it.packageName
            }
            //缓存垃圾"
            packageNames.forEach { packageName ->
                if (!isAdded){
                    return@Thread
                }
                val cacheGarbage=GarbageBean(packageName,null)
                garbageItems[packageName]?.map { GarbageItemBean(it.path,it.des) }?.forEach {
                    FileManager.scanDirFile(File(it.path),{this},{true},object :DirNextFileCallback<File>{
                        override fun onNextFile(item: File) {
                            it.addFile(item)
                        }
                    })
                    if (it.getFiles().isNotEmpty()) {
                        cacheGarbage.addItem(it)
                        totalGarbageSize+=it.fileLength
                    }
                }
                if (cacheGarbage.getItems().isNotEmpty()) {
                    cache.addItem(cacheGarbage)
                }
            }

            //卸载残留
            val unInstallPath= mutableListOf(*applicationGarbagePath.toTypedArray())
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
                unInstallPath.removeIf {
                    packageNames.contains(it.packageName)
                }
            }else{
                packageNames.forEach { packageName->
                    val c=unInstallPath.filter { it.packageName==packageName }
                    unInstallPath.removeAll(c)
                }
            }
            unInstallPath.forEach { garbage ->
                if (!isAdded){
                    return@Thread
                }
                val unInstallGarbage=unInstall.getItemData().find { it.packageName==garbage.packageName }?:GarbageBean(garbage.packageName,garbage.appName)
                val unInstallGarbageItem=GarbageItemBean(garbage.path,garbage.des)
                FileManager.scanDirFile(File(garbage.path),{this},{true},object :DirNextFileCallback<File>{
                    override fun onNextFile(item: File) {
                        unInstallGarbageItem.addFile(item)
                    }
                })
                if (unInstallGarbageItem.getFiles().isNotEmpty()){
                    unInstallGarbage.addItem(unInstallGarbageItem)
                    totalGarbageSize += unInstallGarbageItem.fileLength
                }
                if (unInstallGarbage.getItems().isNotEmpty()){
                    if (!unInstall.getItemData().contains(unInstallGarbage)){
                        unInstall.addItem(unInstallGarbage)
                    }else{
//                        adapter.changeSubItem(unInstall,unInstallGarbage)
                    }
                }
            }

            //广告垃圾
            val adType=adGarbagePaths.toList().map { it.appName }.toSet()
            adType.forEach {appName->
                if (!isAdded){
                    return@Thread
                }
                val itemAd=adGarbagePaths.toList().filter { it.appName==appName }
                if (itemAd.isNotEmpty()){
                    val adGarbageItem=GarbageBean(itemAd.first().packageName,itemAd.first().appName)
                    itemAd.forEach {
                        val garbageItem=GarbageItemBean(it.path,it.des)
                        FileManager.scanDirFile(File(it.path),{this},{true},object :DirNextFileCallback<File>{
                            override fun onNextFile(item: File) {
                                garbageItem.addFile(item)
                            }
                        })
                        if (garbageItem.getFiles().isNotEmpty()){
                            adGarbageItem.addItem(garbageItem)
                            totalGarbageSize += garbageItem.fileLength
                        }
                    }
                    if (adGarbageItem.getItems().isNotEmpty()){
                        ad.addItem(adGarbageItem)
                    }
                }
            }

            //安装包
            FileContainer.getApkFileList().forEach { appBean ->
                totalGarbageSize+=appBean.fileSize
                if (appBean.isInstall){
                    val g=GarbageBean(appBean.packageName,appBean.label,appBean.absolutePath.hashCode())
                    val a=GarbageItemBean(appBean.absolutePath,"").also { it.addFile(File(appBean.absolutePath)) }
                    g.addItem(a)
                    installedApk.addItem(g)
                }else{
                    val g=GarbageBean(appBean.packageName,appBean.label,appBean.absolutePath.hashCode())
                    g.icon=appBean.icon?.get()
                    val a=GarbageItemBean(appBean.absolutePath,"").also { it.addFile(File(appBean.absolutePath)) }
                    g.addItem(a)
                    unInstalledApk.addItem(g)
                }
            }
            activity?.runOnUiThread {
                currentState=ScanState.completeScan
            }
        }.start()
    }
}
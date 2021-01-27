package com.feisukj.cleaning.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Base64
import com.example.module_base.cleanbase.BaseConstant

import com.feisukj.cleaning.file.ApplicationGarbage
import com.feisukj.cleaning.file.ShortVideoPath

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


/**
 * 这个类就是实现从assets目录读取数据库文件然后写入SDcard中,
 * 如果在SDcard中存在，就打开数据库，不存在就从assets目录下复制过去
 * @author wangjian
 */
object SQLiteDbManager {
    //数据库存储路径
    private val dbFilePath = "data/data/${BaseConstant.packageName}/clean.db"
    private val dbDirPath = "data/data/${BaseConstant.packageName}"
    private var database: SQLiteDatabase? = null

    private fun getSQLite(context: Context):SQLiteDatabase?{
        database.let {
            return if (it==null){
                val f=openDatabase(context)
                database= f
                f
            }else{
                it
            }
        }
    }


    private fun getAppInfo():List<AppInfo>{
        val db=getSQLite(BaseConstant.application)?:return emptyList()
        val cursor= db.rawQuery("SELECT * FROM com_shyz_clean_entity_AppInfoClean",null)
        if (cursor==null){
            return emptyList()
        }
        val garbageList=ArrayList<AppInfo>()
        while (cursor.moveToNext()){
            val appName=cursor.getString(cursor.getColumnIndex("appName"))
            val packageName=cursor.getString(cursor.getColumnIndex("packageName"))
            val appinfo=AppInfo(appName, packageName)
            garbageList.add(appinfo)
        }
        cursor.close()
        return garbageList
    }

    fun getGarbagePicPathInfo():List<GarbageData>{
        val garbageList=ArrayList<GarbageData>()
        try {
            val db=getSQLite(BaseConstant.application)
            val cursor=db?.rawQuery("SELECT * FROM com_shyz_clean_entity_FilePathInfoPicClean",null)
            if (cursor==null){
                return emptyList()
            }
            while (cursor.moveToNext()){
                val appName=cursor.getString(cursor.getColumnIndex("appName"))
                val packageName=cursor.getString(cursor.getColumnIndex("packageName"))
                val garbagetype=cursor.getString(cursor.getColumnIndex("garbagetype"))
                val garbageName=cursor.getString(cursor.getColumnIndex("garbageName"))
                val filePath=cursor.getString(cursor.getColumnIndex("filePath"))
                decrypt(filePath)?.let {
                    val garbageDataBean=GarbageData(appName, packageName, garbagetype, garbageName, it)
                    garbageList.add(garbageDataBean)
                }
            }
            cursor.close()
        }catch (e:Exception){

        }

        return garbageList
    }

    fun getShortVideoPathInfo():List<ShortVideoPath>{
        val db=getSQLite(BaseConstant.application)?:return emptyList()
        val cursor= db.rawQuery("SELECT * FROM com_shyz_clean_entity_ShortVideoPath",null)
        if (cursor==null){
            return emptyList()
        }
        val garbageList=ArrayList<ShortVideoPath>()
        val appinfos= getAppInfo()
        while (cursor.moveToNext()){
            val appName=cursor.getString(cursor.getColumnIndex("appName"))
            val filePath=cursor.getString(cursor.getColumnIndex("path"))
            decrypt(filePath)?.let { path ->
                val packageName= appinfos.find { it.appName==appName }?.packageName
                val garbageDataBean= ShortVideoPath(packageName, path, appName)
                garbageList.add(garbageDataBean)
            }
        }
        cursor.close()
        return garbageList
    }

    fun getAppGarbageFile():List<ApplicationGarbage>{
        val db=getSQLite(BaseConstant.application)
        val cursor=db?.rawQuery("SELECT * FROM com_shyz_clean_entity_FilePathInfoClean",null)
        if (cursor==null){
            return emptyList()
        }
        val garbageList=ArrayList<ApplicationGarbage>()
        while (cursor.moveToNext()){
            val appName=cursor.getString(cursor.getColumnIndex("appName"))
            val packageName=cursor.getString(cursor.getColumnIndex("packageName"))
            val garbagetype=cursor.getString(cursor.getColumnIndex("garbagetype"))
            val garbageName=cursor.getString(cursor.getColumnIndex("garbageName"))
            val filePath=cursor.getString(cursor.getColumnIndex("filePath"))
            decrypt(filePath)?.let {
//                Log.i("目录","包名：${packageName} 路径："+it)
                val garbageDataBean=ApplicationGarbage(packageName, appName, garbageName, it)
                garbageList.add(garbageDataBean)
            }
        }
        cursor.close()
        return garbageList
    }

    fun getUnloadingResidue():List<UnloadingResidue>{
        val db=getSQLite(BaseConstant.application)
        val cursor=db?.rawQuery("SELECT * FROM com_shyz_clean_entity_FilePathInfoUnloadingResidue",null)
        if (cursor==null){
            return emptyList()
        }
        val unloadingResidue=ArrayList<UnloadingResidue>()
        while (cursor.moveToNext()){
            val appName=cursor.getString(cursor.getColumnIndex("appName"))
            val packageName=cursor.getString(cursor.getColumnIndex("packageName"))
            val rootPath=cursor.getString(cursor.getColumnIndex("rootPath"))
            decrypt(rootPath)?.let {
                val garbageDataBean=UnloadingResidue(appName, packageName, it)
                unloadingResidue.add(garbageDataBean)
            }
        }
        cursor.close()
        return unloadingResidue
    }

    private fun openDatabase(context: Context): SQLiteDatabase? {
        val jhPath = File(dbFilePath)
        val am = context.assets
        val `is` = am.open("clean.db")
        if (jhPath.exists()){
            if (jhPath.length()!=`is`.available().toLong()){
                jhPath.delete()
            }
        }
        return if (jhPath.exists()) {
            SQLiteDatabase.openOrCreateDatabase(jhPath, null)
        } else {
            val path = File(dbDirPath)
            path.mkdir()
            try {
                val fos = FileOutputStream(jhPath)
                val buffer = ByteArray(1024)
                var count: Int
                while (`is`.read(buffer).also { count = it } > 0) {
                    fos.write(buffer, 0, count)
                }
                fos.flush()
                fos.close()
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
            openDatabase(context)
        }
    }

    private const val sKey = "78D7BFDC144F886E42CA6B55F20F14A8"
    private fun decrypt(sSrc: String): String? {
        return try {
            val raw = sKey.toByteArray(charset("utf-8"))
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")//AES/ECB/NoPadding
            cipher.init(Cipher.DECRYPT_MODE, skeySpec)
            val encrypted1 = Base64.decode(sSrc.toByteArray(), Base64.DEFAULT) // 先用base64解密
            val original = cipher.doFinal(encrypted1)
            "/storage/emulated/0"+
                    String(original, Charset.forName("utf-8"))
        } catch (ex: Exception) {
            ex.printStackTrace()
      //      MobclickAgent.reportError(BaseConstant.application,ex)
            null
        }
    }
}

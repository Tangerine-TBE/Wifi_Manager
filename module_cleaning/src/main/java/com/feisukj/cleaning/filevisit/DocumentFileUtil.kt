package com.feisukj.cleaning.filevisit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.example.module_base.cleanbase.iLog
import com.feisukj.cleaning.ui.activity.OpenAndroidRPermissionActivity
import java.io.File

//@TargetApi(Build.VERSION_CODES.R)
object DocumentFileUtil {
    //目录:[/storage/emulated/0/Android/data/com.tencent.mm/MicroMsg/Download]不存在或无访问权限
    //目录:[/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv]，文件:26个
    fun isContainDataDir(path: String):Boolean{
        return path.startsWith("/storage/emulated/0/Android/data",true)
    }
    private var isDataPermission=false

    fun isDataDirPermission(context: Context):Boolean{
        if (isDataPermission){
            return true
        }
        val dataDocumentFile = DocumentFile.fromTreeUri(context, Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata"))
        isDataPermission =dataDocumentFile?.canRead()?.and(dataDocumentFile.canWrite())?:false
        return isDataPermission
    }

    fun requestDataDocument(fragment: Fragment,requestCode:Int){
        fragment.apply {
            val dataDocumentFile = DocumentFile.fromTreeUri(requireContext(), Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata"))
            val intent1 = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            intent1.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
            intent1.putExtra(DocumentsContract.EXTRA_INITIAL_URI, dataDocumentFile?.uri)
            startActivityForResult(intent1, requestCode)
            startActivity(Intent(requireContext(), OpenAndroidRPermissionActivity::class.java))
        }
    }

    @SuppressLint("WrongConstant")
    fun onActivityResult(activity: Activity,data: Intent) {
        activity.apply {
            val uri=data.data
            if (uri != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    contentResolver.takePersistableUriPermission(
                            uri, data.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    )
                }
                val dataDocumentFile = DocumentFile.fromTreeUri(activity, uri)
                isDataPermission =dataDocumentFile?.canRead()?.and(dataDocumentFile.canWrite())?:false
            }else{
                iLog("文件访问权限保存失败")
            }
        }
    }

    @SuppressLint("WrongConstant")
    fun onActivityResult(fragment: Fragment,data: Intent) {
        fragment.apply {
            val uri=data.data
            if (uri != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    requireContext().contentResolver.takePersistableUriPermission(
                            uri, data.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    )
                }
                val dataDocumentFile = DocumentFile.fromTreeUri(fragment.requireContext(), uri)
                isDataPermission =dataDocumentFile?.canRead()?.and(dataDocumentFile.canWrite())?:false
            }else{
                iLog("文件访问权限保存失败")
            }
        }
    }

    private fun pathToUriString(path: String): Uri {
        return Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A"+
                path.removePrefix("/storage/emulated/0/").removeSuffix("/").replace("/","%2F"))
    }

    fun uriStringToPath(uriString: String):String{//content://com.android.externalstorage.documents/tree/primary:Android/data/document/primary:Android/data/com.tencent.mm/MicroMsg/Download/个人简历.doc
        val root= Environment.getExternalStorageDirectory().path+"/"
        return if (uriString.startsWith("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary")) {
            uriString.let {
                Uri.decode(it).replace("content://com.android.externalstorage.documents/tree/primary:Android/data/document/primary:", root)
            }
        } else {
            throw java.lang.IllegalArgumentException("uriString 不是有效的${root + "Android/data"}目录下的uri")
        }
    }

    fun pathToDocumentFile(context: Context,path: String):DocumentFile?{
        val uri= pathToUriString(path.replace("/storage/emulated/0/android/data","/storage/emulated/0/Android/data"))
        return if (DocumentFile.isDocumentUri(context,uri)){
            val file= File(path)
            if (file.isFile){
                DocumentFile.fromSingleUri(context,uri)
            }else{
                val dir=DocumentFile.fromTreeUri(context,uri)
                if (dir?.name!=file.name|| uriStringToPath(dir?.uri.toString()) =="/storage/emulated/0/Android/data"){
                    iLog("path:${path},uri:${uri},DocumentFile uri:${dir?.uri}","转换为DocumentFile失败")
                    return null
                }else{
//                    iLog("path:${path},uri:${dir?.uri}","转换为DocumentFile成功")
                }
                dir
            }
        }else{
            throw IllegalArgumentException("path 转换为DocumentFile失败")
        }
    }
}
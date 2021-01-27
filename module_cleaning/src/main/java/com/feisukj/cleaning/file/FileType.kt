package com.feisukj.cleaning.file

import com.feisukj.cleaning.utils.Constant
import com.feisukj.cleaning.utils.isImageFile
import java.io.File

enum class FileType(vararg val format:String) {
    garbageImage(),
    music(".mp3"),
    doc(".doc",".docx"),
    pdf(".pdf"),
    ppt(".ppt"),
    xls(".xls",".xlsx"),
    txt(".txt"),
    apk(".apk"),
    bigFile();
    fun isContain(file: File):Boolean{
        return when(this){
            bigFile -> {
                file.length().shr(20)>10
            }
            music,doc,pdf,ppt,xls,txt,apk -> {
                format.any {
                    file.name.endsWith(it,true)
                }
            }
            garbageImage -> {
                val path=file.absolutePath
                val isImportant=
                        file.name.indexOf('.')!=-1 ||
                                path.contains(Constant.TENCENT_PATH,true)
                                ||path.contains(Constant.QQ_TENCENT,true)
                                ||path.contains(Constant.DCIM,true)
                if (isImportant){
                    false
                }else{
                    Constant.garbagePicturePaths.any { file.absolutePath.startsWith(it,true)}|| isImageFile(path)
                }
            }
        }
    }
}
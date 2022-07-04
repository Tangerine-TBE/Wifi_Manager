package com.feisukj.cleaning.bean

import com.feisukj.cleaning.R
import com.feisukj.cleaning.filevisit.FileR
import com.feisukj.cleaning.utils.Constant
import java.io.File

class AllFileBean(file:FileR):FileBean(file) {
    var dirCount:Int=0
    var fileIcon:Int= R.mipmap.icon_no
        private set
        get() {
            if (field== R.mipmap.icon_no) {
                field = if (!isFile){
                    R.mipmap.icon_wjj
                }else {
//                    when (format) {
//                        "mp3" -> R.mipmap.icon_mp_iii
//                        "mp4" -> R.mipmap.icon_mp_iiii
//                        "doc" -> R.mipmap.icon_doc
//                        "pdf" -> R.mipmap.icon_pdf
//                        "txt" -> R.mipmap.icon_txt
//                        "ppt" -> R.mipmap.icon_ppt
//                        "xls" -> R.mipmap.icon_xls
//                        else -> R.mipmap.icon_no
//                    }
                    when {
                        fileName.endsWith(".mp3",true)->{
                            R.mipmap.icon_mp_iii
                        }
                        fileName.endsWith(".mp4",true)->{
                            R.mipmap.icon_mp_iiii
                        }
                        fileName.endsWith(".doc",true)->{
                            R.mipmap.icon_doc
                        }
                        fileName.endsWith(".pdf",true)->{
                            R.mipmap.icon_pdf
                        }
                        fileName.endsWith(".txt",true)->{
                            R.mipmap.icon_txt
                        }
                        fileName.endsWith(".ppt",true)->{
                            R.mipmap.icon_ppt
                        }
                        fileName.endsWith(".xls",true)->{
                            R.mipmap.icon_xls
                        }
                        Constant.ZIP_FORMAT.any { fileName.endsWith(it,true) }->{
                            R.mipmap.icon_zip
                        }
                        else -> R.mipmap.icon_no
                    }
                }
            }
            return field
        }
}
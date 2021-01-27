package com.feisukj.cleaning.file

import com.feisukj.cleaning.bean.FileBean

interface NextFileCallback {
    fun onNextFile(type: FileType,fileBean: FileBean)

    fun onNextFiles(type: FileType,fileBeans: List<FileBean>){}

    fun onComplete()
}
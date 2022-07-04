package com.feisukj.cleaning.bean
import com.feisukj.cleaning.filevisit.FileR
import java.io.File

class ImageBean(file: File):FileBean(file) {
    constructor(path:String):this(File(path)){

    }
    constructor(fileR: FileR):this(File(fileR.absolutePath)){
        this.fileR=fileR
    }
    var finger:Long=0
    var id:Long=0
}
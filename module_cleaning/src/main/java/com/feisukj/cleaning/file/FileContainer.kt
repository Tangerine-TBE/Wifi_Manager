package com.feisukj.cleaning.file

import androidx.lifecycle.MutableLiveData
import com.feisukj.cleaning.bean.AppBean
import com.feisukj.cleaning.bean.FileBean
import com.feisukj.cleaning.bean.ImageBean

object FileContainer {
    private val apkList=HashSet<AppBean>()
    private val bigFile=HashSet<FileBean>()
    private var cachePhoto=ArrayList<ImageBean>()
    val cachePhotoCount=MutableLiveData<Int>(0)

    fun getApkFileList():List<AppBean>{
        synchronized(apkList){
            return apkList.toList()
        }
    }

    fun addApk(appBean: AppBean){
        synchronized(apkList){
            apkList.add(appBean)
        }
    }

    fun addAllApk(appBeanList:List<AppBean>){
        synchronized(apkList){
            apkList.addAll(appBeanList)
        }
    }

    fun removeApk(appBean: AppBean){
        synchronized(apkList){
            apkList.remove(appBean)
        }
    }

    fun removeAllApk(appBeans:List<AppBean>){
        synchronized(apkList){
            apkList.removeAll(appBeans)
        }
    }

    /************************************/
    fun getBigFileList():List<FileBean>{
        synchronized(bigFile){
            return bigFile.toList()
        }
    }

    fun addBigFile(item: FileBean){
        synchronized(bigFile){
            bigFile.add(item)
        }
    }

    fun addAllBigFile(items:List<FileBean>){
        synchronized(bigFile){
            bigFile.addAll(items)
        }
    }

    fun removeBigFile(item: FileBean){
        synchronized(bigFile){
            bigFile.remove(item)
        }
    }

    fun removeAllBigFile(items:List<FileBean>){
        synchronized(bigFile){
            bigFile.removeAll(items)
        }
    }

    /**********************************/
    fun getPhotoFileList():List<ImageBean>{
        synchronized(cachePhoto){
            return cachePhoto.toList()
        }
    }

    fun addPhotoFile(item: ImageBean){
        synchronized(cachePhoto){
            cachePhoto.add(item)
            cachePhotoCount.postValue(cachePhoto.size)
        }
    }

    fun addAllPhotoFile(items:List<ImageBean>){
        synchronized(cachePhoto){
            cachePhoto.addAll(items)
            cachePhotoCount.postValue(cachePhoto.size)
        }
    }

    fun removePhotoFile(item: ImageBean){
        synchronized(cachePhoto){
            cachePhoto.remove(item)
            cachePhotoCount.postValue(cachePhoto.size)
        }
    }

    fun removeAllPhotoFile(items:Collection<ImageBean>){
        synchronized(cachePhoto){
            cachePhoto.removeAll(items)
            cachePhotoCount.postValue(cachePhoto.size)
        }
    }

    fun clearPhotoFile(){
        synchronized(cachePhoto){
            cachePhoto.clear()
            cachePhotoCount.postValue(cachePhoto.size)
        }
    }
}
package com.feisukj.cleaning.db

data class GarbageData(val appName:String,
                       val packageName:String,
                       val garbagetype:String,
                       val garbageName:String,
                       val filePath:String)

data class AppInfo(val appName: String,
                   val packageName: String)

data class UnloadingResidue(val appName: String,
                            val packageName: String,
                            val rootPath:String)
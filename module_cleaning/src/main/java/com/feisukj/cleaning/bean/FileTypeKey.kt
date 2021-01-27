package com.feisukj.cleaning.bean

/**
 *
 */
class FileTypeKey {
    var fileType:FileType?=null
    var lastModified=0L
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileTypeKey

        if (fileType != other.fileType) return false

        return true
    }
    override fun hashCode(): Int {
        return fileType?.hashCode() ?: 0
    }
}
enum class FileType{//文件类型
    picAlbum,picQQ,picWe,picDown,picScreenshots,
    videoAlbum,videoQQ,videoWe,videoDown,
    musicQQ,musicWe,musicDown,
    apkQQ,apkWe,apkDown,
    fileQQ,fileWe,fileDown;
}
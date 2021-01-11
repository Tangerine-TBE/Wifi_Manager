package com.example.module_tool.utils

import android.util.Size

/**
 * 通过对比得到与宽高比最接近的预览尺寸（如果有相同尺寸，优先选择）
 *
 * @param isPortrait 是否竖屏
 * @param surfaceWidth 需要被进行对比的原宽
 * @param surfaceHeight 需要被进行对比的原高
 * @param preSizeList 需要对比的预览尺寸列表
 * @return 得到与原宽高比例最接近的尺寸
 */
fun getCloselyPreSize(isPortrait: Boolean, surfaceWidth: Int, surfaceHeight: Int, preSizeList: List<Size>): Size? {
    val reqTmpWidth: Int
    val reqTmpHeight: Int
    // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
    if (isPortrait) {
        reqTmpWidth = surfaceHeight
        reqTmpHeight = surfaceWidth
    } else {
        reqTmpWidth = surfaceWidth
        reqTmpHeight = surfaceHeight
    }
    //先查找preview中是否存在与surfaceview相同宽高的尺寸
    for (size in preSizeList) {
        if (size.width === reqTmpWidth && size.height === reqTmpHeight) {
            return size
        }
    }

    // 得到与传入的宽高比最接近的size
    val reqRatio = reqTmpWidth.toFloat() / reqTmpHeight
    var curRatio: Float
    var deltaRatio: Float
    var deltaRatioMin = java.lang.Float.MAX_VALUE
    var retSize: Size? = null
    for (size in preSizeList) {
        curRatio = size.width.toFloat() / size.height
        deltaRatio = Math.abs(reqRatio - curRatio)
        if (deltaRatio < deltaRatioMin) {
            deltaRatioMin = deltaRatio
            retSize = size
        }
    }

    return retSize
}
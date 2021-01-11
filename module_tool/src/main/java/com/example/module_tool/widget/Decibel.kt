package com.example.module_tool.widget

/**
 * Author : Gupingping
 * Date : 2019/3/12
 * QQ : 464955343
 */
object Decibel {
    var dbCount = 40f
        set(value) {
            var result = if (value > lastDbCount) {
                if (value - lastDbCount > min) value - lastDbCount else min
            } else {
                if (value - lastDbCount < -min) value - lastDbCount else -min
            } * 0.2f + lastDbCount//防止声音变化太快
            lastDbCount = result
            if (result < minDB) minDB = dbCount
            if (result > maxDB) maxDB = dbCount
            field = result
        }
    var minDB = 100f
    var maxDB = 0f
    var lastDbCount = dbCount
    private val min = 0.5f  //设置声音最低变化

}

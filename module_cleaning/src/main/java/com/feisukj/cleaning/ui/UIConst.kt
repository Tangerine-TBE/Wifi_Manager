package com.feisukj.cleaning.ui

import java.util.*

object UIConst {
    val todayZeroTime: Date ={
        val cal=Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY,0)
        cal.set(Calendar.MINUTE,0)
        cal.set(Calendar.SECOND,0)
        cal.set(Calendar.MILLISECOND,0)
        cal.time
    }.invoke()
    const val TODAY_TIME_ID=0
    val monthWithinTime={
        val cal= Calendar.getInstance()
        if (cal.get(Calendar.MONTH)==0){
            cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1)
            cal.set(Calendar.MONTH, Calendar.DECEMBER)
        }else{
            cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-1)
        }
        cal.time
    }.invoke()
    const val MONTH_TIME_ID=1
    val threeMonthWithinTime={
        val cal= Calendar.getInstance()
        val cm=cal.get(Calendar.MONTH)
        if (cm<3){
            cal.set(Calendar.MONTH, Calendar.DECEMBER-(3-cm)+1)
            cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1)
        }else{
            cal.set(Calendar.MONTH,cm-3)
        }
        cal.time
    }.invoke()
    const val THREE_MONTH_TIME_ID=2
    val sixMonthWithinTime={
        val cal= Calendar.getInstance()
        val cm=cal.get(Calendar.MONTH)
        if (cm<6){
            cal.set(Calendar.MONTH, Calendar.DECEMBER-(6-cm)+1)
            cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1)
        }else{
            cal.set(Calendar.MONTH,cm-6)
        }
        cal.time
    }.invoke()
    const val SIX_MONTH_TIME_ID=3
    const val SIX_MONTH_AGO=4
    val idToTitle= listOf(
            Pair(TODAY_TIME_ID,"今天"),
            Pair(MONTH_TIME_ID,"一个月内"),
            Pair(THREE_MONTH_TIME_ID,"三个月内"),
            Pair(SIX_MONTH_TIME_ID,"半年内"),
            Pair(SIX_MONTH_AGO,"半年前")
    )
}
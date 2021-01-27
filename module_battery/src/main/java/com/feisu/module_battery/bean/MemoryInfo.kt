package com.feisu.module_battery.bean

import java.io.FileReader

class MemoryInfo {
    companion object{
        private const val memoryInfoPath="/proc/meminfo"
    }
    val totalMem:Long
    val memFree:Long
    val buffers:Long
    val cached:Long
    init {
        val fileReader=FileReader(memoryInfoPath)
        val memoryInfoList=fileReader.readLines()

        totalMem=memoryInfoList.first().replace(" ","").replace("kB","").split(':')[1].toLong()*1024

        memFree=memoryInfoList[1].replace(" ","").replace("kB","").split(':')[1].toLong()*1024

        buffers=memoryInfoList[2].replace(" ","").replace("kB","").split(':')[1].toLong()*1024

        cached=memoryInfoList[3].replace(" ","").replace("kB","").split(':')[1].toLong()*1024

    }
//    执行adb shell命令后，输入cat /proc/meminfo
//    "[MemTotal:        1550640 kB, MemFree:          377032 kB, Buffers:           36232 kB, Cached:           664224 kB, SwapCached:            0 kB, Active:           788224 kB, Inactive:         301120 kB, Active(anon):     395936 kB, Inactive(anon):    20484 kB, Active(file):     392288 kB, Inactive(file):   280636 kB, Unevictable:        4280 kB, Mlocked:             256 kB, HighTotal:        704352 kB, HighFree:          13236 kB, LowTotal:         846288 kB, LowFree:          363796 kB, SwapTotal:             0 kB, SwapFree:              0 kB, Dirty:                64 kB, Writeback:             0 kB, AnonPages:        393136 kB, Mapped:           255124 kB, Shmem:             23508 kB, Slab:              31424 kB, SReclaimable:      14948 kB, SUnreclaim:        16476 kB, KernelStack:        5920 kB, PageTables:        10436 kB, NFS_Unstable:          0 kB, Bounce:                0 kB, WritebackTmp:          0 kB, CommitLimit:      775320 kB, Committed_AS:   31850476 kB, VmallocTotal:     122880 kB, VmallocUsed:       41456 kB, VmallocChunk:      18436 kB, HugePages_Total:       0, HugePages_Free:        0, HugePages_Rsvd:        0, HugePages_Surp:        0, Hugepagesize:       4096 kB, DirectMap4k:       16376 kB, DirectMap4M:      851968 kB]"
}
//val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//val memInfo = ActivityManager.MemoryInfo()
//activityManager.getMemoryInfo(memInfo)
//Log.e("memInfo", "availMem:" + memInfo.availMem / 1024 + " kb")
//Log.e("memInfo", "threshold:" + memInfo.threshold / 1024 + " kb") //low memory threshold
//
//Log.e("memInfo", "totalMem:" + memInfo.totalMem / 1024 + " kb")
//Log.e("memInfo", "lowMemory:" + memInfo.lowMemory) //if current is in low memory
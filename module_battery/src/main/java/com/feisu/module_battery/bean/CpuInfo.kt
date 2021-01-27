package com.feisu.module_battery.bean

import java.io.File
import java.io.FileFilter
import java.io.FileReader
import java.lang.Exception
import java.text.DecimalFormat
import java.util.regex.Pattern


class CpuInfo {
    companion object{
        private const val cpuInfoPath="/proc/cpuinfo"
    }

    val cpuHardware:String
    val cores:Int
    val maxFreq:String
    init {
        val fileReader=FileReader(cpuInfoPath)
        val cpuInfoList=fileReader.readLines()
        cpuHardware=cpuInfoList.find { it.contains("Hardware") }?.split(":")?.get(1)?:"未知"
        cores=File("/sys/devices/system/cpu/").listFiles(FileFilter {
            return@FileFilter Pattern.matches("cpu[0-9]", it.name)
        })?.size?:0
        val df = DecimalFormat("#.00")
        var max=-1
        for (i in 0 until cores){
            val arg2="/sys/devices/system/cpu/cpu${i}/cpufreq/cpuinfo_max_freq"
            val cmd=ProcessBuilder("/system/bin/cat",arg2)
            try {
                val process=cmd.start()
                val inputStream=process.inputStream
                val resultItem= String(inputStream.readBytes())
                inputStream.close()
                val value=resultItem.trim().replace("\n","").toInt()
                if (max<value){
                    max=value
                }
            }catch (e:Exception){

            }
        }
        maxFreq = if (max!=-1){
            df.format(max/1000000f)+"GHz"
        }else{
            "未知"
        }
    }

//    fun a(){
//        var result =""
//        val p = Runtime.getRuntime().exec("top -n 1")
//
//        val br = BufferedReader(InputStreamReader(p.inputStream))
//        while (br.readLine().also { result = it } != null) {
//            if (result.trim { it <= ' ' }.isEmpty()) {
//                continue
//            } else {
//                val tv=StringBuilder()
//                val CPUusr = result.split("%").toTypedArray()
//                tv.append("USER:" + CPUusr[0] + "\n")
//                val CPUusage = CPUusr[0].split("User").toTypedArray()
//                val SYSusage = CPUusr[1].split("System").toTypedArray()
//                tv.append("CPU:" + CPUusage[1].trim { it <= ' ' } + " length:" + CPUusage[1].trim { it <= ' ' }.length + "\n")
//                tv.append("SYS:" + SYSusage[1].trim { it <= ' ' } + " length:" + SYSusage[1].trim { it <= ' ' }.length + "\n")
//                tv.append(result + "\n")
//                break
//            }
//        }
//    }
//    [[s[999C[999B[6n[u[H[J[?25l[H[J[s[999C[999B[6n[uTasks: 2 total,   1 running,   1 sleeping,   0 stopped,   0 zombie,   Mem:      5.4G total,      5.3G used,      176M free,      115M buffers,  Swap:      2.5G total,      1.0G used,      1.4G free,      1.8G cached, 800%cpu   0%user   0%nice   0%sys 800%idle   0%iow   0%irq   0%sirq   0%host, [7m  PID USER         PR  NI VIRT  RES  SHR S[%CPU] %MEM     TIME+ ARGS            [0m,  8235 u0_a1285     10 -10 5.8G 164M 105M S  7.6   2.9   0:11.59 com.feisukj.sho+, [1m 8382 u0_a1285     10 -10  29M 2.8M 2.3M R  3.8   0.0   0:00.04 top -n 1, [m[?25h[0m[1000;1H[K[?25h[?25h[0m[1000;1H[K]

}
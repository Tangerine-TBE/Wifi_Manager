package com.example.module_tool.widget

import android.media.MediaRecorder
import java.io.File

/**
 * Author : Gupingping
 * Date : 2019/3/11
 * QQ : 464955343
 */
class MyRecorder(private val file: File) {
    private var mediaRecorder: MediaRecorder? = null

    private var isRecording = false
    private val recordingFile: String by lazy {
        file.absolutePath
    }

    /**
     * 获取音频最大Amplitude
     */
    fun getMaxAmplitude(): Int {
        try {
            if (mediaRecorder == null) return 5
            return mediaRecorder?.maxAmplitude?:5
        }catch (e:java.lang.Exception){
            return 5
        }
    }

    /**
     * 开始录音
     */
    fun startRecording(): Boolean {
        try {
            mediaRecorder = MediaRecorder()
            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder!!.setOutputFile(recordingFile)
            mediaRecorder!!.prepare()
            mediaRecorder!!.start()
            isRecording = true

            return true
        } catch (e: Exception) {
            mediaRecorder?.reset()
            mediaRecorder?.release()
            isRecording = false
            e.printStackTrace()
        }
        return false
    }

    /**
     * 停止录音
     */
    fun stopRecording() {
        mediaRecorder?.let {
            if (isRecording) {

                it.stop()
                it.release()
            }
            isRecording = false
        }
    }

    fun deleteRecordingFile() {

        stopRecording()
        file.delete()
    }
}
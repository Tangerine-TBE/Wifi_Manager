package com.feisukj.cleaning.file

interface DirNextFileCallback<T> {
    fun onNextFile(item:T)
}
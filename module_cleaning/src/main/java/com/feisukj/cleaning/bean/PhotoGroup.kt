package com.feisukj.cleaning.bean

import kotlin.properties.Delegates

class PhotoGroup {
    var isSelect:Boolean by Delegates.observable(false,{property, oldValue, newValue ->
        if (oldValue!=newValue){
            photos?.forEach {
                it.isCheck=newValue
            }
        }
    })
    var photos: List<ImageBean>?=null
}
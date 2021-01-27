package com.feisukj.cleaning.bean

import com.example.module_base.cleanbase.SectionData


class ApkSectionData<T,V:FileBean>: SectionData<T, V>() {
    var isSorted=false
        set(value) {
            if (value!=field){
                itemData= ArrayList(itemData.asReversed())
                field=value
            }
        }

    override fun addItem(item: V): Boolean {
        if (isSorted){
            val f=itemData.find { it.fileSize>item.fileSize }
            if (f==null){
                itemData.add(item)
            }else{
                itemData.add(itemData.indexOf(f),item)
            }
        }else{
            val f=itemData.find { it.fileSize<item.fileSize }
            if (f==null){
                itemData.add(item)
            }else{
                itemData.add(itemData.indexOf(f),item)
            }
        }
        return true
    }

    override fun addAllItem(c: Collection<V>): Boolean {
        c.forEach {item->
            if (isSorted){
                val f=itemData.find { it.fileSize>item.fileSize }
                if (f==null){
                    itemData.add(item)
                }else{
                    itemData.add(itemData.indexOf(f),item)
                }
            }else{
                val f=itemData.find { it.fileSize<item.fileSize }
                if (f==null){
                    itemData.add(item)
                }else{
                    itemData.add(itemData.indexOf(f),item)
                }
            }
        }
        return true
    }
}
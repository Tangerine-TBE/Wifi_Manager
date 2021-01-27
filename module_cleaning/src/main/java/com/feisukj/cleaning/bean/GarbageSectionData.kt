package com.feisukj.cleaning.bean

import com.example.module_base.cleanbase.SectionData
import kotlin.math.abs

class GarbageSectionData<T>: SectionData<T, GarbageBean>() {
    override fun addItem(item: GarbageBean): Boolean {
        if (itemData.isEmpty()){
            itemData.add(item)
        }else{
            val index=binary(itemData.map { it.fileSize }.toLongArray(),item.fileSize)
            itemData.add(index,item)
        }
        return true
    }

    companion object{
        private fun binary(array: LongArray, value: Long): Int {
            var low = 0
            var high = array.size - 1

            var middle = (low + high) / 2
            while (low <= high) {
                middle = (low + high) / 2
                if (value == array[middle]) {
                    return middle
                }
                if (low+1==high){
                    return if (value>array[low]){
                        low
                    }else{
                        high
                    }
                }
                if (low+2==high){
                    val lowC= abs(value-array[low])
                    val highC= abs(value-array[high])
                    return if (lowC<=highC){
                        if(value>array[low]){
                            low
                        }else{
                            low+1
                        }
                    }else{
                        if(value>array[high]){
                            high
                        }else{
                            high+1
                        }
                    }
                }

                if (value > array[middle]) {
                    high = middle - 1

                }
                if (value < array[middle]) {
                    low = middle + 1
                }
            }

            return if (value>array[middle]){
                middle
            }else{
                middle+1
            }
        }
    }
}

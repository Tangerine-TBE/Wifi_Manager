package com.example.module_base.cleanbase

open class SectionData<T,V> {
    @Deprecated("")
    var isFold=true
    var groupData:T?=null
    @Deprecated("")
    var groupPosition:Int=0
    protected var itemData=ArrayList<V>()
    var id=-1

    fun getItemData():List<V>{
        return itemData.toList()
    }

    open fun addItem(item:V):Boolean{
        return itemData.add(item)
    }

    open fun addAllItem(c: Collection<V>):Boolean{
        return itemData.addAll(c)
    }

    open fun removeItem(item:V):Boolean{
        return itemData.remove(item)
    }

    open fun removeAllItem(c:Collection<V>):Boolean{
        return itemData.removeAll(c)
    }

    open fun clearItem(){
        itemData.clear()
    }
}
package com.feisukj.cleaning.bean

import java.lang.StringBuilder

/**
 * 用于存放目录
 */
class CatalogTree(val root: String, val separator:String="/") {
    val listChilds=HashSet<CatalogTree>()
    val pathList=ArrayList<String>()
        get() {
            pathList.clear()
            preOrder(this)
            return field
        }
    private fun preOrder(tree:CatalogTree):String{
        val stringBuilder=StringBuilder()
        stringBuilder.append(tree.root)
        tree.listChilds.forEach {
            stringBuilder.append(separator)
            stringBuilder.append(preOrder(it))
            pathList.add(stringBuilder.toString())
        }
        return stringBuilder.toString()
    }
}
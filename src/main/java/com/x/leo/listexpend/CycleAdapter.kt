package com.x.leo.listexpend

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * @作者:XLEO
 * @创建日期: 2017/8/30 14:04
 * @描述:${TODO}
 *
 * @更新者:${Author}$
 * @更新时间:${Date}$
 * @更新描述:${TODO}
 * @下一步：
 */
abstract class  CycleAdapter():BaseAdapter() {
    private var realSize = 0
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       return getRealView(if(realSize == 0) 0 else position % realSize,convertView,parent)
    }

    abstract fun getRealView(i: Int, convertView: View?, parent: ViewGroup?): View

    override fun getItem(position: Int): Any {

        return getRealItem(if(realSize == 0) 0 else position % realSize)
    }

    abstract fun getRealItem(position: Int):Any

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        realSize = getRealCount()
        return if(realSize == 0) 0 else Int.MAX_VALUE
    }

    abstract fun getRealCount(): Int

    override fun getItemViewType(position: Int): Int {
        return getRealItemViewType(if(realSize == 0) 0 else position % realSize)
    }

    fun getRealItemViewType(i: Int): Int{
        return 0
    }

}
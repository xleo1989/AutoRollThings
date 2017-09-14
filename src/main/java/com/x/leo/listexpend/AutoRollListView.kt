package com.x.leo.listexpend

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ListAdapter
import android.widget.ListView

/**
 * @作者:XLEO
 * @创建日期: 2017/8/30 14:00
 * @描述:${TODO}
 *
 * @更新者:${Author}$
 * @更新时间:${Date}$
 * @更新描述:${TODO}
 * @下一步：
 */
class AutoRollListView(ctx:Context,attributeSet: AttributeSet?): ListView(ctx,attributeSet){
    init {
        if (attributeSet != null) {
        }
        isVerticalScrollBarEnabled = false
        isVerticalFadingEdgeEnabled = false
    }
    val doLog = false
    fun logd(s:String){
        if (doLog) {
            Log.d("autorolllistview",s)
        }
    }
    private val animator:IncreaseAnimator by lazy{
        IncreaseAnimator(1000,object : IncreaseAnimatorListener{
            override fun onCurrentDiff(diff: Float) {
            }

            override fun onSumDiff(diff: Float) {
            }

            override fun onNumIncrease() {
                smoothScrollBy(itemHeight,1000)
            }

            override fun onNumReset() {
            }

        })
    }

    override fun computeScroll() {
        super.computeScroll()
    }
    override fun setAdapter(adapter: ListAdapter?) {
        stopRoll()
        if (adapter is CycleAdapter) {
            super.setAdapter(adapter)
            if (adapter != null) {
                startRoll()
            }
        }else{
            throw IllegalArgumentException("adapter must be out of CycleAdapter")
        }
    }

    private fun stopRoll() {
        animator.stopRoll()
    }

    private var itemHeight = 0
    private fun startRoll() {
        if((adapter as CycleAdapter).getRealCount() > 0){
            val childAt = adapter.getView(0,null,this)
            childAt.measure(0,0)
            val measuredHeight = childAt.measuredHeight
            itemHeight = dividerHeight + measuredHeight
            animator.startRoll()
        }
    }
}
package com.x.leo.listexpend

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView

/**
 * @作者:XLEO
 * @创建日期: 2017/8/29 14:31
 * @描述:${TODO}
 *
 * @更新者:${Author}$
 * @更新时间:${Date}$
 * @更新描述:${TODO}
 * @下一步：
 */
class AutoRollText(ctx: Context, attributes: AttributeSet?) : TextView(ctx, attributes) {
    private var rollTime: Int = 0
    private var showLines: Int = 0
    private var strings: Array<String> = emptyArray()
    private var currentStartIndex = 0
    private var currentDiff = 0.0f
    private val animator:IncreaseAnimator by lazy {
        IncreaseAnimator(rollTime.toLong(),object : IncreaseAnimatorListener{
            override fun onCurrentDiff(diff: Float) {
            }

            override fun onSumDiff(diff: Float) {
                currentDiff = diff
                invalidate()
            }

            override fun onNumIncrease() {
                currentStartIndex ++
            }

            override fun onNumReset() {
                currentStartIndex = 0
                invalidate()
            }

        })
    }
    private val doLog = false
    fun logd(s: String) {
        if (doLog) {
            Log.e("AutoRollText", s)
        }
    }

    init {
        if (attributes != null) {
            val attrs = ctx.obtainStyledAttributes(attributes, R.styleable.AutoRollText)
            rollTime = attrs.getInt(R.styleable.AutoRollText_rollTime, 100)
            showLines = attrs.getInt(R.styleable.AutoRollText_showLines, 1)
        }
    }

    fun setStrings(sts: Array<String>) {
        animator.stopRoll()
        strings = sts
        animator.startRoll()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (strings.size > showLines && showLines > 0) {
            animator.startRoll()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.stopRoll()
    }



    fun setStrings(sts: List<String>) {
        animator.stopRoll()
        strings = Array<String>(sts.size, { i -> sts[i] })
        animator.startRoll()
    }

    private var localmeasuredHeight = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode == MeasureSpec.EXACTLY) {
            localmeasuredHeight = heightSize
        } else {
            localmeasuredHeight = (lineHeight * showLines).toInt()
        }
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(localmeasuredHeight, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas?) {
        background?.draw(canvas)
        paint.color = textColors.getColorForState(drawableState, textColors.defaultColor)
        setClipRect(canvas)
        if (strings.size > showLines && showLines > 0) {
            for (i in 0..(showLines + 1)) {
                logd("i:" + i + ";currentIndex:" + currentStartIndex + ";currentDiff:" + currentDiff)
                canvas?.drawText(strings[(i + currentStartIndex) % strings.size],
                        0f,
                        i * lineHeight - currentDiff * lineHeight,
                        paint)
            }
        } else if (showLines <= 0) {
            return
        } else {
            for (i in 0..strings.size - 1) {
                canvas?.drawText(strings[i], 0f, i * lineHeight.toFloat(), paint)
            }
        }
    }

    private var clipRect: RectF? = null
    private fun setClipRect(canvas: Canvas?) {
        if (clipRect == null) {
            var clipLeft = (compoundPaddingLeft + scrollX).toFloat()
            var clipTop = (extendedPaddingTop + scrollY).toFloat()
            var clipRight = (right - left - compoundPaddingRight + scrollX).toFloat()
            var clipBottom = (bottom - top + scrollY).toFloat()

            if (shadowRadius != 0f) {
                clipLeft += Math.min(0f, shadowDx - shadowRadius)
                clipRight += Math.max(0f, shadowDx + shadowRadius)

                clipTop += Math.min(0f, shadowDy - shadowRadius)
                clipBottom += Math.max(0f, shadowDy + shadowRadius)
            }
            clipRect = RectF(clipLeft, clipTop, clipRight, clipBottom)
            logd("clipLeft:" + clipLeft + ";clipTop:" + clipTop + ";clipRight:" + clipRight + ";clipBottom:" + clipBottom)
        }

        canvas?.clipRect(clipRect)
    }
}
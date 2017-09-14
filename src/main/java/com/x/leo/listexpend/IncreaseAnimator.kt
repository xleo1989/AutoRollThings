package com.x.leo.listexpend

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.SystemClock
import android.view.animation.LinearInterpolator

/**
 * @作者:XLEO
 * @创建日期: 2017/8/30 14:27
 * @描述:${TODO}
 *
 * @更新者:${Author}$
 * @更新时间:${Date}$
 * @更新描述:${TODO}
 * @下一步：
 */
class IncreaseAnimator(var duration:Long,var listener: IncreaseAnimatorListener) {
    private var animator: ValueAnimator? = null
    private var  currentDiff: Float = 0f
    private val updateListener by lazy {
        object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                val i = animation!!.animatedValue as Int
                val temp = currentDiff
                currentDiff = i / 100f
                if (temp != currentDiff) {
                    listener?.onSumDiff(currentDiff)
                    listener?.onCurrentDiff(currentDiff - temp)
                }
            }
        }
    }
    private var repeatTime = 0L
    private val animatorListener: Animator.AnimatorListener by lazy {
        object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                val currentThreadTimeMillis = SystemClock.elapsedRealtime()
                if ((currentThreadTimeMillis - repeatTime) > duration * 2 / 3) {
                    listener?.onNumIncrease()
                    repeatTime = currentThreadTimeMillis
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                listener?.onNumReset()
            }

        }
    }

    fun startRoll() {
        if (animator == null) {
            animator = ValueAnimator.ofInt(0, 100)
            animator!!.setInterpolator(LinearInterpolator())
            animator!!.repeatMode = ValueAnimator.RESTART
            animator!!.repeatCount = ValueAnimator.INFINITE
            animator!!.addUpdateListener(updateListener)
            animator!!.addListener(animatorListener)
            animator!!.duration = duration
        } else {
            animator!!.addUpdateListener(updateListener)
            animator!!.addListener(animatorListener)
        }
        animator!!.start()

    }

    fun stopRoll() {
        if (animator != null) {
            animator!!.removeAllUpdateListeners()
            animator!!.removeAllListeners()
            animator!!.cancel()
        }
    }
}

interface IncreaseAnimatorListener {
    fun onNumIncrease()
    fun onNumReset()
    fun onCurrentDiff(diff:Float)
    fun onSumDiff(diff:Float)
}
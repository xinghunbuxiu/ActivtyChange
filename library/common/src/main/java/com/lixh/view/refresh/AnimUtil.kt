package com.lixh.view.refresh

import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

import com.nineoldandroids.animation.ObjectAnimator
import com.nineoldandroids.view.ViewHelper

/**
 * Created by Ybao on 2015/11/7 0007.
 */
object AnimUtil {
    fun startRotation(view: View, toRotation: Float) {
        ObjectAnimator.ofFloat(view, "rotation", ViewHelper.getRotation(view), toRotation).start()
    }

    fun startRotation(view: View, toRotation: Float, duration: Long, startDelay: Long) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "rotation", ViewHelper.getRotation(view), toRotation).setDuration(duration)
        objectAnimator.startDelay = startDelay
        objectAnimator.start()
    }

    fun startRotation(view: View, toRotation: Float, duration: Long, startDelay: Long, times: Int) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "rotation", ViewHelper.getRotation(view), toRotation).setDuration(duration)
        objectAnimator.startDelay = startDelay
        objectAnimator.repeatCount = times
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.start()
    }

    fun startShow(view: View, fromAlpha: Float, duration: Long, startDelay: Long) {
        ViewHelper.setAlpha(view, fromAlpha)
        view.visibility = View.VISIBLE
        val objectAnimator = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, 1f).setDuration(duration)
        objectAnimator.startDelay = startDelay
        objectAnimator.start()
    }

    fun startHide(view: View, duration: Long, startDelay: Long) {
        view.visibility = View.VISIBLE
        val objectAnimator = ObjectAnimator.ofFloat(view, "alpha", ViewHelper.getAlpha(view), 0f).setDuration(duration)
        objectAnimator.startDelay = startDelay
        objectAnimator.start()

    }

    fun startShow(view: View, fromAlpha: Float) {
        ViewHelper.setAlpha(view, fromAlpha)
        view.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(view, "alpha", fromAlpha, 1f).start()
    }

    fun startHide(view: View) {
        ObjectAnimator.ofFloat(view, "alpha", ViewHelper.getAlpha(view), 0f).start()
        //        view.setVisibility(View.INVISIBLE);
    }


    fun startScale(view: View, toScale: Float) {
        ObjectAnimator.ofFloat(view, "scaleX", ViewHelper.getScaleX(view), toScale).start()
        ObjectAnimator.ofFloat(view, "scaleY", ViewHelper.getScaleY(view), toScale).start()
    }

    fun startScale(view: View, toScale: Float, duration: Long, startDelay: Long, setInterpolator: Interpolator) {
        var objectAnimator = ObjectAnimator.ofFloat(view, "scaleX", ViewHelper.getScaleX(view), toScale).setDuration(duration)
        objectAnimator.startDelay = startDelay
        objectAnimator.interpolator = setInterpolator
        objectAnimator.start()
        objectAnimator = ObjectAnimator.ofFloat(view, "scaleY", ViewHelper.getScaleY(view), toScale).setDuration(duration)
        objectAnimator.startDelay = startDelay
        objectAnimator.interpolator = setInterpolator
        objectAnimator.start()
    }

    fun startScale(view: View, fromScale: Float, toScale: Float) {
        ObjectAnimator.ofFloat(view, "scaleX", fromScale, toScale).start()
        ObjectAnimator.ofFloat(view, "scaleY", fromScale, toScale).start()
    }


    fun startScale(view: View, fromScale: Float, toScale: Float, duration: Long, startDelay: Long, setInterpolator: Interpolator) {
        var objectAnimator = ObjectAnimator.ofFloat(view, "scaleX", fromScale, toScale).setDuration(duration)
        objectAnimator.startDelay = startDelay
        objectAnimator.interpolator = setInterpolator
        objectAnimator.start()
        objectAnimator = ObjectAnimator.ofFloat(view, "scaleY", fromScale, toScale).setDuration(duration)
        objectAnimator.startDelay = startDelay
        objectAnimator.interpolator = setInterpolator
        objectAnimator.start()
    }

    fun startFromY(view: View, fromY: Float) {
        ObjectAnimator.ofFloat(view, "translationY", fromY, 0f).start()
    }

    fun startToY(view: View, toY: Float) {
        ObjectAnimator.ofFloat(view, "translationY", 0f, toY).start()
    }
}

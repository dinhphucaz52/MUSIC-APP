package com.example.mymusicapp.util

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.widget.TextView

object AnimatorFactory {
    @SuppressLint("Recycle")
    fun runningAnimation(tvSongName: TextView): Animator {
        val animator = ValueAnimator.ofFloat(0f, -tvSongName.width.toFloat())
        animator.apply {
            duration = 5000
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                tvSongName.translationX = animatedValue
            }
        }
        return animator
    }

}
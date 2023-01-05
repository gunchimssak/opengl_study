package com.example.openglstudy.common

import android.view.MotionEvent
import android.view.View

class TouchListener(private val scene: Scene) : View.OnTouchListener {
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return true
    }
}
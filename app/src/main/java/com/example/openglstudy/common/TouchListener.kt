package com.example.openglstudy.common

import android.util.Log
import android.view.MotionEvent
import android.view.View

class TouchListener(private val scene: Scene) : View.OnTouchListener {
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                event.x.let { scene.camera.touch(it) }
            }
            MotionEvent.ACTION_MOVE -> {
                event.x.let { scene.camera.touch(it) }
            }
        }
        return true
    }
}
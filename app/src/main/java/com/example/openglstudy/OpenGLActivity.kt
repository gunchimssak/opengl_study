package com.example.openglstudy

import android.app.Activity
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.example.openglstudy.common.TouchListener
import com.example.openglstudy.common.deviceSize

class OpenGLActivity : Activity() {
    private lateinit var gLView: GLSurfaceView

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val bounds = windowManager.currentWindowMetrics.bounds
        deviceSize = Pair(bounds.width(), bounds.height())
        gLView = GLSurfaceView(this).apply {
            setEGLContextClientVersion(3)
            setOnTouchListener(TouchListener(AssetScene.create(this@OpenGLActivity)))
            setRenderer(Renderer(AssetScene.create(this@OpenGLActivity)))
        }
        setContentView(gLView)
    }
}
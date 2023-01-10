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
import com.example.openglstudy.databinding.ActivityMainBinding

class OpenGLActivity : Activity() {
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val bounds = windowManager.currentWindowMetrics.bounds
        deviceSize = Pair(bounds.width(), bounds.height())
        binding = ActivityMainBinding.inflate(layoutInflater)
        val scene = AssetScene.create(this@OpenGLActivity)
        setContentView(binding.root)
        binding.glview.apply {
            setEGLContextClientVersion(3)
            setOnTouchListener(TouchListener(scene))
            setRenderer(Renderer(scene))
        }

        binding.rotateX.setOnClickListener {
            scene.rotateX()
        }
        binding.rotateY.setOnClickListener {
            scene.rotateY()
        }
        binding.rotateZ.setOnClickListener {
            scene.rotateZ()
        }
        binding.scaleUp.setOnClickListener {
            scene.scaleUp()
        }
        binding.scaleDown.setOnClickListener {
            scene.scaleDown()
        }
        binding.zoomIn.setOnClickListener {
            scene.cameraZoomIn()
        }
        binding.zoomOut.setOnClickListener {
            scene.cameraZoomOut()
        }
    }
}
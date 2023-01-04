package com.example.openglstudy

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle

class OpenGLActivity : Activity() {
    private lateinit var gLView: GLSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gLView = GLSurfaceView(this).apply {
            setEGLContextClientVersion(3)
            setRenderer(Renderer(CubeScene.create(this@OpenGLActivity)))
        }
        setContentView(gLView)
    }
}
package com.example.openglstudy

import android.opengl.GLSurfaceView
import com.example.openglstudy.common.Scene
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Renderer(private val scene: Scene) : GLSurfaceView.Renderer {
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        scene.init(width,height)
    }

    override fun onDrawFrame(gl: GL10?) {
        gl
        scene.draw()
    }

}
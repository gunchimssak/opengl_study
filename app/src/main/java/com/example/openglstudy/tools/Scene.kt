package com.example.openglstudy.tools

import com.example.openglstudy.common.Camera
import com.example.openglstudy.common.Timer

abstract class Scene {
    protected val timer = Timer()
    val camera = Camera()
    abstract fun init(width: Int, height: Int)
    abstract fun draw()
}
package com.example.openglstudy.common

import com.example.openglstudy.common.Timer

abstract class Scene {
    protected val timer = Timer()
    val camera = Camera()
    abstract fun init(width: Int, height: Int)
    abstract fun draw()
}
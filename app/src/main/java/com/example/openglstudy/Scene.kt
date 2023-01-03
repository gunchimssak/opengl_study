package com.example.openglstudy

abstract class Scene {
    protected val timer = Timer()
    abstract fun init(width: Int, height: Int)
    abstract fun draw()
}
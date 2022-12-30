package com.example.openglstudy

abstract class Scene {
    abstract fun init(width: Int, height: Int)
    abstract fun draw()
}
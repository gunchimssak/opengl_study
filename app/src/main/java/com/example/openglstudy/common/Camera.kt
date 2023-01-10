package com.example.openglstudy.common

import android.util.Log
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3

class Camera(
    private val eye: Vec3 = Vec3(0, 0, -10)
) {
    var currentMat = glm.translate(Mat4(), eye)
    fun touch(x: Float) {
        if (x < deviceSize.first / 2) {
            angle += 0.02f
        } else {
            angle -= 0.02f
        }
    }

    fun getView(): Mat4 = glm.rotate(currentMat, -angle, Vec3(0, 1, 0))

}
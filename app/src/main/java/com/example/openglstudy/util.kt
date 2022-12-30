package com.example.openglstudy

import android.content.res.Resources
import android.opengl.GLES20.*
import androidx.annotation.RawRes
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

fun compileShader(type: Int, code: String) = glCreateShader(type).also { shader ->
    glShaderSource(shader, code)
    glCompileShader(shader)
}

fun FloatArray.toFloatBuffer(): FloatBuffer = ByteBuffer
    .allocateDirect(this.size * Float.SIZE_BYTES)
    .order(ByteOrder.nativeOrder())
    .asFloatBuffer().also {
        it.put(this).position(0)
    }

fun Resources.readRawTextFile(@RawRes id: Int) =
    openRawResource(id).bufferedReader().use { it.readText() }
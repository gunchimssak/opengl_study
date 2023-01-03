package com.example.openglstudy

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.util.Log
import androidx.annotation.RawRes
import glm_.mat4x4.Mat4
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

fun compileShader(type: Int, code: String) = glCreateShader(type).also { shader ->
    glShaderSource(shader, code)
    glCompileShader(shader)
    val result = intArrayOf(-99999)
    glGetShaderiv(shader, GL_COMPILE_STATUS, result, 0)
    GL_INVALID_VALUE
    GL_INVALID_OPERATION
    result[0]
    Log.e("123","${result[0]}")
}
fun toFloatArray(mat4: Mat4) : FloatArray{
    var a: FloatArray = floatArrayOf()
    with(mat4) {
        a = floatArrayOf(
            a0, a1, a2, a3,
            b0, b1, b2, b3,
            c0, c1, c2, c3,
            d0, d1, d2, d3
        )
    }
    return a
}
fun FloatArray.toFloatBuffer(): FloatBuffer = ByteBuffer
    .allocateDirect(this.size * Float.SIZE_BYTES)
    .order(ByteOrder.nativeOrder())
    .asFloatBuffer().also {
        it.put(this).position(0)
    }

fun IntArray.toIntBuffer(): IntBuffer = ByteBuffer
    .allocateDirect(this.size * Int.SIZE_BYTES)
    .order(ByteOrder.nativeOrder())
    .asIntBuffer().also {
        it.put(this).position(0)
    }

internal fun loadBitmap(context: Context, @RawRes textureId: Int) = BitmapFactory.decodeResource(
    context.resources,
    textureId,
    BitmapFactory.Options().apply { inScaled = false })

fun Resources.readRawTextFile(@RawRes id: Int) =
    openRawResource(id).bufferedReader().use { it.readText() }
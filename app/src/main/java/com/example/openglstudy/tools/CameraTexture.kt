package com.example.openglstudy.tools

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLUtils
import java.nio.IntBuffer

class CameraTexture {
    private var id: Int? = null

    fun getId() = id ?: throw IllegalStateException("Call load() before using texture")

    fun load() {
        if (id != null) {
            return
        }

        val id = IntBuffer.allocate(1)
        GLES30.glGenTextures(1, id)

        id[0].also {
            this.id = it
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, it)
        }

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, getId())
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_NEAREST
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_NEAREST
        )

    }
}
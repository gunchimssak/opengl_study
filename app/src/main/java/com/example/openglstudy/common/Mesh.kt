package com.example.openglstudy.common

import android.opengl.GLES20
import android.opengl.GLES30.*
import android.util.Log
import glm_.mat4x4.Mat4
import java.nio.*

data class Mesh(
    val vertices: FloatBuffer,
    val normals: FloatBuffer,
    val texCoords: FloatBuffer,
    val indices: IntBuffer
) {
    private val data: VertexData

    init {
        val capacity = vertices.capacity() + normals.capacity() + texCoords.capacity()
        val buffer = ByteBuffer.allocateDirect(capacity * Float.SIZE_BYTES)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        var a = 0
        while (vertices.hasRemaining()) {
            buffer.put(vertices.get())
            buffer.put(vertices.get())
            buffer.put(vertices.get())
            buffer.put(normals.get())
            buffer.put(normals.get())
            buffer.put(normals.get())
            buffer.put(texCoords.get())
            buffer.put(texCoords.get())
            //Log.e("mesh", "${buffer[a]} ${buffer[a + 1]} ${buffer[a + 2]} ${buffer[a  + 3]} ${buffer[a + 4]} ${buffer[a + 5]} ${buffer[a + 6]} ${buffer[a + 7]}")
            a += 1
        }
        buffer.position(0)

        data = VertexData(buffer, indices, 8)
    }

    fun bind(program: Program) {
        data.addAttribute(program.getAttributeLocation("aPos"), 3, 0)
        data.addAttribute(program.getAttributeLocation("aNormals"), 3, 3)
        data.addAttribute(program.getAttributeLocation("aTexCoords"), 2, 6)
        data.bind()
        program.use()
    }

    fun draw() {
        glBindVertexArray(data.getVaoId())
        GLES20.glDrawElements(GL_TRIANGLES, indices.capacity(), GL_UNSIGNED_INT, 0)
    }
}
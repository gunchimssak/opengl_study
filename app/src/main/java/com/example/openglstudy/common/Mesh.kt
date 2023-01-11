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
    val indices: IntBuffer,
) {
    private val data: VertexData

    init {
        val capacity = vertices.capacity() + normals.capacity() + texCoords.capacity()
        val buffer = createFloatBuffer(capacity)

        while (vertices.hasRemaining()) {
            buffer.put(vertices.get())
            buffer.put(vertices.get())
            buffer.put(vertices.get())
            buffer.put(normals.get())
            buffer.put(normals.get())
            buffer.put(normals.get())
            buffer.put(texCoords.get())
            buffer.put(texCoords.get())
        }

        buffer.position(0)
        data = VertexData(buffer, indices, 8)
    }

    //현재 vertex 데이터 한가지만 사용 normal 과 texCoord 사용하여 빛처리와 텍스쳐 입히기!!!
    fun bind(program: Program) {
        data.addAttribute(program.getAttributeLocation("aPos"), 3, 0)
        data.addAttribute(program.getAttributeLocation("aNormals"), 3, 3)
        data.addAttribute(program.getAttributeLocation("aTexCoord"), 2, 6)
        data.bind()
        program.use()
    }

    fun draw() {
        glBindVertexArray(data.getVaoId())
        GLES20.glDrawElements(GL_TRIANGLES, indices.capacity(), GL_UNSIGNED_INT, 0)
    }
}
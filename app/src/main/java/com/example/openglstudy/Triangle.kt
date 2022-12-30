package com.example.openglstudy

import android.content.Context
import android.content.res.Resources
import android.opengl.GLES20
import android.opengl.GLES30.*
import java.nio.IntBuffer

class Triangle(
    private val vertexShaderCode: String,
    private val fragmentShaderCode: String
) : Scene() {
    private var vaoID = -1
    private lateinit var program: Program
    private val coordinate = floatArrayOf(
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.0f, 0.5f, 0.0f
    )

    override fun init(width: Int, height: Int) {
        glViewport(0, 0, width, height)
        program = Program.create(
            vertexShaderCode = vertexShaderCode,
            fragmentShaderCode = fragmentShaderCode
        )
        val vbo = IntBuffer.allocate(1)
        glGenBuffers(1, vbo)
        val vao = IntBuffer.allocate(1)
        glGenVertexArrays(1, vao)
        vaoID = vao[0]
        val vertexBuffer = coordinate.toFloatBuffer()

        glBindVertexArray(vaoID)
        glBindBuffer(GL_ARRAY_BUFFER, vbo[0])
        glBufferData(
            GL_ARRAY_BUFFER,
            Float.SIZE_BYTES * vertexBuffer.capacity(),
            vertexBuffer,
            GL_STATIC_DRAW
        )
        val aPosition = program.getAttributeLocation("aPos")
        GLES20.glVertexAttribPointer(aPosition, 3, GL_FLOAT, false, 3 * Float.SIZE_BYTES, 0)
        glEnableVertexAttribArray(aPosition)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
        program.use()
    }

    override fun draw() {
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT)
        glBindVertexArray(vaoID)
        glDrawArrays(GL_TRIANGLES, 0, 3)
    }

    companion object {
        fun create(context: Context): Scene {
            var resources = context.resources
            val vertexShaderCode = resources.readRawTextFile(R.raw.triangle_vertex_shader)
            val fragmentShaderCode = resources.readRawTextFile(R.raw.triangle_fragment_shader)
            return Triangle(vertexShaderCode, fragmentShaderCode)
        }
    }
}
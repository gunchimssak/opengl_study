package com.example.openglstudy

import android.content.Context
import android.content.res.Resources
import android.opengl.GLES20
import android.opengl.GLES30.*
import android.util.Log
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.quat.Quat
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import java.nio.IntBuffer

class Triangle(
    private val vertexShaderCode: String,
    private val fragmentShaderCode: String,
    private val texture1: Texture
) : Scene() {
    private lateinit var program: Program
    private val coordinate = floatArrayOf(
        0.5f * 1.8f, 0.5f * 0.51f * 1.8f, 0.0f, 1.0f, 1.0f,
        0.5f * 1.8f, -0.5f * 0.51f * 1.8f, 0.0f, 1.0f, 0.0f,
        -0.5f * 1.8f, -0.5f * 0.51f * 1.8f, 0.0f, 0.0f, 0.0f,
        -0.5f * 1.8f, 0.5f * 0.51f * 1.8f, 0.0f, 0.0f, 1.0f
    )

    private val indices = intArrayOf(
        0, 1, 3,
        1, 2, 3
    )
    private lateinit var vertexData: VertexData
    override fun init(width: Int, height: Int) {
        glViewport(0, 0, width, height)
        texture1.load()
        program = Program.create(
            vertexShaderCode = vertexShaderCode,
            fragmentShaderCode = fragmentShaderCode
        )
        vertexData = VertexData(coordinate, indices, 5)
        vertexData.addAttribute(program.getAttributeLocation("aPos"), 3, 0)
        vertexData.addAttribute(program.getAttributeLocation("aTexCoord"), 2, 3)
        vertexData.bind()
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texture1.getId())
        program.use()

        glUniform1i(program.getUniformLocation("texture1"), 0)
    }

    override fun draw() {
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT)
        var transform = Mat4()
        transform = glm.rotate(transform, timer.sinceStartSecs() * 5.0f, Vec3(0.0f, 0.0f, 1.0f))
        var fa = toFloatArray(transform)
        glUniformMatrix4fv(
            program.getUniformLocation("transform"),
            1,
            true,
            fa,
            0
        )
        glBindVertexArray(vertexData.getVaoId())
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)
    }

    companion object {
        fun create(context: Context): Scene {
            var resources = context.resources
            val vertexShaderCode = resources.readRawTextFile(R.raw.triangle_vertex_shader)
            val fragmentShaderCode = resources.readRawTextFile(R.raw.triangle_fragment_shader)
            return Triangle(
                vertexShaderCode,
                fragmentShaderCode,
                Texture(loadBitmap(context, R.raw.bonobono))
            )
        }
    }
}
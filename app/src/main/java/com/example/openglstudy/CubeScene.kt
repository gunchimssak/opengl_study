package com.example.openglstudy

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30.*
import android.opengl.Matrix
import android.util.Log
import com.example.openglstudy.common.*
import com.example.openglstudy.common.loadBitmap
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.toLong
import glm_.vec2.Vec2
import glm_.vec3.Vec3

class CubeScene(
    private val vertexShaderCode: String,
    private val fragmentShaderCode: String,
    private val texture1: Texture
) : Scene() {

    private lateinit var program: Program
    private lateinit var vertexData: VertexData

    private val vertices = floatArrayOf(
        -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
        0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
        -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

        -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

        0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
        0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
    )

    override fun init(width: Int, height: Int) {
        glEnable(GL_DEPTH_TEST)
        glViewport(0, 0, width, height)
        texture1.load()
        program = Program.create(
            vertexShaderCode = vertexShaderCode,
            fragmentShaderCode = fragmentShaderCode
        )
        vertexData = VertexData(vertices, null, 5)
        vertexData.addAttribute(program.getAttributeLocation("aPos"), 3, 0)
        vertexData.addAttribute(program.getAttributeLocation("aTexCoord"), 2, 3)
        vertexData.bind()
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texture1.getId())
        program.use()
        val viewF = FloatArray(16) { 0f }
        Matrix.setIdentityM(viewF, 0)
        Matrix.translateM(viewF, 0, 0f, 0f, -5f)
        Log.e("view", "${viewF.toList()}")
        GLES20.glUniformMatrix4fv(
            program.getUniformLocation("view"),
            1,
            false,
            viewF,
            0
        )
        val proj = FloatArray(16) { 0f }
        Matrix.setIdentityM(proj, 0)
        Matrix.perspectiveM(proj, 0, 45f, width.toFloat() / height, .1f, 10000f)
        GLES20.glUniformMatrix4fv(
            program.getUniformLocation("projection"),
            1,
            false,
            proj,
            0
        )
    }

    override fun draw() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glBindVertexArray(vertexData.getVaoId())
        val model = FloatArray(16) { 0f }
       // Matrix.setRotateM(model,0,3f,1f,2f,0f)
        var trans = Mat4()
        trans = glm.rotate(trans, timer.sinceStartSecs(), Vec3(3, 1, 2))
        GLES20.glUniformMatrix4fv(
            program.getUniformLocation("model"),
            1,
            true,
            toFloatArray(trans),
            0
        )
        glDrawArrays(GL_TRIANGLES, 0, 36)
    }

    companion object {
        fun create(context: Context) = CubeScene(
            context.resources.readRawTextFile(R.raw.cube_vertex_shader),
            context.resources.readRawTextFile(R.raw.cube_fragment_shader),
            Texture(loadBitmap(context, R.raw.bonobono))
        )
    }
}

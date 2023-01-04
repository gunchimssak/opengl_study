package com.example.openglstudy

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30.*
import com.example.openglstudy.common.*
import com.example.openglstudy.common.loadBitmap
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3

class CubeScene(
    private val vertexShaderCode: String,
    private val fragmentShaderCode: String,
    private val texture1: Texture
) : Scene() {

    private lateinit var program: Program
    private lateinit var vertexData: VertexData
    private var width: Int = 0
    private var height: Int = 0
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
    val cubePositions = listOf(
        Vec3(0.0f, 0.0f, 0.0f),
        Vec3(2.0f, 5.0f, -15.0f),
        Vec3(-1.5f, -2.2f, -2.5f),
        Vec3(-3.8f, -2.0f, -2.3f),
        Vec3(2.4f, -0.4f, -3.5f),
        Vec3(-1.7f, 3.0f, -7.5f),
        Vec3(1.3f, -2.0f, -2.5f),
        Vec3(1.5f, 2.0f, -2.5f),
        Vec3(1.5f, 0.2f, -1.5f),
        Vec3(-1.3f, 1.0f, -1.5f)
    )

    override fun init(width: Int, height: Int) {
        glEnable(GL_DEPTH_TEST)
        glViewport(0, 0, width, height)
        this.width = width
        this.height = height
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
    }

    override fun draw() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glBindVertexArray(vertexData.getVaoId())
        // Matrix.setRotateM(model,0,3f,1f,2f,0f)
        val view = glm.translate(Mat4(), Vec3(0f, 0f, -5f))
        val proj = glm.perspective(Mat4(), glm.PIf * 0.45f, width.toFloat() / height, 0.1f, 10000f)
        cubePositions.forEachIndexed { index, vec3 ->
            var model = glm.translate(Mat4(), vec3) * glm.rotate(Mat4(), timer.sinceStartSecs(), Vec3(index, 1, 2))
            GLES20.glUniformMatrix4fv(
                program.getUniformLocation("mvp"),
                1,
                false,
                toFloatArray(proj * view * model),
                0
            )
            glDrawArrays(GL_TRIANGLES, 0, 36)
        }
    }

    companion object {
        fun create(context: Context) = CubeScene(
            context.resources.readRawTextFile(R.raw.cube_vertex_shader),
            context.resources.readRawTextFile(R.raw.cube_fragment_shader),
            Texture(loadBitmap(context, R.raw.bonobono))
        )
    }
}

package com.example.openglstudy

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30.*
import com.example.openglstudy.common.*
import com.example.openglstudy.common.loadBitmap
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.toFloat
import glm_.vec3.Vec3

class CubeScene(
    private val vertexShaderCode: String,
    private val fragmentShaderCode: String,
    private val texture1: Texture,
    private val texture2: Texture
) : Scene() {

    private lateinit var program: Program
    private lateinit var cube: VertexData
    private var width: Int = 0
    private var height: Int = 0

    val cubePositions = listOf(
        Vec3(0.0f, 0.0f, 0.0f),
    )

    override fun init(width: Int, height: Int) {
        glEnable(GL_DEPTH_TEST)
        glViewport(0, 0, width, height)
        this.width = width
        this.height = height
        texture1.load()
        texture2.load()
        program = Program.create(
            vertexShaderCode = vertexShaderCode,
            fragmentShaderCode = fragmentShaderCode
        )
        cube = VertexData(cubeVertices, null, 5)
        cube.addAttribute(program.getAttributeLocation("aPos"), 3, 0)
        cube.addAttribute(program.getAttributeLocation("aTexCoord"), 2, 3)
        cube.bind()
        program.use()
        val proj = glm.perspective(Mat4(), glm.PIf * 0.45f, width.toFloat() / height, 0.1f, 10000f)
        val view = glm.translate(Mat4(), Vec3(0f, 0f, -5f))
        program.setUniformMat4("projection", proj)
        program.setUniformMat4("view", view)
    }

    override fun draw() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        //cube
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texture2.getId())
        glBindVertexArray(cube.getVaoId())
        cubePositions.forEachIndexed { index, vec3 ->
            var model = glm.translate(Mat4(), vec3) * glm.rotate(Mat4(), timer.sinceStartSecs(), Vec3(0, 1, 0))
            program.setUniformMat4("model",model)
            glDrawArrays(GL_TRIANGLES, 0, 36)
        }
    }

    companion object {
        fun create(context: Context) = CubeScene(
            context.resources.readRawTextFile(R.raw.cube_vertex_shader),
            context.resources.readRawTextFile(R.raw.cube_fragment_shader),
            Texture(loadBitmap(context, R.raw.landscape)),
            Texture(loadBitmap(context, R.raw.bonobono))
        )
    }
}

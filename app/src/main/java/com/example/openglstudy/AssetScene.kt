package com.example.openglstudy

import android.content.Context
import android.opengl.GLES30.*
import com.example.openglstudy.common.*
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.toInt
import glm_.vec3.Vec3
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class AssetScene(
    private val mesh: Mesh,
    private val vShader: String,
    private val fShader: String
) : Scene() {

    private lateinit var program: Program
    override fun init(width: Int, height: Int) {
        glViewport(0, 0, width, height)
        program = Program.create(vShader, fShader)
        program.use()
        mesh.bind(program)
        val model = glm.translate(Mat4(), Vec3(0, 0, 0))* glm.scale(
            Mat4(),
            Vec3(0.1f, 0.1f, 0.1f)
        )  * glm.rotate(Mat4(), glm.PIf * -0.5f, Vec3(1, 0, 0))
        val proj = glm.perspective(Mat4(), glm.PIf * 0.45f, width.toFloat() / height, 0.1f, 10000f)
        program.setUniformMat4("model", model)
        program.setUniformMat4("projection", proj)
    }

    override fun draw() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        val view = camera.getView()
        program.setUniformMat4("view", view)
        mesh.draw()
    }

    companion object {
        fun create(context: Context): AssetScene {
            val resources = context.resources
            return AssetScene(
                fromAssets(context, "10680_Dog_v2.obj"),
                resources.readRawTextFile(R.raw.asset_vertex),
                resources.readRawTextFile(R.raw.asset_fragment)
            )
        }
    }
}
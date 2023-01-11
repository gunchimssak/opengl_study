package com.example.openglstudy

import android.content.Context
import android.opengl.GLES30.*
import android.util.Log
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
    private val fShader: String,
    private val diffuse: Texture,
    private val bump: Texture
) : Scene() {
    private var model = glm.translate(Mat4(), Vec3(0, 0, 0))
    private lateinit var program: Program
    fun rotateX() {
        model *= glm.rotate(Mat4(), glm.PIf * 0.5f, Vec3(1, 0, 0))
    }

    fun rotateY() {
        model *= glm.rotate(Mat4(), glm.PIf * 0.5f, Vec3(0, 1, 0))
    }

    fun rotateZ() {
        model *= glm.rotate(Mat4(), glm.PIf * 0.5f, Vec3(0, 0, 1))
    }

    fun scaleUp() {
        model *= glm.scale(Mat4(), Vec3(2f, 2f, 2f))
    }

    fun scaleDown() {
        model *= glm.scale(Mat4(), Vec3(0.5f, 0.5f, 0.5f))
    }

    fun cameraZoomIn() {
        camera.eye = camera.eye + Vec3(0, 0, 1)
    }

    fun cameraZoomOut() {
        camera.eye = camera.eye + Vec3(0, 0, -1)
    }

    override fun init(width: Int, height: Int) {
        glViewport(0, 0, width, height)
        program = Program.create(vShader, fShader)
        diffuse.load()
        bump.load()
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, diffuse.getId())
        glActiveTexture(GL_TEXTURE1)
        glBindTexture(GL_TEXTURE_2D, bump.getId())
        mesh.bind(program)
        val proj = glm.perspective(Mat4(), glm.PIf * 0.45f, width.toFloat() / height, 0.1f, 10000f)
        program.setUniformMat4("projection", proj)
        program.setInt("diffuse", 0)
        program.setInt("bump", 1)
    }

    override fun draw() {
        glEnable(GL_DEPTH_TEST)
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        var view = camera.getView()
        program.setUniformMat4("model", model)
        program.setUniformMat4("view", view)
        mesh.draw()
    }

    companion object {
        fun create(context: Context): AssetScene {
            val resources = context.resources
            return AssetScene(
                fromAssets(context, "backpack.obj"),
                resources.readRawTextFile(R.raw.asset_vertex),
                resources.readRawTextFile(R.raw.asset_fragment),
                Texture(loadBitmap(context, R.raw.diffuse)),
                Texture(loadBitmap(context, R.raw.specular))
            )
        }
    }
}
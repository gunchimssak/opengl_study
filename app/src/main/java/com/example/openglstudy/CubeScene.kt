package com.example.openglstudy

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30.*
import android.util.Log
import com.example.openglstudy.common.*
import com.example.openglstudy.common.loadBitmap
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.toFloat
import glm_.vec3.Vec3
import kotlin.random.Random

class CubeScene(
    private val vertexShaderCode: String,
    private val fragmentShaderCode: String,
    private val landscape: Texture,
    private val bonobono: Texture,
    private val dirt: Texture,
    private val grass: Texture,
) : Scene() {

    private lateinit var program: Program

    private lateinit var cube: VertexData
    private lateinit var plane: VertexData
    private lateinit var grasss: VertexData
    private lateinit var background: VertexData

    private var width: Int = 0
    private var height: Int = 0

    private lateinit var grassPositions: List<Vec3>

    override fun init(width: Int, height: Int) {
        glEnable(GL_DEPTH_TEST)
        //blending
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glViewport(0, 0, width, height)
        getRandomPosition()
        this.width = width
        this.height = height
        landscape.load()
        bonobono.load()
        dirt.load()
        grass.load()
        program = Program.create(
            vertexShaderCode = vertexShaderCode,
            fragmentShaderCode = fragmentShaderCode
        )
        program.use()

        background = VertexData(backgroundVertices, null, 5)
        background.addAttribute(program.getAttributeLocation("aPos"), 3, 0)
        background.addAttribute(program.getAttributeLocation("aTexCoord"), 2, 3)
        background.bind()

        cube = VertexData(cubeVertices, null, 5)
        cube.addAttribute(program.getAttributeLocation("aPos"), 3, 0)
        cube.addAttribute(program.getAttributeLocation("aTexCoord"), 2, 3)
        cube.bind()

        plane = VertexData(planeVertices, null, 5)
        plane.addAttribute(program.getAttributeLocation("aPos"), 3, 0)
        plane.addAttribute(program.getAttributeLocation("aTexCoord"), 2, 3)
        plane.bind()

        grasss = VertexData(grassVertices, null, 5)
        grasss.addAttribute(program.getAttributeLocation("aPos"), 3, 0)
        grasss.addAttribute(program.getAttributeLocation("aTexCoord"), 2, 3)
        grasss.bind()
    }

    private fun getRandomPosition() {
        val random = java.util.Random()
        val list = mutableListOf<Vec3>()

        repeat(500) {
            val x = random.nextFloat() * 20 - 10
            val z = random.nextFloat() * 20 - 10
            list.add(Vec3(x, 0, z))
        }
        grassPositions = list
    }

    override fun draw() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        //cube

        val proj = glm.perspective(Mat4(), glm.PIf * 0.45f, width.toFloat() / height, 0.1f, 10000f)
        val view = glm.translate(Mat4(), Vec3(0f, -1f, 0f)) * glm.rotate(
            Mat4(),
            timer.sinceStartSecs() * 0.2f,
            Vec3(0, 1, 0)
        )
        program.setUniformMat4("projection", proj)
        program.setUniformMat4("view", view)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, dirt.getId())
        glBindVertexArray(plane.getVaoId())
        program.setUniformMat4("model", Mat4())
        glDrawArrays(GL_TRIANGLES, 0, 6)


        glBindTexture(GL_TEXTURE_2D, grass.getId())
        glBindVertexArray(grasss.getVaoId())
        grassPositions.forEachIndexed { index, vec3 ->
            val model = glm.translate(Mat4(), vec3) * glm.rotate(Mat4(), glm.PIf, Vec3(0, 0, 1))
            program.setUniformMat4("model", model)
            glDrawArrays(GL_TRIANGLES, 0, 6)
        }

        program.setUniformMat4("projection", Mat4())
        program.setUniformMat4("view", Mat4())

        glBindTexture(GL_TEXTURE_2D, landscape.getId())
        glBindVertexArray(background.getVaoId())
        val model = glm.rotate(Mat4(), glm.PIf * 1f, Vec3(0, 0, 1))
        program.setUniformMat4("model", model)
        glDrawArrays(GL_TRIANGLES, 0, 6)

    }


    companion object {
        fun create(context: Context) = CubeScene(
            context.resources.readRawTextFile(R.raw.cube_vertex_shader),
            context.resources.readRawTextFile(R.raw.cube_fragment_shader),
            Texture(loadBitmap(context, R.raw.sky5)),
            Texture(loadBitmap(context, R.raw.bonobono)),
            Texture(loadBitmap(context, R.raw.dirt)),
            Texture(loadBitmap(context, R.raw.texture_grass))
        )
    }
}

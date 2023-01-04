package com.example.openglstudy.common

import android.opengl.GLES20.*
import android.util.Log
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import java.nio.IntBuffer

class Program(private val vertexShader: Int, private val fragmentShader: Int) {
    private var program: Int = -1
    private var attributes = mutableMapOf<String, Int>()
    private var uniforms = mutableMapOf<String, Int>()
    fun getAttributeLocation(name: String) = attributes.getValue(name)
    fun getUniformLocation(name: String) = uniforms.getValue(name)
    fun link(){
        program = glCreateProgram()
        glAttachShader(program, vertexShader)
        glAttachShader(program, fragmentShader)
        glLinkProgram(program)
    }
    private fun fetchAttributes() {
        val count = IntBuffer.allocate(1)
        glGetProgramiv(program, GL_ACTIVE_ATTRIBUTES, count) //프로그램의 정보를 가져옵니다.
        for (i in 0 until count[0]) {
            val name = glGetActiveAttrib(program, i, IntBuffer.allocate(1), IntBuffer.allocate(1))
            val location = glGetAttribLocation(program, name)
            Log.e("name","${name}")
            attributes[name] = location
        }
    }
    private fun fetchUniforms(){

        val count = IntBuffer.allocate(1)
        glGetProgramiv(program, GL_ACTIVE_UNIFORMS, count) //프로그램의 정보를 가져옵니다.
        for (i in 0 until count[0]) {
            val name = glGetActiveUniform(program, i, IntBuffer.allocate(1), IntBuffer.allocate(1))
            val location = glGetUniformLocation(program, name)
            uniforms[name] = location
        }
    }
    fun camera(translate: Float, rotate: Vec2): Mat4 {

        val projection = glm.perspective(glm.PIf * 0.25f, 4.0f / 3.0f, 0.1f, 100.0f)
        var view = glm.translate(Mat4(1.0f), Vec3(0.0f, 0.0f, -translate))
        view = glm.rotate(view, rotate.y, Vec3(-1.0f, 0.0f, 0.0f))
        view = glm.rotate(view, rotate.x, Vec3(0.0f, 1.0f, 0.0f))
        val model = glm.scale(Mat4(1.0f), Vec3(0.5f))
        return projection * view * model
    }

    fun use() = glUseProgram(program)
    companion object{
        fun create(vertexShaderCode: String, fragmentShaderCode: String): Program {
            var vertexShader = compileShader(GL_VERTEX_SHADER, vertexShaderCode)
            var fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentShaderCode)
            return Program(vertexShader, fragmentShader).apply {
                link()
                fetchAttributes()
                fetchUniforms()
                glDeleteShader(vertexShader)
                glDeleteShader(fragmentShader)
            }
        }
    }
}
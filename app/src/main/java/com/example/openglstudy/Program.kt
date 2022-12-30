package com.example.openglstudy

import android.opengl.GLES20.*
import java.nio.IntBuffer

class Program(private val vertexShader: Int, private val fragmentShader: Int) {
    private var program: Int = -1
    private var attributes = mutableMapOf<String, Int>()
    fun getAttributeLocation(name: String) = attributes.getValue(name)
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
            attributes[name] = location
        }
    }
    fun use() = glUseProgram(program)
    companion object{
        fun create(vertexShaderCode: String, fragmentShaderCode: String): Program {
            var vertexShader = compileShader(GL_VERTEX_SHADER, vertexShaderCode)
            var fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentShaderCode)
            return Program(vertexShader, fragmentShader).apply {
                link()
                fetchAttributes()
                glDeleteShader(vertexShader)
                glDeleteShader(fragmentShader)
            }
        }
    }
}
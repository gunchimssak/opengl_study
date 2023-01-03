#version 300 es
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
uniform mat4 model;
out vec2 TexCoord;
void main() {
    vec4 a = vec4(aPos.x, -aPos.y, aPos.z, 1.0);
    model * a;
    gl_Position = vec4(aPos.x, -aPos.y, aPos.z, 1.0);
    TexCoord = aTexCoord;
}

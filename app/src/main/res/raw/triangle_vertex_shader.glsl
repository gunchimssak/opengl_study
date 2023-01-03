#version 300 es
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
out vec2 TexCoord;

uniform mat4 transform;

void main() {
    vec4 a = vec4(aPos.x, -aPos.y, aPos.z, 1.0);
    gl_Position = transform * vec4(aPos, 1.0);
    TexCoord = aTexCoord;
}

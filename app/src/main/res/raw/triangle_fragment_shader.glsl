#version 300 es
precision mediump float;

out vec4 FragColor;
in vec2 TexCoord;


void main() {
    FragColor = texture(TexCoord);
}

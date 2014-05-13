#version 330 core

uniform sampler2D texture01;

in vec2 passTextureCoord;

out vec4 fragColor;

void main(void) {
    fragColor = texture(texture01, passTextureCoord);
}

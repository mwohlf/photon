#version 330 core

// fragments are possible pixels

uniform sampler2D texture01;
uniform sampler2D texture02;

in vec4 pass_Light;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;          // must be already normalized
in vec3 pass_Position;

out vec4 fragColor;

void main(void) {
    fragColor = texture(texture01, pass_TextureCoord) * pass_Light;
}

#version 330 core

in vec4 pass_Color;

out vec4 fragColor;

void main(void) {
    fragColor = pass_Color;
}

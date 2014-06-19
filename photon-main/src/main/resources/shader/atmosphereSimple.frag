#version 330 core

in float brightness;

out vec4 fragColor;

void main(void) {

    fragColor = vec4(0.2, 0.2, 0.7, 0.7) * brightness;

}

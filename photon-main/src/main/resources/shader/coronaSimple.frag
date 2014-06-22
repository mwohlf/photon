#version 330 core

in float brightness;
in vec4 passColor;

out vec4 fragColor;

void main(void) {

    fragColor = passColor * brightness;

}

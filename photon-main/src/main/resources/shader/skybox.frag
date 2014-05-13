#version 330 core

// skybox shader
// see: http://ogldev.atspace.co.uk/www/tutorial25/tutorial25.html

in vec2 passTextureCoord;

uniform sampler2D texture01;

out vec4 fragColor;

void main(void) {
    fragColor = texture(texture01, passTextureCoord);
}

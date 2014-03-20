#version 330 core

uniform sampler2D texture01;
//uniform sampler2D texture02;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

out vec4 fragColor;

void main(void) {
    // pass_color is set to 0 if we use textures
    fragColor = pass_Color + texture(texture01, pass_TextureCoord);
    //out_Color = texture(texture01, pass_TextureCoord);
}

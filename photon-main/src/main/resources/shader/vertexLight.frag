#version 330 core

// fragments are possible pixels

uniform sampler2D texture01;
uniform sampler2D texture02;

in vec4 passLight;
in vec2 passTextureCoord;
in vec3 passNormal;          // must be already normalized
in vec3 passPosition;

out vec4 fragColor;

void main(void) {
    // fragColor = texture(texture01, passTextureCoord) * passLight;
    
        fragColor = texture(texture01, passTextureCoord);
    
}

#version 330 core

// we can use vec2 here
in vec3 ${position};
in vec3 ${color};
in vec2 ${texture};

// this is a 2d renderer, we don't use the cam perspective 
uniform mat4 modelToWorldMatrix;     // modelMatrix
uniform mat4 worldToCameraMatrix;    // viewMatrix

out vec4 pass_Color;
out vec2 pass_TextureCoord;

void main(void) {

    vec4 worldPos = modelToWorldMatrix * vec4(${position}, 1.0);

    gl_Position = worldToCameraMatrix * worldPos;

    pass_Color = vec4(in_Color, 0.0);
    
    pass_TextureCoord = in_TextureCoord;
}

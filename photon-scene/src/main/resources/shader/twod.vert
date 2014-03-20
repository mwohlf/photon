#version 330 core

// we can use vec2 here
in vec3 $position$;

// this is a 2d renderer, we don't use the cam perspective 
uniform mat4 modelToWorldMatrix;     // modelMatrix
uniform mat4 worldToCameraMatrix;    // viewMatrix

out vec4 pass_Color;

void main(void) {

    vec4 worldPos = modelToWorldMatrix * vec4($position$, 1.0);

    gl_Position = worldToCameraMatrix * worldPos;

    pass_Color = vec4(1.0, 0.0, 0.0, 0.0);
    
}

#version 330 core

in vec3 ${position};
in vec2 ${texture};
in vec3 ${normal};

uniform mat4 modelToWorldMatrix;     // modelMatrix
uniform mat4 worldToCameraMatrix;    // viewMatrix
uniform mat4 cameraToClipMatrix;     // projectionMatrix

uniform mat4 normalMatrix;     
uniform mat4 modelViewMatrix;     

varying vec3 normalInterp;
varying vec3 vertPos;

void main(void) {

    // step 1: rotate then move the object to its position in the world
    vec4 worldPos = modelToWorldMatrix * vec4(${position}, 1.0);

    // step 2: move, rotate, scale the object according to the cam direction/position/field of view
    vec4 cameraPos = worldToCameraMatrix * worldPos;

    // step 3: project the object from 3D cam space into 2D view
    gl_Position = cameraToClipMatrix * cameraPos;


    vec4 vertPos4 = modelViewMatrix * vec4(${position}, 1.0);
    
    vertPos = vec3(vertPos4) / vertPos4.w;
    
    normalInterp = vec3(normalMatrix * vec4(${normal}, 0.0));
}

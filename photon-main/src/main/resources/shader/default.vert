#version 110

attribute vec3 ${position};

uniform mat4 modelToWorldMatrix;     // modelMatrix
uniform mat4 worldToCameraMatrix;    // viewMatrix
uniform mat4 cameraToClipMatrix;     // projectionMatrix


// see: http://www.lighthouse3d.com/tutorials/glsl-core-tutorial/vertex-shader/
void main(void) {

    // step 1: rotate then move the object to its position in the world
    vec4 worldPos = modelToWorldMatrix * vec4(${position}, 1.0);

    // step 2: move, rotate, scale the object according to the cam direction/position/field of view
    vec4 cameraPos = worldToCameraMatrix * worldPos;

    // step 3: project the object from 3D cam space into 2D view
    // the homogenous coordinates of the output vertex’s position
    gl_Position = cameraToClipMatrix * cameraPos;
    
}

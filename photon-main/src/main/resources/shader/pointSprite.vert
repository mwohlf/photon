#version 330 core

in vec3 ${position};
in vec2 ${texture};
// in vec4 ${color};

// uniform float ${pointSize};
uniform mat4 modelToWorldMatrix;     // modelMatrix
uniform mat4 worldToCameraMatrix;    // viewMatrix
uniform mat4 cameraToClipMatrix;     // projectionMatrix

out vec4 passColor;
out vec2 passTextureCoord;

void main(void) {

    // step 1: rotate then move the object to its position in the world
    vec4 worldPos = modelToWorldMatrix * vec4(${position}, 1.0);

    // step 2: move, rotate, scale the object according to the cam direction/position/field of view
    vec4 cameraPos = worldToCameraMatrix * worldPos;

    // step 3: project the object from 3D cam space into 2D view
    // the homogeneous coordinates of the output vertex’s position
    gl_Position = cameraToClipMatrix * cameraPos;
   
    //gl_PointSize = ${pointSize};
	gl_PointSize = 20.0;

    // passColor = ${color};
    passColor = vec4(1.0, 1.0, 1.0, 1.0);
    
    passTextureCoord = ${texture};
    
}

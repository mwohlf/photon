#version 330 core

// skybox shader
// see: http://ogldev.atspace.co.uk/www/tutorial25/tutorial25.html

in vec3 $position$;
in vec2 $texture$;

uniform mat4 modelToWorldMatrix;     // modelMatrix
uniform mat4 worldToCameraMatrix;    // viewMatrix  FIXME: this is no longer needed
uniform mat4 cameraToClipMatrix;     // projectionMatrix

out vec2 pass_TextureCoord;

void main(void) {

    // step 1: rotate then move the object to its position in the world
    vec4 worldPos = modelToWorldMatrix * vec4($position$, 1.0);

    // step 2: move, rotate, morph the object according to the cam direction/position/field of view
    vec4 cameraPos = worldToCameraMatrix * worldPos;

    // step 3: project the object from 3D cam space into 2D view
    gl_Position = cameraToClipMatrix * cameraPos;

    // this is the trick to be able to render the skybox last 
    // without running into trouble with z coords
    gl_Position = gl_Position.xyww;
    
    pass_TextureCoord = $texture$;
    
}


#version 330 core

// see: http://stackoverflow.com/questions/10213361/how-can-i-render-an-atmosphere-over-a-rendering-of-the-earth-in-three-js


in vec3 ${position};    // vertex position in model space
in vec3 ${normal};      // the normal for the current vertex
in vec2 ${texture};     // the texture position of the vertex

void main(void) {
    passPosition=${position};
    passNormal=${normal};
    passTexture=${texture};
}

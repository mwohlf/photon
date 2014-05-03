#version 330 core

uniform sampler2D texture01;

in vec4 passColor;
in vec2 passTextureCoord;


out vec4 fragColor;

// see: http://gamedev.stackexchange.com/questions/11095/opengl-es-2-0-point-sprites-size/15528#15528
// see: http://forum.jogamp.org/JOGL-Shader-Questions-td4028783.html
void main() {
   //fragColor = texture2D(texture01, passTextureCoord);
   fragColor = texture2D(texture01, gl_PointCoord);
   //fragColor = vec4(1.0, 0.0, 0.0, 1.0);
}

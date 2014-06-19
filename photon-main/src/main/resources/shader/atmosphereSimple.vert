#version 330 core

// see: http://www.lighthouse3d.com/tutorials/glsl-tutorial/lighting/
// see: http://www.gamedev.net/page/resources/_/technical/graphics-programming-and-theory/real-time-atmospheric-scattering-r2093


struct VertexLight {
  vec3  color;
  vec4  diffuse;              // same intensity regardless of the viewer’s position
  vec4  specular;
  vec3  position;
  vec4  halfVector;
  float constantAttenuation;
  float linearAttenuation;
  float quadraticAttenuation;
};

// see: http://iloveshaders.blogspot.de/2011/04/implementing-basic-lights.html
struct Material {
  vec3 emissive;    // Ke : outgoing light
  vec3 ambient;    // Ka : multiplied with the global ambient light
  vec3 diffuse;     // Kd
  vec3 specular;    // Ks
  float shininess;
};


in vec3 ${position};    // vertex position in model space
in vec2 ${texture};     // the texture position of the vertex
in vec3 ${normal};      // the normal for the current vertex

uniform float ${coronaRadius};
uniform float ${planetRadius};
// center of the corona is always (0,0,0) in model space, use modelToWorldMatrix 
uniform vec4 ${coronaColor};

 
uniform VertexLight vertexLight[${maxVertexLightCount}];

uniform mat4 modelToWorldMatrix;
uniform mat4 worldToCameraMatrix;
uniform mat4 cameraToClipMatrix;
uniform mat3 normalMatrix;           // == inverse(transpose(ModelviewMatrix)), or just the 3x3 piece;

out vec4 passLight;
out vec2 passTextureCoord;
out vec3 passNormal;                // normal in eye-space
out vec3 passPosition;              // vertex position in eye-space
out vec4 passColor;

out float brightness;
out vec4 gl_Position;


// cam pos is (0,0,0)

void main(void) {
	// position of the center of corona sphere 
	vec4 corePosition =  modelToWorldMatrix * vec4(0.0, 0.0, 0.0, 1.0);
	vec4 farSpherePos =  modelToWorldMatrix * vec4(${position}, 1.0);
	// length of the path inside the corona [0..1], 1 is when we cross the center the dot product s at its max which equals the radius
	float pathLengthInCorona = dot(-normalize(farSpherePos.xyz), corePosition.xyz - farSpherePos.xyz) / ${coronaRadius};
	
    gl_Position = cameraToClipMatrix * worldToCameraMatrix * modelToWorldMatrix * vec4(${position}, 1.0);
 	brightness = pathLengthInCorona;
 	passColor = ${coronaColor};
}

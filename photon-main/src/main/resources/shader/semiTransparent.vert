#version 330 core

// see: http://www.lighthouse3d.com/tutorials/glsl-tutorial/lighting/

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

uniform VertexLight vertexLight[${maxVertexLightCount}];

uniform mat4 modelToWorldMatrix;
uniform mat4 worldToCameraMatrix;
uniform mat4 cameraToClipMatrix;
uniform mat3 normalMatrix;           // == inverse(transpose(ModelviewMatrix)), or just the 3x3 piece;

out vec4 passLight;
out vec2 passTextureCoord;
out vec3 passNormal;                // normal in eye-space
out vec3 passPosition;              // vertex position in eye-space

out vec4 gl_Position;


void main(void) {
    // gl_Position is a special variable used to store the final position.
    // its a vec4() in normalized screen coordinates calculated by
    // transferring the in_Position from model-space to world-space to view/cam-space to clip-space
    // the 1.0 is needed to do translations with the matrices
    gl_Position = cameraToClipMatrix * worldToCameraMatrix * modelToWorldMatrix * vec4(${position}, 1.0);

    // transforms the vertex into cam-space (means minus the cameraToClipMatrix)
    passPosition = vec3(worldToCameraMatrix * modelToWorldMatrix * vec4(${position}, 1.0));
    // transforms the normal's orientation into cam-space, normal is without position, no need for the modelToWorldMatrix here
    passNormal =  normalize(vec3(normalMatrix * ${normal}));
    // nothing happens to the texture coords, they are picked up in the fragment shader and mapped to
    // one of the textures that are bound
    passTextureCoord = ${texture};

    passLight = vec4(0.1, 0.1, 0.1, 0);
    for (int index = 0; index < ${maxVertexLightCount}; index++) {
    
       // the light ray from the light source to the vertex position
       vec3 lightPos = vertexLight[index].position.xyz;
       vec3 vertexPos = vec3(modelToWorldMatrix * vec4(${position}, 1.0));
             
	   // dot product is max for 0degree between light vector and normal	
       vec3 lightVector = normalize(lightPos - vertexPos);
       float crossSection = max(dot(passNormal, lightVector), 0.0);

       // attenuate the light based on distance.
       float distance = max(vertexLight[index].position - length(vertexPos), 0.0);

       //diffuse = diffuse * (1.0 / (1.0 + (vertexLight[index].attenuation * distance * distance)));


       //diffuse = 5; // for testing only

       // Multiply the color by the illumination level. It will be interpolated across the triangle.
       passLight = passLight + vertexLight[index].diffuse * crossSection;
       // passLight = vec4(1,0,0,0);
       passLight = vec4(1, 1, 1, 0) * crossSection;
    }

}

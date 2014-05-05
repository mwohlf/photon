#version 330 core

struct VertexLight {
  vec3  position;
  float attenuation;
  vec3  color;
  vec4  diffuse;
};

struct Material {
  vec3 specular;
  float shininess;
  vec3 ambient;
  vec4 diffuse;
};

in vec3 ${position};    // vertex position in model space
in vec2 ${texture};     // the texture position of the vertex
in vec3 ${normal};      // the normal for the current vertex

uniform VertexLight lights[${maxVertexLightCount}];
uniform Material material;

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

    // transforms the vertex into cam-space
    passPosition = vec3(worldToCameraMatrix * modelToWorldMatrix * vec4(${position}, 1.0));
    // transforms the normal's orientation into cam-space
    passNormal =  normalize(vec3(${normal} * normalMatrix));
    // nothing happens to the texture coords, they are picked up in the fragment shader and mapped to
    // one of the textures that are bound
    passTextureCoord = ${texture};

    passLight = vec4(0,0,0,0);
    for (int index = 0; index < ${maxVertexLightCount}; index++) {
       vec3 lightPos = lights[index].position;
       vec3 vertexPos = vec3(modelToWorldMatrix * vec4(${position}, 1.0));
       vec3 lightVector = normalize(lights[index].position - vertexPos);

       // the dot product of the light vector and vertex normal is a indication for the amount of light on the surface
       float diffuse = max(dot(passNormal, lightVector), 0.0);

       // Attenuate the light based on distance.
       float distance = max(length(lights[index].position - vertexPos), 0.0);

       diffuse = diffuse * (1.0 / (1.0 + (lights[index].attenuation * distance * distance)));


       //diffuse = 0.5; // for testing only

       // Multiply the color by the illumination level. It will be interpolated across the triangle.
       passLight = passLight + lights[index].diffuse * diffuse;
    }

}

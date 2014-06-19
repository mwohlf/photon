package net.wohlfart.photon.shader;

import java.util.HashMap;
import java.util.Map;

public class ShaderParser {
    private final TemplateParser delegate;

    // ----- these are the constants that should be used in the java code -----
    public static final String MAX_VERTEX_LIGHT_COUNT = "maxVertexLightCount";
    public static final Integer MAX_VERTEX_LIGHT_COUNT_VALUE = 1;

    public static final String MAX_FRAGMENT_LIGHT_COUNT = "maxFragmentLightCount";
    public static final Integer MAX_FRAGMENT_LIGHT_COUNT_VALUE = 3;

    // strings to access the vertex attributes in the shaders
    public static final String VERTEX_POSITION = "in_Position";
    public static final String VERTEX_COLOR = "in_Color";
    public static final String VERTEX_NORMAL = "in_Normal";
    public static final String VERTEX_TEXTURE = "in_TextureCoord";

    // strings to access the uniforms in the shaders
    public static final String UNIFORM_MODEL_2_WORLD_MTX = "modelToWorldMatrix";
    public static final String UNIFORM_WORLD_2_CAM_MTX = "worldToCameraMatrix";
    public static final String UNIFORM_CAM_2_CLIP_MTX = "cameraToClipMatrix";
    public static final String UNIFORM_NORMAL_MTX = "normalMatrix";
    public static final String UNIFORM_POINT_SIZE = "pointSize";

    public static final String UNIFORM_PLANET_RADIUS = "planetRadius";
    public static final String UNIFORM_CORONA_RADIUS = "coronaRadius";
    public static final String UNIFORM_CORONA_COLOR = "coronaColor";

    public static final String TEXTURE01 = "texture01";
    public static final String TEXTURE02 = "texture02";
    public static final String TEXTURE03 = "texture03";
    public static final String TEXTURE04 = "texture04";

    public static final String VERTEX_LIGHT = "vertexLight";



    // ----- this are the constants that should be used in the shader code, wrapped in ${} -----
    public static final HashMap<String, String> CONSTANTS = new HashMap<String, String>();
    {
    	CONSTANTS.put(MAX_VERTEX_LIGHT_COUNT, Integer.toString(MAX_VERTEX_LIGHT_COUNT_VALUE));
    	CONSTANTS.put(MAX_FRAGMENT_LIGHT_COUNT, Integer.toString(MAX_FRAGMENT_LIGHT_COUNT_VALUE));
    }

    // common vertex attribute names used in the shader code
    // and their mapping to the strings used in java code
    public static final HashMap<String, String> VERTEX_ATTRIBUTES = new HashMap<String, String>();
    {
        VERTEX_ATTRIBUTES.put("position", VERTEX_POSITION);
        VERTEX_ATTRIBUTES.put("color", VERTEX_COLOR);
        VERTEX_ATTRIBUTES.put("normal", VERTEX_NORMAL);
        VERTEX_ATTRIBUTES.put("texture", VERTEX_TEXTURE);
    }

    // common uniform names used in the shader code
    // and their mapping to the strings used in java code
    public static final HashMap<String, String> UNIFORMS = new HashMap<String, String>();
    {
        UNIFORMS.put("modelToWorldMatrix", UNIFORM_MODEL_2_WORLD_MTX);
        UNIFORMS.put("worldToCameraMatrix", UNIFORM_WORLD_2_CAM_MTX);
        UNIFORMS.put("cameraToClipMatrix", UNIFORM_CAM_2_CLIP_MTX);
        UNIFORMS.put("normalMatrix", UNIFORM_NORMAL_MTX);
        UNIFORMS.put("pointSize", UNIFORM_POINT_SIZE);
        UNIFORMS.put("planetRadius", UNIFORM_PLANET_RADIUS);
        UNIFORMS.put("coronaRadius", UNIFORM_CORONA_RADIUS);
        UNIFORMS.put("coronaColor", UNIFORM_CORONA_COLOR);
    }


    public static final HashMap<String, String> TEXTURES = new HashMap<String, String>();
    {
        TEXTURES.put("texture01", TEXTURE01);
        TEXTURES.put("texture02", TEXTURE02);
        TEXTURES.put("texture03", TEXTURE03);
        TEXTURES.put("texture04", TEXTURE04);
    }


    public ShaderParser(String string) {
        delegate = new TemplateParser(string);
    }

    @SuppressWarnings("unchecked")
    public String render() {
        setKeyValuePairs(VERTEX_ATTRIBUTES, UNIFORMS, TEXTURES, CONSTANTS);
        return doRender();
    }

    private void setKeyValuePairs(HashMap<String, String>... hashMaps) {
        for (HashMap<String, String> map : hashMaps) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                delegate.add(entry.getKey(), entry.getValue());
            }
        }
    }

    private String doRender() {
    	String result = delegate.render();
    	return result;
    }

}

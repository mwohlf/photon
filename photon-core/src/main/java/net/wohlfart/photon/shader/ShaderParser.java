package net.wohlfart.photon.shader;

import java.util.HashMap;
import java.util.Map;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

public class ShaderParser {
    private final ST delegate;

    // ----- these are the constants that should be used in the java code -----
    
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
    
    public static final String TEXTURE01 = "texture01";        
    public static final String TEXTURE02 = "texture02";        
    public static final String TEXTURE03 = "texture03";        
    public static final String TEXTURE04 = "texture04";        
    

    
    // ----- this are the constants that should be used in the shader code, wrapped in $$ -----
    
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
    }


    public static final HashMap<String, String> TEXTURES = new HashMap<String, String>();
    {
        TEXTURES.put("texture01", TEXTURE01);    
        TEXTURES.put("texture02", TEXTURE02);    
        TEXTURES.put("texture03", TEXTURE03);    
        TEXTURES.put("texture04", TEXTURE04);    
    }


    public ShaderParser(String string) {   
        STGroup group = new STGroup('$','$');     
        delegate = new ST(group, string);
    }

    @SuppressWarnings("unchecked")
    public String render() {
        setKeyValuePairs(VERTEX_ATTRIBUTES, UNIFORMS, TEXTURES);       
        return delegate.render();
    }

    @SuppressWarnings("unchecked")
    private void setKeyValuePairs(HashMap<String, String>... hashMaps) {
        for (HashMap<String, String> map : hashMaps) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                delegate.add(entry.getKey(), entry.getValue());
            }
        }
    }

}

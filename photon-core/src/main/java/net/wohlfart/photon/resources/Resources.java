package net.wohlfart.photon.resources;

import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.texture.CelestialType;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;

public interface Resources {

	public static final ShaderIdentifier TEXTURE_SHADER_ID = ShaderIdentifier.create("shader/texture.vert", "shader/texture.frag");

	public static final ShaderIdentifier SKYBOX_SHADER_ID = ShaderIdentifier.create("shader/skybox.vert", "shader/skybox.frag");

    // used for the labels, no z-coordinates
	public static final ShaderIdentifier PLAIN_SHADER_ID = ShaderIdentifier.create("shader/plain.vert", "shader/plain.frag");

	public static final ShaderIdentifier TWOD_SHADER_ID = ShaderIdentifier.create("shader/twod.vert", "shader/twod.frag");












	// --

	public static final ShaderIdentifier TEXTURE_SIMPLE_SHADER_ID = ShaderIdentifier.create("shader/texture/simple.vert", "shader/texture/simple.frag");
	public static final ShaderIdentifier TEXTURE_RADBLUR_SHADER_ID = ShaderIdentifier.create("shader/texture/radblur.vert", "shader/texture/radblur.frag");
	public static final ShaderIdentifier SIMPLE_SHADER_ID = ShaderIdentifier.create("shader/simple/vertex.glsl", "shader/simple/fragment.glsl");


	public static final ITextureIdentifier TEXTURE_ID1 = TextureIdentifier.create("gfx/textures/texture-1.jpg");
	public static final TextureIdentifier TEXTURE_ID2 =  TextureIdentifier.create(30, CelestialType.LAVA_PLANET, 2);
	public static final TextureIdentifier TEXTURE_ID3 =  TextureIdentifier.create(30, CelestialType.CONTINENTAL_PLANET, 2);




}

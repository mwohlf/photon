package net.wohlfart.photon.resources;

import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.texture.CelestialType;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;

public interface Resources {

	public static final ShaderIdentifier TEXTURE_SIMPLE_SHADER_ID = ShaderIdentifier.create("shader/texture/simple.vert", "shader/texture/simple.frag");
	public static final ShaderIdentifier TEXTURE_RADBLUR_SHADER_ID = ShaderIdentifier.create("shader/texture/radblur.vert", "shader/texture/radblur.frag");

	public static final ITextureIdentifier TEXTURE_ID1 = TextureIdentifier.create("gfx/textures/texture-1.jpg");
	public static final TextureIdentifier TEXTURE_ID2 =  TextureIdentifier.create(30, CelestialType.LAVA_PLANET, 2);
	public static final TextureIdentifier TEXTURE_ID3 =  TextureIdentifier.create(30, CelestialType.CONTINENTAL_PLANET, 2);

}

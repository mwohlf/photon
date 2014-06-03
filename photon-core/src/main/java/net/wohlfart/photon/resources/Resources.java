package net.wohlfart.photon.resources;

import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;
import net.wohlfart.photon.texture.simplex.ContinentalPlanetTexture;
import net.wohlfart.photon.texture.simplex.LavaPlanetTexture;

public interface Resources {

	public static final ITextureIdentifier TEXTURE_ID1 = TextureIdentifier.create("gfx/textures/texture-1.jpg");
	public static final TextureIdentifier TEXTURE_ID2 =  TextureIdentifier.create(30, LavaPlanetTexture.ID, 2);
	public static final TextureIdentifier TEXTURE_ID3 =  TextureIdentifier.create(30, ContinentalPlanetTexture.ID, 2);

}

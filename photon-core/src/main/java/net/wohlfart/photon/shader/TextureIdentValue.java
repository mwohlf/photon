package net.wohlfart.photon.shader;

import net.wohlfart.photon.resources.ResourceManager;
import net.wohlfart.photon.texture.ITexture;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;

public class TextureIdentValue extends AbstractTextureValue {

	private final ITextureIdentifier textureIdentifier;
	private final String name;

	public TextureIdentValue(String name, ITextureIdentifier textureIdentifier) {
		this.name = name;
		this.textureIdentifier = textureIdentifier;
	}

	@Override
	public int getTextureHandle(IShaderProgram shader) {
		ITexture texture = ResourceManager.loadResource(ITexture.class, textureIdentifier);
		return texture.getHandle(shader.getGl());
	}

	@Override
	int getLocation(IShaderProgram shader) {
		// TODO Auto-generated method stub
		return 0;
	}

}
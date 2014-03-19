package net.wohlfart.photon.shader;

import net.wohlfart.photon.resources.ResourceManager;
import net.wohlfart.photon.texture.ITexture;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;

public class TextureIdentValue extends TextureValue {

	private final ITextureIdentifier textureIdentifier;

	public TextureIdentValue(ITextureIdentifier textureIdentifier) {
		this.textureIdentifier = textureIdentifier;
	}

	@Override
	public int getTextureHandle(IUniformHandle handle) {
		ITexture texture = ResourceManager.loadResource(ITexture.class, textureIdentifier);
		return texture.getHandle(handle.getShader().getGl());
	}

}
package net.wohlfart.photon.shader;

import net.wohlfart.photon.texture.ITexture;

public class TextureValue extends AbstractTextureValue {

	private final ITexture texture;

	public TextureValue(ITexture texture) {
		this.texture = texture;
	}

	@Override
	public int getTextureHandle(IUniformHandle handle) {
		return texture.getHandle(handle.getShader().getGl());
	}

}
package net.wohlfart.photon.shader;

import net.wohlfart.photon.texture.ITexture;

public class TextureValue extends AbstractTextureValue {

	private final String name;
	private final ITexture texture;

	public TextureValue(String name, ITexture texture) {
		this.name = name;
		this.texture = texture;
	}

	@Override
	public String getKey() {
		return name;
	}

	@Override
	public int getTextureHandle(IShaderProgram shader) {
		return texture.getHandle(shader.getGl());
	}

	@Override
	int getLocation(IShaderProgram shader) {
		// TODO Auto-generated method stub
		return 0;
	}

}
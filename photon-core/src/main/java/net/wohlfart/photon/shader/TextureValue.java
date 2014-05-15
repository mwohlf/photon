package net.wohlfart.photon.shader;

import net.wohlfart.photon.texture.ITexture;

public class TextureValue extends AbstractTextureValue {

	private final String name;
	private final ITexture texture;

	public TextureValue(String name, ITexture texture) {
		super(name); // using the name as the key
		this.name = name;
		this.texture = texture;
	}

	@Override
	public int getTextureHandle(IShaderProgram shader) {
		return texture.getHandle(shader.getGl());
	}

	@Override
	public int getLocation(IShaderProgram shader) {
		return shader.getUniformLocation(name);
	}

}
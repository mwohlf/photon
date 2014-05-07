package net.wohlfart.photon.shader;


public class TextureHandleValue extends AbstractTextureValue {

	private final String name;
	private final int textureHandle;

	public TextureHandleValue(String name, int textureHandle) {
		assert textureHandle > -1;
		this.name = name;
		this.textureHandle = textureHandle;
	}

	@Override
	public String getKey() {
		return name;
	}

	@Override
	public int getTextureHandle(IShaderProgram shader) {
		return textureHandle;
	}

	@Override
	public int getLocation(IShaderProgram shader) {
		// TODO Auto-generated method stub
		return 0;
	}

}
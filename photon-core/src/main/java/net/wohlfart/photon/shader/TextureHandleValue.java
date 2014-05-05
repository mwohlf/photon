package net.wohlfart.photon.shader;


public class TextureHandleValue extends AbstractTextureValue {

	private final String name;
	private final int textureHandle;

	public TextureHandleValue(String name, int textureHandle) {
		assert textureHandle > -1;
		this.textureHandle = textureHandle;
		this.name = name;
	}

	@Override
	public int getTextureHandle(IShaderProgram shader) {
		return textureHandle;
	}

	@Override
	int getLocation(IShaderProgram shader) {
		// TODO Auto-generated method stub
		return 0;
	}

}
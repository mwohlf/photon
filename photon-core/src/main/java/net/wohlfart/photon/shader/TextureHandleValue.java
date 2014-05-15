package net.wohlfart.photon.shader;


public class TextureHandleValue extends AbstractTextureValue {

	protected final String name;
	protected final int textureHandle;

	public TextureHandleValue(String name, int textureHandle) {
		super(name);
		assert textureHandle > -1;
		this.name = name;
		this.textureHandle = textureHandle;
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
package net.wohlfart.photon.shader;


public class TextureHandleValue extends AbstractTextureValue {

	private final int textureHandle;

	public TextureHandleValue(int textureHandle) {
		assert textureHandle > -1;
		this.textureHandle = textureHandle;
	}

	@Override
	public int getTextureHandle(IUniformHandle handle) {
		return textureHandle;
	}

}
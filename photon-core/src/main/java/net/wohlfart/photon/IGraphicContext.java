package net.wohlfart.photon;

import java.util.Map;

import javax.media.nativewindow.util.Dimension;

import net.wohlfart.photon.render.IFrameBuffer;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.UniformHandle.IUniformValue;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;

public interface IGraphicContext {

	void setFrameBuffer(IFrameBuffer frameBuffer);

	void setRenderConfig(ShaderIdentifier shaderId, IRenderConfig<RenderConfigImpl> newConfig);

	void setUniformValues(Map<String, ITextureIdentifier> textures, Map<String, IUniformValue> uniformValues);

	void drawGeometry(IGeometry geometry);

	Dimension getDimension();

}

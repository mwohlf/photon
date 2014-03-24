package net.wohlfart.photon;

import java.util.Map;

import net.wohlfart.photon.render.IFrameBuffer;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.shader.IUniformValue;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.tools.Dimension;

public interface IGraphicContext {

	void setFrameBuffer(IFrameBuffer frameBuffer);

	void setRenderConfig(ShaderIdentifier shaderId, IRenderConfig<RenderConfigImpl> newConfig);

	void setUniformValues(Map<String, IUniformValue> uniformValues);

	void drawGeometry(IGeometry geometry);

	Dimension getScreenDimension();

}

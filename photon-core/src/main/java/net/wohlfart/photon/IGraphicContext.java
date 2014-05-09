package net.wohlfart.photon;

import java.util.Collection;

import net.wohlfart.photon.render.IFrameBuffer;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.shader.IShaderProgram.IShaderProgramIdentifier;
import net.wohlfart.photon.shader.IUniformValue;
import net.wohlfart.photon.tools.Perspective;

public interface IGraphicContext {

	void setFrameBuffer(IFrameBuffer frameBuffer);

	void setRenderConfig(IShaderProgramIdentifier shaderIdent, IRenderConfig<RenderConfigImpl> newConfig);

	void addUniformValues(Collection<IUniformValue> uniformValues);

	void drawGeometry(IGeometry geometry);

	Perspective getPerspective();

}

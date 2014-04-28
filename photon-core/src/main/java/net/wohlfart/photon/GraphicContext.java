package net.wohlfart.photon;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;

import net.wohlfart.photon.render.IFrameBuffer;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.resources.ResourceManager;
import net.wohlfart.photon.shader.IShaderProgram;
import net.wohlfart.photon.shader.IShaderProgram.IShaderProgramIdentifier;
import net.wohlfart.photon.shader.IUniformValue;
import net.wohlfart.photon.shader.ShaderProgram;
import net.wohlfart.photon.tools.Dimension;
import net.wohlfart.photon.tools.Perspective;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * facade for the OpenGL context
 *
 * - need to be initialized with the drawable from the GLEventListener callback (init/display/reshape)
 *
 * see: https://jogamp.org/wiki/index.php/Maven_And_Android
 *
 * @author Michael Wohlfart
 */
public class GraphicContext implements IGraphicContext {
	protected static final Logger LOGGER = LoggerFactory.getLogger(GraphicContext.class);

	private final Perspective perspective = new Perspective();

	private final Map<String, IUniformValue> uniformValues = new HashMap<String, IUniformValue>(); // contains uniforms and textures

	private RenderConfigImpl currentConfig = RenderConfigImpl.NULL_CONFIG;

	private IShaderProgram currentShader = ShaderProgram.NULL_SHADER;

	private GL2ES2 gl;


	// store the current context, clear color and depth buffers and reset the shader so the new OpenGL context
	// will be provided to the shader, called once per frame at the beginning of the render run
	public IGraphicContext init(GLAutoDrawable drawable) {
		assert drawable != null : "drawable is null";
		assert drawable.getGL() != null : "drawable.gl is null";
		assert drawable.getGL().getGL2ES2() != null : "drawable.gl.gl2 is null";

		gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);
		currentShader = ShaderProgram.NULL_SHADER;
		return this;
	}


	// setup shader and state for the next render run
	// shader and state are not changed in they have already been set in the previous run
	// this also calls the bind method with the current OpenGl context on the shader if the shader is new
	// this is the only way to let the shader know the current OpenGL context
	@Override
	public void setRenderConfig(IShaderProgramIdentifier newShaderId, IRenderConfig<RenderConfigImpl> newConfig) {
		IShaderProgram newShader = ResourceManager.loadResource(IShaderProgram.class, newShaderId);
		if (!currentConfig.equals(newConfig)) {
			// no need to update the state if it didn't change
			currentConfig = newConfig.updateValues(gl, currentConfig);
		}
		if (!currentShader.equals(newShader)) {
			currentShader.unbind();
			currentShader = newShader;
			currentShader.bind(gl);
		}
	}

	// configure the shader uniforms and textures
	@Override
	public void setUniformValues(Map<String, IUniformValue> newUniformValues) {
		uniformValues.putAll(newUniformValues);
		currentShader.useUniforms(uniformValues);
	}

	@Override
	public void setFrameBuffer(IFrameBuffer frameBuffer) {

		if (frameBuffer == null) {
			// unbind
			gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
			return;
		}

		int fboHandle = frameBuffer.getHandle();
		if (fboHandle < 0) {
			frameBuffer.setup(gl);
			fboHandle = frameBuffer.getHandle();
		}
		assert (fboHandle >= 0);

        // switch to rendering on FBO
		gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, fboHandle);
		final Dimension d = frameBuffer.getDimension();
		gl.glViewport (0, 0, d.getWidthi(), d.getHeighti());
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);
	}

	@Override
	public void drawGeometry(IGeometry geometry) {
		geometry.draw(currentShader, gl);
	}

	@Override
	public Perspective getPerspective() {
		return perspective;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, IUniformValue> entry : uniformValues.entrySet()) {
			builder.append(entry.getKey() + ":" + entry.getValue() + " ");
		}
		return builder.toString();
	}

}

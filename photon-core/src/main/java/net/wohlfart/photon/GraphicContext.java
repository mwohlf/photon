package net.wohlfart.photon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
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
 * shader examples: https://code.google.com/p/sravan-work/source/browse/#svn%2Ftrunk%2FOpenGL%20ES2%2Fes2LightingPerVertex%2Fsrc%2Fes2
 * @author Michael Wohlfart
 */
public class GraphicContext implements IGraphicContext {
	protected static final Logger LOGGER = LoggerFactory.getLogger(GraphicContext.class);

	private final Perspective perspective = new Perspective();

	private final Map<String, IUniformValue> uniformValues = new HashMap<String, IUniformValue>(); // contains uniforms and textures

	private RenderConfigImpl currentConfig = RenderConfigImpl.NULL_CONFIG;

	private IShaderProgram currentShader = IShaderProgram.NULL_SHADER;

	// see: http://forum.jogamp.org/What-profile-to-choose-td3575514.html
	private GL2ES2 gl;


	// see: https://code.google.com/p/jmonkeyengine/source/browse/branches/experimental/engine/src/jogl/com/jme3/renderer/jogl/JoglRenderer.java?spec=svn11058&r=11058
	// for startup tests
	// store the current context, clear color and depth buffers and reset the shader so the new OpenGL context
	// will be provided to the shader, called once per frame at the beginning of the render run
	public IGraphicContext init(GLAutoDrawable drawable) {
		assert drawable != null : "drawable is null";
		assert drawable.getGL() != null : "drawable.gl is null";
		assert drawable.getGL().getGL2ES2() != null : "drawable.gl.gl2es2 is null";

		gl = drawable.getGL().getGL2ES2();
		checkError();

		// throws error on dispose with  -Djogl.debug.DebugGL, moved to the root node
		// gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);
		currentShader = ShaderProgram.NULL_SHADER;
		return this;
	}

	// setup shader and state for the next render run
	// shader and state are not changed if they have already been set in the previous run
	// this also calls the bind method with the current OpenGl context on the shader if the shader is new
	// this is the only way to let the shader know the current OpenGL context
	@Override
	public void setRenderConfig(IShaderProgramIdentifier newShaderIdent, IRenderConfig<RenderConfigImpl> newConfig) {
		checkError();

		IShaderProgram newShader = ResourceManager.loadResource(IShaderProgram.class, newShaderIdent);
		// no need to update the state if it didn't change
		if (!currentConfig.equals(newConfig)) {
			currentConfig = newConfig.updateValues(gl, currentConfig);
			checkError();
		}
		if (!currentShader.equals(newShader)) {
			checkError();
			LOGGER.debug("switching shaders '{}' -> '{}'", currentShader.getId(), newShader.getId());
			currentShader.unbind();
			checkError();
			currentShader = newShader;
			currentShader.bind(gl);
			checkError();
		} else {
			LOGGER.debug("not switching shaders, shader id is still the same '{}'", currentShader.getId());
		}
		currentShader.reset();
	}

	// configure the shader's uniforms and textures
	@Override
	public void addUniformValues(Collection<IUniformValue> newUniformValues) {
		checkError();

		assert newUniformValues!= null : "uniforma values must not be null, use an empty collection instead";
		for (IUniformValue uniformValue : newUniformValues) {
			uniformValues.put(uniformValue.getKey(), uniformValue);
		}
		currentShader.useUniforms(uniformValues.values());
	}

	// set or un-set a framebuffer as rendering target
	@Override
	public void setFrameBuffer(IFrameBuffer frameBuffer) {
		checkError();

		if (frameBuffer == null) {
			// unbind
			gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
			final Dimension d = perspective.getScreenDimension();
			gl.glViewport (0, 0, (int)d.getWidth(), (int)d.getHeight());
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
		final Dimension bufferDim = frameBuffer.getBufferDimension();
		gl.glViewport (0, 0, (int)bufferDim.getWidth(), (int)bufferDim.getHeight());
		// gives us an error when running with GL debug
		// gl.glScissor(0, 0, (int)d.getWidth(), (int)d.getHeight());
		// gl.glEnable(GL2.GL_SCISSOR_TEST);
		// gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		//gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);
		// gl.glDisable(GL2.GL_SCISSOR_TEST);
	}

	@Override
	public void drawGeometry(IGeometry geometry) {
		checkError();
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
			builder.append(" " + entry);
		}
		builder.append(readGlInfo());
		builder.append(readShaderInfo());
		return builder.toString();
	}

	// TODO: print this and more info somewhere at startup instead of hiding here
	private String readGlInfo() {
		checkError();
		StringBuilder builder = new StringBuilder();
		float[] pointSizeRange = new float[2];
		gl.glGetFloatv(GL.GL_ALIASED_POINT_SIZE_RANGE, pointSizeRange, 0);
		builder.append("GL.GL_ALIASED_POINT_SIZE_RANGE: '" + pointSizeRange[0] + "' - '" + pointSizeRange[1] + "'");
		return builder.toString();
	}

	private String readShaderInfo() {
		checkError();
		StringBuilder builder = new StringBuilder();
		builder.append(" shader: '" + currentShader.toString() + "'");
		return builder.toString();
	}

	private void checkError() {
		int error = gl.glGetError();
		if (error != GL2ES2.GL_NO_ERROR) {// @formatter:off
			throw new IllegalStateException("error validating shader, error code is: " + error + "\n");
		}
	}

}

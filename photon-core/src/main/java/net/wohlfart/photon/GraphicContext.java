package net.wohlfart.photon;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.media.nativewindow.util.Dimension;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import net.wohlfart.photon.render.IFrameBuffer;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.resources.ResourceManager;
import net.wohlfart.photon.shader.IShaderProgram;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.ShaderProgram;
import net.wohlfart.photon.shader.UniformHandle.IUniformValue;
import net.wohlfart.photon.texture.ITexture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.common.nio.Buffers;


/**
 * facade for the OpenGL context
 *
 * - need to be initialized with the drawable from the GLEventListener callback (init/display/reshape)
 * -
 *
 *
 *  FIXME: use identifier for shader and texture instead of the implementaions to delay the
 *         load until the call to the render method and also to have a resource manager available
 *
 * @author Michael Wohlfart
 */
public class GraphicContext implements IGraphicContext {
	protected static final Logger LOGGER = LoggerFactory.getLogger(GraphicContext.class);

	// also contains the texture indices
	private final Map<String, IUniformValue> uniformValues = new HashMap<>();
	private final Map<String, ITexture> textures = new HashMap<>();


	private RenderConfigImpl currentConfig = RenderConfigImpl.NULL_CONFIG;
	private IShaderProgram currentShader = ShaderProgram.NULL_SHADER;

	private GL2 gl;

	private final Dimension dim = new Dimension();

	// store the current context, clear color and depth buffers and reset the shader so the new OpenGL context
	// will be provided to the shader, called once per frame at the beginning
	public IGraphicContext init(GLAutoDrawable drawable) {
		assert drawable != null : "drawable is null";
		assert drawable.getGL() != null : "drawable.gl is null";
		assert drawable.getGL().getGL2() != null : "drawable.gl.gl2 is null";

		gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);
		currentShader = ShaderProgram.NULL_SHADER;
		dim.setHeight(drawable.getHeight());
		dim.setWidth(drawable.getWidth());
		return this;
	}


	// setup shader and state for the next render run
	// shader and state are not changed in they have already been set in the previous run
	// this also calls the bind method with the current OpenGl context on the shader if the shader is new
	// this is the only way to let the shader know the current OpenGL context
	@Override
	public void setRenderConfig(ShaderIdentifier newShaderId, IRenderConfig<RenderConfigImpl> newConfig) {
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
            gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
		} else {
			int fboHandle = frameBuffer.getHandle();
			if (fboHandle < 0) {
				frameBuffer.setup(gl);
				fboHandle = frameBuffer.getHandle();
			}
            gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, fboHandle);
            gl.glViewport(0, 0, dim.getWidth(), dim.getHeight());
		}
	}


	@Override
	public void drawGeometry(IGeometry geometry) {

		int vaoHandle = geometry.getHandle();

		if (vaoHandle != -1) {
			gl.glBindVertexArray(vaoHandle);
		} else {
			int[] vaoID = new int[1];
			gl.glGenVertexArrays(1, vaoID, 0);  // FIXME: can we move this into geometry?
			vaoHandle = vaoID[0];

			gl.glBindVertexArray(vaoHandle);
			geometry.setHandle(vaoHandle);

			createAndBindVboHandle(geometry);
			currentShader.useAttributes(geometry.getVertexFormat());

			if (geometry.isIndexed()) {
				// render with an index buffer
				createAndBindIdxBufferHandle(geometry);
			}
		}

		final int primitiveType = getPrimitiveType(geometry.getStreamFormat());
		if (geometry.isIndexed()) {
			gl.glDrawElements( // see: http://www.opengl.org/wiki/GlDrawElements
					primitiveType, // mode: primitive type see: http://www.opengl.org/wiki/Primitive
					geometry.getIndicesCount(), // indicesCount
					getIndexElemSize(geometry), // indexElemSize
					0); // indexOffset
		} else {
			// render plain vertices without indices
			gl.glDrawArrays(primitiveType, // mode: primitive type see: http://www.opengl.org/wiki/Primitive
					0, geometry.getVerticesCount());
		}

		// unbind
		gl.glBindVertexArray(0);
	}

	@Override
	public Dimension getDimension() {
		return dim;
	}


	// keep the OpenGL stuff inside this class
	private int getPrimitiveType(IGeometry.StreamFormat streamFormat) {
		switch (streamFormat) {
		case LINES:
			return GL2.GL_LINES;
		case LINE_STRIP:
			return GL2.GL_LINE_STRIP;
		case LINE_LOOP:
			return GL2.GL_LINE_LOOP;
		case TRIANGLES:
			return GL2.GL_TRIANGLES;
		default:
			throw new IllegalArgumentException("unknown stream format: " + streamFormat);
		}
	}

	private int createAndBindVboHandle(IGeometry geometry) {
		final FloatBuffer verticesBuffer = geometry.createVertexFloatBuffer();
		final long size = verticesBuffer.capacity();
		int handle = createAndBindBuffer(GL2.GL_ARRAY_BUFFER);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, size  * Buffers.SIZEOF_FLOAT, verticesBuffer, GL2.GL_STATIC_DRAW);
		return handle;
	}

	// move to the geometry class
	// see: http://stackoverflow.com/questions/6172308/opengl-java-vbo
	private int createAndBindIdxBufferHandle(IGeometry geometry) {
		int indicesCount = geometry.getIndicesCount();
		final int idxBufferHandle = createAndBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER);
		if (indicesCount > Short.MAX_VALUE) {
			final IntBuffer indicesBuffer = geometry.createIndexIntBuffer();
			gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.capacity() * Buffers.SIZEOF_INT, indicesBuffer, GL2.GL_STATIC_DRAW);
			return idxBufferHandle;
		} else if (indicesCount > Byte.MAX_VALUE) {
			final ShortBuffer indicesBuffer = geometry.createIndexShortBuffer();
			gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.capacity() * Buffers.SIZEOF_SHORT, indicesBuffer, GL2.GL_STATIC_DRAW);
			return idxBufferHandle;
		} else {
			final ByteBuffer indicesBuffer = geometry.createIndexByteBuffer();
			gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.capacity() * Buffers.SIZEOF_BYTE, indicesBuffer, GL2.GL_STATIC_DRAW);
			return idxBufferHandle;
		}
	}

	private int createAndBindBuffer(int type) {
		int[] handle = new int[1];
		gl.glGenBuffers(1, handle, 0);
		gl.glBindBuffer(type, handle[0]);
		return handle[0];
	}

	// FIXME: move this method to the geometry class
	private int getIndexElemSize(IGeometry geometry) {
		int indicesCount = geometry.getIndicesCount();
		if (indicesCount > Short.MAX_VALUE) {
			return GL2.GL_UNSIGNED_INT;
		} else if (indicesCount > Byte.MAX_VALUE) {
			return GL2.GL_UNSIGNED_SHORT;
		} else {
			return GL2.GL_UNSIGNED_BYTE;
		}
	}

}

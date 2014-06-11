package net.wohlfart.photon.shader;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2ES2;

import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.texture.ITexture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.common.nio.Buffers;

/**
 * DefaultShaderProgram class
 *
 * see: https://github.com/mattdesl/lwjgl-basics/blob/master/src/mdesl/graphics/glutils/ShaderProgram.java
 *
 */
public class ShaderProgram implements IShaderProgram {
	protected static final Logger LOGGER = LoggerFactory.getLogger(ShaderProgram.class);

	private int shaderId = -1;

	private int vertexShaderId = -1;
	private int fragmentShaderId = -1;

	private final String vertexShaderCode;
	private final String fragmentShaderCode;

	// mapping the uniform name to value and location
	private final Map<String, IUniformValue> uniformValues = new HashMap<String, IUniformValue>();
	private final Map<String, Integer> uniformLocations = new HashMap<String, Integer>();

	private final Map<String, AttributeHandle> attributeHandles = new HashMap<String, AttributeHandle>();

	// the current GLContext this is only valid for the current render run
	// and needs to be refreshed on each bind() call
	private GL2ES2 gl;

	private int currentTextureSlot;
	private int currentLightSlot;

	public ShaderProgram(String vertexShaderCode, String fragmentShaderCode) {
		this.vertexShaderCode = vertexShaderCode;
		this.fragmentShaderCode = fragmentShaderCode;
	}

	@Override
	public int getId() {
		return shaderId;
	}

	@Override
	public GL2ES2 getGl() {
		return gl;
	}

	@Override
	public int nextTextureSlot() {
		assert ITexture.TEXTURE_SLOTS.length > currentTextureSlot: "running out of texture slots";
		currentTextureSlot += 1;
		LOGGER.debug("returning next texture slot which is '{}' ", currentTextureSlot);
		return currentTextureSlot;  // TODO: return -1 when we are out of texture slots
	}

	@Override
	public int nextLightSlot() {
		assert ShaderParser.MAX_VERTEX_LIGHT_COUNT_VALUE > currentLightSlot: "running out of texture slots";
		currentLightSlot += 1;
		LOGGER.debug("returning next light slot which is '{}' ", currentTextureSlot);
		return currentLightSlot;  // TODO: return -1 when we are out of texture slots
	}

	@Override
	public void reset() {
		LOGGER.debug("resetting texture slot from '{}' to '-1' ins shader '{}'", currentTextureSlot, shaderId);
		this.currentTextureSlot = -1; // resetting texture slot count
		this.currentLightSlot = -1;
	}

	@Override
	public void bind(GL2ES2 gl) {
		if (shaderId == -1) {
			LOGGER.debug("setup the shader since programId is -1");
			setup(gl);
		}
		LOGGER.debug("binding shaderProgramId '{}' ", shaderId);
		gl.glUseProgram(shaderId);
		this.gl = gl;
	}

	// delayed since the OpenGL context needs to be up in order for this to work
	private void setup(GL2ES2 gl) {
		checkError(gl);

		vertexShaderId = loadShader(gl, vertexShaderCode, GL2ES2.GL_VERTEX_SHADER);
		fragmentShaderId = loadShader(gl, fragmentShaderCode, GL2ES2.GL_FRAGMENT_SHADER);
		linkAndValidate(gl, vertexShaderId, fragmentShaderId);

		LOGGER.info("shader validated, shader id was '{}', now checking uniforms and attributes", shaderId);
		findUniforms(gl);
		findAttributes(gl);
	}


	@Override
	public void unbind() {
		gl.glUseProgram(0);
	}

	@Override
	public void dispose() {
		unlink(vertexShaderId, fragmentShaderId);
		gl.glDeleteProgram(shaderId);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [vertexShaderId=" + vertexShaderId
				+ ", fragmentShaderId=" + fragmentShaderId
				+ ", programId=" + shaderId + "]";
	}

	@Override
	public void useAttributes(VertexFormat vertexFormat) {
		assert gl != null : "gl need to be configured by calling the bind method";
		int attributeSize;
		int stride = vertexFormat.getTotalSize() * Buffers.SIZEOF_FLOAT;
		int offset = 0;

		attributeSize = vertexFormat.positionSize();
		setupAttribute(gl, ShaderParser.VERTEX_POSITION, attributeSize, stride, offset);
		offset += (attributeSize * Buffers.SIZEOF_FLOAT);

		attributeSize = vertexFormat.colorSize();
		setupAttribute(gl, ShaderParser.VERTEX_COLOR, attributeSize, stride, offset);
		offset += (attributeSize * Buffers.SIZEOF_FLOAT);

		attributeSize = vertexFormat.normalSize();
		setupAttribute(gl, ShaderParser.VERTEX_NORMAL, attributeSize, stride, offset);
		offset += (attributeSize * Buffers.SIZEOF_FLOAT);

		attributeSize = vertexFormat.textureSize();
		setupAttribute(gl, ShaderParser.VERTEX_TEXTURE, attributeSize, stride, offset);
		// offset += (attributeSize * Buffers.SIZEOF_FLOAT);
	}

	@Override
	public void useUniforms(Collection<IUniformValue> uniformValues) {
		for (IUniformValue uniformValue : uniformValues) {
			uniformValue.accept(this);
		}
	}

	@Override
	public Integer getUniformLocation(String name) {
		return uniformLocations.get(name);
	}

	protected void unlink(int... handles) {
		gl.glUseProgram(0);
		for (final int handle : handles) {
			gl.glDetachShader(shaderId, handle);
		}
		for (final int handle : handles) {
			gl.glDeleteShader(handle);
		}
	}

	private int loadShader(GL2ES2 gl, final String code, int shaderType) {
		LOGGER.debug("loading shader, type is '{}'", shaderType);
		checkError(gl);

		int shader = gl.glCreateShader(shaderType);
		if (shader == 0) {
			throw new ShaderException("glCreateShader returned 0");
		}
		gl.glShaderSource(shader, 1, new String[] { code }, (int[]) null, 0);
		gl.glCompileShader(shader);
		checkCompileStatus(gl, shader); // throws illegal state exception
		LOGGER.info("shader compiled, shader id was '{}', type is '{}'", shader, shaderType);
		return shader;
	}

	private void checkCompileStatus(GL2ES2 gl, int shader) {
		final int[] compiled = new int[1];
		gl.glGetShaderiv(shader, GL2ES2.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] == 0) {
			final int[] logLength = new int[1];
			gl.glGetShaderiv(shader, GL2ES2.GL_INFO_LOG_LENGTH, logLength, 0);

			final byte[] log = new byte[logLength[0]];
			gl.glGetShaderInfoLog(shader, logLength[0], (int[])null, 0, log, 0);

			throw new IllegalStateException(new String(log));
		}
	}

	// attach, link and validate the shaders into a shader program
	private void linkAndValidate(GL2ES2 gl, int... handles) {
		checkError(gl);
		shaderId = gl.glCreateProgram();
		for (final int handle : handles) {
			gl.glAttachShader(shaderId, handle);
		}
		gl.glLinkProgram(shaderId);
		gl.glValidateProgram(shaderId);
		checkError(gl);
	}

	private void checkError(GL2ES2 gl) {
		int error = gl.glGetError();
		if (error != GL2ES2.GL_NO_ERROR) {// @formatter:off
			final String shaderInfoLog = getShaderInfoLog(gl, shaderId);
			throw new ShaderException("" + "error validating shader, error code is: " + error
					+ " error string is '" + shaderInfoLog + "' \n"
					+ " programmId is '" + shaderId + "' \n");
		}
	}

	private String getShaderInfoLog(GL2ES2 gl, int shaderId) {
        int[] infoLogLength=new int[1];
        gl.glGetShaderiv(shaderId, GL2ES2.GL_INFO_LOG_LENGTH, infoLogLength, 0);

        if(infoLogLength[0]==0) {
            return "(no info log)";
        }
        int[] charsWritten=new int[1];
        byte[] infoLogBytes = new byte[infoLogLength[0]];
        gl.glGetShaderInfoLog(shaderId, infoLogLength[0], charsWritten, 0, infoLogBytes, 0);

        return new String(infoLogBytes, 0, charsWritten[0]);
    }

	// see: http://www.guyford.co.uk/showpage.php?id=50&page=How_to_setup_and_load_GLSL_Shaders_in_JOGL_2.0
	// see: http://jogamp.org/wiki/index.php/How_to_write_cross_GLProfile_compatible_shader_using_JOGL
	private void findUniforms(GL2ES2 gl) {

		final int[] iBuff = new int[1];

		gl.glGetProgramiv(shaderId, GL2ES2.GL_ACTIVE_UNIFORMS, iBuff, 0);
		final int len = iBuff[0];

		gl.glGetProgramiv(shaderId, GL2ES2.GL_ACTIVE_UNIFORM_MAX_LENGTH, iBuff, 0);
		final int strLen = iBuff[0];

		final byte[] nameBuffer = new byte[strLen];
		final int[] sizeBuffer = new int[1];
		final int[] typeBuffer = new int[1];
		final int[] nameLenBuffer = new int[1];
		for (int i = 0; i < len; ++i) {
			gl.glGetActiveUniform(shaderId, i, strLen, nameLenBuffer, 0, sizeBuffer, 0, typeBuffer, 0, nameBuffer, 0);
			final String name = new String(Arrays.copyOfRange(nameBuffer, 0, nameLenBuffer[0]));
			final int location = gl.glGetUniformLocation(shaderId, name);
			uniformValues.put(name, IUniformValue.UNIFORM_NULL_VALUE);
			uniformLocations.put(name, location);
			LOGGER.info("found uniform name: '{}'", name);
		}
	}

	private void findAttributes(GL2ES2 gl2) {
		// see: http://forum.jogamp.org/getActiveAttrib-name-offset-equals-or-exceeds-array-length-td4028207.html

		final int[] iBuff = new int[1];
		gl2.glGetProgramiv(shaderId, GL2ES2.GL_ACTIVE_ATTRIBUTES, iBuff, 0);
		final int len = iBuff[0];

		gl2.glGetProgramiv(shaderId, GL2ES2.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH, iBuff, 0);
		final int strLen = iBuff[0];

		final byte[] nameBuffer = new byte[strLen];
		final int[] sizeBuffer = new int[1];
		final int[] typeBuffer = new int[1];
		final int[] nameLenBuffer = new int[1];
		for (int i = 0; i < len; ++i) {
			gl2.glGetActiveAttrib(shaderId, i, strLen, nameLenBuffer, 0, sizeBuffer, 0, typeBuffer, 0, nameBuffer, 0);
			final String name = new String(Arrays.copyOfRange(nameBuffer, 0, nameLenBuffer[0]));
			final int location = gl2.glGetAttribLocation(shaderId, name);
			final AttributeHandle handle = new AttributeHandle(this.shaderId, name, sizeBuffer[0], typeBuffer[0], location);
			attributeHandles.put(name, handle);
			LOGGER.info("created attribute handle: '{}'", handle);
		}
	}

	private void setupAttribute(GL2ES2 gl, String attributeName, int attributeSize, int stride, int offset) {
		AttributeHandle currentHandle = attributeHandles.get(attributeName);
		if (attributeSize > 0) {
			if (currentHandle != null)  {
				if (attributeSize == currentHandle.getAttributeSize()) {
					currentHandle.enable(gl, attributeSize, stride, offset);
				} else {
					LOGGER.info("attribute '{}' has different size in shader '{}' ({} != {})",
							new Object[] {attributeName, this, attributeSize, currentHandle.getAttributeSize()});
				}
			} else {
				LOGGER.info("attribute '{}' not found in shader '{}', available attribute names are {}",
						attributeName, this, attributeHandles.keySet());
			}
		} else {
			if (currentHandle != null)  {
				currentHandle.disable(gl);
			}
		}
	}

}

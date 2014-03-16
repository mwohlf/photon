package net.wohlfart.photon.shader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.media.opengl.GL2;

import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.shader.UniformHandle.IUniformValue;
import net.wohlfart.photon.texture.ITexture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.common.nio.Buffers;

/**
 * DefaultShaderProgram class
 *
 * see: https://github.com/mattdesl/lwjgl-basics/blob/master/src/mdesl/graphics/glutils/ShaderProgram.java
 *
 * TODO: use ShaderUtil to check for errors
 */
public class ShaderProgram implements IShaderProgram {
	protected static final Logger LOGGER = LoggerFactory.getLogger(ShaderProgram.class);

	public static final IShaderProgram NULL_SHADER = new IShaderProgram.NullShader();

	private final String vertexShaderCode;
	private final String fragmentShaderCode;

	private int vertexShaderId = -1;
	private int fragmentShaderId = -1;

	private int programId = -1;

	private final Map<String, UniformHandle> uniforms = new HashMap<>();
	private final Map<String, AttributeHandle> attributes = new HashMap<>();

	// the current GLContext this is only valid for the current render run
	// and needs to be refreshed on each bind() call
	private GL2 gl;

	// texture names, need to skip when setting uniforms
	private final Set<String> textureNames = new HashSet<String>();

	public ShaderProgram(String vertexShaderCode, String fragmentShaderCode) {
		this.vertexShaderCode = vertexShaderCode;
		this.fragmentShaderCode = fragmentShaderCode;
	}


	@Override
	public void bind(GL2 gl) {
		if (programId == -1) {
			setup(gl);
		}
		LOGGER.debug("binding programId '{}' ", programId);
		gl.glUseProgram(programId);
		this.gl = gl;
	}

	// delayed since the OpenGL context needs to be up in order for this to work
	private void setup(GL2 gl2) {
		vertexShaderId = loadShader(gl2, vertexShaderCode, GL2.GL_VERTEX_SHADER);
		fragmentShaderId = loadShader(gl2, fragmentShaderCode, GL2.GL_FRAGMENT_SHADER);
		linkAndValidate(gl2, vertexShaderId, fragmentShaderId);

		findUniforms(gl2);
		findAttributes(gl2);
	}


	@Override
	public void unbind() {
		//       GL20.glUseProgram(0);
	}

	@Override
	public void dispose() {
		unlink(vertexShaderId, fragmentShaderId);
		//       GL20.glDeleteProgram(programId);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [vertexShaderId=" + vertexShaderId
				+ ", fragmentShaderId=" + fragmentShaderId
				+ ", programId=" + programId + "]";
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
	public void useTextures(Map<String, ITexture> textures) {
		textureNames.clear();
		textureNames.addAll(textures.keySet());
		// see: http://stackoverflow.com/questions/16497794/sending-two-textures-to-glsl-shader
		int slot = 0;
		for (Map.Entry<String, ITexture> entry : textures.entrySet()) {
			final String key = entry.getKey();
			final ITexture texture = entry.getValue();
			// for each texture we activate a texture slot
			gl.glActiveTexture(ITexture.TEXTURE_SLOTS[slot]);
			// bind the texture to the current OpenGL context
			gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getHandle(gl));
			setTexture(key, slot);
			slot++; // check if we are out of texture slots
			if (slot > ITexture.TEXTURE_SLOTS.length) {
				break;
			}
		}
	}

	@Override
	public void useUniforms(Map<String, IUniformValue> uniformValues) {
		for (String uniformName : getUniformHandleNames()) {
			// check if the uniform slot is a defined texture, if so we can skip that since
			// we already did set the texture index on that uniform
			if (!textureNames.contains(uniformName)) {
				IUniformValue uniformValue = uniformValues.get(uniformName);
				setUniform(gl, uniformName, uniformValue);
			}
		}
	}


	protected void unlink(int... handles) {
		gl.glUseProgram(0);
		for (final int handle : handles) {
			gl.glDetachShader(programId, handle);
		}
		for (final int handle : handles) {
			gl.glDeleteShader(handle);
		}
	}

	private UniformHandle getUniformHandle(String string) {
		return uniforms.get(string);
	};

	private Set<String> getUniformHandleNames() {
		return uniforms.keySet();
	}

	private AttributeHandle getVertexAttributeHandle(String string) {
		return attributes.get(string);
	}

	private Set<String> getVertexAttributeHandleNames() {
		return attributes.keySet();
	}

	private void setTexture(String uniformName, int textureSlot) {
		UniformHandle currentHandle = getUniformHandle(uniformName);
		if (currentHandle == null) {
			LOGGER.error("uniform for texture slot '{}' can't be found in shader {}, skipping this texture", uniformName, this);
			return;
		}
		currentHandle.setTextureIndex(gl, textureSlot);
	}

	private void setUniform(GL2 gl2, String uniformName, IUniformValue uniformValue) {
		UniformHandle currentHandle = getUniformHandle(uniformName);
		if (currentHandle == null) {
			LOGGER.error("uniform for '{}' can't be found in shader {}, skipping this uniform", uniformName, this);
			return;
		}

		if (uniformValue == null) {
			LOGGER.error("uniform or texture value for '{}' can't be found", uniformName);
			return;
		}

		uniformValue.accept(gl2, currentHandle);
	}

	private int loadShader(GL2 gl2, final String code, int shaderType) {
		LOGGER.debug("loading shader from '{}' type is '{}'", code, shaderType);
		int shader = 0;


		shader = gl2.glCreateShader(shaderType);
		if (shader == 0) {
			throw new ShaderException("glCreateShader returned 0");
		}
		gl2.glShaderSource(shader, 1, new String[] { code }, (int[]) null, 0);
		gl2.glCompileShader(shader);

		// TODO: check compile status
		return shader;
	}

	// attach, link and validate the shaders into a shader program
	private void linkAndValidate(GL2 gl2, int... handles) {
		int error = gl2.glGetError();
		if (error != GL2.GL_NO_ERROR) {// @formatter:off
			throw new ShaderException("" + "error before linking shader, error string is '" + "" + "' \n" + "programmId is '"
					+ programId + "' \n" + "handles are: " + Arrays.toString(handles)); // @formatter:on
		}
		programId = gl2.glCreateProgram();
		for (final int handle : handles) {
			gl2.glAttachShader(programId, handle);
		}
		gl2.glLinkProgram(programId);
		gl2.glValidateProgram(programId);
		error = gl2.glGetError();
		if (error != GL2.GL_NO_ERROR) {// @formatter:off
			throw new ShaderException("" + "error validating shader, error string is '" + "" + "' \n" + "programmId is '"
					+ programId + "' \n" + "handles are: " + Arrays.toString(handles)); // @formatter:on
		}
	}

	// see: http://www.guyford.co.uk/showpage.php?id=50&page=How_to_setup_and_load_GLSL_Shaders_in_JOGL_2.0
	// see: http://jogamp.org/wiki/index.php/How_to_write_cross_GLProfile_compatible_shader_using_JOGL
	private void findUniforms(GL2 gl2) {
		/*
        int len = gl2.glGetProgramiv(program, pname, params); //.glGetProgrami(programId, GL2.GL_ACTIVE_UNIFORMS);
        int strLen = GL20.glGetProgrami(programId, GL2.GL_ACTIVE_UNIFORM_MAX_LENGTH);
		 */

		final int[] iBuff = new int[1];
		//int len = GL20.glGetProgrami(programId, GL20.GL_ACTIVE_ATTRIBUTES);
		gl2.glGetProgramiv(programId, GL2.GL_ACTIVE_UNIFORMS, iBuff, 0);
		final int len = iBuff[0];

		//int strLen = gl2.glGetProgramiv(programId, GL2.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);
		gl2.glGetProgramiv(programId, GL2.GL_ACTIVE_UNIFORM_MAX_LENGTH, iBuff, 0);
		final int strLen = iBuff[0];

		final byte[] nameBuffer = new byte[strLen];
		final int[] sizeBuffer = new int[1];
		final int[] typeBuffer = new int[1];
		final int[] nameLenBuffer = new int[1];
		for (int i = 0; i < len; ++i) {
			gl2.glGetActiveUniform(programId, i, strLen, nameLenBuffer, 0, sizeBuffer, 0, typeBuffer, 0, nameBuffer, 0);
			String name = new String(Arrays.copyOfRange(nameBuffer, 0, nameLenBuffer[0]));
			int location = gl2.glGetUniformLocation(programId, name);
			UniformHandle handle = new UniformHandle(this.programId, name, location);
			uniforms.put(name, handle);
			LOGGER.info("created uniform handle: " + handle);
		}
	}

	private void findAttributes(GL2 gl2) {
		// see: http://forum.jogamp.org/getActiveAttrib-name-offset-equals-or-exceeds-array-length-td4028207.html

		final int[] iBuff = new int[1];
		gl2.glGetProgramiv(programId, GL2.GL_ACTIVE_ATTRIBUTES, iBuff, 0);
		final int len = iBuff[0];

		gl2.glGetProgramiv(programId, GL2.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH, iBuff, 0);
		final int strLen = iBuff[0];

		final byte[] nameBuffer = new byte[strLen];
		final int[] sizeBuffer = new int[1];
		final int[] typeBuffer = new int[1];
		final int[] nameLenBuffer = new int[1];
		for (int i = 0; i < len; ++i) {
			gl2.glGetActiveAttrib(programId, i, strLen, nameLenBuffer, 0, sizeBuffer, 0, typeBuffer, 0, nameBuffer, 0);
			String name = new String(Arrays.copyOfRange(nameBuffer, 0, nameLenBuffer[0]));
			int location = gl2.glGetAttribLocation(programId, name);
			AttributeHandle handle = new AttributeHandle(this.programId, name, sizeBuffer[0], typeBuffer[0], location);
			attributes.put(name, handle);
			LOGGER.info("created attribute handle: " + handle);
		}
	}

	// TODO: compare shader attribute sizes with size from handler
	private void setupAttribute(GL2 gl2, String attributeName, int attributeSize, int stride, int offset) {
		AttributeHandle currentHandle = getVertexAttributeHandle(attributeName);
		if (attributeSize > 0) {
			if (currentHandle != null)  {
				if (attributeSize == currentHandle.getAttributeSize()) {
					currentHandle.enable(gl2, attributeSize, stride, offset);
				} else {
					LOGGER.info("attribute '{}' has different size in shader '{}' ({} != {})",
							new Object[] {attributeName, this, attributeSize, currentHandle.getAttributeSize()});
				}
			} else {
				LOGGER.info("attribute '{}' not found in shader '{}', available attribute names are {}",
						attributeName, this, getVertexAttributeHandleNames());
			}
		} else {
			if (currentHandle != null)  {
				currentHandle.disable(gl2);
			}
		}
	}

}

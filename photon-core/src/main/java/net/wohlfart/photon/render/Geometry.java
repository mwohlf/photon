package net.wohlfart.photon.render;

import gnu.trove.list.TFloatList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.list.array.TIntArrayList;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;

import net.wohlfart.photon.shader.IShaderProgram;

import com.jogamp.common.nio.Buffers;


public class Geometry implements IGeometry {

	protected final TFloatList vertexData;
	protected final VertexFormat vertexFormat;

	protected final TIntList indices;
	protected final StreamFormat streamFormat;

	protected int vboHandle = -1;
	protected int iboHandle = -1;


	// the last vertex position, reused for performance not part of the state
	// this is basically just a pointer to the end of the vertexData
	protected final transient Vertex currentVertex;

	public class Vertex {

		public Vertex withPosition(float... position) {
			assert (position.length == vertexFormat.positionSize());
			assert (vertexData.size() % vertexFormat.getTotalSize() == 0) : ""
			+ "the buffer size is not a multiple of the vertex size, make sure you use all vertex attributes you specified when"
			+ "setting up the vertex buffer";
			vertexData.add(position);
			return this;
		}

		public Vertex withColor(float... color) {
			assert (color.length == vertexFormat.colorSize()) : "the color elem count doesn't match what is configured in the vertex format";
			assert (vertexData.size() % vertexFormat.getTotalSize() == vertexFormat.positionSize()): ""
			+ "you need to add a position before you can add a color";
			vertexData.add(color);
			return this;
		}

		public Vertex withNormal(float... normal) {
			assert (normal.length == vertexFormat.normalSize());
			assert (vertexData.size() % vertexFormat.getTotalSize() == vertexFormat.positionSize() + vertexFormat.colorSize()) : ""
			+ "you need to add the color before you can add a normal"	;
			vertexData.add(normal);
			return this;
		}

		public Vertex withTexture(float... texture) {
			assert (texture.length == vertexFormat.textureSize()) : "the texture coord count doesn't match what is configured in the vertex format";
			assert (vertexData.size() % vertexFormat.getTotalSize() == vertexFormat.positionSize() + vertexFormat.colorSize() + vertexFormat.normalSize()) : ""
			+ "you need to add a normal before you can add a texture";
			vertexData.add(texture);
			return this;
		}
	}



	public Geometry(VertexFormat vertexFormat, StreamFormat streamFormat) {
		this.vertexFormat = vertexFormat;
		this.streamFormat = streamFormat;
		vertexData = new TFloatArrayList();
		indices = new TIntArrayList();
		currentVertex = new Vertex();
	}

	@Override
	public void draw(IShaderProgram shader, GL2ES2 gl) {

		bindVboHandle(gl);
		shader.useAttributes(vertexFormat);

		if (gl.isGL2GL3()) {
            gl.glEnable(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
        }
		if (gl.isGL2ES1()) {
            gl.glEnable(GL2ES1.GL_POINT_SPRITE);
        }

		if (indices.size() > 0) {
			// render with an index buffer
			bindIdxBufferHandle(gl);
			gl.glDrawElements( // see: http://www.opengl.org/wiki/GlDrawElements
					getPrimitiveType(streamFormat), // mode: primitive type see: http://www.opengl.org/wiki/Primitive
					indices.size(), // indicesCount
					getBestIndexElemSize(), // indexElemSize
					0); // indexOffset
		} else {
			// render plain vertices without indices
			gl.glDrawArrays(
					getPrimitiveType(streamFormat), // mode: primitive type see: http://www.opengl.org/wiki/Primitive
					0,  // offset
					getVerticesCount());
		}

	}

	private void bindVboHandle(GL2ES2 gl) {
		if (vboHandle < 0) {
			final FloatBuffer verticesBuffer = createVertexFloatBuffer();
			final long size = verticesBuffer.capacity();
			vboHandle = createBuffer(GL2.GL_ARRAY_BUFFER, gl);
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboHandle);
			gl.glBufferData(GL2.GL_ARRAY_BUFFER, size  * Buffers.SIZEOF_FLOAT, verticesBuffer, GL2.GL_STATIC_DRAW);
		} else {
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboHandle);
		}
	}

	private void bindIdxBufferHandle(GL2ES2 gl) {
		if (iboHandle < 0) {
			int indicesCount = indices.size();
			iboHandle = createBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, gl);
			gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, iboHandle);
			if (indicesCount > Short.MAX_VALUE) {
				final IntBuffer indicesBuffer = createIndexIntBuffer();
				gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.capacity() * Buffers.SIZEOF_INT, indicesBuffer, GL2.GL_STATIC_DRAW);
			} else if (indicesCount > Byte.MAX_VALUE) {
				final ShortBuffer indicesBuffer = createIndexShortBuffer();
				gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.capacity() * Buffers.SIZEOF_SHORT, indicesBuffer, GL2.GL_STATIC_DRAW);
			} else {
				final ByteBuffer indicesBuffer = createIndexByteBuffer();
				gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.capacity() * Buffers.SIZEOF_BYTE, indicesBuffer, GL2.GL_STATIC_DRAW);
			}
		} else {
			gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, iboHandle);
		}
	}

	private int createBuffer(int type, GL2ES2 gl) {
		int[] handle = new int[1];
		gl.glGenBuffers(1, handle, 0);
		return handle[0];
	}

	private int getBestIndexElemSize() {
		int indicesCount = indices.size();
		if (indicesCount > Short.MAX_VALUE) {
			return GL2.GL_UNSIGNED_INT;
		} else if (indicesCount > Byte.MAX_VALUE) {
			return GL2.GL_UNSIGNED_SHORT;
		} else {
			return GL2.GL_UNSIGNED_BYTE;
		}
	}

	private int getVerticesCount() {
		assert ((vertexData.size() % vertexFormat.getTotalSize()) == 0) : "the last vertex is not complete";
		return vertexData.size() / vertexFormat.getTotalSize();
	}

	// keep the OpenGL stuff inside this class
	private int getPrimitiveType(IGeometry.StreamFormat streamFormat) {
		switch (streamFormat) {
		case POINTS:
			return GL2.GL_POINTS;
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

	public IGeometry addLine(int i, int j) {
		assert (streamFormat.equals(StreamFormat.LINES));
		indices.add(i);
		indices.add(j);
		return this;
	}

	public IGeometry addTriangle(int a, int b, int c) {
		assert (streamFormat.equals(StreamFormat.TRIANGLES)) : ""
				+ "can't add a triangle when stream format is not StreamFormat.TRIANGLES";
		indices.add(a);
		indices.add(b);
		indices.add(c);
		return this;
	}

	public IGeometry addRectangle(int a, int b, int c, int d) {
		assert (streamFormat.equals(StreamFormat.TRIANGLES)) : ""
				+ "can't add a rectangle when stream format is not StreamFormat.TRIANGLES"
				+ "rectangles are transformed to triangles";
		indices.add(a);
		indices.add(b);
		indices.add(c);
		indices.add(a);
		indices.add(c);
		indices.add(d);
		return this;
	}

	public IGeometry transformVertices(IVertexTransform geometryTransform) {
		assert ((vertexData.size() % vertexFormat.getTotalSize()) == 0) : ""
				+ "the vertexData.size() (" + vertexData.size() + ") "
				+ "should be a multiple of vertexFormat.getTotalSize() (" + vertexFormat.getTotalSize() + ")";
		int vertexSize = vertexFormat.getTotalSize();
		int vertexCount = vertexData.size() / vertexSize;
		for (int i = 0; i < vertexCount; i++) {
			int pos = i * vertexSize;
			vertexData.set(pos, geometryTransform.execute(vertexFormat, vertexData.toArray(pos, vertexSize)));
		}
		return this;
	}

	private FloatBuffer createVertexFloatBuffer() {
		final FloatBuffer verticesBuffer = createFloatBuffer(vertexData.size());
		verticesBuffer.put(vertexData.toArray());
		verticesBuffer.flip();
		return verticesBuffer;
	}

	private IntBuffer createIndexIntBuffer() {
		final IntBuffer indicesBuffer = createIntBuffer(indices.size());
		indicesBuffer.put(indices.toArray());
		indicesBuffer.flip();
		return indicesBuffer;
	}

	private ShortBuffer createIndexShortBuffer() {
		final ShortBuffer indicesBuffer = createShortBuffer(indices.size());
		final short[] temp = new short[indices.size()];
		for (int i = 0; i < indices.size(); i++) {
			temp[i] = (short) indices.get(i);
		}
		indicesBuffer.put(temp);
		indicesBuffer.flip();
		return indicesBuffer;
	}

	public ByteBuffer createIndexByteBuffer() {
		final ByteBuffer indicesBuffer = createByteBuffer(indices.size());
		final byte[] temp = new byte[indices.size()];
		for (int i = 0; i < indices.size(); i++) {
			temp[i] = (byte) indices.get(i);
		}
		indicesBuffer.put(temp);
		indicesBuffer.flip();
		return indicesBuffer;
	}

	public Vertex addVertex() {
		return currentVertex;
	}

	protected StreamFormat getStreamFormat() {
		return streamFormat;
	}

	protected VertexFormat getVertexFormat() {
		return vertexFormat;
	}

	private ByteBuffer createByteBuffer(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
	}

	private ShortBuffer createShortBuffer(int size) {
		return createByteBuffer(size << 1).asShortBuffer();
	}

	private IntBuffer createIntBuffer(int size) {
		return createByteBuffer(size << 2).asIntBuffer();
	}

	private FloatBuffer createFloatBuffer(int size) {
		return createByteBuffer(size << 2).asFloatBuffer();
	}

}

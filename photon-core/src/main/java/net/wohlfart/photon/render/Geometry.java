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


public class Geometry implements IGeometry {

    protected final TFloatList vertexData;
    protected final VertexFormat vertexFormat;

    protected final TIntList indices;
    protected final StreamFormat streamFormat;

    protected int handle = -1;


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
            assert (color.length == vertexFormat.colorSize());
            assert (vertexData.size() % vertexFormat.getTotalSize() == vertexFormat.positionSize());
            vertexData.add(color);
            return this;
        }

        public Vertex withNormal(float... normal) {
            assert (normal.length == vertexFormat.normalSize());
            assert (vertexData.size() % vertexFormat.getTotalSize() == vertexFormat.positionSize() + vertexFormat.colorSize());
            vertexData.add(normal);
            return this;
        }

        public Vertex withTexture(float... texture) {
            assert (texture.length == vertexFormat.textureSize());
            assert (vertexData.size() % vertexFormat.getTotalSize() == vertexFormat.positionSize() + vertexFormat.colorSize() + vertexFormat.normalSize());
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
    public int getIndicesCount() {
        return indices.size();
    }

    @Override
    public boolean isIndexed() {
        return indices.size() > 0;
    }

    @Override
    public int getVerticesCount() {
        assert ((vertexData.size() % vertexFormat.getTotalSize()) == 0);
        return vertexData.size() / vertexFormat.getTotalSize();
    }

    @Override
    public StreamFormat getStreamFormat() {
        return streamFormat;
    }

    @Override
    public VertexFormat getVertexFormat() {
        return vertexFormat;
    }

    @Override
    public int getHandle() {
        return handle;
    }

    @Override
    public void setHandle(int handle) {
        this.handle = handle;
    }




    public Vertex addVertex() {
        return currentVertex;
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


    // --- the following method are only used by the renderer

    // should only be called by the renderer
    @Override
    public FloatBuffer createVertexFloatBuffer() {
        final FloatBuffer verticesBuffer = createFloatBuffer(vertexData.size());
        verticesBuffer.put(vertexData.toArray());
        verticesBuffer.flip();
        return verticesBuffer;
    }

    // should only be called by the renderer
    @Override
    public IntBuffer createIndexIntBuffer() {
        final IntBuffer indicesBuffer = createIntBuffer(indices.size());
        indicesBuffer.put(indices.toArray());
        indicesBuffer.flip();
        return indicesBuffer;
    }

    // should only be called by the renderer
    @Override
    public ShortBuffer createIndexShortBuffer() {
        final ShortBuffer indicesBuffer = createShortBuffer(indices.size());
        final short[] temp = new short[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            temp[i] = (short) indices.get(i);
        }
        indicesBuffer.put(temp);
        indicesBuffer.flip();
        return indicesBuffer;
    }

    // should only be called by the renderer
    @Override
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

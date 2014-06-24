package net.wohlfart.photon.render;

import javax.media.opengl.GL2ES2;

import net.wohlfart.photon.shader.IShaderProgram;


/**
 * see:  http://stackoverflow.com/questions/166356/what-are-some-best-practices-for-opengl-coding-esp-w-r-t-object-orientation
 *       http://www.opengl.org/wiki/Vertex_Specification#Vertex_Stream
 *       http://www.opengl.org/wiki/Primitive
 *
 * contains vertex data for a renderable object and also the vertex format for
 * an object mostly all the data of a mesh
 *   - vertices
 *   - vertex format
 *   - vertex structure
 *
 * no OpenGL specific stuff here, the final transformation into a VAO VBO is
 * done in the platform specific renderer only data and structure definitions
 * here
 *
 * - stride is the byte offset between two vertices
 * - primitive defines how to interpret a vertex stream
 *
 *
 * the renderer turns the Geometry into a Vertex Array Object, the Handle is stored once it is created
 * the main reason for this interface is to keep the enums out of the subclass
 */
public interface IGeometry {

	public static final IGeometry NULL_GEOMETRY = new IGeometry() {
		@Override
		public void draw(IShaderProgram currentShader, GL2ES2 gl) {
			// do nothing
		}
	};

    public enum VertexFormat {

        VERTEX_P2C0N0T0(2, 0, 0, 0),
        VERTEX_P3C0N0T0(3, 0, 0, 0),
        VERTEX_P4C0N0T0(4, 0, 0, 0),
        VERTEX_P2C3N0T0(2, 3, 0, 0),
        VERTEX_P3C3N0T0(3, 3, 0, 0),
        VERTEX_P4C3N0T0(4, 3, 0, 0),
        VERTEX_P2C4N0T0(2, 4, 0, 0),
        VERTEX_P3C4N0T0(3, 4, 0, 0),
        VERTEX_P4C4N0T0(4, 4, 0, 0),

        VERTEX_P2C0N3T0(2, 0, 3, 0),
        VERTEX_P3C0N3T0(3, 0, 3, 0),
        VERTEX_P4C0N3T0(4, 0, 3, 0),
        VERTEX_P2C3N3T0(2, 3, 3, 0),
        VERTEX_P3C3N3T0(3, 3, 3, 0),
        VERTEX_P4C3N3T0(4, 3, 3, 0),
        VERTEX_P2C4N3T0(2, 4, 3, 0),
        VERTEX_P3C4N3T0(3, 4, 3, 0),
        VERTEX_P4C4N3T0(4, 4, 3, 0),

        VERTEX_P2C0N0T2(2, 0, 0, 2),
        VERTEX_P3C0N0T2(3, 0, 0, 2),
        VERTEX_P4C0N0T2(4, 0, 0, 2),
        VERTEX_P2C3N0T2(2, 3, 0, 2),
        VERTEX_P3C3N0T2(3, 3, 0, 2),
        VERTEX_P4C3N0T2(4, 3, 0, 2),
        VERTEX_P2C4N0T2(2, 4, 0, 2),
        VERTEX_P3C4N0T2(3, 4, 0, 2),
        VERTEX_P4C4N0T2(4, 4, 0, 2),

        VERTEX_P2C0N3T2(2, 0, 3, 2),
        VERTEX_P3C0N3T2(3, 0, 3, 2),
        VERTEX_P4C0N3T2(4, 0, 3, 2),
        VERTEX_P2C3N3T2(2, 3, 3, 2),
        VERTEX_P3C3N3T2(3, 3, 3, 2),
        VERTEX_P4C3N3T2(4, 3, 3, 2),
        VERTEX_P2C4N3T2(2, 4, 3, 2),
        VERTEX_P3C4N3T2(3, 4, 3, 2),
        VERTEX_P4C4N3T2(4, 4, 3, 2);

        private int posSize = 0;  // size in float
        private int colSize = 0;
        private int normSize = 0;
        private int texSize = 0;

        private final int size; // count of floats

        VertexFormat(int posSize, int colSize, int normSize, int texSize) {
            this.size = posSize + colSize + normSize + texSize;
            this.posSize = posSize;
            this.colSize = colSize;
            this.normSize = normSize;
            this.texSize = texSize;
        }

        public int positionSize() {
            return posSize;
        }

        public int colorSize() {
            return colSize;
        }

        public int normalSize() {
            return normSize;
        }

        public int textureSize() {
            return texSize;
        }

        public int getTotalSize() {
            return size;
        }

    }

    public enum StreamFormat {
    	POINTS,		  // see: http://www.opengl.org/wiki/Primitive

        LINES,        // 0-1, 2-3, 4-5
        LINE_STRIP,   // 0-1-2-3-4-5
        LINE_LOOP,    // 0-1-2-3-4-5-0

        TRIANGLES,    // 0-1-2, 3-4-5, 6-7-8
    }

	void draw(IShaderProgram currentShader, GL2ES2 gl);

}

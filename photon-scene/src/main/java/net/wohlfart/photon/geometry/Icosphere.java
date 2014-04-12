package net.wohlfart.photon.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Vector3f;

import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.IGeometry;

/**
 * Icosphere class.
 * see: http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
 */
public class Icosphere extends Geometry {

    // FIXME: remove this
    private float radius = 1;

    private final float t = (1.0f + (float)Math.sqrt(5.0f)) / 2.0f;
    private final List<Vector3f> vertices = new ArrayList<Vector3f>(Arrays.<Vector3f> asList(new Vector3f[] {// @formatter:off
            new Vector3f(-1f, t, 0),
            new Vector3f(+1f, t, 0),
            new Vector3f(-1f, -t, 0),
            new Vector3f(+1f, -t, 0),
            new Vector3f(0f, -1, t),
            new Vector3f(0f, 1, t),
            new Vector3f(0f, -1, -t),
            new Vector3f(0f, 1, -t),
            new Vector3f(t, 0, -1),
            new Vector3f(t, 0, 1),
            new Vector3f(-t, 0, -1),
            new Vector3f(-t, 0, 1), }));

    private List<Integer> indices = Arrays.asList(new Integer[] {
            0, 11,
            11,  5,
            5,  0,

            0,  5,
            5,  1,
            1,  0,

            0,  1,
            1,  7,
            7,  0,

            0,  7,
            7, 10,
            10,  0,

            0, 10,
            10, 11,
            11,  0,

            1,  5,
            5,  9,
            9,  1,

            5, 11,
            11,  4,
            4,  5,

            11, 10,
            10,  2,
            2, 11,

            10,  7,
            7,   6,
            6, 10,

            7,  1,
            1,  8,
            8,  7,

            3,  9,
            9, 4,
            4,  3,

            3,  4,
            4,  2,
            2,  3,

            3,  2,
            2,  6,
            6,  3,

            3,  6,
            6, 8,
            8,  3,

            3,  8,
            8,  9,
            9,  3,

            4,  9,
            9,  5,
            5,  4,

            2,  4,
            4,  11,
            11,  2,

            6,  2,
            2,  10,
            10,  6,

            8,  6,
            6,  7,
            7,  8,

            9,  8,
            8,  1,
            1,  9,
    }); // @formatter:on



    public Icosphere(float radius, int lod) {
        super(IGeometry.VertexFormat.VERTEX_P3C0N0T0, IGeometry.StreamFormat.LINES);
        //super(IGeometry.VertexFormat.VERTEX_P3C0N0T0, IGeometry.StreamFormat.TRIANGLES);
        this.radius = radius;
        normalize();
        splitPlanes(lod);
        setupBufferData();
    }

    private void setupBufferData() {
        for (Vector3f vector : vertices) {
            currentVertex.withPosition(vector.x, vector.y, vector.z);
        }

        switch (getStreamFormat()) {
        case TRIANGLES:
            // every 6 indices build a single triangle plane
            for (int i=0; i < indices.size(); i = i +6) {
                addTriangle(
                        indices.get(i + 0),
                        indices.get(i + 2),
                        indices.get(i + 4));
            }
            break;
        case LINES:
            for (int i=0; i < indices.size(); i = i +2) {
                addLine(indices.get(i + 0),
                        indices.get(i + 1));
            }
            break;
        case LINE_LOOP:
        case LINE_STRIP:
        default:
            throw new IllegalStateException("we don't support '" + getStreamFormat()
                    + "' as stream format in " + this.getClass().getSimpleName());
        }
    }

	private void normalize() {
        for (final Vector3f vec : vertices) {
            final float l = vec.length();
            vec.scale(radius / l);
        }
    }

    private void splitPlanes(final int iterations) {
        for (int i = 0; i < iterations; i++) {
            splitPlanes();
        }
    }

    private void splitPlanes() {
        // for each side we have 4 new smaller sides now
        final Integer[] indices2 = new Integer[indices.size() * 4];
        final int indicesPerSide = 6;
        final int newIndices = indicesPerSide * 4;

        for (int i = 0; i < indices.size(); i += indicesPerSide) {

            // get the start of the lines of a side
            final Vector3f v1 = vertices.get(indices.get(i + 0)); // top
            final Vector3f v2 = vertices.get(indices.get(i + 2)); // left
            final Vector3f v3 = vertices.get(indices.get(i + 4)); // right

            // find the midpoints
            final Vector3f n1 = splitLine(v1, v2); // mid-left
            final Vector3f n2 = splitLine(v2, v3); // mid-bottom
            final Vector3f n3 = splitLine(v3, v1); // mid-right

            // to keep them on the sphere
            n1.scale(radius / n1.length());
            n2.scale(radius / n2.length());
            n3.scale(radius / n3.length());

            final int offset = vertices.size();
            vertices.add(n1); // offset + 0
            vertices.add(n2); // offset + 1
            vertices.add(n3); // offset + 2

            // top triangle
            final int j = i / indicesPerSide * newIndices;
            indices2[j + 0] = indices.get(i + 0); // top
            indices2[j + 1] = offset + 0;
            indices2[j + 2] = offset + 0; // mid-left
            indices2[j + 3] = offset + 2;
            indices2[j + 4] = offset + 2; // mid-right
            indices2[j + 5] = indices.get(i + 0);

            // left triangle
            indices2[j + 6] = offset + 0; // mid-left
            indices2[j + 7] = indices.get(i + 2);
            indices2[j + 8] = indices.get(i + 2); // left
            indices2[j + 9] = offset + 1;
            indices2[j + 10] = offset + 1; // mid-bottom
            indices2[j + 11] = offset + 0;

            // right triangle
            indices2[j + 12] = offset + 2; // mid-right
            indices2[j + 13] = offset + 1;
            indices2[j + 14] = offset + 1; // mid-bottom
            indices2[j + 15] = indices.get(i + 4);
            indices2[j + 16] = indices.get(i + 4); // right
            indices2[j + 17] = offset + 2;

            // center triangle
            indices2[j + 18] = offset + 0; // mid-left
            indices2[j + 19] = offset + 1;
            indices2[j + 20] = offset + 1; // mid-bottom
            indices2[j + 21] = offset + 2;
            indices2[j + 22] = offset + 2; // mid-right
            indices2[j + 23] = offset + 0;
        }
        indices = Arrays.asList(indices2);
    }

    private Vector3f splitLine(final Vector3f v1, final Vector3f v2) {
        return new Vector3f((v1.x + v2.x) / 2f, (v1.y + v2.y) / 2f, (v1.z + v2.z) / 2f);
    }

}

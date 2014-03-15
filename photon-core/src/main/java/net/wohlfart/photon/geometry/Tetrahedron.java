package net.wohlfart.photon.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Vector3f;

import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.VertexTransform;

// @formatter:off
/**
 *        0
 *       /|\
 *      / | \
 *     /  |  \
 *    /   3   \
 *   /  -   -  \
 *  1 --------- 2
 */
public class Tetrahedron extends Geometry { // @formatter:on 

    private float length;

    private final List<Integer> indices = Arrays.asList(new Integer[] {// @formatter:off
            0, 1,  1, 2,  2, 0,
            0, 2,  2, 3,  3, 0,
            0, 3,  3, 1,  1, 0, });  // @formatter:on


    public Tetrahedron() {
        this(5);
    }

    public Tetrahedron(float length) {
        super(IGeometry.VertexFormat.VERTEX_P3C0N0T0, IGeometry.StreamFormat.LINES);
        //super(IGeometry.VertexFormat.VERTEX_P3C0N0T0, IGeometry.StreamFormat.TRIANGLES);
        this.length = length;
        setupBufferData();
    }

    private void setupBufferData() {
        List<Vector3f> vertices = createVertices();
        for (Vector3f vector : vertices) {
            addVertex().withPosition(vector.x, vector.y, vector.z);
        }
        
        switch (getStreamFormat()) {
        case TRIANGLES:
            // every 6 indices build a single triangle
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
        
        transformVertices(VertexTransform.move(0, 0, -15));    
    }

    private List<Vector3f> createVertices() {
        final float h = + (float)Math.sqrt(2f / 3f) * length;
        final List<Vector3f> result = new ArrayList<Vector3f>(4);
        result.add(new Vector3f(0, +h / 2f, 0));
        result.add(new Vector3f(-length / 2f, -h / 2f, +h / 2f));
        result.add(new Vector3f(+length / 2f, -h / 2f, +h / 2f));
        result.add(new Vector3f(0, -h / 2f, -h / 2f));
        return result;
    }

}

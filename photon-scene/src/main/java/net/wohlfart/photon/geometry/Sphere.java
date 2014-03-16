package net.wohlfart.photon.geometry;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.tools.MathTool;
import net.wohlfart.photon.tools.VertexData;

public class Sphere extends Geometry {

    private final float radius;
    private final int lod;


    public Sphere(float radius, int lod) {
        this(radius, lod, IGeometry.VertexFormat.VERTEX_P3C0N0T0, IGeometry.StreamFormat.LINES);
    }

    public Sphere(float radius, int lod, VertexFormat vertexFormat, StreamFormat streamFormat) {
        super(vertexFormat, streamFormat);
        assert (lod >= 0);
        assert (radius >= 0);
        this.radius = radius;
        this.lod = lod;
        setupBufferData();
    }

    
    private void setupBufferData() {
        final ArrayList<VertexData> vertices = new ArrayList<VertexData>();
        final float radFragment = MathTool.HALF_PI / (lod + 1);

        
        // this creates a triangle stream
        for (int slice = 0; slice <= lod; slice++) { // slices along the y-axis

            for (int sector = 0; sector <= lod; sector++) {  // sectors rotate around the y-axis

                float a1 = MathTool.HALF_PI + (radFragment * sector);
                float a2 = a1 + radFragment;
                float b1 = MathTool.PI + (radFragment * sector);
                float b2 = b1 + radFragment;
                float c1 = MathTool.THREE_HALF_PI + (radFragment * sector);
                float c2 = c1 + radFragment;
                float d1 = MathTool.TWO_PI + (radFragment * sector);
                float d2 = d1 + radFragment;

                float up1 = (radFragment * slice);
                float up2 = up1 + radFragment;
                float down1 = -(radFragment * slice);
                float down2 = down1 - radFragment;

                vertices.addAll(createQuad(a1, up1, a2, up2));                
                vertices.addAll(createQuad(b1, up1, b2, up2));
                vertices.addAll(createQuad(c1, up1, c2, up2));
                vertices.addAll(createQuad(d1, up1, d2, up2));

                vertices.addAll(createQuad(a1, down2, a2, down1));
                vertices.addAll(createQuad(b1, down2, b2, down1));
                vertices.addAll(createQuad(c1, down2, c2, down1));
                vertices.addAll(createQuad(d1, down2, d2, down1));
            }

        }

        switch (getStreamFormat()) {
        case TRIANGLES:
            setupTriangle(vertices);
            break;
        case LINES:
            setupLines(vertices); // turning triangles into line stream
            break;
        case LINE_LOOP:
        case LINE_STRIP:
        default:
            throw new IllegalStateException("we don't support '" + getStreamFormat() 
                    + "' as stream format in " + this.getClass().getSimpleName());           
        }

    }
    
    private void setupLines(ArrayList<VertexData> vertices) {
        for (int i = 0; i < (vertices.size()); i = i +3) {
            final VertexData vertex1 = vertices.get(i + 0);
            final VertexData vertex2 = vertices.get(i + 1);
            final VertexData vertex3 = vertices.get(i + 2);            

            addVertexData(vertex1);
            addVertexData(vertex2);

            addVertexData(vertex2);
            addVertexData(vertex3);

            addVertexData(vertex3);
            addVertexData(vertex1);
            
        }
  
    }

        
    private void setupTriangle(ArrayList<VertexData> vertices) {
        for (int i = 0; i < (vertices.size()); i++) {
            addVertexData(vertices.get(i));
        }
    }
    
    private void addVertexData(VertexData vertex) { 
        Vertex v = addVertex();
        VertexFormat format = getVertexFormat();
        if (format.positionSize() == 3) {
            v.withPosition(vertex.getXYZ()[0], vertex.getXYZ()[1],vertex.getXYZ()[2]);
        }
        
        if (format.normalSize() == 3) {
            v.withNormal(vertex.getNormal()[0], vertex.getNormal()[1],vertex.getNormal()[2]);
        }

        if (format.textureSize() == 2) {
            v.withTexture(vertex.getST()[0], vertex.getST()[1]);
        }  
    }

    
    /**
     * @param u [0 .. 2pi]           rotate around y-axis
     * @param v [-pi/2 .. +pi/2]     down/up
     * @return
     */
    private ArrayList<VertexData> createQuad(final float u1, final float v1, final float u2, final float v2) {
        ArrayList<VertexData> result = new ArrayList<VertexData>();

        final Vector3f bottomLeft = createVector(u1, v1);
        final Vector3f bottomRight = createVector(u2, v1);
        final Vector3f topRight = createVector(u2, v2);
        final Vector3f topLeft = createVector(u1, v2);

        if (!tooClose(bottomLeft, bottomRight)) {
            result.add(new VertexData()
                .withXYZ(bottomLeft)
                .withNormal(bottomLeft)
                .withST(u1/MathTool.TWO_PI, 0.5f - v1/MathTool.PI)
            );
            result.add(new VertexData()
                .withXYZ(bottomRight)
                .withNormal(bottomRight)
                .withST(u2/MathTool.TWO_PI, 0.5f - v1/MathTool.PI)
            );
            result.add(new VertexData()
                .withXYZ(topRight)
                .withNormal(topRight)
                .withST(u2/MathTool.TWO_PI, 0.5f - v2/MathTool.PI)
            );
        }
        
        if (!tooClose(topRight, topLeft)) {
            result.add(new VertexData()
                .withXYZ(topRight)
                .withNormal(topRight)
                .withST(u2/MathTool.TWO_PI, 0.5f - v2/MathTool.PI)
            );
            result.add(new VertexData()
                .withXYZ(topLeft)
                .withNormal(topLeft)
                .withST(u1/MathTool.TWO_PI, 0.5f - v2/MathTool.PI)
            );
            result.add(new VertexData()
                .withXYZ(bottomLeft)
                .withNormal(bottomLeft)
                .withST(u1/MathTool.TWO_PI, 0.5f - v1/MathTool.PI)
            );
        }

        return result;
    }

    /**
     *
     * @param lat rad in y direction [-PI ... +PI]
     * @param lon rad around y axix [0 ... 2PI]
     * @return
     */
    private Vector3f createVector(float lon, float lat) {
        final float xx = (float) Math.cos(lat) * (float) Math.sin(lon); // 0,0 -> 0;
        final float yy = (float) Math.sin(lat); // -PI -> -1; 0 -> 0 ; PI -> +1
        final float zz = (float) Math.cos(lat) * (float) Math.cos(lon); // 0,0 -> 1
        Vector3f vector = new Vector3f(xx, yy, zz);
        vector.scale(radius);
        return vector;
    }

    private static final float EPSILON = 0.01f;
    private boolean tooClose(Vector3f vec1, Vector3f vec2) {
        return (Math.abs(vec1.x - vec2.x) < EPSILON)
                && (Math.abs(vec1.y - vec2.y) < EPSILON)
                && (Math.abs(vec1.z - vec2.z) < EPSILON);
    }

}


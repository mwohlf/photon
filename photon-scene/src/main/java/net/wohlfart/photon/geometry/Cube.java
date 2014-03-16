package net.wohlfart.photon.geometry;

import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.VertexTransform;

public class Cube extends Geometry {

    private float length;

	public Cube() {
        this(5);
    }

    public Cube(float length) {
        super(IGeometry.VertexFormat.VERTEX_P3C0N0T0, IGeometry.StreamFormat.LINES);
        //super(IGeometry.VertexFormat.VERTEX_P3C0N0T0, IGeometry.StreamFormat.TRIANGLES);
        this.length = length;
        setupBufferData();
    }

    private void setupBufferData() {
        final float l = length / 2f;

        addVertex().withPosition(+l, +l, +l);
        addVertex().withPosition(-l, +l, +l);
        addVertex().withPosition(-l, -l, +l);
        addVertex().withPosition(+l, -l, +l);

        addVertex().withPosition(+l, +l, -l);
        addVertex().withPosition(-l, +l, -l);
        addVertex().withPosition(-l, -l, -l);
        addVertex().withPosition(+l, -l, -l);

        addLine(0, 1); 
        addLine(1, 2);
        addLine(2, 3);
        addLine(3, 0);

        addLine(0, 4);
        addLine(1, 5);
        addLine(2, 6);
        addLine(3, 7);

        addLine(4, 5);
        addLine(5, 6);
        addLine(6, 7);
        addLine(7, 4);

        transformVertices(VertexTransform.move(0, 0, -15));    
    }
	
}

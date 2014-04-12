package net.wohlfart.photon.geometry;

import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.VertexTransform;

public class Quad extends Geometry {

    private final float length;

	public Quad(float length) {
		super(VertexFormat.VERTEX_P3C0N0T2, StreamFormat.TRIANGLES);
		this.length = length;
        setupBufferData();
	}

	private void setupBufferData() {
        final float l = length / 2f;

	    currentVertex.withPosition(+l,+l, 0).withTexture( 1, 0);
	    currentVertex.withPosition(-l,+l, 0).withTexture( 0, 0);
	    currentVertex.withPosition(-l,-l, 0).withTexture( 0, 1);
	    currentVertex.withPosition(+l,-l, 0).withTexture( 1, 1);
	    //addRectangle(3,2,1,0);
	    addRectangle(0,1,2,3);
	    transformVertices(VertexTransform.move(0,0,-10f));
	}

}

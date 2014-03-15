package net.wohlfart.photon.render;

import net.wohlfart.photon.render.IGeometry.VertexFormat;

public class VertexTransform implements IVertexTransform {

    public static IVertexTransform move(final float x, final float y, final float z) {
        return new IVertexTransform() {
            @Override
            public float[] execute(VertexFormat format, float[] vertexElement) {
                vertexElement[0] += x; // moving left
                vertexElement[1] += y; // moving top
                vertexElement[2] += z; // moving back
                return vertexElement;
            }
        };
    }

    @Override
    public float[] execute(VertexFormat format, float[] vertexElement) {
        return vertexElement;
    }

}

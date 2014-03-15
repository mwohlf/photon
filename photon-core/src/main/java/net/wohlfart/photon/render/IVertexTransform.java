package net.wohlfart.photon.render;

import net.wohlfart.photon.render.IGeometry.VertexFormat;

public interface IVertexTransform {

    public IVertexTransform IDENTITY_TRANSFORM = new IVertexTransform() {
        @Override
        public float[] execute(VertexFormat format, float[] vertexElement) {
            return vertexElement;
        }
    };

    float[] execute(VertexFormat format, float[] vertexElement);

}

package net.wohlfart.photon.hud;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.hud.layout.LayoutStrategy;
import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.resources.Resources;
import net.wohlfart.photon.shader.IUniformValue;
import net.wohlfart.photon.shader.Matrix4fValue;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.tools.Dimension;
import net.wohlfart.photon.tools.MathTool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @param <C> the constraints for the layout strategy of this component within its parent container
 */
public class RingBufferChart<C> extends AbstractComponent<C> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(RingBufferChart.class);

    private final float[] ringBuffer; // values should be in the range of 0..1
    private final int ringSize = 70;
    private int oldestIdx = 0;


    public RingBufferChart(int width, int height) {
        this.width = width;
        this.height = height;
        ringBuffer = new float[ringSize];
        isDirty = true;
        // the 2D shader is also used by the label to render characters
        //textures.put(ShaderTemplateWrapper.TEXTURE01, NullTexture.INSTANCE);
        uniforms.put(ShaderParser.TEXTURE01, IUniformValue.SHADER_UNIFORM_NULL_VALUE);

    }

    public void addData(float value) {
        ringBuffer [oldestIdx] = value;
        oldestIdx = (oldestIdx + 1) % ringSize;
        isDirty = true;
    }

    @Override
    public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
        if (container == null) {
            LOGGER.warn("no container for '"+ this + "' skipping double dispatch");
            return;
        }

        if (isDirty) {
            Dimension dim = renderer.getDimension();
            geometry = createGeometry(dim);
            //model2WorldValue.set(createModelMatrix(dim, container.getLayoutManager(), model2WorldValue.get()));
            uniforms.put(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, new Matrix4fValue(model2WorldMatrix));
            isDirty = false;
        }

        renderer.setRenderConfig(Resources.PLAIN_SHADER_ID, renderConfig);
        renderer.setUniformValues(getUniformValues());
        renderer.drawGeometry(getGeometry());
        renderer.renderChildren(tree);
    }

    @Override
    public IGeometry getGeometry() {
        return geometry;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [zOrder=" + zOrder
                    + " container=" + container
                    + ", renderConfig=" + renderConfig + "]";
    }

    private Geometry createGeometry(Dimension screenDimension) {
        Geometry geometry = new Geometry(VertexFormat.VERTEX_P3C0N0T0, StreamFormat.LINE_STRIP);

        float xScale = this.width / screenDimension.getWidth();
        float yScale = this.height / screenDimension.getHeight();

        for (int x = 0; x < ringSize; x++) {
            geometry
                .addVertex()
                .withPosition(
                    ((float)x / (float)ringSize) * xScale * 2f,   // [0..1] * scale * 2
                    (ringBuffer[(x + oldestIdx) % ringSize]) * yScale * 2f,     // [0..1] * scale * 2
                    0);
        }
        return geometry;
    }

    private Matrix4f createModelMatrix(Dimension screenDimension, LayoutStrategy<C> layoutManager, Matrix4f modelMatrix) {
        assert(modelMatrix != null);
        float alignX = layoutManager.getLayoutAlignmentX(this); // [0..1]
        float alignY = layoutManager.getLayoutAlignmentY(this); // [0..1]
        // origin of the subcomponents is top left
        alignY += this.height / screenDimension.getHeight();
        // screen range: [-1 .. +1] x to the right, y upwards   z in the range of [-1..+1]
        return MathTool.convert(new Vector3f(alignX * 2f - 1f, 1f - alignY * 2f, 0f), modelMatrix);
    }

}

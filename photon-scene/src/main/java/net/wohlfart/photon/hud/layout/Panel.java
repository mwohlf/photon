package net.wohlfart.photon.hud.layout;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.resources.Resources;
import net.wohlfart.photon.shader.Matrix4fValue;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.tools.Dimension;
import net.wohlfart.photon.tools.MathTool;


/**
 * @param <P> this panel's parent layout contraints
 * @param <C> this panel's children's layout contraints
 */
public class Panel<P, C> extends Container<C> implements IComponent<P> {

    private Container<P> parent;

    private final Border borderNode = new Border();


    public Panel(Dimension dimension, LayoutStrategy<C> layoutStrategy) {
        super(dimension, layoutStrategy);
        isDirty = true;
    }

    @Override
    public void setParent(Container<P> parent) {
        this.parent = parent;
    }

    @Override
    public Container<P> getParent() {
        return parent;
    }


    @Override
    public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
        assert (parent != null) : "we shouldn't be rendering this object if it has no parent";
        super.accept(renderer, tree); // render the subcomponents in this container

        if (isDirty) {
            Dimension dim = renderer.getDimension();
            Matrix4f borderModelMatrix = createModelMatrix(dim, parent.getLayoutManager(), borderNode.getModel2WorldMatrix());
            borderNode.setGeometry(createBorderGeometry(dim));
            //borderNode.getModel2WorldMatrix().load(borderModelMatrix);
            borderNode.getUniformValues().put(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, new Matrix4fValue(borderModelMatrix));
            isDirty = false;
        }
        borderNode.accept(renderer, tree);
    }

    private Matrix4f createModelMatrix(Dimension screenDimension, LayoutStrategy<P> layoutManager, Matrix4f modelMatrix) {
        float alignX = layoutManager.getLayoutAlignmentX(this); // [0..1]
        float alignY = layoutManager.getLayoutAlignmentY(this); // [0..1]
        // origin of the subcomponents is top left
        alignY += this.height / screenDimension.getHeight();
        // screen range: [-1 .. +1] x to the right, y upwards   z in the range of [-1..+1]
        return MathTool.convert(new Vector3f(alignX * 2f - 1f, 1f - alignY * 2f, 0f), modelMatrix);
    }


    private IGeometry createBorderGeometry(Dimension screenDimension) {
        Geometry geometry = new Geometry(VertexFormat.VERTEX_P3C0N0T0, StreamFormat.LINE_LOOP);

        float z = 0.5f;       // [-1...1]

        float screenWidth = screenDimension.getWidth();
        float screenHeight = screenDimension.getHeight();


        // the x/y coordinates must fit into a [-1 .. +1] interval for the OpenGL screen space
        float x1 = 0 / (screenWidth/2);
        float x2 = width / (screenWidth/2f);
        float y1 = 0 / (screenHeight/2);
        float y2 = height / (screenHeight/2f);

        // origin is bottom left
        geometry.addVertex().withPosition( x2, y2, z);
        geometry.addVertex().withPosition( x1, y2, z);
        geometry.addVertex().withPosition( x1, y1, z);
        geometry.addVertex().withPosition( x2, y1, z);

        return geometry;
    }

    protected class Border extends AbstractRenderElement {

        Border() {
            renderConfig = RenderConfigImpl.DEFAULT;
            shaderId = Resources.TWOD_SHADER_ID;
        }

        public void setGeometry(IGeometry geometry) {
            this.geometry = geometry;
        }

    }
}

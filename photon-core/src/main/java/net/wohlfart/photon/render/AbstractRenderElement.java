package net.wohlfart.photon.render;

import java.util.Collection;
import java.util.HashSet;

import javax.vecmath.Matrix4f;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.graph.NodeSortStrategy.ISortToken;
import net.wohlfart.photon.shader.IShaderProgram.IShaderProgramIdentifier;
import net.wohlfart.photon.shader.IUniformValue;
import net.wohlfart.photon.shader.Model2WorldMatrixValue;


/**
 * this is the base component for rendering 3d objects
 * you need at least set a geometry or override the getGeometry mehtod
 *
 * @author Michael Wohlfart
 */
public abstract class AbstractRenderElement implements IRenderer.IRenderElem {

    protected final Collection<IUniformValue> uniforms = new HashSet<IUniformValue>();

    protected final Matrix4f model2WorldMatrix =  new Matrix4f();

    protected IShaderProgramIdentifier shaderIdent;

    protected IRenderConfig<RenderConfigImpl> renderConfig = IRenderConfig.DEFAULT;

    protected IGeometry geometry;

    protected double zOrder = Double.NaN; // provided via the sort token

    protected ISortToken sortToken = new SortToken();


    protected AbstractRenderElement() {
    	model2WorldMatrix.setIdentity();
    	Model2WorldMatrixValue matrix = new Model2WorldMatrixValue(model2WorldMatrix);
        uniforms.add(matrix);
        uniforms.add(matrix. new NormalMatrix());
    }

    @Override
    public void setZOrder(double zOrder) {
        this.zOrder = zOrder;
    }

    // modify the matrix to set the elements position
    @Override
    public Matrix4f getModel2WorldMatrix() {
        return model2WorldMatrix;
    }

    @Override
    public IGeometry getGeometry() {
    	assert geometry != null : "need to set a geometry";
        return geometry;
    }

    @Override
    public void accept(IRenderer renderer, ITree<IRenderer.IRenderNode> tree) {
    	assert shaderIdent != null : "need to set a shader id";
        renderer.setRenderConfig(shaderIdent, renderConfig);
        renderer.addUniformValues(getUniformValues());
        renderer.drawGeometry(getGeometry());
        renderer.renderChildren(tree);
    }

    @Override
    public ISortToken getSortToken() {
        return sortToken;
    }

    @Override
    public Collection<IUniformValue> getUniformValues() {
        return uniforms;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [zOrder=" + zOrder
                    + " renderConfig=" + renderConfig + "]";
    }


    public class SortToken implements ISortToken {

        @Override
        public boolean isTranslucent() {
            return renderConfig.isTranslucent();
        }

        @Override
        public double getZOrder() {
            return zOrder;
        }

    }

}

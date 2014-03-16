package net.wohlfart.photon.render;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Matrix4f;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.graph.NodeSortStrategy.ISortToken;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.UniformHandle;
import net.wohlfart.photon.shader.UniformHandle.IUniformValue;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;


/**
 * this is the base component for rendering 3d objects
 *
 * @author Michael Wohlfart
 */
public abstract class AbstractRenderElement implements IRenderer.IRenderElem {

	public static final ShaderIdentifier DEFAULT_SHADER_ID = ShaderIdentifier.create("shader/default.vert", "shader/default.frag");

    protected final Map<String, IUniformValue> uniforms = new HashMap<>();

    protected final Map<String, ITextureIdentifier> textures = new HashMap<>();

    protected final Matrix4f model2WorldMatrix =  new Matrix4f();

    protected double zOrder = Double.NaN;

    protected IGeometry geometry;

    protected SortToken sortToken = new SortToken();

    public IRenderConfig<RenderConfigImpl> renderConfig = IRenderConfig.DEFAULT;

    protected ShaderIdentifier shaderId = DEFAULT_SHADER_ID;


    protected AbstractRenderElement() {
    	model2WorldMatrix.setIdentity();
        uniforms.put(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, new UniformHandle.Matrix4fValue(model2WorldMatrix));
    }

    @Override
    public void setZOrder(double zOrder) {
        this.zOrder = zOrder;
    }

    @Override
    public Matrix4f getModel2WorldMatrix() {
        return model2WorldMatrix;
    }

    @Override
    public final Map<String, ITextureIdentifier> getTextures() {
        return textures;
    }

    @Override
    public IGeometry getGeometry() {
        return geometry;
    }

    @Override
    public void accept(IRenderer renderer, ITree<IRenderer.IRenderNode> tree) {
        renderer.setRenderConfig(shaderId, renderConfig);
        renderer.setUniformValues(getTextures(), getUniformValues());
        renderer.drawGeometry(getGeometry());
        renderer.renderChildren(tree);
    }

    @Override
    public ISortToken getSortToken() {
        return sortToken;
    }

    @Override
    public Map<String, IUniformValue> getUniformValues() {
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

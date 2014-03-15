package net.wohlfart.photon.render.node;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Matrix4f;

import net.wohlfart.photon.graph.NodeSortStrategy.ISortToken;
import net.wohlfart.photon.graph.Tree;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.IRenderer.IRenderElem;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.UniformHandle;
import net.wohlfart.photon.shader.UniformHandle.UniformValue;
import net.wohlfart.photon.texture.CelestialType;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;


/**
 * this is the base component for rendering 3d objects
 *
 *
 * @author Michael Wohlfart
 */
public abstract class AbstractRenderElement implements IRenderElem {


    protected final ShaderIdentifier DEFAULT_SHADER_ID = ShaderIdentifier.create("shader/default.vert", "shader/default.frag");
    protected final ShaderIdentifier TEXTURE_SHADER_ID = ShaderIdentifier.create("shader/texture.vert", "shader/texture.frag");



    protected final ShaderIdentifier SKYBOX_SHADER_ID = ShaderIdentifier
            .create("shader/skybox/vertex.glsl", "shader/skybox/fragment.glsl");

    protected final ShaderIdentifier TEXTURE_SIMPLE_SHADER_ID = ShaderIdentifier
            .create("shader/texture/simple.vert", "shader/texture/simple.frag");

    protected final ShaderIdentifier TEXTURE_RADBLUR_SHADER_ID = ShaderIdentifier
            .create("shader/texture/radblur.vert", "shader/texture/radblur.frag");

    protected final ShaderIdentifier SIMPLE_SHADER_ID = ShaderIdentifier
            .create("shader/simple/vertex.glsl", "shader/simple/fragment.glsl");

    // used for the labels, no z-coordinates
    protected final ShaderIdentifier PLAIN_SHADER_ID = ShaderIdentifier
            .create("shader/plain/vertex.glsl", "shader/plain/fragment.glsl");

    protected final ShaderIdentifier TWOD_SHADER_ID = ShaderIdentifier
            .create("shader/2d/vertex.glsl", "shader/2d/fragment.glsl");



    protected static final ITextureIdentifier TEXTURE_ID1 = TextureIdentifier
            .create("gfx/textures/texture-1.jpg");

    protected static final TextureIdentifier TEXTURE_ID2 =  TextureIdentifier
            .create(30, CelestialType.LAVA_PLANET, 2);

    protected static final TextureIdentifier TEXTURE_ID3 =  TextureIdentifier
            .create(30, CelestialType.CONTINENTAL_PLANET, 2);



    public IRenderConfig<RenderConfigImpl> renderConfig = IRenderConfig.DEFAULT;

    protected ShaderIdentifier shaderId = DEFAULT_SHADER_ID;

    private final Matrix4f model2WorldMatrix =  new Matrix4f();

    protected final Map<String, UniformValue> uniforms = new HashMap<>();

    protected final Map<String, ITextureIdentifier> textures = new HashMap<>();

    protected double zOrder = Double.NaN;

    protected IGeometry geometry;

    protected ISortToken sortToken = new SortToken();


    protected AbstractRenderElement() {
    	model2WorldMatrix.setIdentity();
        uniforms.put(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, new UniformHandle.Matrix4fValue(model2WorldMatrix));
    }

    @Override
    public void setZOrder(double zOrder) {
        sortToken.setZOrder(zOrder);
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
    public void accept(IRenderer renderer, Tree<IRenderNode> tree) {
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
    public Map<String, UniformValue> getUniformValues() {
        return uniforms;
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

        @Override
        public void setZOrder(double z) {
            zOrder = z;
        }

    }

}

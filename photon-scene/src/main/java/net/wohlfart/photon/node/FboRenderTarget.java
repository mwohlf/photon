package net.wohlfart.photon.node;

import java.util.HashMap;
import java.util.Map;

import javax.media.nativewindow.util.Dimension;
import javax.vecmath.Matrix4f;

import net.wohlfart.photon.geometry.Quad;
import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.graph.NodeSortStrategy.ISortToken;
import net.wohlfart.photon.render.FrameBufferObject;
import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.IRenderer.IFrameBufferNode;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.render.VertexTransform;
import net.wohlfart.photon.resources.Resources;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.UniformHandle.IUniformValue;
import net.wohlfart.photon.texture.ITexture;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;

public class FboRenderTarget implements IFrameBufferNode {

	public static final ITextureIdentifier TEXTURE_ID1 = TextureIdentifier.create("gfx/textures/texture.jpg");
	public static final ShaderIdentifier TEXTURE_SHADER_ID = ShaderIdentifier.create("shader/texture.vert", "shader/texture.frag");


    protected FrameBufferObject frameBufferObject = new FrameBufferObject();

    protected RenderConfigImpl renderConfig = IRenderConfig.DEFAULT;
    //protected RenderConfigImpl renderConfig = IRenderConfig.DEFAULT;

    protected ISortToken sortToken = new SortToken();

    protected final Map<String, IUniformValue> uniforms = new HashMap<>();

    protected IGeometry geometry = new Quad(2); //createGeometry();

    protected Matrix4f model2WorldMatrix = new Matrix4f();

    protected double zOrder = Double.NaN;

    protected Dimension dim = new Dimension(600,400);

    public FboRenderTarget() {
       // uniforms.put(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, new UniformHandle.Matrix4fValue(model2WorldMatrix));
    }

    @Override
    public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
        // render on framebuffer
    	renderer.setFrameBuffer(getFrameBufferObject());
        renderer.renderChildren(tree);
    	renderer.setFrameBuffer(null);
        // render on screen
 //       renderer.renderChildren(tree);

        // rendering the quad last, maybe we need to put it in the render cache in order to be sorted...
        renderQuad(renderer);
    }

    private void renderQuad(IRenderer renderer) {
        //visitor.setConfig(TEXTURE_SIMPLE_SHADER_ID, renderConfig);
        //renderer.setRenderConfig(TEXTURE_RADBLUR_SHADER_ID, renderConfig);
        renderer.setRenderConfig(Resources.TEXTURE_SHADER_ID, renderConfig);

      //  final FrameBufferObject frameBufferObject = getFrameBufferObject();
        final Map<String, ITexture.ITextureIdentifier> textures = new HashMap<>();

        // TODO:
        //textures.put(ShaderParser.TEXTURE01, frameBufferObject.getTextureHandle());
    	//ITextureIdentifier TEXTURE_ID1 = TextureIdentifier.create("gfx/textures/texture.jpg");
        textures.put(ShaderParser.TEXTURE01, TEXTURE_ID1);

        /*
        Dimension dim = renderer.getDimension();
        model2WorldMatrix.m00 = (float)dim.getWidth()/(float)dim.getHeight();
        model2WorldMatrix.m11 = 1;
        model2WorldMatrix.m22 = 1;
        model2WorldMatrix.m32 = -2.5f; // z transform     this probably depends on the range of view...
         */

        renderer.setUniformValues(textures, uniforms);
        renderer.drawGeometry(geometry);
    }

    @Override
    public ISortToken getSortToken() {
        return sortToken;
    }

    @Override
    public FrameBufferObject getFrameBufferObject() {
        return frameBufferObject;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [getSortToken()=" + getSortToken() + "]";
    }

    private IGeometry createGeometry() {
        // see: http://lwjgl.org/wiki/index.php?title=The_Quad_textured
        // see: http://www.idevgames.com/forums/thread-1632.html for flipping y-axis
        Geometry geometry = new Geometry(VertexFormat.VERTEX_P3C0N0T2, StreamFormat.TRIANGLES);
        geometry.addVertex().withPosition(+1,+1, 0).withTexture( 1, 1);
        geometry.addVertex().withPosition(-1,+1, 0).withTexture( 0, 1);
        geometry.addVertex().withPosition(-1,-1, 0).withTexture( 0, 0);
        geometry.addVertex().withPosition(+1,-1, 0).withTexture( 1, 0);
        geometry.addRectangle(0, 1, 2, 3);
        geometry.transformVertices(VertexTransform.move(0, 0, -10));
        return geometry;
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

package net.wohlfart.photon.node;

import java.util.Collection;
import java.util.HashSet;

import javax.vecmath.Matrix4f;

import net.wohlfart.photon.ShaderIdent;
import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.graph.NodeSortStrategy.ISortToken;
import net.wohlfart.photon.render.FrameBufferObject;
import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.render.VertexTransform;
import net.wohlfart.photon.shader.IUniformValue;
import net.wohlfart.photon.shader.Model2WorldMatrixValue;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.TextureHandleValue;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;
import net.wohlfart.photon.tools.Dimension;

public class FrameBufferNode implements IRenderNode {

	public static final ITextureIdentifier TEXTURE_ID1 = TextureIdentifier.create("gfx/textures/texture.jpg");

    protected FrameBufferObject frameBufferObject = new FrameBufferObject();

    protected RenderConfigImpl renderConfig = IRenderConfig.DEFAULT;
    //protected RenderConfigImpl renderConfig = IRenderConfig.DEFAULT;

    protected ISortToken sortToken = new SortToken();

    protected final Collection<IUniformValue> uniforms = new HashSet<IUniformValue>();

    protected IGeometry geometry; // = new Quad(2); //createGeometry();

    protected Matrix4f model2WorldMatrix = new Matrix4f();

    protected double zOrder = Double.NaN;

    protected Dimension screenDimension;

    public FrameBufferNode() {
       // uniforms.put(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, new UniformHandle.Matrix4fValue(model2WorldMatrix));
    	geometry = createGeometry();
    	model2WorldMatrix.setIdentity();
        uniforms.add(new Model2WorldMatrixValue(model2WorldMatrix));
    }

    @Override
    public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
        // render on framebuffer
    	frameBufferObject = getFrameBufferObject();
    	screenDimension = renderer.getPerspective().getScreenDimension();
    	frameBufferObject.setDimension(screenDimension);
    	renderer.setFrameBuffer(frameBufferObject);
        renderer.renderChildren(tree);
    	renderer.setFrameBuffer(null);
        // render on screen
        renderer.renderChildren(tree);

        // rendering the quad last, maybe we need to put it in the render cache in order to be sorted...
        renderer.setRenderConfig(ShaderIdent.TEXTURE_SHADER_ID, renderConfig);
        //uniforms.put(ShaderParser.TEXTURE01, new TextureHandleValue(frameBufferObject.getTextureHandle()));
        uniforms.add(new TextureHandleValue(ShaderParser.TEXTURE01, frameBufferObject.getDepthBufferHandle()));
        //uniforms.put(ShaderParser.TEXTURE01, new UniformHandle.TextureIdentValue(TEXTURE_ID1));
        renderer.addUniformValues(uniforms);
        renderer.drawGeometry(geometry);

    }

    @Override
    public ISortToken getSortToken() {
        return sortToken;
    }

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
        geometry.transformVertices(VertexTransform.move(0, 0, -6));
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

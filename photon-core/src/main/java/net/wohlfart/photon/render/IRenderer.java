package net.wohlfart.photon.render;

import java.util.Map;

import javax.vecmath.Matrix4f;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.graph.NodeSortStrategy.HasSortToken;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.UniformHandle.IUniformValue;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;

public interface IRenderer {

    public interface IRenderNode extends HasSortToken {

        // the render calls, must call renderer.renderChildren(tree)
        void accept(IRenderer renderer, ITree<IRenderNode> tree);

    }


    /* implementations of this interface contain all data needed to
     * rendered an element on screen by a renderer and also
     * everything to be sorted into the render queue/cache
     *
     *
     * see: http://stackoverflow.com/questions/16497794/sending-two-textures-to-glsl-shader
     * for multi texture binding
     */
    public interface IRenderElem extends IRenderNode {

        // the shader uniforms
        Map<String, IUniformValue> getUniformValues();

        // texture uniforms
        Map<String, ITextureIdentifier> getTextures();

        // contains the vertex attributes, in some cases we return subclasses of IGeometry
        IGeometry getGeometry();

        // the matrix that transforms this element form model to world space
        Matrix4f getModel2WorldMatrix();

        void setZOrder(double zOrder);

    }


    public interface IRenderEffect extends IRenderNode {

        FrameBufferObject getFrameBufferObject();

    }


    // display debug info during the next render run
    void setDebugMode(boolean enableDebug);

    // init call to start rendering the command cache
    void renderParent(ITree<IRenderNode> tree);

    // render the subnodes of a render command
    void renderChildren(ITree<IRenderNode> tree);

    // call sequence for drawing a  standard render command from the render cache
    void setRenderConfig(ShaderIdentifier shaderId, IRenderConfig<RenderConfigImpl> nextRenderConfig);

    // updating the uniform values
    void setUniformValues(Map<String, ITextureIdentifier> textures, Map<String, IUniformValue> uniformValues);

    // the draw call
    void drawGeometry(IGeometry geometry);

}

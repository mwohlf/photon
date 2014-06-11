package net.wohlfart.photon.render;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.shader.IShaderProgram;


public class RootRenderNode extends NullRenderNode {

    public RootRenderNode(String name) {
        super(name);
    }

    @Override
    public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
    	renderer.setRenderConfig(IShaderProgram.NULL_SHADER_ID, IRenderConfig.CLEAR);
        renderer.renderChildren(tree);
    }

}

package net.wohlfart.photon.render;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.render.IRenderer.IRenderNode;


public class RootRenderCommand extends NullRenderNode {

    public RootRenderCommand(String name) {
        super(name);
    }

    @Override
    public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
        renderer.renderChildren(tree);
    }

}

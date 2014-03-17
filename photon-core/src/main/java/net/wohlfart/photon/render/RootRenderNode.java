package net.wohlfart.photon.render;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.render.IRenderer.IRenderNode;


public class RootRenderNode extends NullRenderNode {

    public RootRenderNode(String name) {
        super(name);
    }

    @Override
    public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
        renderer.renderChildren(tree);
    }

}

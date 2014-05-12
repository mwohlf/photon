package net.wohlfart.photon.render;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.graph.NodeSortStrategy;
import net.wohlfart.photon.graph.NodeSortStrategy.ISortToken;
import net.wohlfart.photon.render.IRenderer.IRenderNode;

public class NullRenderNode implements IRenderNode {

    protected final String name;

    public NullRenderNode(String name) {
        this.name = name;
    }

    @Override
    public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
        // do nothing
    }

    @Override
    public ISortToken getSortToken() {
        return NodeSortStrategy.ZERO_SORT_TOKEN;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [name=" + name + "]";
    }

}
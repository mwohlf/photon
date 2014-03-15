package net.wohlfart.photon.graph;

import java.util.Collection;
import java.util.Collections;

import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.render.RootRenderCommand;


//
public class RenderCache implements IRenderCache {

    protected final TreeImpl<IRenderNode> root = new TreeImpl<IRenderNode>(new RootRenderCommand("root"));

    protected final NodeSortStrategy<IRenderNode> sorter = new NodeSortStrategy<IRenderNode>();



    @Override
    public Tree<IRenderNode> getRoot() {
        return root;
    }

    @Override
    public TreeImpl<IRenderNode> add(IRenderNode command) {
        return root.add(command);
    }

    @Override
    public void reOrder() {
        Collections.sort(root.children, sorter);
    }

    @Override
    public void removeAll(Collection<? extends IRenderNode> collection) {
        root.removeAll(collection);
    }

}

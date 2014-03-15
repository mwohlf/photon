package net.wohlfart.photon.graph;

import java.util.Collection;

import net.wohlfart.photon.render.IRenderer.IRenderNode;

public interface IRenderCache {

    Tree<IRenderNode> getRoot();

    TreeImpl<IRenderNode> add(IRenderNode node);

    void removeAll(Collection<? extends IRenderNode> collection);

    void reOrder();

}

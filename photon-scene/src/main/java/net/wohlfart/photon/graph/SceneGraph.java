package net.wohlfart.photon.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.wohlfart.photon.events.CommandEvent;
import net.wohlfart.photon.events.CommandEvent.CommandKey;
import net.wohlfart.photon.events.Subscribe;
import net.wohlfart.photon.render.IRenderer.IRenderNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * the core of the whole engine,
 *  - this object stores the scene,
 *  - creates/ extracts rendering commands to create a render cache
 *  - decides which objesct are visibale at all
 *  - fires up the renderer
 *
 * some interesting readings about scene graphs:
 *   http://www.highperformancegraphics.org/wp-content/uploads/Steinlechner-LazySG.pdf
 *   http://home.comcast.net/~tom_forsyth/blog.wiki.html#[[Scene%20Graphs%20-%20just%20say%20no]]
 *
 * @author Michael Wohlfart
 */
public class SceneGraph implements ISceneGraph {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneGraph.class);

    // we need a semantic view for calling in on action and update methods
    protected final HashSet<IEntity> semanticView;

    // we need a spatial view as boundingVolumeHierarchy for culling and picking
 //   protected final TreeImpl<BoundingVolumeSphere> spatialView;

    // we need a state view for doing the rendering run
    protected final IRenderCache renderCache;


    public SceneGraph() {
        this.renderCache = new RenderCache();
        this.semanticView = new HashSet<>();
 //     this.spatialView = new TreeImpl<>(new BoundingVolumeSphere(new Vector3d(), 0));
        LOGGER.info("init done");
    }

    @Subscribe
    public void commandEvent(CommandEvent evt) {
        LOGGER.debug("incoming command event: " + evt);
        if (evt.getKey() == CommandKey.DUMP_SCENE) {
            LOGGER.error(dumpNode("", new StringBuilder(), renderCache.getRoot()).toString());
        }
    }

    @Override
    public void addEntity(IEntity entity) {
        assert entity != null : "entity is null";
        semanticView.add(entity);
    }

    @Override
    public void removeEntity(IEntity entity) {
        assert entity != null : "entity is null";
        semanticView.remove(entity);
    }

    // add a set of render commands to the renderCache
    @Override
    public void addRenderCommands(Collection<? extends IRenderNode> nodes) {
        assert nodes != null;
        for (IRenderNode node: nodes) {
            renderCache.add(node);
        }
    }

    // add a single render command and return the created render node
    // this might be needed if we want to add subnodes
    @Override
    public ITree<IRenderNode> addRenderCommand(IRenderNode node) {
        assert node != null;
        return renderCache.add(node);
    }

    // remove render nodes and their subnodes
    @Override
    public void removeRenderCommands(Collection<? extends IRenderNode> nodes) {
        assert nodes != null;
        renderCache.removeAll(nodes);
    }

    public ITree<IRenderNode> getRenderCache() {
        renderCache.reOrder();
        return renderCache.getRoot();
    }

    public Set<IEntity> getEntities() {
        return semanticView;
    }

    protected StringBuilder dumpNode(String inset, StringBuilder builder, ITree<IRenderNode> renderNode) {
        builder.append(inset);
        builder.append(renderNode.getValue());
        builder.append(System.lineSeparator());

        Iterator<? extends ITree<IRenderNode>> iter = renderNode.getChildren();
        while (iter.hasNext()) {
            dumpNode(inset + "   ", builder, iter.next());
        }
        return builder;
    }

}

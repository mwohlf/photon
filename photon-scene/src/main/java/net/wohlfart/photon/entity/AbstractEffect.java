package net.wohlfart.photon.entity;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import net.wohlfart.photon.graph.ISceneGraph;
import net.wohlfart.photon.graph.ISceneGraph.IEntity;
import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.node.FrameBufferNode;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.tools.Quaternion;

public abstract class AbstractEffect implements IEntity  {

    private final FrameBufferNode overlay = new FrameBufferNode();

    // child entities that are affected by this effect
    private final Set<AbstractEntity> childEntities = new HashSet<>();

    private ITree<IRenderNode> tree = null;

    private ISceneGraph sceneGraph;


    @Override
    public void register(ISceneGraph sceneGraph) {
        this.sceneGraph = sceneGraph;
        sceneGraph.addEntity(this);
        tree = sceneGraph.createSubTree(overlay);
        for (AbstractEntity entity : childEntities) {
            entity.setup();
            for (IRenderNode command : entity.getRenderCommands()) {
                tree.add(command);
            }
        }
    }

    @Override
    public void unregister() {
        assert sceneGraph != null : "need to call register before you can call unregister";
        sceneGraph.removeEntity(this);
        sceneGraph.removeRenderCommands(Collections.singleton(overlay));
        tree = null;
    }

    public void addEntity(AbstractEntity entity) {
        childEntities.add(entity);
        if (tree != null) {
            entity.setup();
            for (IRenderNode command : entity.getRenderCommands()) {
                tree.add(command);
            }
        }
    }

    @Override
    public void update(Quaternion rot, Vector3f mov, float delta) {
        for (AbstractEntity entity : childEntities) {
            entity.update(rot, mov, delta);
        }
    }

    @Override
    public Vector3d getPosition() {
        throw new IllegalAccessError("getPosition not supported");
    }

    @Override
    public Quaternion getRotation() {
        throw new IllegalAccessError("getRotation not supported");
    }

    @Override
    public float getSize() {
        throw new IllegalAccessError("getSize not supported");
    }

}

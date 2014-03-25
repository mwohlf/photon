package net.wohlfart.photon.hud;

import java.util.Collections;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import net.wohlfart.photon.graph.ISceneGraph;
import net.wohlfart.photon.graph.ISceneGraph.IEntity;
import net.wohlfart.photon.hud.layout.Container;
import net.wohlfart.photon.hud.layout.LayoutStrategy;
import net.wohlfart.photon.tools.Quaternion;


/**
 * contains the component containers
 * TODO: read the elements from a config file
 */
public abstract class AbstractLayer<C> implements IEntity {

    private ISceneGraph sceneGraph;

    protected final Container<C> container;


    public AbstractLayer(LayoutStrategy<C> layout) {
        this.container = new Container<C>(layout);
    }

    public Container<C> getContainer() {
        return container;
    }

    @Override
    public void update(Quaternion rot, Vector3f mov, float delta) {
        // do nothing
    }

    @Override
    public void register(ISceneGraph sceneGraph) {
        this.sceneGraph = sceneGraph;
        sceneGraph.addEntity(this);
        //sceneGraph.addRenderCommands(container.getComponents());
        sceneGraph.addRenderCommands(Collections.singleton(container));
    }

    @Override
    public void unregister() {
        sceneGraph.removeEntity(this);
        //sceneGraph.removeRenderCommands(container.getComponents());
        sceneGraph.removeRenderCommands(Collections.singleton(container));
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

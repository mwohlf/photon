package net.wohlfart.photon.hud;

import java.util.HashSet;
import java.util.Set;

import javax.vecmath.Vector3d;

import net.wohlfart.photon.graph.ISceneGraph;
import net.wohlfart.photon.graph.ISceneGraph.IEntity;
import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.hud.layout.IComponent;
import net.wohlfart.photon.hud.layout.IContainer;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.tools.Quaternion;


/**
 * contains the component containers this is the root of the UI / hud tree
 *
 * TODO: read the elements from a config file
 */
public abstract class AbstractLayer implements IEntity {

	protected ISceneGraph sceneGraph;
    protected final Set<IContainer<?>> containers = new HashSet<>();

    @Override
    public void register(ISceneGraph sceneGraph) {
        this.sceneGraph = sceneGraph;
        sceneGraph.addEntity(this);
        for (IContainer<?> container : containers) {
        	addComponents(container, sceneGraph.createSubTree(container));
        }
    }

    private void addComponents(IContainer<?> parent, ITree<IRenderNode> tree) {
        for (IComponent component : parent.getComponents()) {
        	if (component instanceof IContainer) {
        		IContainer<?> container = (IContainer<?>)component;
        		addComponents(container, tree.add(container));
        	} else {
        		tree.add(component);
        	}
        }
	}

	@Override
    public void unregister() {
        sceneGraph.removeEntity(this);
        sceneGraph.removeRenderCommands(containers);
    }

    @Override
    public Vector3d getPosition() {
        throw new IllegalAccessError("getPosition not supported, a layer covers the whole visible screen");
    }

    @Override
    public Quaternion getRotation() {
        throw new IllegalAccessError("getRotation not supported, a layer is not rotated");
    }

    @Override
    public float getSize() {
        throw new IllegalAccessError("getSize not supported, a layr covers the whole screen");
    }

}

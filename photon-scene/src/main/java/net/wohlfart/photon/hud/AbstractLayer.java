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
import net.wohlfart.photon.tools.Dimension;
import net.wohlfart.photon.tools.Quaternion;


/**
 * contains the component containers this is the root of the UI / hud tree
 *
 * TODO: read the elements from a config file
 */
public abstract class AbstractLayer implements IEntity, IScreenSizeListener {

    protected final Set<IContainer<?>> containers = new HashSet<>();

    protected final Dimension screenDimension = new Dimension();

	protected ISceneGraph sceneGraph;


    @Override
    public void register(ISceneGraph sceneGraph) {
        this.sceneGraph = sceneGraph;
        sceneGraph.addEntity(this);
        for (IContainer<?> container : containers) {
        	container.setScreenDimension(screenDimension);
        	addComponents(container, sceneGraph.addRenderCommand(container));
        }
    }

    @Override
    public void setScreenDimension(Dimension dimension) {
    	this.screenDimension.set(dimension);
        for (IContainer<?> container : containers) {
        	container.setScreenDimension(dimension);
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

}

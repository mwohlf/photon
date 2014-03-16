package net.wohlfart.photon.graph;

import java.util.Collection;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.tools.Quaternion;


public interface ISceneGraph {


    /**
     * semantic element of a scene graph
     */
    public interface IEntity3D {

        // current position, we use a 3d vector for this, TODO: scale the objects size later
        Vector3d getPosition();

        // rotation of the geometry
        Quaternion getRotation();

        // size, needed for culling, ray picking etc.
        float getSize();

        // incoming update for cam moves as well as moving the object
        void update(Quaternion rot, Vector3f mov, float delta);

        // register this object to the scene graph
        void register(ISceneGraph graph);

        // remove from the scene graph
        void unregister();

    }



    void addEntity(IEntity3D entity);

    void removeEntity(IEntity3D entity);

    void addRenderCommands(Collection<? extends IRenderNode> nodes);

    void removeRenderCommands(Collection<? extends IRenderNode> nodes);

    ITree<IRenderNode> createSubTree(IRenderNode effect);

}

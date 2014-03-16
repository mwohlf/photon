package net.wohlfart.photon.render.entity;

import java.util.Collection;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import net.wohlfart.photon.graph.ISceneGraph;
import net.wohlfart.photon.graph.ISceneGraph.IEntity3D;
import net.wohlfart.photon.render.IRenderer.IRenderElem;
import net.wohlfart.photon.tools.MathTool;
import net.wohlfart.photon.tools.Quaternion;


/**
 * implements the basic features for most 3D entities
 * basically this class holds the render commands
 *
 * need to implement:
 *  setUp()
 *  getRenderCommands()
 *  tearDown()
 *
 */
public abstract class AbstractEntity3D implements IEntity3D {

    protected final Vector3d position = new Vector3d();

    protected final Quaternion rotation = new Quaternion();

    protected ISceneGraph sceneGraph;

    protected float size = -1;

    protected double zOrder = 0;

    protected int lod = 0;


    @Override
    public void update(Quaternion rot, Vector3f mov, float delta) {
    	//rotation.mult(rot);
    	Quaternion r = new Quaternion(rot);
    	r.mult(rotation);
    	rotation.setX(r.getX());
    	rotation.setY(r.getY());
    	rotation.setZ(r.getZ());
    	rotation.setW(r.getW());

        position.x += mov.x;
        position.y += mov.y;
        position.z += mov.z;

        MathTool.mul(rot, position);

        double zOrder = Math.sqrt(position.x * position.x + position.y * position.y + position.z * position.z);
        for (IRenderElem command : getRenderCommands()) {
            Matrix4f m = command.getModel2WorldMatrix();
            MathTool.convert(rotation, m);
            m.m30 = (float) position.x;
            m.m31 = (float) position.y;
            m.m32 = (float) position.z;
            m.m33 = 1;
            command.setZOrder(zOrder);
        }
    }

    @Override
    public float getSize() {
        assert size >= 0;
        return size;
    }

    @Override
    public Vector3d getPosition() {
        return position;
    }

    @Override
    public Quaternion getRotation() {
        return rotation;
    }

    @Override
    public void register(ISceneGraph sceneGraph) {
        this.sceneGraph = sceneGraph;
        setup();
        sceneGraph.addEntity(this);
        sceneGraph.addRenderCommands(getRenderCommands());
    }

    @Override
    public void unregister() {
        destroy();
        sceneGraph.removeRenderCommands(getRenderCommands());
        sceneGraph.removeEntity(this);
        sceneGraph = null;
    }


    public abstract void setup();

    public abstract Collection<? extends IRenderElem> getRenderCommands();

    public abstract void destroy();


    // -- setters

    public AbstractEntity3D withSize(float size) {
        this.size = size;
        return this;
    }

    public AbstractEntity3D withPosition(Vector3d position) {
        this.position.set(position);
        return this;
    }

    public AbstractEntity3D withPosition(double x, double y, double z) {
        this.position.set(x, y, z);
        return this;
    }

    public AbstractEntity3D withRotation(Quaternion rotation) {
        this.rotation.setX(rotation.getX());
        this.rotation.setY(rotation.getY());
        this.rotation.setZ(rotation.getZ());
        this.rotation.setW(rotation.getW());
        return this;
    }

}

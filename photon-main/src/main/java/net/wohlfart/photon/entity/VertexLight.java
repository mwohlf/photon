package net.wohlfart.photon.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import net.wohlfart.photon.graph.ISceneGraph;
import net.wohlfart.photon.graph.ISceneGraph.IEntity;
import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.graph.NodeSortStrategy;
import net.wohlfart.photon.graph.NodeSortStrategy.ISortToken;
import net.wohlfart.photon.node.SphereElement;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.IRenderer.IRenderElem;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.shader.IUniformValue;
import net.wohlfart.photon.shader.Model2WorldMatrixValue;
import net.wohlfart.photon.shader.VertexLightValue;
import net.wohlfart.photon.tools.MathTool;
import net.wohlfart.photon.tools.Quaternion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertexLight implements IEntity {
	protected static final Logger LOGGER = LoggerFactory.getLogger(VertexLight.class);

	private ISceneGraph sceneGraph;

	protected final Vector3d position = new Vector3d();

	protected final Quaternion rotation = new Quaternion();

	protected final ISortToken sortToken = NodeSortStrategy.NEGATIVE_INFINITY_SORT_TOKEN;

	protected final Set<IRenderElem> lights = new HashSet<IRenderElem>();



	protected final VertexLightValue vertexLightValue = new VertexLightValue(
    		0f,  // attenuation
    		new Vector3f((float) position.x, (float) position.y, (float) position.z),
    		new Vector4f(1f,1f,1f,1f), // color
    		new Vector3f(1f,1f,1f)     // diffuse light
    		);


	public VertexLight() {
		// a marker sphere
		SphereElement sphere = SphereElement.createLightMarker();
		// model2WorldMatrix = sphere.getModel2WorldMatrix();
		// lights.add(new LightElement());
		lights.add(sphere);
	}

	@Override
	public void register(ISceneGraph sceneGraph) {
		this.sceneGraph = sceneGraph;
		sceneGraph.addEntity(this);
		sceneGraph.addRenderCommands(lights);
	}

	@Override
	public void unregister() {
		sceneGraph.removeEntity(this);
		sceneGraph.removeRenderCommands(lights);
	}


	@Override
	public void update(Quaternion rot, Vector3f mov, float delta) {
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
        for (IRenderElem command : lights) {
            Matrix4f m = command.getModel2WorldMatrix();
            MathTool.convert(rotation, m);
            m.m30 = (float) position.x;
            m.m31 = (float) position.y;
            m.m32 = (float) position.z;
            m.m33 = 1;
            command.setZOrder(zOrder);
        }

		vertexLightValue.setPosition((float) position.x, (float) position.y, (float) position.z);

	}

    protected class LightElement implements IRenderElem {

    	protected final Collection<IUniformValue> uniforms = new HashSet<IUniformValue>();

        protected final Matrix4f model2WorldMatrix =  new Matrix4f();

    	LightElement() {
            uniforms.add(new Model2WorldMatrixValue(model2WorldMatrix));
            uniforms.add(vertexLightValue);
    	}

		@Override
		public ISortToken getSortToken() {
			return sortToken;
		}

		@Override
		public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
			// only add the uniforms, don't draw anything here
	        renderer.addUniformValues(getUniformValues());
		}

		@Override
		public Collection<IUniformValue> getUniformValues() {
			return uniforms;
		}

		@Override
		public IGeometry getGeometry() {
			return IGeometry.NULL_GEOMETRY;
		}

		@Override
		public Matrix4f getModel2WorldMatrix() {
			return model2WorldMatrix;
		}

		@Override
		public void setZOrder(double zOrder) { // method needs to be removed
			// nothing to do here
		}

    }

	public IEntity withPosition(double x, double y, double z) {
		position.set(x, y, z);
		return this;
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
	public float getSize() {
		return 0;
	}

}

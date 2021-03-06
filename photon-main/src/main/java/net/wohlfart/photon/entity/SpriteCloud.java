package net.wohlfart.photon.entity;

import java.util.Collections;
import java.util.Set;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import net.wohlfart.photon.ShaderIdent;
import net.wohlfart.photon.graph.ISceneGraph;
import net.wohlfart.photon.graph.ISceneGraph.IEntity;
import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.graph.NodeSortStrategy;
import net.wohlfart.photon.graph.NodeSortStrategy.ISortToken;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.shader.FloatValue;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.TextureIdentValue;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;
import net.wohlfart.photon.tools.MathTool;
import net.wohlfart.photon.tools.Quaternion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// see: http://stackoverflow.com/questions/17397724/point-sprites-for-particle-system
public class SpriteCloud implements IEntity {
	protected static final Logger LOGGER = LoggerFactory.getLogger(SpriteCloud.class);

	public static final ITextureIdentifier TEXTURE_ID1 = TextureIdentifier.create("gfx/textures/texture.jpg");

    private ISceneGraph sceneGraph;

    protected final Vector3d position = new Vector3d();

    protected final Quaternion rotation = new Quaternion();

    protected final Set<SpriteSet> sprites;

    public SpriteCloud() {
    	sprites = Collections.singleton(new SpriteSet());
    }

    @Override
    public void register(ISceneGraph sceneGraph) {
        this.sceneGraph = sceneGraph;
        sceneGraph.addEntity(this);
        sceneGraph.addRenderCommands(sprites);
    }

    @Override
    public void unregister() {
        sceneGraph.removeEntity(this);
        sceneGraph.removeRenderCommands(sprites);
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

        // double zOrder = Math.sqrt(position.x * position.x + position.y * position.y + position.z * position.z);
        for (SpriteSet spriteSet : sprites) {
            Matrix4f m = spriteSet.getModel2WorldMatrix();
            MathTool.convert(rotation, m);
            m.m30 = (float) position.x;
            m.m31 = (float) position.y;
            m.m32 = (float) position.z;
            m.m33 = 1;
        }

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
        return Float.POSITIVE_INFINITY;
    }

    private class SpriteGeometry extends Geometry {

		public SpriteGeometry() {
	        super(IGeometry.VertexFormat.VERTEX_P3C0N0T0, IGeometry.StreamFormat.POINTS);
	        setupBufferData();
		}

		private void setupBufferData() {
	        currentVertex.withPosition(+0.5f, +0.5f, -1.1f);
	        currentVertex.withPosition(-0.5f, +0.5f, -1.1f);
	        currentVertex.withPosition(-0.5f, -0.5f, -1.1f);
	        currentVertex.withPosition(+0.5f, -0.5f, -1.1f);
		}

    }


    private class SpriteSet extends AbstractRenderElement {

    	SpriteSet() {
    		super();
    		sortToken = NodeSortStrategy.ZERO_SORT_TOKEN;
    		shaderIdent = ShaderIdent.POINT_SPRITE_SHADER;
    		uniforms.add(new TextureIdentValue(ShaderParser.TEXTURE01, TEXTURE_ID1));
    		uniforms.add(new FloatValue(ShaderParser.UNIFORM_POINT_SIZE, 30f));
    		geometry = new SpriteGeometry();
    		renderConfig = IRenderConfig.SPRITE_CLOUD;
    	}

		@Override
		public ISortToken getSortToken() {
			return sortToken;
		}

		@Override
		public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
			LOGGER.debug("setting shaderIdent '{}'", shaderIdent);
	        renderer.setRenderConfig(shaderIdent, renderConfig);
	        renderer.addUniformValues(getUniformValues());
	        renderer.drawGeometry(getGeometry());
		}

    }

}

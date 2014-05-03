package net.wohlfart.photon.entity;

import java.util.Collections;
import java.util.Set;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import net.wohlfart.photon.ShaderIdent;
import net.wohlfart.photon.graph.ISceneGraph;
import net.wohlfart.photon.graph.ISceneGraph.IEntity;
import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.graph.NodeSortStrategy.ISortToken;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.shader.FloatValue;
import net.wohlfart.photon.shader.IShaderProgram;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.TextureIdentValue;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;
import net.wohlfart.photon.tools.Quaternion;

// see: http://stackoverflow.com/questions/17397724/point-sprites-for-particle-system
@SuppressWarnings("unused")
public class SpriteCloud implements IEntity {

	public static final ITextureIdentifier TEXTURE_ID1 = TextureIdentifier.create("gfx/textures/texture.jpg");

    private ISceneGraph sceneGraph;

    protected final Vector3d position = new Vector3d();

    protected final Quaternion rotation = new Quaternion();

    protected final SortToken sortToken = new SortToken();

	private final Set<Sprites> sprites;

    public SpriteCloud() {
    	sprites = Collections.singleton(new Sprites());
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
		// TODO

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
	        //super(IGeometry.VertexFormat.VERTEX_P3C0N0T2, IGeometry.StreamFormat.POINTS);
	        //super(IGeometry.VertexFormat.VERTEX_P3C0N0T0, IGeometry.StreamFormat.LINE_LOOP);
	        super(IGeometry.VertexFormat.VERTEX_P3C4N0T2, IGeometry.StreamFormat.POINTS);
	        setupBufferData();
		}

		private void setupBufferData() {
	        currentVertex.withPosition(+1, +1, -5).withColor(1f,1f,1f,1f).withTexture(+1f, +1f);
	        currentVertex.withPosition(-1, +1, -5).withColor(1f,1f,1f,1f).withTexture(-0f, +1f);
	        currentVertex.withPosition(-1, -1, -5).withColor(1f,1f,1f,1f).withTexture(-0f, -0f);
	        currentVertex.withPosition(+1, -1, -5).withColor(1f,1f,1f,1f).withTexture(+1f, -0f);
			/*
	        currentVertex.withPosition(+1, +1, -5).withTexture(+1f, +1f);
	        currentVertex.withPosition(-1, +1, -5).withTexture(-0f, +1f);
	        currentVertex.withPosition(-1, -1, -5).withTexture(-0f, -0f);
	        currentVertex.withPosition(+1, -1, -5).withTexture(+1f, -0f);

	        currentVertex.withPosition(+0.5f, +0.5f, -1.5f);
	        currentVertex.withPosition(-0.5f, +0.5f, -1.5f);
	        currentVertex.withPosition(-0.5f, -0.5f, -1.5f);
	        currentVertex.withPosition(+0.5f, -0.5f, -1.5f);
	        */
		}

		@Override
		public void draw(IShaderProgram shader, GL2ES2 gl) {
			//gl.glEnable(GLES2.GL_POINT_SIZE);
			((GL2)gl).glPointParameteri(GL2.GL_POINT_SPRITE_COORD_ORIGIN, GL2.GL_LOWER_LEFT);

            gl.glEnable(GL2ES1.GL_POINT_SPRITE);
            gl.glEnable(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
            gl.glDisable(GL2.GL_BLEND);
    		gl.glDisable(GL2.GL_DEPTH_TEST );
			//gl.glEnable(GL2GL3.GL_PROGRAM_POINT_SIZE);
			((GL2ES1)gl).glPointSize(1f);
			super.draw(shader, gl);
		}

    }


    private class Sprites extends AbstractRenderElement {

    	Sprites() {
    		super();
    		shaderId = ShaderIdent.POINT_SPRITE_SHADER;
    		uniforms.put(ShaderParser.TEXTURE01, new TextureIdentValue(TEXTURE_ID1));
    		uniforms.put(ShaderParser.UNIFORM_POINT_SIZE, new FloatValue(3f));
    		geometry = new SpriteGeometry();
    		renderConfig = IRenderConfig.DEFAULT;
    	}

		@Override
		public ISortToken getSortToken() {
			return sortToken;
		}

		@Override
		public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
	        renderer.setRenderConfig(shaderId, renderConfig);
	        renderer.setUniformValues(getUniformValues());
	        renderer.drawGeometry(getGeometry());
		}

    }


    public class SortToken implements ISortToken {

        @Override
        public boolean isTranslucent() {
            return false;
        }

        @Override
        public double getZOrder() {
            return Double.POSITIVE_INFINITY;
        }

    }

}

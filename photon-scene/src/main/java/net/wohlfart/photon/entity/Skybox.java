package net.wohlfart.photon.entity;

import java.util.ArrayList;
import java.util.Collection;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import net.wohlfart.photon.graph.ISceneGraph;
import net.wohlfart.photon.graph.ISceneGraph.IEntity3D;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.render.IVertexTransform;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.resources.Resources;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.TextureIdentValue;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;
import net.wohlfart.photon.tools.MathTool;
import net.wohlfart.photon.tools.Quaternion;


/**
 * see: http://ogldev.atspace.co.uk/www/tutorial25/tutorial25.html
 *
 * we need a feature to turn the skybox off on the fly...
 */
@SuppressWarnings("unused")
public class Skybox implements IEntity3D {

    private ISceneGraph sceneGraph;

    protected final Quaternion rotation = new Quaternion();

    private final Collection<Skybox.Side> sides = new ArrayList<>(6);

    private static final String BLUE_1024 = "skybox/blue-nebula";
    private static final String GREEN_1024 = "skybox/green-nebula-stars";
    private static final String PURPLE1_1024 = "skybox/purple-nebula";
    private static final String PURPLE2_1024 = "skybox/purple-nebula-complex";


    public Skybox() {
        this(BLUE_1024);
    }

    public Skybox(String resourcePath) {
        float dist = 2; // near frustum
        Vector3f translation;
        Quaternion rotation;
        TextureIdentifier textureIdent;

        // PLUS_X
        translation = new Vector3f(+dist, 0, 0);
        rotation = new Quaternion(new Vector3f(0, 0, +dist), translation);
        textureIdent = TextureIdentifier.create(resourcePath + "/right1.png");
        sides.add(new Side(createGeometry(rotation, translation, dist), textureIdent));

        // MINUS_X
        translation = new Vector3f(-dist, 0, 0);
        rotation = new Quaternion(new Vector3f(0, 0, +dist), translation);
        textureIdent = TextureIdentifier.create(resourcePath + "/left2.png");
        sides.add(new Side(createGeometry(rotation, translation, dist), textureIdent));

        // PLUS_Y
        translation = new Vector3f(0, +dist, 0);
        rotation = new Quaternion(new Vector3f(0, 0, +dist), translation);
        textureIdent = TextureIdentifier.create(resourcePath + "/top3.png");
        sides.add(new Side(createGeometry(rotation, translation, dist), textureIdent));

        // MINUS_Y
        translation = new Vector3f(0, -dist, 0);
        rotation = new Quaternion(new Vector3f(0, 0, +dist), translation);
        textureIdent = TextureIdentifier.create(resourcePath + "/bottom4.png");
        sides.add(new Side(createGeometry(rotation, translation, dist), textureIdent));

        // PLUS_Z
        translation = new Vector3f(0, 0, +dist);
        rotation = new Quaternion(new Vector3f(0, 0, +dist), translation);
        textureIdent = TextureIdentifier.create(resourcePath + "/front5.png");
        sides.add(new Side(createGeometry(rotation, translation, dist), textureIdent));

        // MINUS_Z
        translation = new Vector3f(0, 0, -dist);
        rotation = new Quaternion(new Vector3f(0, 0, +dist), translation);
        textureIdent = TextureIdentifier.create(resourcePath + "/back6.png");
        sides.add(new Side(createGeometry(rotation, translation, dist), textureIdent));
    }

    @Override
    public void register(ISceneGraph sceneGraph) {
        this.sceneGraph = sceneGraph;
        sceneGraph.addEntity(this);
        sceneGraph.addRenderCommands(sides);
    }

    @Override
    public void unregister() {
        sceneGraph.removeEntity(this);
        sceneGraph.removeRenderCommands(sides);
    }

    @Override
    public void update(Quaternion rot, Vector3f mov, float delta) {
        //Quaternion.mul(rot, rotation, rotation); // order is important, here: add rot to rotation
    	Quaternion r = new Quaternion(rot);
    	r.mult(rotation);
    	rotation.setX(r.getX());
    	rotation.setY(r.getY());
    	rotation.setZ(r.getZ());
    	rotation.setW(r.getW());

        for (Side side : sides) {
            MathTool.convert(rotation, side.getModel2WorldMatrix());
        }
    };

    @Override
    public Vector3d getPosition() {
        throw new IllegalAccessError("getPosition not supported for Skybox");
    }

    @Override
    public Quaternion getRotation() {
        throw new IllegalAccessError("getRotation not supported for Skybox");
    }

    @Override
    public float getSize() {
        throw new IllegalAccessError("getSize not supported for Skybox");
    }

    private IGeometry createGeometry(final Quaternion rotation, final Vector3f translation, float dist) {
        float l = dist;
        // see: http://lwjgl.org/wiki/index.php?title=The_Quad_textured
        Geometry geometry = new Geometry(VertexFormat.VERTEX_P3C0N0T2, StreamFormat.TRIANGLES);
        geometry.addVertex().withPosition(+l,+l, 0).withTexture( 1, 0);
        geometry.addVertex().withPosition(-l,+l, 0).withTexture( 0, 0);
        geometry.addVertex().withPosition(-l,-l, 0).withTexture( 0, 1);
        geometry.addVertex().withPosition(+l,-l, 0).withTexture( 1, 1);
        geometry.addRectangle(3,2,1,0);
        geometry.transformVertices(new IVertexTransform() {
            @Override
            public float[] execute(VertexFormat format, float[] vertexElement) {
                Vector3f vector = new Vector3f(vertexElement[0], vertexElement[1], vertexElement[2]);
                rotation.mult(vector);
                //MathTool.mul(rotation, vector, vector);
                vertexElement[0] = vector.x + translation.x;
                vertexElement[1] = vector.y + translation.y;
                vertexElement[2] = vector.z + translation.z;
                return vertexElement;
            }
        });
        return geometry;
    }


    public static class Side extends AbstractRenderElement {

        public Side(IGeometry geometry, ITextureIdentifier textureId) {
            this.geometry = geometry;
            this.uniforms.put(ShaderParser.TEXTURE01, new TextureIdentValue(textureId));
            this.renderConfig = RenderConfigImpl.SKYBOX;
            this.zOrder = Double.POSITIVE_INFINITY;
            this.shaderId = Resources.SKYBOX_SHADER_ID;
        }

    }

}

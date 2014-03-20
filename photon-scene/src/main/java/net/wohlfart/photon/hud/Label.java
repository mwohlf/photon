package net.wohlfart.photon.hud;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.hud.layout.LayoutStrategy;
import net.wohlfart.photon.hud.txt.CharAtlasFactory;
import net.wohlfart.photon.hud.txt.CharInfo;
import net.wohlfart.photon.hud.txt.FontIdentifier;
import net.wohlfart.photon.hud.txt.ICharAtlas;
import net.wohlfart.photon.hud.txt.ICharData;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.Geometry;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.resources.ResourceManager;
import net.wohlfart.photon.resources.Resources;
import net.wohlfart.photon.shader.Matrix4fValue;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.TextureValue;
import net.wohlfart.photon.texture.ITexture;
import net.wohlfart.photon.tools.Dimension;
import net.wohlfart.photon.tools.MathTool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: instead of recreating the geometry for any text changes we could
// keep a geometry for each single letter...
// not thread safe

/**
 * @param <C> the constraints for the layout strategy of this component within its parent container
 */
public class Label<C> extends AbstractComponent<C> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Label.class);

    static final FontIdentifier SIMPLE_FONT_ID = FontIdentifier
            .create("fonts/liberation/LiberationMono-Regular.ttf", 12f);

    private final FontIdentifier fontIdentifier = SIMPLE_FONT_ID;

    private final Text textNode = new Text();
    private final Border borderNode = new Border();


    private ICharData charData;
    private String text;


    public Label(String text) { // NO_UCD (public API)
        this.text = text;
        isDirty = true;
        refreshSize();
    }


    public void setText(String text) {
        this.text = text;
        isDirty = true;
        refreshSize();
    }


    @Override
    public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
        assert (container != null) : "we shouldn't be rendering this object if it has no parent";

        if (isDirty) {
            charData = ResourceManager.loadResource(ICharData.class, fontIdentifier);
            Dimension dim = renderer.getDimension();

            Matrix4f textModelMatrix = createModelMatrix(dim, container.getLayoutManager(), textNode.getModel2WorldMatrix());
            textNode.setGeometry(createTextGeometry(dim));
            //textNode.getModel2WorldMatrix().load(textModelMatrix);
            textNode.getUniformValues().put(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, new Matrix4fValue(textModelMatrix));

            Matrix4f borderModelMatrix = createModelMatrix(dim, container.getLayoutManager(), borderNode.getModel2WorldMatrix());
            borderNode.setGeometry(createBorderGeometry(dim));
            //borderNode.getModel2WorldMatrix().load(borderModelMatrix);
            textNode.getUniformValues().put(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, new Matrix4fValue(borderModelMatrix));

            isDirty = false;
        }
        textNode.accept(renderer, tree);
        borderNode.accept(renderer, tree);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [zOrder=" + zOrder
                    + " container=" + container
                    + ", renderConfig=" + renderConfig + "]";
    }

    private void refreshSize() {
        charData = ResourceManager.loadResource(ICharData.class, fontIdentifier);
        setupHeightAndWidth();
        //textNode.setTexture(charData.getCharTexture());
        textNode.setTexture(charData.getCharTexture());
    }

    private void setupHeightAndWidth() { // in real pixel
        ICharAtlas charAtlas = charData.getCharAtlas();
        this.height = charAtlas.getCharInfo(CharAtlasFactory.NULL_CHAR).getHeight();

        this.width = 0; // updated for each char
        for (char c : text.toCharArray()) {
            CharInfo info = charAtlas.getCharInfo(c);
            this.width += info.getWidth() - info.getG();
        }
    }

    private IGeometry createTextGeometry(Dimension screenDimension) {

        ICharAtlas charAtlas = charData.getCharAtlas();
        // see: http://lwjgl.org/wiki/index.php?title=The_Quad_textured
        Geometry geometry = new Geometry(VertexFormat.VERTEX_P3C0N0T2, StreamFormat.TRIANGLES);

        int n = 0;
        float screenX = 0f; // this is in pixel
        float screenY = 0;

        float atlasWidth = charAtlas.getImage().getWidth();
        float atlasHeight = charAtlas.getImage().getHeight();


        float screenWidth = screenDimension.getWidth();
        float screenHeight = screenDimension.getHeight();
        float z = 0f;       // [-1...1]
        for (char c : text.toCharArray()) {
            CharInfo info = charAtlas.getCharInfo(c);
            if (info == null) {
                info = charAtlas.getCharInfo(CharAtlasFactory.NULL_CHAR);
            }
            // the x/y coordinates must fit into a [-1 .. +1] interval for the OpenGL screen space
            float x1 = ( screenX - info.getG()) / (screenWidth/2);
            float x2 = ( screenX + info.getWidth() - info.getG()) / (screenWidth/2f);
            float y1 = ( screenY ) / (screenHeight/2);
            float y2 = ( screenY + info.getHeight()) / (screenHeight/2f);

            // texture coordinates are in the [0...1] interval
            final float s1 = (info.getX())/ atlasWidth;
            final float s2 = (info.getX() + info.getWidth()) / atlasWidth;
            final float t1 = (info.getY()) / atlasHeight;
            final float t2 = (info.getY() + info.getHeight()) / atlasHeight;

            geometry.addVertex().withPosition(x2,y2,z).withTexture(s2, t1);
            geometry.addVertex().withPosition(x1,y2,z).withTexture(s1, t1);
            geometry.addVertex().withPosition(x1,y1,z).withTexture(s1, t2);
            geometry.addVertex().withPosition(x2,y1,z).withTexture(s2, t2);
            geometry.addRectangle(n + 0, n + 1, n + 2, n + 3);

            // one char forward
            screenX += info.getWidth() - info.getG();
            n += 4;
        }

        return geometry;
    }


    private IGeometry createBorderGeometry(Dimension screenDimension) {
        Geometry geometry = new Geometry(VertexFormat.VERTEX_P3C0N0T0, StreamFormat.LINE_LOOP);

        float z = 0.5f;       // [-1...1]

        float screenWidth = screenDimension.getWidth();
        float screenHeight = screenDimension.getHeight();


        // the x/y coordinates must fit into a [-1 .. +1] interval for the OpenGL screen space
        float x1 = 0 / (screenWidth/2);
        float x2 = width / (screenWidth/2f);
        float y1 = 0 / (screenHeight/2);
        float y2 = height / (screenHeight/2f);

        // origin is bottom left
        geometry.addVertex().withPosition( x2, y2, z);
        geometry.addVertex().withPosition( x1, y2, z);
        geometry.addVertex().withPosition( x1, y1, z);
        geometry.addVertex().withPosition( x2, y1, z);

        return geometry;
    }



    private Matrix4f createModelMatrix(Dimension screenDimension, LayoutStrategy<C> layoutManager, Matrix4f modelMatrix) {
        float alignX = layoutManager.getLayoutAlignmentX(this); // [0..1]
        float alignY = layoutManager.getLayoutAlignmentY(this); // [0..1]
        // origin of the subcomponents is top left
        alignY += this.height / screenDimension.getHeight();
        // screen range: [-1 .. +1] x to the right, y upwards   z in the range of [-1..+1]
        return MathTool.convert(new Vector3f(alignX * 2f - 1f, 1f - alignY * 2f, 0f), modelMatrix);
    }



    protected class Text extends AbstractRenderElement {

        Text() {
            shaderId = Resources.PLAIN_SHADER_ID;
            renderConfig = RenderConfigImpl.BLENDING_ON;
        }

        public void setGeometry(IGeometry geometry) {
            this.geometry = geometry;
        }

        public void setTexture(ITexture texture) {
            uniforms.put(ShaderParser.TEXTURE01, new TextureValue(texture));
        }

    }

    protected class Border extends AbstractRenderElement {

        Border() {
            shaderId = Resources.TWOD_SHADER_ID;
            renderConfig = RenderConfigImpl.DEFAULT;
        }

        public void setGeometry(IGeometry geometry) {
            this.geometry = geometry;
        }

    }

}

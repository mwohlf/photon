package net.wohlfart.photon.hud;

import javax.vecmath.Matrix4f;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.hud.layout.IComponent;
import net.wohlfart.photon.hud.layout.IContainer;
import net.wohlfart.photon.hud.layout.LayoutConstraints;
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
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.resources.ResourceManager;
import net.wohlfart.photon.shader.Matrix4fValue;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.TextureValue;
import net.wohlfart.photon.tools.Dimension;
import net.wohlfart.photon.tools.Perspective;

public class Label extends AbstractRenderElement implements IComponent {

	protected final float fontSize = 12f;

	protected final FontIdentifier fontIdentifier = FontIdentifier.create("fonts/liberation/LiberationMono-Regular.ttf", fontSize);

	protected String text;

	protected boolean isDirty = true;

	protected IContainer<? extends LayoutConstraints> container;

	protected ICharData charData;

	protected Perspective perspective;

	private float height;

	private float width;

	public Label withText(String text) {
		this.text = text;
		this.geometry = null;
		return this;
	}

	@Override
	public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
		this.perspective = renderer.getPerspective();
		assert text != null : "need to set a text if you use Label";
		if (isDirty) {
			refresh();
			isDirty = false;
		}
		renderer.setRenderConfig(shaderId, renderConfig);
		renderer.setUniformValues(getUniformValues());
		renderer.drawGeometry(getGeometry());
		renderer.renderChildren(tree);
	}

	private void refresh() {
		shaderId = ShaderIdentifier.create("shader/texture.vert", "shader/texture.frag");
		renderConfig = IRenderConfig.BLENDING_ON;
		charData = ResourceManager.loadResource(ICharData.class, fontIdentifier);
		geometry = createTextGeometry();

		getUniformValues().put(ShaderParser.TEXTURE01, new TextureValue(charData.getCharTexture()));

		Matrix4f modelMatrix = createModelMatrix(container.getLayoutManager(), getModel2WorldMatrix());
		getUniformValues().put(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, new Matrix4fValue(modelMatrix));
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public void setParent(IContainer<? extends LayoutConstraints> container) {
		this.container = container;
	}

	@Override
	public IContainer<? extends LayoutConstraints> getParent() {
		return container;
	}

	private Matrix4f createModelMatrix(LayoutStrategy<?> layoutManager, Matrix4f dest) {
		//float alignX = layoutManager.getLayoutAlignmentX(this); // [0..1]
		//float alignY = layoutManager.getLayoutAlignmentY(this); // [0..1]
		// origin of the subcomponents is top left
		// alignY += getHeight() / perspective.getScreenDimension().getHeight();
		// screen range: [-1 .. +1] x to the right, y upwards   z in the range of [-1..+1]
		// return MathTool.convert(new Vector3f(alignX * 2f - 1f, 1f - alignY * 2f, 0f), modelMatrix);


		Dimension dim = perspective.getScreenDimension();
		float pixel = Math.max(dim.getWidth(), dim.getHeight());
		float z = perspective.getNearPlane();
		float aspect = perspective.getAspectRatio();

		// x column, inoming: 0...dim.x outgoing: -1 ... +1
		dest.m00 = 1 / dim.getHeight();
		dest.m01 = 0;
		dest.m02 = 0;
		dest.m03 = 0;

		dest.m10 = 0;
		dest.m11 = 1 / dim.getHeight();
		dest.m12 = 0;
		dest.m13 = 0;

		dest.m20 = 0;
		dest.m21 = 0;
		dest.m22 = 0;
		dest.m23 = 0;

		dest.m30 = -0.3f;
		dest.m31 = +0.3f;
		dest.m32 = -z; // fix at the near frustum
		dest.m33 = 1f; // need to be non zero so the next matrix can do a move

		return dest;
	}

	// the geometry is pixel based, any transformations are done with the
	// model2world matrix
	private IGeometry createTextGeometry() {

		ICharAtlas charAtlas = charData.getCharAtlas();
		Geometry geometry = new Geometry(VertexFormat.VERTEX_P3C0N0T2, StreamFormat.TRIANGLES);

		int n = 0;
		float screenX = 0;
		float screenY = 0;
		width = 0;
		height = 0;

		float atlasWidth = charAtlas.getImage().getWidth();
		float atlasHeight = charAtlas.getImage().getHeight();

		float z = 0; // doesn't matter

		for (char c : text.toCharArray()) {

			CharInfo info = charAtlas.getCharInfo(c);
			if (info == null) {
				info = charAtlas.getCharInfo(CharAtlasFactory.NULL_CHAR);
			}

			float x1 = screenX - info.getG();
			float x2 = screenX - info.getG() + info.getWidth() ;
			float y1 = screenY + 0;
			float y2 = screenY + info.getHeight();

			// texture coordinates are in the [0...1] interval
			final float s1 = (info.getX()) / atlasWidth;
			final float s2 = (info.getX() + info.getWidth()) / atlasWidth;
			final float t1 = (info.getY()) / atlasHeight;
			final float t2 = (info.getY() + info.getHeight()) / atlasHeight;

			geometry.addVertex().withPosition(x2, y2, z).withTexture(s2, t1);
			geometry.addVertex().withPosition(x1, y2, z).withTexture(s1, t1);
			geometry.addVertex().withPosition(x1, y1, z).withTexture(s1, t2);
			geometry.addVertex().withPosition(x2, y1, z).withTexture(s2, t2);
			geometry.addRectangle(n + 0, n + 1, n + 2, n + 3);

			// one char forward
			screenX += info.getWidth() - info.getG();
			n += 4; // four vertices forward

			height = Math.max(height, info.getHeight());
			width = screenX;
		}

		return geometry;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [zOrder=" + getSortToken().getZOrder()
				+ " container=" + container
				+ ", renderConfig=" + renderConfig + "]";
	}

}

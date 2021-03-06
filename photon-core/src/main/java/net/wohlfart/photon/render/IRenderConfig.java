package net.wohlfart.photon.render;

import javax.media.opengl.GL2ES2;

import net.wohlfart.photon.render.RenderConfigImpl.Blending;
import net.wohlfart.photon.render.RenderConfigImpl.Clear;
import net.wohlfart.photon.render.RenderConfigImpl.ClearColor;
import net.wohlfart.photon.render.RenderConfigImpl.ClearDepth;
import net.wohlfart.photon.render.RenderConfigImpl.ColorMask;
import net.wohlfart.photon.render.RenderConfigImpl.DepthTest;
import net.wohlfart.photon.render.RenderConfigImpl.FaceCulling;
import net.wohlfart.photon.render.RenderConfigImpl.PointSprite;
import net.wohlfart.photon.render.RenderConfigImpl.SissorTest;
import net.wohlfart.photon.render.RenderConfigImpl.StencilTest;


public interface IRenderConfig<T extends IRenderConfig<T>> {

	// this is for testing and debugging
	public static final RenderConfigImpl DEFAULT = new RenderConfigImpl(
			Clear.OFF,
			Blending.OFF,
			ClearColor.BLUE,
			PointSprite.OFF,
			ClearDepth.ONE,
			ColorMask.ON,
			DepthTest.OFF,
			FaceCulling.OFF,
			SissorTest.OFF,
			StencilTest.OFF);

	public static final RenderConfigImpl CLEAR = new RenderConfigImpl(
			Clear.ON,
			Blending.OFF,
			ClearColor.BLUE,
			PointSprite.OFF,
			ClearDepth.ONE,
			ColorMask.ON,
			DepthTest.OFF,
			FaceCulling.OFF,
			SissorTest.OFF,
			StencilTest.OFF);

	public static final RenderConfigImpl BLENDING_ON = new RenderConfigImpl(
			Clear.OFF,
			Blending.ON,
			ClearColor.BLUE,
			PointSprite.OFF,
			ClearDepth.ONE,
			ColorMask.ON,
			DepthTest.GL_LEQUAL,
			FaceCulling.BACK,
			SissorTest.OFF,
			StencilTest.OFF);

	public static final RenderConfigImpl ATMOSPHERE_FRONT = new RenderConfigImpl(
			Clear.OFF,
			Blending.ON,
			ClearColor.BLUE,
			PointSprite.OFF,
			ClearDepth.ONE,
			ColorMask.ON,
			DepthTest.GL_LEQUAL,
			FaceCulling.FRONT,
			SissorTest.OFF,
			StencilTest.OFF);

	public static final RenderConfigImpl DEFAULT_3D = new RenderConfigImpl(
			Clear.OFF,
			Blending.OFF,
			ClearColor.BLUE,
			PointSprite.OFF,
			ClearDepth.ONE,
			ColorMask.ON,
			DepthTest.GL_LEQUAL,
			FaceCulling.BACK,                   // use BACK for production
			SissorTest.OFF,
			StencilTest.OFF);

	public static final RenderConfigImpl SPRITE_CLOUD = new RenderConfigImpl(
			Clear.OFF,
			Blending.OFF,
			ClearColor.BLUE,
			PointSprite.SIZE_FROM_SHADER,
			ClearDepth.ONE,
			ColorMask.ON,
			DepthTest.GL_LEQUAL,
			FaceCulling.BACK,
			SissorTest.OFF,
			StencilTest.OFF);

	public static final RenderConfigImpl SKYBOX = new RenderConfigImpl(
			Clear.OFF,
			Blending.OFF,
			ClearColor.BLUE,
			PointSprite.OFF,
			ClearDepth.ONE,
			ColorMask.ON,
			DepthTest.GL_LEQUAL,
			FaceCulling.BACK,
			SissorTest.OFF,
			StencilTest.OFF);

	public static final RenderConfigImpl NULL_CONFIG = new RenderConfigImpl();


    public T updateValues(GL2ES2 gl, T oldState);

    // FIXME: remove this method
    public boolean isTranslucent();

}

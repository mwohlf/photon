package net.wohlfart.photon.render;

import javax.media.opengl.GL2;

import net.wohlfart.photon.render.RenderConfigImpl.Blending;
import net.wohlfart.photon.render.RenderConfigImpl.ClearColor;
import net.wohlfart.photon.render.RenderConfigImpl.ClearDepth;
import net.wohlfart.photon.render.RenderConfigImpl.ColorMask;
import net.wohlfart.photon.render.RenderConfigImpl.DepthTest;
import net.wohlfart.photon.render.RenderConfigImpl.FaceCulling;
import net.wohlfart.photon.render.RenderConfigImpl.PrimitiveRestartIndex;
import net.wohlfart.photon.render.RenderConfigImpl.SissorTest;
import net.wohlfart.photon.render.RenderConfigImpl.StencilTest;


public interface IRenderConfig<T extends IRenderConfig<T>> {

	public static final RenderConfigImpl DEFAULT = new RenderConfigImpl(
			Blending.OFF,
			ClearColor.BLUE,
			ClearDepth.ONE,
			ColorMask.ON,
			DepthTest.OFF,
			FaceCulling.OFF,
			PrimitiveRestartIndex.MAX_INT,
			SissorTest.OFF,
			StencilTest.OFF);

	public static final RenderConfigImpl BLENDING_ON = new RenderConfigImpl(
			Blending.ON,
			ClearColor.GREY,
			ClearDepth.ONE,
			ColorMask.ON,
			DepthTest.GL_LEQUAL,
			FaceCulling.OFF,
			PrimitiveRestartIndex.MAX_INT,
			SissorTest.OFF,
			StencilTest.OFF);

	public static final RenderConfigImpl DEFAULT_3D = new RenderConfigImpl(
			Blending.OFF,
			ClearColor.GREY,
			ClearDepth.ONE,
			ColorMask.ON,
			DepthTest.GL_LEQUAL,
			FaceCulling.BACK,                   // use BACK for production
			PrimitiveRestartIndex.MAX_INT,
			SissorTest.OFF,
			StencilTest.OFF);

	public static final RenderConfigImpl SKYBOX = new RenderConfigImpl(
			Blending.OFF,
			ClearColor.GREY,
			ClearDepth.ONE,
			ColorMask.ON,
			DepthTest.GL_LEQUAL,
			FaceCulling.BACK,
			PrimitiveRestartIndex.MAX_INT,
			SissorTest.OFF,
			StencilTest.OFF);

	public static final RenderConfigImpl NULL_CONFIG = new RenderConfigImpl();


    public T updateValues(GL2 gl2, T oldState);

    // FIXME: remove this method
    public boolean isTranslucent();

}

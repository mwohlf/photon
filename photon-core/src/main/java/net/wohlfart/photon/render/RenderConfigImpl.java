package net.wohlfart.photon.render;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;

import net.wohlfart.photon.tools.EnumWeights;


/**
 *  see: "3D Engine Design for Virtual Globes"
 */
public class RenderConfigImpl implements IRenderConfig<RenderConfigImpl> {

	// need to setup the weights first since they are used in the constructor...
	private static final EnumWeights WEIGHTS = new EnumWeights(
			Clear.class,
			Blending.class,   // opaque objects first front to back, then the transparent back to front
			DepthTest.class,
			PointSprite.class,
			// probably never change:
			ColorMask.class,
			SissorTest.class,
			StencilTest.class,
			// values should never change:
			ClearDepth.class,
			ClearColor.class,
			FaceCulling.class
			);

	private final int hash;


	private final Clear clear;

	private final Blending blending;

	private final DepthTest depthTest;

	private final PointSprite pointSprite;

	private final ColorMask colorMask;

	private final SissorTest scissorTest;

	private final StencilTest stencilTest;

	private final ClearDepth clearDepth;

	private final ClearColor clearColor;

	private final FaceCulling faceCulling;



	RenderConfigImpl() {
		this.clear = null;
		this.blending = null;
		this.depthTest = null;
		this.pointSprite = null;
		this.colorMask = null;
		this.scissorTest = null;
		this.stencilTest = null;
		this.clearDepth = null;
		this.clearColor = null;
		this.faceCulling = null;
		hash = Integer.MIN_VALUE;
	}

	RenderConfigImpl(
			Clear clear,
			Blending blending,
			ClearColor clearColor,
			PointSprite pointSprite,
			ClearDepth clearDepth,
			ColorMask colorMask,
			DepthTest depthTest,
			FaceCulling faceCulling,
			SissorTest scissorTest,
			StencilTest stencilTest) {

		this.clear = clear;
		this.blending = blending;
		this.clearColor = clearColor;
		this.pointSprite = pointSprite;
		this.clearDepth = clearDepth;
		this.colorMask = colorMask;
		this.depthTest = depthTest;
		this.faceCulling = faceCulling;
		this.scissorTest = scissorTest;
		this.stencilTest = stencilTest;

		hash = WEIGHTS.getWeightFor(
				clear,
				blending,
				clearColor,
				clearDepth,
				colorMask,
				depthTest,
				faceCulling,
				scissorTest,
				stencilTest);
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if ((object == null) || (this.getClass() != object.getClass())) {
			return false;
		}
		RenderConfigImpl that = (RenderConfigImpl) object;
		return this.hash == that.hash;
	}

	@Override
	public RenderConfigImpl updateValues(GL2ES2 gl, RenderConfigImpl that) {
		if (that.clear != this.clear) {
			clear.setValue(gl);
		}
		if (that.blending != this.blending) {
			blending.setValue(gl);
		}
		if (that.clearColor != this.clearColor) {
			clearColor.setValue(gl);
		}
		if (that.pointSprite != this.pointSprite) {
			pointSprite.setValue(gl);
		}
		if (that.clearDepth != this.clearDepth) {
			clearDepth.setValue(gl);
		}
		if (that.colorMask != this.colorMask) {
			colorMask.setValue(gl);
		}
		if (that.depthTest != this.depthTest) {
			depthTest.setValue(gl);
		}
		if (that.faceCulling != this.faceCulling) {
			faceCulling.setValue(gl);
		}
		if (that.scissorTest != this.scissorTest) {
			scissorTest.setValue(gl);
		}
		if (that.stencilTest != stencilTest) {
			stencilTest.setValue(gl);
		}
		return this;
	}


	@Override
	public boolean isTranslucent() {
		return blending == Blending.ON;
	}



	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [clear=" + clear
	            + ", blending=" + blending
				+ ", clearColor=" + clearColor
				+ ", clearDepth=" + clearDepth
				+ ", colorMask=" + colorMask
				+ ", depthTest=" + depthTest
				+ ", hash=" + hash + "]";
	}



	interface RenderProperty {
		// implementations get called with the old property value
		void setValue(GL2ES2 gl);
	}

	public enum Clear implements RenderProperty {
		OFF {
			@Override
			public void setValue(GL2ES2 gl) {
				// do nothing
			}
		},
		ON {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);
			}
		}
	}


	public enum Blending implements RenderProperty {
		OFF {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glDisable(GL.GL_BLEND);
			}
		},
		ON {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glEnable(GL.GL_BLEND);
				gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			}
		}
	}

	public enum ClearColor implements RenderProperty {
		BLACK {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glClearColor(0f, 0f, 0f, 0.5f);
			}
		},
		GREY {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

			}
		},
		BLUE {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glClearColor(0.0f, 0.0f, 0.5f, 0.5f);

			}
		},
		WHITE {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glClearColor(1f, 1f, 1f, 0.5f);

			}
		}
	}

	public enum ClearDepth implements RenderProperty {
		ONE {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glClearDepth(1f);
			}
		}
	}

	public enum ColorMask implements RenderProperty {
		ON {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glColorMask(true, true, true, true);
			}
		}
	}

	public enum PointSprite implements RenderProperty {
		SIZE_FROM_SHADER {
			@Override
			public void setValue(GL2ES2 gl) {
		           gl.glEnable(GL2ES1.GL_POINT_SPRITE);
		           gl.glEnable(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
			}
		},
		OFF {
			@Override
			public void setValue(GL2ES2 gl) {
		           gl.glDisable(GL2ES1.GL_POINT_SPRITE);
		           gl.glDisable(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
			}
		}

	}

	public enum DepthTest implements RenderProperty {
		GL_LEQUAL {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glEnable(GL.GL_DEPTH_TEST);
				gl.glDepthFunc(GL.GL_LEQUAL);
			}
		},
		GL_LESS {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glEnable(GL.GL_DEPTH_TEST);
				gl.glDepthFunc(GL.GL_LESS);

			}
		},
		OFF {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glDisable(GL.GL_DEPTH_TEST);
			}
		};
	}

	public enum FaceCulling implements RenderProperty {
		BACK { // the default
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glEnable(GL.GL_CULL_FACE);
				gl.glCullFace(GL.GL_BACK);
			}
		},
		FRONT {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glEnable(GL.GL_CULL_FACE);
				gl.glCullFace(GL.GL_FRONT);

			}
		},
		FRONT_AND_BACK {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glEnable(GL.GL_CULL_FACE);
				gl.glCullFace(GL.GL_FRONT_AND_BACK);

			}
		},
		OFF {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glDisable(GL.GL_CULL_FACE);
			}
		};
	}

	public enum SissorTest implements RenderProperty {
		OFF {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glDisable(GL.GL_DEPTH_TEST);
			}
		}
	}

	public enum StencilTest implements RenderProperty {
		OFF {
			@Override
			public void setValue(GL2ES2 gl) {
				gl.glDisable(GL.GL_DEPTH_TEST);
			}
		}
	}

}

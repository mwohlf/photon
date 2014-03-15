package net.wohlfart.photon.render;

import javax.media.opengl.GL2;

import net.wohlfart.photon.tools.EnumWeights;


/**
 *  see: "3D Engine Design for Virtual Globes"
 */
public class RenderConfigImpl implements IRenderConfig<RenderConfigImpl> {
	

	// need to setup the weights first since they are used in the constructor...
	private static final EnumWeights WEIGHTS = new EnumWeights(
			Blending.class,   // opaque objects first front to back, then the transparent back to front
			DepthTest.class,
			// probably never change:
			ColorMask.class,
			SissorTest.class,
			StencilTest.class,
			// values should never change:
			ClearDepth.class,
			ClearColor.class,
			FaceCulling.class,
			PrimitiveRestartIndex.class);


	private final Blending blending;

	private final ClearColor clearColor;

	private final ClearDepth clearDepth;

	private final ColorMask colorMask;

	private final DepthTest depthTest;

	private final FaceCulling faceCulling;

	private final PrimitiveRestartIndex primitiveRestartIndex;

	private final SissorTest scissorTest;

	private final StencilTest stencilTest;


	private final int hash;


	RenderConfigImpl() {
		this.blending = null;
		this.clearColor = null;
		this.clearDepth = null;
		this.colorMask = null;
		this.depthTest = null;
		this.faceCulling = null;
		this.primitiveRestartIndex = null;
		this.scissorTest = null;
		this.stencilTest = null; 
		hash = Integer.MIN_VALUE;
	}

	RenderConfigImpl(
			Blending blending,
			ClearColor clearColor,
			ClearDepth clearDepth,
			ColorMask colorMask,
			DepthTest depthTest,
			FaceCulling faceCulling,
			PrimitiveRestartIndex primitiveRestartIndex,
			SissorTest scissorTest,
			StencilTest stencilTest) {

		this.blending = blending;
		this.clearColor = clearColor;
		this.clearDepth = clearDepth;
		this.colorMask = colorMask;
		this.depthTest = depthTest;
		this.faceCulling = faceCulling;
		this.primitiveRestartIndex = primitiveRestartIndex;
		this.scissorTest = scissorTest;
		this.stencilTest = stencilTest; 

		hash = WEIGHTS.getWeightFor(blending,
				clearColor,
				clearDepth,
				colorMask,
				depthTest,
				faceCulling,
				primitiveRestartIndex,
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
	public RenderConfigImpl updateValues(GL2 gl2, RenderConfigImpl that) {
		if (that.blending != this.blending) {
			blending.setValue(gl2);
		}
		if (that.clearColor != this.clearColor) {
			clearColor.setValue(gl2);
		}
		if (that.clearDepth != this.clearDepth) {
			clearDepth.setValue(gl2);
		}
		if (that.colorMask != this.colorMask) {
			colorMask.setValue(gl2);
		}
		if (that.depthTest != this.depthTest) {
			depthTest.setValue(gl2);      
		}
		if (that.faceCulling != this.faceCulling) {
			faceCulling.setValue(gl2);
		}
		if (that.primitiveRestartIndex != this.primitiveRestartIndex) {
			primitiveRestartIndex.setValue(gl2);
		}
		if (that.scissorTest != this.scissorTest) {
			scissorTest.setValue(gl2);
		}
		if (that.stencilTest != stencilTest) {
			stencilTest.setValue(gl2);
		}
		return this;
	}


	@Override
	public boolean isTranslucent() {
		return blending == Blending.ON;
	}



	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [blending=" + blending 
				+ ", clearColor=" + clearColor 
				+ ", clearDepth=" + clearDepth 
				+ ", colorMask=" + colorMask
				+ ", depthTest=" + depthTest 
				+ ", hash=" + hash + "]";
	}



	interface RenderProperty {
		// implementations get called with the old property value
		void setValue(GL2 gl2);
	} 


	public enum Blending implements RenderProperty {
		OFF {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glDisable(GL2.GL_BLEND);
			}
		},
		ON {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glEnable(GL2.GL_BLEND);
				gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
			}
		}
	}

	public enum ClearColor implements RenderProperty {
		BLACK {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glClearColor(0f, 0f, 0f, 0.5f);
			}
		},
		GREY {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

			}
		},
		BLUE {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glClearColor(0.0f, 0.0f, 0.5f, 0.5f);

			}
		},
		WHITE {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glClearColor(1f, 1f, 1f, 0.5f);

			}
		}
	}

	public enum ClearDepth implements RenderProperty {
		ONE {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glClearDepth(1f);
			}
		}
	}

	public enum ColorMask implements RenderProperty {
		ON {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glColorMask(true, true, true, true);  
			}
		}
	}

	public enum DepthTest implements RenderProperty {
		GL_LEQUAL {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glEnable(GL2.GL_DEPTH_TEST);
				gl2.glDepthFunc(GL2.GL_LEQUAL);           
			}                 
		},
		GL_LESS {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glEnable(GL2.GL_DEPTH_TEST);
				gl2.glDepthFunc(GL2.GL_LESS);           

			}                 
		},
		OFF {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glDisable(GL2.GL_DEPTH_TEST);
			}
		};              
	}

	public enum FaceCulling implements RenderProperty {
		BACK { // the default
			@Override
			public void setValue(GL2 gl2) {
				gl2.glEnable(GL2.GL_CULL_FACE);
				gl2.glCullFace(GL2.GL_BACK);
			}            
		},
		FRONT {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glEnable(GL2.GL_CULL_FACE);
				gl2.glCullFace(GL2.GL_FRONT);

			}                
		},
		FRONT_AND_BACK {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glEnable(GL2.GL_CULL_FACE);
				gl2.glCullFace(GL2.GL_FRONT_AND_BACK);

			}   
		},
		OFF {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glDisable(GL2.GL_CULL_FACE);
			}
		};          
	}

	public enum PrimitiveRestartIndex implements RenderProperty {
		MAX_INT {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glPrimitiveRestartIndex(Integer.MAX_VALUE);
			}
		}
	}

	public enum SissorTest implements RenderProperty {
		OFF {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glDisable(GL2.GL_DEPTH_TEST);
			}
		}
	}

	public enum StencilTest implements RenderProperty {
		OFF {
			@Override
			public void setValue(GL2 gl2) {
				gl2.glDisable(GL2.GL_DEPTH_TEST);
			}
		}
	}

}

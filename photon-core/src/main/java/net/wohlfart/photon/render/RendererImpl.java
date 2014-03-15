package net.wohlfart.photon.render;

import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.media.opengl.GLAutoDrawable;

import net.wohlfart.photon.IGraphicContext;
import net.wohlfart.photon.graph.Tree;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.UniformHandle.UniformValue;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * delegates the low level work to the graphic context
 * just responsible for calling the render cache's methods in the right order
 */
public class RendererImpl implements IRenderer, IGraphicContext {
	protected static final Logger LOGGER = LoggerFactory.getLogger(RendererImpl.class);

	protected IGraphicContext delegate;
	protected volatile boolean debug;

	@Inject
	public RendererImpl() {
	}

	public void setGfxContext(IGraphicContext gfxCtx) {
		this.delegate = gfxCtx;
	}

    @Override
    public void setDebugMode(boolean debug) {
        this.debug = debug;
    }

    @Override
    public void renderParent(Tree<IRenderNode> tree) {
        final IRenderNode node = tree.getValue();
        if (debug) { LOGGER.error("rendering: {}", node); }
        node.accept(this, tree);
    }

    @Override
    public void renderChildren(Tree<IRenderNode> tree) {
        final Iterator<? extends Tree<IRenderNode>> iter = tree.getChildren();
        while (iter.hasNext()) {
            if (debug) { LOGGER.error(" {"); }
            renderParent(iter.next());
            if (debug) { LOGGER.error(" }"); }
        }
    }




    @Override
    public IGraphicContext init(GLAutoDrawable drawable) {
    	return delegate.init(drawable);
    }

	@Override
	public void setRenderConfig(ShaderIdentifier shaderId, IRenderConfig<RenderConfigImpl> newConfig) {
		delegate.setRenderConfig(shaderId, newConfig);
	}

	@Override
	public void setUniformValues(Map<String, ITextureIdentifier> textures, Map<String, UniformValue> uniformValues) {
		delegate.setUniformValues(textures, uniformValues);
	}

	@Override
	public void drawGeometry(IGeometry geometry) {
		delegate.drawGeometry(geometry);
	}

}

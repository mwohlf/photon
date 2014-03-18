package net.wohlfart.photon.render;

import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.media.nativewindow.util.Dimension;

import net.wohlfart.photon.IGraphicContext;
import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.UniformHandle.IUniformValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * delegates the low level work to the graphic context
 * just responsible for calling the render cache's methods in the right order
 */
public class RendererImpl implements IRenderer {
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
    public void renderParent(ITree<IRenderNode> tree) {
        final IRenderNode node = tree.getValue();
        if (debug) { LOGGER.error("rendering: {}", node); }
        node.accept(this, tree);
    }

    @Override
    public void renderChildren(ITree<IRenderNode> tree) {
        final Iterator<? extends ITree<IRenderNode>> iter = tree.getChildren();
        while (iter.hasNext()) {
            if (debug) { LOGGER.error(" {"); }
            renderParent(iter.next());
            if (debug) { LOGGER.error(" }"); }
        }
    }

	@Override
	public void setRenderConfig(ShaderIdentifier shaderId, IRenderConfig<RenderConfigImpl> newConfig) {
		delegate.setRenderConfig(shaderId, newConfig);
	}

	@Override
	public void setUniformValues(Map<String, IUniformValue> uniformValues) {
		delegate.setUniformValues(uniformValues);
	}

	@Override
	public void drawGeometry(IGeometry geometry) {
		delegate.drawGeometry(geometry);
	}

	@Override
	public Dimension getDimension() {
		return delegate.getDimension(); // FIXME: get rid of this method
	}

	@Override
	public void setFrameBuffer(IFrameBuffer frameBuffer) {
		delegate.setFrameBuffer(frameBuffer);
	}

}

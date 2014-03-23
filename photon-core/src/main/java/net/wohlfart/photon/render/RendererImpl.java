package net.wohlfart.photon.render;

import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import net.wohlfart.photon.IGraphicContext;
import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.shader.IUniformValue;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.tools.Dimension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * delegates the low level work to the graphic context
 * just responsible for calling the render cache's methods in the right order
 */
public class RendererImpl implements IRenderer {
	protected static final Logger LOGGER = LoggerFactory.getLogger(RendererImpl.class);

	protected IGraphicContext graphicContext;
	protected volatile boolean debug;


	public void setGfxContext(IGraphicContext gfxCtx) {
		this.graphicContext = gfxCtx;
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
		graphicContext.setRenderConfig(shaderId, newConfig);
	}

	@Override
	public void setUniformValues(Map<String, IUniformValue> uniformValues) {
		graphicContext.setUniformValues(uniformValues);
	}

	@Override
	public void drawGeometry(IGeometry geometry) {
		graphicContext.drawGeometry(geometry);
	}

	@Override
	public Dimension getDimension() {
		return graphicContext.getDimension(); // FIXME: get rid of this method
	}

	@Override
	public void setFrameBuffer(IFrameBuffer frameBuffer) {
		graphicContext.setFrameBuffer(frameBuffer);
	}

}

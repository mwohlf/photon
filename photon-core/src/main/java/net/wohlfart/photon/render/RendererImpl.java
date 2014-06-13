package net.wohlfart.photon.render;

import java.util.Collection;
import java.util.Iterator;

import net.wohlfart.photon.IGraphicContext;
import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.shader.IShaderProgram.IShaderProgramIdentifier;
import net.wohlfart.photon.shader.IUniformValue;
import net.wohlfart.photon.tools.Perspective;

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
	protected volatile int inset = 0;


	public void setGfxContext(IGraphicContext gfxCtx) {
		this.graphicContext = gfxCtx;
	}

    @Override
    public void setDebugMode(boolean debug) {
        this.debug = debug;
        this.inset = 0;
    }

    @Override
    public void renderParent(ITree<IRenderNode> tree) {
        final IRenderNode node = tree.getValue();
        if (debug) {
        	print(node);
        }
        node.accept(this, tree);
    }

    @Override
    public void renderChildren(ITree<IRenderNode> tree) {
        final Iterator<? extends ITree<IRenderNode>> iter = tree.getChildren();
        while (iter.hasNext()) {
        	if (debug) {
        		inset++;
        	}
            renderParent(iter.next());
            if (debug) {
            	inset--;
            }
        }
    }

	@Override
	public void setRenderConfig(IShaderProgramIdentifier shaderId, IRenderConfig<RenderConfigImpl> newConfig) {
		graphicContext.setRenderConfig(shaderId, newConfig);
	}

	@Override
	public void addUniformValues(Collection<IUniformValue> uniformValues) {
		graphicContext.addUniformValues(uniformValues);
	}

	@Override
	public void drawGeometry(IGeometry geometry) {
		graphicContext.drawGeometry(geometry);
	}

	@Override
	public void setFrameBuffer(IFrameBuffer frameBuffer) {
		graphicContext.setFrameBuffer(frameBuffer);
	}

	@Override
	public Perspective getPerspective() {
		return graphicContext.getPerspective();
	}


    private void print(IRenderNode node) {
    	String string = "";
    	for (int i = 0; i < inset; i++) {
    		string += "  ";
    	}
    	LOGGER.error(string + String.format("node: %s", node));
    	LOGGER.error(string + String.format("node.sort: %s", node.getSortToken()));
    	LOGGER.error(string + String.format("context: %s", graphicContext));
    }

}

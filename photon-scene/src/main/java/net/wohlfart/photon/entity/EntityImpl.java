package net.wohlfart.photon.entity;

import java.util.Collection;
import java.util.HashSet;

import net.wohlfart.photon.render.AbstractRenderElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * simple example how to implement a Entity3D
 */
public class EntityImpl extends AbstractEntity {
    protected static final Logger LOGGER = LoggerFactory.getLogger(EntityImpl.class);

    protected final Collection<AbstractRenderElement> renderElements = new HashSet<AbstractRenderElement>();


    @Override
    public Collection<AbstractRenderElement> getRenderCommands() {
        return renderElements;
    }

    @Override
    public void setup() {
        // nothing to do
    }

    @Override
    public void destroy() {
        // nothing to do
    }

    static class RenderCommand extends AbstractRenderElement {
        // nothing to do
    }

};

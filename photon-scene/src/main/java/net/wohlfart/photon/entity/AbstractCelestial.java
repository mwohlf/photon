package net.wohlfart.photon.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.vecmath.Vector3d;

import net.wohlfart.photon.node.Corona;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.TextureIdentValue;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.tools.Quaternion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 * this class only needs a getRenderCommands() method
 */
public abstract class AbstractCelestial extends AbstractEntity  {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractCelestial.class);

	public static final ShaderIdentifier TEXTURE_SHADER_ID = ShaderIdentifier.create("shader/texture.vert", "shader/texture.frag");


    protected final Collection<AbstractRenderElement> renderCommands = new HashSet<>();

	private Corona corona;


    @Override
    public Collection<AbstractRenderElement> getRenderCommands() {
        return renderCommands;
    }

    @Override
    public void destroy() {
        // nothing to do
    }


    // -- setters

    @Override
    public AbstractCelestial withSize(float size) {
        super.withSize(size);
        if (corona != null) {
            this.corona.setPlanetSize(size);
        }
        return this;
    }

    @Override
    public AbstractCelestial withPosition(Vector3d position) {
        super.withPosition(position);
        return this;
    }

    @Override
    public AbstractCelestial withPosition(double x, double y, double z) {
        super.withPosition(x, y, z);
        return this;
    }

    @Override
    public AbstractCelestial withRotation(Quaternion rotation) {
        super.withRotation(rotation);
        return this;
    }

    public AbstractCelestial withCorona(Corona corona) {
    	this.corona = corona;
        this.corona.setPlanetSize(size);
        renderCommands.add(corona);
        if (sceneGraph != null) {
            sceneGraph.addRenderCommands(Collections.singleton(corona));
        }
        return this;
    }

    protected static class RenderCommand extends AbstractRenderElement {

        public RenderCommand(IGeometry g, ITextureIdentifier texture) {
            geometry = g;
            renderConfig = IRenderConfig.DEFAULT_3D;
            uniforms.put(ShaderParser.TEXTURE01, new TextureIdentValue(texture));
            shaderId = TEXTURE_SHADER_ID;
        }

        @Override
        public String toString() {
            return this.getClass().getName() + " [zOrder=" + zOrder
                    + " renderConfig=" + renderConfig + "]";
        }

    }

}

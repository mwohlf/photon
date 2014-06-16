package net.wohlfart.photon.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.vecmath.Vector3d;

import net.wohlfart.photon.ShaderIdent;
import net.wohlfart.photon.node.Corona;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.shader.IShaderProgram.IShaderProgramIdentifier;
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

    protected final Collection<AbstractRenderElement> renderCommands = new HashSet<AbstractRenderElement>();

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
        	this(g, texture, ShaderIdent.TEXTURE_SHADER_ID);
        }

        public RenderCommand(IGeometry g, ITextureIdentifier texture, IShaderProgramIdentifier shaderIdent) {
            geometry = g;
            renderConfig = IRenderConfig.DEFAULT_3D;
            uniforms.add(new TextureIdentValue(ShaderParser.TEXTURE01, texture));
            this.shaderIdent = shaderIdent;
        }

        @Override
        public String toString() {
            return this.getClass().getName() + " [zOrder=" + zOrder
                    + " renderConfig=" + renderConfig + "]";
        }

    }

}

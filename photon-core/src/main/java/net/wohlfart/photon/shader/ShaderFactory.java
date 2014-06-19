package net.wohlfart.photon.shader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import net.wohlfart.photon.resources.ResourceProducer;
import net.wohlfart.photon.resources.ResourceTool;
import net.wohlfart.photon.shader.IShaderProgram.IShaderProgramIdentifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public class ShaderFactory implements ResourceProducer<IShaderProgram, IShaderProgram.IShaderProgramIdentifier>{
	protected static final Logger LOGGER = LoggerFactory.getLogger(ShaderFactory.class);


	@Override
	public Class<IShaderProgram> flavour() {
		return IShaderProgram.class;
	}

	@Override
	public IShaderProgram produce(IShaderProgramIdentifier ident) {
		try {
			final URI vertUri = ident.getVertexShaderResource();
			final URI fragUri = ident.getFragmentShaderResource();
			return createShaderProgramImpl(vertUri, fragUri);
		} catch (Exception ex) {
			throw new IllegalStateException("Can't load shader for '" + ident + "'", ex);
		}
	}

	private IShaderProgram createShaderProgramImpl(URI vertUri, URI fragUri) throws IOException {
		return new ShaderProgram(readShaderCode(vertUri), readShaderCode(fragUri));
	}

    private String readShaderCode(URI uri) throws IOException {
    	LOGGER.info("reading shader program from {}", uri.getPath());
		final InputStream inputStream = ResourceTool.openStream(uri);
        final String string = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
        final ShaderParser shaderParser = new ShaderParser(string);
        return shaderParser.render();
    }

}

package net.wohlfart.photon.shader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import net.wohlfart.photon.resources.ResourceProducer;
import net.wohlfart.photon.resources.ResourceTool;
import net.wohlfart.photon.shader.IShaderProgram.IShaderProgramIdentifier;
import net.wohlfart.photon.shader.ShaderProgram;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public class ShaderFactory implements ResourceProducer<IShaderProgram, IShaderProgram.IShaderProgramIdentifier>{

	@Override
	public IShaderProgram produce(IShaderProgramIdentifier ident) {
		try {
			final URI vertUri = ident.getVertexShaderResource();
			final URI fragUri = ident.getFragmentShaderResource();
			return createShaderProgramImpl(vertUri, fragUri);
		} catch (IOException ex) {
			throw new IllegalStateException("Can't load shader for '" + ident + "'", ex);
		}  
	}

	private IShaderProgram createShaderProgramImpl(URI vertUri, URI fragUri) throws IOException {
		return new ShaderProgram(readShaderCode(vertUri), readShaderCode(fragUri));                                     
	}

    private String readShaderCode(URI uri) throws IOException {
		final InputStream inputStream = ResourceTool.openStream(uri);
        String string = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
        ShaderParser stringTemplate = new ShaderParser(string);
        return stringTemplate.render();
    }

}

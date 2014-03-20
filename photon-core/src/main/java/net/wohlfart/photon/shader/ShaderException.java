package net.wohlfart.photon.shader;

import net.wohlfart.photon.GenericException;

public class ShaderException extends GenericException {
	private static final long serialVersionUID = 1L;

	ShaderException(String string) {
        super(string);
    }

    ShaderException(String string, Exception ex) {
        super(string, ex);
    }

}

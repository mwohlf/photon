package net.wohlfart.photon.shader;

import net.wohlfart.photon.GenericException;

@SuppressWarnings("serial")
public class ShaderException extends GenericException {

    ShaderException(String string) {
        super(string);
    }

    ShaderException(String string, Exception ex) {
        super(string, ex);
    }

}

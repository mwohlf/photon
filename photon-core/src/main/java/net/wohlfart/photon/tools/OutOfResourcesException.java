package net.wohlfart.photon.tools;

import net.wohlfart.photon.GenericException;

public class OutOfResourcesException extends GenericException {
    private static final long serialVersionUID = 1L;

    public OutOfResourcesException(String message) {
        super(message);
    }

}

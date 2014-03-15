package net.wohlfart.photon.resources;

import java.io.IOException;

@SuppressWarnings("serial")
public class ResourceException extends RuntimeException {

    ResourceException(String string) {
        super(string);
    }

    ResourceException(String string, IOException ex) {
        super(string, ex);
    }

}

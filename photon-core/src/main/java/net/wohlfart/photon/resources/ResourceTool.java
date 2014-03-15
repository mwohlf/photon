package net.wohlfart.photon.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;

// class for dealing with URI
public final class ResourceTool {
    
    public static final String FILE_SCHEME = "file";
    public static final String PROC_SCHEME = "proc";
   

    private ResourceTool() {
        // a tools class with only static methods
    }
  
    public static BufferedImage readImage(URI uri) throws IOException {
        final String path = uri.getPath().substring(1);
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final URL url = classLoader.getResource(path);        
        return ImageIO.read(url);
    }
    
    public static InputStream openStream(URI uri) throws IOException {
        // we need a relative path for lookup in the jar version
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final URL url = classLoader.getResource(uri.getPath().substring(1));
        return url.openStream();
    }
    
}

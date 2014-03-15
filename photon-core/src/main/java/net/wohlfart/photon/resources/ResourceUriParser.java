package net.wohlfart.photon.resources;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;


// parsing resource URIs
public class ResourceUriParser {
    private Map<String, String> params = new LinkedHashMap<String, String>();
    private String path;

    public ResourceUriParser(URI uri) throws UnsupportedEncodingException {
        path = uri.getPath();
        String query = uri.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            params.put(pair.substring(0, idx), pair.substring(idx + 1));
        }
    }
    
    String getValue(String key) {
        return params.get(key);
    }
    
    public String getPath() {
        return path;
    }

    public float getFloat(String key) {
        return Float.parseFloat(getValue(key));
    }

    public long getLong(String key) {
        return Long.parseLong(getValue(key));
    }

    public String getString(String key) {
        return getValue(key);
    }
}

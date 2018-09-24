package com.alanmrace.jimzmlparser.obo;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceOBOLoader implements OBOLoader {
    private static final Logger LOGGER = Logger.getLogger(ResourceOBOLoader.class.getName());

    @Override
    public InputStream getInputStream(String resourcePath) {
        // Strip off the URL details if they exist
        if (resourcePath.contains("http://") || resourcePath.contains("https://")) {
            resourcePath = resourcePath.substring(resourcePath.lastIndexOf('/') + 1);
        }

        LOGGER.log(Level.FINER, "Parsing OBO /obo/{0}", resourcePath);

        return ResourceOBOLoader.class.getResourceAsStream("/obo/" + resourcePath);
    }
}

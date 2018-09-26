package com.alanmrace.jimzmlparser.obo;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceOBOLoader implements OBOLoader {
    private static final Logger LOGGER = Logger.getLogger(ResourceOBOLoader.class.getName());

    @Override
    public InputStream getInputStream(String location) {
        String filename = location.substring(location.lastIndexOf('/') + 1);

        try {
            InputStream in = ResourceOBOLoader.class.getResourceAsStream("/obo/" + filename);

            OBO.installOBO(in, filename);

            in.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to extract OBO for use later", ex);
        }

        return ResourceOBOLoader.class.getResourceAsStream("/obo/" + filename);
    }
}

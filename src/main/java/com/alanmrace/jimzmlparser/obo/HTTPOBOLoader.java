package com.alanmrace.jimzmlparser.obo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HTTPOBOLoader implements OBOLoader {
    /** Class logger. */
    private static final Logger LOGGER = Logger.getLogger(OBO.class.getName());

    @Override
    public InputStream getInputStream(String location) throws IOException {
        try {
            OBO.downloadOBO(location);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to download OBO for use later", ex);
        }

        return new URL(location).openStream();
    }
}

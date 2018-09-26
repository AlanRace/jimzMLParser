package com.alanmrace.jimzmlparser.obo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceOBOLoader implements OBOLoader {
    private static final Logger LOGGER = Logger.getLogger(ResourceOBOLoader.class.getName());

    @Override
    public InputStream getInputStream(String location) {
        String filename = location.substring(location.lastIndexOf('/') + 1);

        try {
            File ontologiesFolder = new File("Ontologies");
            if(!ontologiesFolder.exists())
                ontologiesFolder.mkdir();

            InputStream in = ResourceOBOLoader.class.getResourceAsStream("/obo/" + filename);

            File file = new File(ontologiesFolder, filename);

            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to extract OBO for use later", ex);
        }

        return ResourceOBOLoader.class.getResourceAsStream("/obo/" + filename);
    }
}

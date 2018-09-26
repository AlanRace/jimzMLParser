package com.alanmrace.jimzmlparser.obo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileOBOLoader implements OBOLoader {
    @Override
    public InputStream getInputStream(String location) throws IOException {
        // Look in the current folder for the file, if not, try the Ontologies folder

        location = location.substring(location.lastIndexOf('/') + 1);
        FileInputStream inputStream = null;

        File file = new File(location);

        if(file.isFile()) {
            inputStream = new FileInputStream(file);
        } else {
            file = new File("Ontologies", location);

            // If this fails, then we want an IOException to be thrown so that
            inputStream = new FileInputStream(file);
        }

        return inputStream;
    }
}

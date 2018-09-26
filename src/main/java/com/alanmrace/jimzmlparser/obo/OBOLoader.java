package com.alanmrace.jimzmlparser.obo;

import java.io.IOException;
import java.io.InputStream;

public interface OBOLoader {
    InputStream getInputStream(String location) throws IOException;
}

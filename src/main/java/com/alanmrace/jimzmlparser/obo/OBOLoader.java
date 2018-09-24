package com.alanmrace.jimzmlparser.obo;

import java.io.InputStream;

public interface OBOLoader {
    InputStream getInputStream(String location);
}

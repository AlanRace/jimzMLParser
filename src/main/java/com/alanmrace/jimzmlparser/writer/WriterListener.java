package com.alanmrace.jimzmlparser.writer;

/**
 *
 * @author alan.race
 */
public interface WriterListener {
    void start();
    void progress(long current);
    void end();
}

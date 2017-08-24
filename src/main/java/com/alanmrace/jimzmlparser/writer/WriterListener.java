/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

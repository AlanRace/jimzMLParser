/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.listener;

import com.alanmrace.jimzmlparser.mzml.MzMLTag;

/**
 *
 * @author Alan Race
 * @param <T>
 */
public interface ReferenceListener<T extends MzMLTag> {

    /**
     * Notified when a reference is modified, for example the addition or change of the 
     * dataProcessingRef attribute on a Spectrum.
     * 
     * @param reference
     * @return
     */
    T referenceModified(T reference);
}

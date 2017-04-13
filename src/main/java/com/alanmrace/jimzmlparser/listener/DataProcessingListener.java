/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.listener;

import com.alanmrace.jimzmlparser.mzml.DataProcessing;

/**
 *
 * @author Alan Race
 */
public interface DataProcessingListener {
    DataProcessing referenceCheck(DataProcessing processing);
}

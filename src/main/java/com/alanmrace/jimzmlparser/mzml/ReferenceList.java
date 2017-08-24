package com.alanmrace.jimzmlparser.mzml;

/**
 * Used when a reference is modified, for example the addition or change of the 
 * dataProcessingRef attribute on a Spectrum. The implementation must ensure that 
 * referenceModified returns the valid version of the reference (i.e. the one stored
 * within the list if it already exists, or the same reference passed in if not)
 * 
 * @author Alan Race
 * @param <T> Data type of the reference
 */
public interface ReferenceList<T extends ReferenceableTag> {

    /**
     * Check the list for a valid version of the supplied reference, or add it to 
     * the list and return the same value if one does not exist.
     * 
     * @param reference The reference that was either added or the new version of the modified reference
     * @return Return a valid reference to use after checking whether the reference exists already
     */
    T getValidReference(T reference);
}

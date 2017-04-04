package com.alanmrace.jimzmlparser.mzml;

public class CVList extends MzMLContentList<CV> {

    public CVList(int count) {
        super(count);
    }

    public CVList(CVList cvList) {
        super(cvList);
    }
    
    @Override
    public String getTagName() {
        return "cvList";
    }
}

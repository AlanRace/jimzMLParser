package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class DataProcessingList extends MzMLContentList<DataProcessing> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ArrayList<DataProcessing> dataProcessingList;

    public DataProcessingList(int count) {
        dataProcessingList = new ArrayList<DataProcessing>(count);
    }

    public DataProcessingList(DataProcessingList dpList, ReferenceableParamGroupList rpgList, SoftwareList softwareList) {
        this(dpList.size());

        for (DataProcessing dp : dpList) {
            dataProcessingList.add(new DataProcessing(dp, rpgList, softwareList));
        }
    }

    public DataProcessing getDataProcessing(String dataProcessingRef) {
        for (DataProcessing dp : dataProcessingList) {
            if (dp.getID().equals(dataProcessingRef)) {
                return dp;
            }
        }

        return null;
    }

    public DataProcessing getDataProcessing(int index) {
        return dataProcessingList.get(index);
    }

    public void addDataProcessing(DataProcessing dataProcessing) {
        dataProcessing.setParent(this);

        dataProcessingList.add(dataProcessing);
    }

    public void removeDataProcessing(int index) {
        DataProcessing removed = dataProcessingList.remove(index);
        removed.setParent(null);
    }

    public int size() {
        return dataProcessingList.size();
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/dataProcessing")) {
            if (dataProcessingList == null) {
                throw new UnfollowableXPathException("No dataProcessingList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            for (DataProcessing dataProcessing : dataProcessingList) {
                dataProcessing.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
            }
        }
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<dataProcessingList");
        output.write(" count=\"" + dataProcessingList.size() + "\"");
        output.write(">\n");

        for (DataProcessing dp : dataProcessingList) {
            dp.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</dataProcessingList>\n");
    }

    @Override
    public Iterator<DataProcessing> iterator() {
        return dataProcessingList.iterator();
    }

    @Override
    public String toString() {
        return "dataProcessingList";
    }

    @Override
    public String getTagName() {
        return "dataProcessingList";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(dataProcessingList != null)
            children.addAll(dataProcessingList);
        
        super.addChildrenToCollection(children);
    }
}

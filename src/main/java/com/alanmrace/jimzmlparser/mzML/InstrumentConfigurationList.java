package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class InstrumentConfigurationList extends MzMLContent implements Iterable<InstrumentConfiguration>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<InstrumentConfiguration> instrumentConfigurationList;

    public InstrumentConfigurationList(int count) {
        instrumentConfigurationList = new ArrayList<InstrumentConfiguration>(count);
    }

    public InstrumentConfigurationList(InstrumentConfigurationList icList, ReferenceableParamGroupList rpgList, ScanSettingsList ssList, SoftwareList softwareList) {
        this(icList.size());

        for (InstrumentConfiguration ic : icList) {
            instrumentConfigurationList.add(new InstrumentConfiguration(ic, rpgList, ssList, softwareList));
        }
    }

    public void addInstrumentConfiguration(InstrumentConfiguration instrumentConfiguration) {
        instrumentConfiguration.setParent(this);

        instrumentConfigurationList.add(instrumentConfiguration);
    }

    public void removeInstrumentConfiguration(int index) {
        InstrumentConfiguration removed = instrumentConfigurationList.remove(index);

        removed.setParent(null);
    }

    public InstrumentConfiguration getInstrumentConfiguration(String id) {
        for (InstrumentConfiguration instrumentConfiguration : instrumentConfigurationList) {
            if (instrumentConfiguration.getID().equals(id)) {
                return instrumentConfiguration;
            }
        }

        return null;
    }

    public InstrumentConfiguration getInstrumentConfiguration(int index) {
        if (index >= instrumentConfigurationList.size()) {
            return null;
        }

        return instrumentConfigurationList.get(index);
    }

    public int size() {
        return instrumentConfigurationList.size();
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/instrumentConfiguration")) {
            if (instrumentConfigurationList == null || instrumentConfigurationList.isEmpty()) {
                throw new UnfollowableXPathException("No instrumentConfigurationList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            for (InstrumentConfiguration ic : instrumentConfigurationList) {
                ic.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
            }
        }
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<instrumentConfigurationList");
        output.write(" count=\"" + instrumentConfigurationList.size() + "\"");
        output.write(">\n");

        for (InstrumentConfiguration ic : instrumentConfigurationList) {
            ic.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</instrumentConfigurationList>\n");
    }

    @Override
    public Iterator<InstrumentConfiguration> iterator() {
        return instrumentConfigurationList.iterator();
    }

    @Override
    public String toString() {
        return "instrumentConfigurationList";
    }

    @Override
    public String getTagName() {
        return "instrumentConfigurationList";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(instrumentConfigurationList != null)
            children.addAll(instrumentConfigurationList);
        
        super.addChildrenToCollection(children);
    }
}

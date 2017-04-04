package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;

public class ChromatogramList extends MzMLIDContentList<Chromatogram> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private DataProcessing defaultDataProcessingRef;

    public ChromatogramList(int count, DataProcessing defaultDataProcessingRef) {
        super(count);
        
        this.defaultDataProcessingRef = defaultDataProcessingRef;
    }

    public ChromatogramList(ChromatogramList chromatogramList, ReferenceableParamGroupList rpgList,
            DataProcessingList dpList, SourceFileList sourceFileList) {
        super(chromatogramList.size());

        if (chromatogramList.defaultDataProcessingRef != null && dpList != null) {
            for (DataProcessing dp : dpList) {
                if (chromatogramList.defaultDataProcessingRef.getID().equals(dp.getID())) {
                    this.defaultDataProcessingRef = dp;

                    break;
                }
            }
        }

        for (Chromatogram chromatogram : chromatogramList) {
            this.add(new Chromatogram(chromatogram, rpgList, dpList, sourceFileList));
        }
    }

    public DataProcessing getDefaultDataProcessingRef() {
        return defaultDataProcessingRef;
    }

    public void addChromatogram(Chromatogram chromatogram) {
        add(chromatogram);
    }
    
    public Chromatogram getChromatogram(int index) {
        return get(index);
    }
    
    public Chromatogram getChromatogram(String id) {
        return get(id);
    }
    
    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<chromatogramList");
        output.write(" count=\"" + list.size() + "\"");
        output.write(" defaultDataProcessingRef=\"" + XMLHelper.ensureSafeXML(defaultDataProcessingRef.getID()) + "\"");
        output.write(">\n");

        int index = 0;

        for (Chromatogram chromatogram : list) {
            chromatogram.outputXML(output, indent + 1, index++);
        }

        MzMLContent.indent(output, indent);
        output.write("</chromatogramList>\n");
    }

    @Override
    public String getTagName() {
        return "chromatogramList";
    }
}

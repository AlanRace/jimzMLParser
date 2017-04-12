package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoftwareList extends MzMLIDContentList<Software> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SoftwareList(int count) {
        super(count);
    }

    public SoftwareList(SoftwareList softwareList, ReferenceableParamGroupList rpgList) {
        this(softwareList.size());

        for (Software software : softwareList) {
            this.add(new Software(software, rpgList));
        }
    }

    public void addSoftware(Software software) {
        add(software);
    }

    public Software getSoftware(String id) {
        return get(id);
    }

    public Software getSoftware(int index) {
        return get(index);
    }

    public void removeSoftware(int index) {
        remove(index);
    }

    @Override
    public String getTagName() {
        return "softwareList";
    }
    
    public static SoftwareList create() {
        String version = "Unknown";
        
        // Get the version of the software from the properites file
        Properties prop = new Properties();
        InputStream in = SoftwareList.class.getResourceAsStream("/jimzMLParser.properties");
        
        try {
            prop.load(in);
            version = prop.getProperty("version");
            
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(SoftwareList.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SoftwareList softwareList = new SoftwareList(1);
        Software software = new Software("jimzMLParser", version);
        software.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(Software.customUnreleasedToolID)));
        
        softwareList.add(software);
        
        return softwareList;
    }
}

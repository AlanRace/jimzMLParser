package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ReferenceableParamGroupList extends MzMLContent implements Iterable<ReferenceableParamGroup>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<ReferenceableParamGroup> referenceableParamGroupList;

    public ReferenceableParamGroupList(int count) {
        referenceableParamGroupList = new ArrayList<ReferenceableParamGroup>(count);
    }

    public ReferenceableParamGroupList(ReferenceableParamGroupList rpgList) {
        referenceableParamGroupList = new ArrayList<ReferenceableParamGroup>(rpgList.size());

        for (ReferenceableParamGroup rpg : rpgList) {
            referenceableParamGroupList.add(new ReferenceableParamGroup(rpg));
        }
    }

    public void addReferenceableParamGroup(ReferenceableParamGroup rpg) {
        boolean exists = false;

        for (ReferenceableParamGroup currentRPG : referenceableParamGroupList) {
            if (currentRPG.getID().equals(rpg.getID())) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            rpg.setParent(this);
            referenceableParamGroupList.add(rpg);
        }
    }

    public void remove(int index) {
        ReferenceableParamGroup removed = referenceableParamGroupList.remove(index);
        removed.setParent(null);
    }

    public int size() {
        return referenceableParamGroupList.size();
    }

    public ReferenceableParamGroup getReferenceableParamGroup(String id) {
        for (ReferenceableParamGroup rpg : referenceableParamGroupList) {
            if (rpg.getID().equals(id)) {
                return rpg;
            }
        }

        return null;
    }

    public ReferenceableParamGroup getReferenceableParamGroup(int index) {
        return referenceableParamGroupList.get(index);
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<referenceableParamGroupList");
        output.write(" count=\"" + referenceableParamGroupList.size() + "\"");
        output.write(">\n");

        for (ReferenceableParamGroup rpg : referenceableParamGroupList) {
            rpg.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</referenceableParamGroupList>\n");
    }

    @Override
    public Iterator<ReferenceableParamGroup> iterator() {
        return referenceableParamGroupList.iterator();
    }

    @Override
    public String toString() {
        return "referenceableParamGroupList";
    }

    @Override
    public String getTagName() {
        return "referenceableParamGroupList";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(referenceableParamGroupList != null)
            children.addAll(referenceableParamGroupList);
        
        super.addChildrenToCollection(children);
    }
}

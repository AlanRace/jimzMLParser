package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class FileDescription extends MzMLContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // Required
    protected FileContent fileContent;

    // Optional
    protected SourceFileList sourceFileList;
    protected ArrayList<Contact> contacts;

    public FileDescription() {
        contacts = new ArrayList<Contact>();
    }

    public FileDescription(FileDescription fileDescription, ReferenceableParamGroupList rpgList) {
        fileContent = new FileContent(fileDescription.fileContent, rpgList);

        if (fileDescription.sourceFileList != null) {
            sourceFileList = new SourceFileList(fileDescription.sourceFileList, rpgList);
        }

        contacts = new ArrayList<Contact>(fileDescription.contacts.size());

        for (Contact contact : fileDescription.contacts) {
            contacts.add(new Contact(contact, rpgList));
        }
    }

    public void setFileContent(FileContent fileContent) {
        fileContent.setParent(this);

        this.fileContent = fileContent;
    }

    public FileContent getFileContent() {
        return fileContent;
    }

    public SourceFileList getSourceFileList() {
        return sourceFileList;
    }

    public void setSourceFileList(SourceFileList sourceFileList) {
        sourceFileList.setParent(this);

        this.sourceFileList = sourceFileList;
    }

//	public void addSourceFile(SourceFile sf) {
//		sourceFileList.addSourceFile(sf);
//	}
    public void addContact(Contact contact) {
        contact.setParent(this);

        contacts.add(contact);
    }

    public void removeContact(int index) {
        Contact removed = contacts.remove(index);

        removed.setParent(null);
    }

    public Contact getContact(int index) {
        return contacts.get(index);
    }

    public int getNumberOfContacts() {
        return contacts.size();
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/fileContent")) {
            fileContent.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if(currentXPath.startsWith("/sourceFileList")) {
            if (sourceFileList == null) {
                throw new UnfollowableXPathException("No sourceFileList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            sourceFileList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/contact")) {
            if (contacts == null || contacts.isEmpty()) {
                throw new UnfollowableXPathException("No contact exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            for (Contact contact : contacts) {
                contact.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
            }
        }
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<fileDescription");
        output.write(">\n");

        fileContent.outputXML(output, indent + 1);

        if (sourceFileList != null && sourceFileList.size() > 0) {
            sourceFileList.outputXML(output, indent + 1);
        }

        for (Contact contact : contacts) {
            contact.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</fileDescription>\n");
    }

    @Override
    public String toString() {
        return "fileDescription";
    }

    @Override
    public String getTagName() {
        return "fileDescription";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(fileContent != null)
            children.add(fileContent);
        if(sourceFileList != null)
            children.add(sourceFileList);
        if(contacts != null)
            children.addAll(contacts);
        
        super.addChildrenToCollection(children);
    }
}

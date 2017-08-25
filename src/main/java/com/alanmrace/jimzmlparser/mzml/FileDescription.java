package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class FileDescription extends MzMLContentWithParams implements Serializable {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    // Required

    /**
     * Description of the content of the file.
     */
    protected FileContent fileContent;

    // Optional

    /**
     * List of files containing data used to generate this ImzML/MzML.
     */
    protected SourceFileList sourceFileList;

    /**
     * List of contacts associated with this ImzML/MzML.
     */
    protected ArrayList<Contact> contacts;

    /**
     * Constructs an empty FileDescription. 
     */
    public FileDescription() {
        contacts = new ArrayList<Contact>();
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references
     * to.
     * 
     * @param fileDescription Old FileDescription to copy
     * @param rpgList New ReferenceableParamGroupList
     */
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

    /**
     * Set the FileContent for describing the contents of this ImzML/MzML.
     * 
     * @param fileContent FileContent
     */
    public void setFileContent(FileContent fileContent) {
        fileContent.setParent(this);

        this.fileContent = fileContent;
    }

    /**
     * Returns the FileContent for describing the contents of this ImzML/MzML.
     * 
     * @return FileContent
     */
    public FileContent getFileContent() {
        return fileContent;
    }

    /**
     * Returns the SourceFileList describing the list of files containing data 
     * used to generate this ImzML/MzML.
     * 
     * @return SourceFileList
     */
    public SourceFileList getSourceFileList() {
        return sourceFileList;
    }

    /**
     * Sets the SourceFileList describing the list of files containing data 
     * used to generate this ImzML/MzML.
     * 
     * @param sourceFileList SourceFileList
     */
    public void setSourceFileList(SourceFileList sourceFileList) {
        sourceFileList.setParent(this);

        this.sourceFileList = sourceFileList;
    }

//	public void addSourceFile(SourceFile sf) {
//		sourceFileList.addSourceFile(sf);
//	}

    /**
     * Add a contact to the FileDescription.
     * 
     * @param contact Contact
     */
    public void addContact(Contact contact) {
        contact.setParent(this);

        contacts.add(contact);
    }

    /**
     * Remove a contact at the specified index within the list from the FileDescription.
     * 
     * @param index Index of the contact within the list
     */
    public void removeContact(int index) {
        Contact removed = contacts.remove(index);

        removed.setParent(null);
    }

    /**
     * Get the contact at the specified index within the list.
     * 
     * @param index Index of the contact within the list
     * @return Contact at the index, or throw IndexOutOfBoundsException if invalid index
     */
    public Contact getContact(int index) {
        return contacts.get(index);
    }

    /**
     * Returns the number of contacts associated with this FileDescription.
     * 
     * @return Number of contacts
     */
    public int getNumberOfContacts() {
        return contacts.size();
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
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
    
    public static FileDescription create() {
        FileDescription fd = new FileDescription();
        
        FileContent fc = FileContent.create();
        fd.setFileContent(fc);
        
        return fd;
    }
}

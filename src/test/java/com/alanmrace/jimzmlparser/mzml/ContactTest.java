package com.alanmrace.jimzmlparser.mzml;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alan Race
 */
public class ContactTest {
    
    public ContactTest() {
    }

    /**
     * Test of toString method, of class Contact.
     */
    @Test
    public void testToString() {
        String name = "Alan Race";
        String organisation = "Mass Spectrometry Imaging";
        
        Contact contact = new Contact();
        
        contact.setName(name);
        contact.setOrganisation(organisation);
        
        String contactString = contact.toString();
        
        assert(contactString.contains(name));
        assert(contactString.contains(organisation));
        
        // Check that changing details works properly
        name = "Race, Alan";
        organisation = "Mass Spectrometry";
        
        contact.setName(name);
        contact.setOrganisation(organisation);
        
        contactString = contact.toString();
        
        assert(contactString.contains(name));
        assert(contactString.contains(organisation));
        
        // Check that the copy constructor functions correctly
        Contact newContact = new Contact(contact, null);
        
        assert(contact.getName().equals(newContact.getName()));
    }

    /**
     * Test of getTagName method, of class Contact.
     */
    @Test
    public void testGetTagName() {
        Contact contact = new Contact();
        
        assertEquals("contact", contact.getTagName());
    }
    
}

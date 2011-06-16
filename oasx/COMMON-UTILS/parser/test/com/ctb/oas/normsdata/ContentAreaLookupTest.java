package com.ctb.oas.normsdata;


/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ContentAreaLookupTest extends NormsDataTestCase {
    public void testGetContentArea() {
        String reading = ContentAreaLookup.getContentArea("001");
        String mathematics = ContentAreaLookup.getContentArea("007");
        assertEquals("Reading", reading);
        assertEquals("Mathematics", mathematics);
    }
}


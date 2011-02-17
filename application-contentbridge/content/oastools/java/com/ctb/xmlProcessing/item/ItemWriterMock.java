/*
 * Created on Nov 3, 2003
 */
package com.ctb.xmlProcessing.item;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.jdom.Element;
import org.xml.sax.SAXException;

import com.ctb.common.tools.media.Media;
import com.ctb.util.iknowxml.R2XmlOutputter;

public class ItemWriterMock extends TestCase implements ItemWriter {
    Item expectedItem;

    public String write(Item item) {
        try {
            validate(item);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Item validation failed: " + e.getMessage());
        }
		return item.getId();
    }
    
    public void writeDatapointConditionCode( Item item )
    {
        
    }

    public void setExpectedItem(Item item) {
        expectedItem = item;
    }

    private void validate(Item item) throws Exception {
        assertEquals("getID", expectedItem.getId(), item.getId());
        assertEquals(
            "getCorrectAnswer",
            expectedItem.getCorrectAnswer(),
            item.getCorrectAnswer());
        assertEquals("getType", expectedItem.getType(), item.getType());
        assertEquals(
            "getFieldTest",
            expectedItem.getFieldTest(),
            item.getFieldTest());
        assertEquals(
            "getSuppressed",
            expectedItem.getSuppressed(),
            item.getSuppressed());
        assertEquals("getSample", expectedItem.getSample(), item.getSample());
        assertEquals(
            "getHistory",
            expectedItem.getHistory(),
            item.getHistory());
        assertEquals(
            "getDescription",
            expectedItem.getDescription(),
            item.getDescription());
        assertEquals(
            "getExtStimulusId",
            expectedItem.getExtStimulusId(),
            item.getExtStimulusId());
        assertEquals(
            "getExtStimulusTitle",
            expectedItem.getExtStimulusTitle(),
            item.getExtStimulusTitle());
        assertEquals(
            "getVersion",
            expectedItem.getVersion(),
            item.getVersion());
        assertEquals(
            "getTemplateId",
            expectedItem.getTemplateId(),
            item.getTemplateId());
        assertEquals(
            "getActivationStatus",
            expectedItem.getActivationStatus(),
            item.getActivationStatus());
        assertEquals(
            "getSubject",
            expectedItem.getSubject(),
            item.getSubject());
        assertEquals(
            "getMinPoints",
            expectedItem.getMinPoints(),
            item.getMinPoints());
        assertEquals(
            "getMaxPoints",
            expectedItem.getMaxPoints(),
            item.getMaxPoints());
        assertEquals(
            "getObjectiveId",
            expectedItem.getObjectiveId(),
            item.getObjectiveId());
        assertEquals(
            "getDisplayId",
            expectedItem.getDisplayId(),
            item.getDisplayId());
        assertEquals(
            "getFrameworkId",
            expectedItem.getFrameworkId(),
            item.getFrameworkId());
        assertEquals(
            "getFrameworkCode",
            expectedItem.getFrameworkCode(),
            item.getFrameworkCode());

        assertDOMElement(
            expectedItem.getItemRootElement(),
            item.getItemRootElement());

        if (item.getMedia() != null) {
            Media.writeMedia(new File("./build/test"), item.getMedia());
        }

        assertMedia(expectedItem.getMedia(), item.getMedia());
    }

    private void assertMedia(Media expectedMedia, Media actualMedia)
        throws Exception {
        if ((expectedMedia == null) && (actualMedia == null))
            return;

        if ((expectedMedia != null) && (actualMedia != null)) {
            // compare the xml generated for the item
            assertXML(expectedMedia.getXml(), actualMedia.getXml());

        } else {
            if (expectedMedia == null) {
                fail("No media expected.");
            } else {
                fail("No media generated for the item.");
            }
        }
    }

    private void assertDOMElement(
        Element expectedelement,
        Element actualElement)
        throws Exception {

        R2XmlOutputter outputter = new R2XmlOutputter();

        // convert the xml to a string and campare the result string
        String expectedXML = outputter.outputString(expectedelement);
        String actualXML = outputter.outputString(actualElement);

        compare(expectedXML, actualXML);
    }

    private void assertXML(char[] expectedXML, char[] actualXML)
        throws Exception {
        compare(String.valueOf(expectedXML), String.valueOf(actualXML));
    }

    private void compare(String expectedXML, String actualXML)
        throws SAXException, IOException, ParserConfigurationException {
        Diff diff = new Diff(expectedXML, actualXML);
        DetailedDiff detailDiff = new DetailedDiff(diff);

        for (Iterator iter = detailDiff.getAllDifferences().iterator();
            iter.hasNext();
            ) {
            Difference difference = (Difference) iter.next();
            fail(difference.toString());
        }
    }
}

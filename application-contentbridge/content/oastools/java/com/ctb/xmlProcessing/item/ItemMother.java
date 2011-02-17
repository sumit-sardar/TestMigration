/*
 * Created on Oct 30, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ctb.xmlProcessing.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import com.ctb.common.tools.OASConstants;
import com.ctb.common.tools.media.Media;
import com.ctb.util.iknowxml.R2DocumentBuilder;
import com.ctb.util.iknowxml.R2XmlOutputter;

/**
 * @author wmli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ItemMother {

    public static Item createImportItem() throws Exception {
        String itemMapFile = "./testdata/itemProcessing/3R.2.2.2.03_import.xml";
        Element rootElement = getRootElementFromFile(itemMapFile);
        Item item = new Item(rootElement);

        item.setId("3R.2.2.2.03");
        item.setCorrectAnswer("B");
        item.setType(OASConstants.ITEM_TYPE_SR);
        item.setFieldTest("F");
        item.setSuppressed("F");
        item.setSample("F");
        item.setHistory("");
        item.setDescription(" This passage is mostly about raccoon kits");
        item.setExtStimulusId("The Racoons' Busy Day");
        item.setExtStimulusTitle("The Racoons' Busy Day");
        item.setVersion("");
        item.setTemplateId("8");
        item.setActivationStatus("");
        item.setSubject("Reading");
        item.setMinPoints(0);
        item.setMaxPoints(1);
        item.setObjectiveId("3R.2.2.2");
        item.setDisplayId("3R.2.2.2.03");
        item.setFrameworkId(0);
        item.setFrameworkCode("CTB");

        return item;
    }

    public static Item createImportItemWithMedia() throws Exception {
        Item item = createImportItem();

        String itemMapFile = "./testdata/itemProcessing/3R.2.2.2.03_import.xml";
        Element rootElement = getRootElementFromFile(itemMapFile);

        Media media = new Media();
        R2XmlOutputter outputter = new R2XmlOutputter();
        media.setXml(outputter.outputString(rootElement).toCharArray());
        item.setMedia(media);

        return item;
    }

    public static Item createMappedItem() throws Exception {
        String itemMapFile = "./testdata/itemProcessing/3R.2.2.2.03_map.xml";
        Element rootElement = getRootElementFromFile(itemMapFile);

        Item item = new Item(rootElement);

        //		item.setId("3R.2.2.2.03_WV_RLA.3.1.2");
        item.setId("3R.2.2.2.03_WV");
        item.setCorrectAnswer("B");
        item.setType(OASConstants.ITEM_TYPE_SR);
        item.setFieldTest("F");
        item.setSuppressed("F");
        item.setSample("F");
        item.setHistory("3R.2.2.2.03");
        item.setDescription(" This passage is mostly about raccoon kits");
        item.setExtStimulusId("The Racoons' Busy Day");
        item.setExtStimulusTitle("The Racoons' Busy Day");
        item.setVersion("");
        item.setTemplateId("8");
        item.setActivationStatus("");
        item.setSubject("Reading Language Arts");
        item.setMinPoints(0);
        item.setMaxPoints(1);
        item.setObjectiveId("RLA.3.1.2");
        item.setDisplayId("3R.2.2.2.03");
        item.setFrameworkId(0);
        item.setFrameworkCode("WV");

        return item;
    }

    public static Item createRoundTripItem() throws Exception {
        String itemMapFile =
            "./testdata/itemProcessing/3R.2.2.2.03_roundtrip.xml";
        Element rootElement = getRootElementFromFile(itemMapFile);

        Item item = new Item(rootElement);

        //		item.setId("3R.2.2.2.03_WV_RLA.3.1.2");
        item.setId("3R.2.2.2.03_WV");
        item.setCorrectAnswer("B");
        item.setType(OASConstants.ITEM_TYPE_SR);
        item.setFieldTest("F");
        item.setSuppressed("F");
        item.setSample("F");
        item.setHistory("3R.2.2.2.03");
        item.setDescription(" This passage is mostly about raccoon kits");
        item.setExtStimulusId("The Racoons' Busy Day");
        item.setExtStimulusTitle("The Racoons' Busy Day");
        item.setVersion("");
        item.setTemplateId("8");
        item.setActivationStatus("");
        item.setSubject("Reading Language Arts");
        item.setMinPoints(0);
        item.setMaxPoints(1);
        item.setObjectiveId("RLA.3.1.2");
        item.setDisplayId("3R.2.2.2.03");
        item.setFrameworkId(0);
        item.setFrameworkCode("WV");

        Media media = new Media();
        R2XmlOutputter outputter = new R2XmlOutputter();
        media.setXml(outputter.outputString(rootElement).toCharArray());
        item.setMedia(media);

        return item;
    }

    public static Item createMappedItemWithMedia() throws Exception {
        Item item = createMappedItem();

        String itemMapFile = "./testdata/itemProcessing/3R.2.2.2.03_map.xml";
        Element rootElement = getRootElementFromFile(itemMapFile);

        Media media = new Media();
        R2XmlOutputter outputter = new R2XmlOutputter();
        media.setXml(outputter.outputString(rootElement).toCharArray());
        item.setMedia(media);

        return item;
    }

    private static Element getRootElementFromFile(String itemMapFile)
        throws JDOMException, IOException, FileNotFoundException {
        R2DocumentBuilder builder = new R2DocumentBuilder();

        Document document =
            builder.build(new FileInputStream(new File(itemMapFile)));
        Element rootElement = document.getRootElement();
        return rootElement;
    }

}

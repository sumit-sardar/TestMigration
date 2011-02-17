package com.ctb.xmlProcessing.item;

import java.io.CharArrayReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ctb.common.tools.SystemException;
import com.ctb.common.tools.media.Media;
import com.ctb.roundtrip.MediaReader;
import com.ctb.util.iknowxml.R2DocumentBuilder;

public class ItemBuilderRoundTrip implements ItemBuilder {
    private final ItemBuilder itemBuilder;
    private final MediaReader reader;
    private final ItemMediaCache itemMediaCache;

    public ItemBuilderRoundTrip(
        int validationMode,
        MediaReader reader,
        ItemMediaCache itemMediaCache) {
        this.itemBuilder = ItemBuilderFactory.getItemBuilder(validationMode);
        this.reader = reader;
        this.itemMediaCache = itemMediaCache;
    }

    public Item build(Element item) {
        // get the id from the xml
        String id;

        try {
            id = item.getAttributeValue("ID");
        } catch (Exception e) {
            throw new SystemException("Cannot extract ID field from XML.");
        }

        // retrieve the media from the source.
        Media media = reader.readMedia(id);
        itemMediaCache.addMedia(id, media);

        if (media.getXml() == null) {
    //        throw new SystemException("Cannot retrieve media.");
            return null;
        }

        // cache the media
        itemMediaCache.addMedia(id, media);

        // build the media using regular builder
        SAXBuilder builder = new SAXBuilder(R2DocumentBuilder.SAX_PARSER_NAME);

        Document doc = null;
        try {
            doc = builder.build(new CharArrayReader(media.getXml()));
        } catch (Exception e) {
            throw new SystemException("Cannot convert the xml to jdom document.");
        }

        Element rootElement = doc.getRootElement();

        return itemBuilder.build(rootElement);

    }

}

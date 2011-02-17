package com.ctb.common.tools;


import java.io.*;

import org.xml.sax.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 8, 2003
 * Time: 4:06:48 PM
 * To change this template use Options | File Templates.
 */
public class CtbEntityResolver implements EntityResolver {
    public InputSource resolveEntity(String publicid, String systemid) throws SAXException, IOException {

        if (systemid.endsWith(".dtd") || systemid.endsWith(".ent")) {

            return findAsResource(systemid);
        } else {
            return null;
        }
    }

    private InputSource findAsResource(String systemid) {
        if (systemid.startsWith("file:")) {
            systemid = systemid.substring(systemid.lastIndexOf("/") + 1);
        }
        InputStream resource = getClass().getResourceAsStream("/" + systemid);

        if (resource == null) {
            return null;
        }
        return new InputSource(resource);
    }
}


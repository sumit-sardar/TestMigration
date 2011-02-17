package com.ctb.common.tools;


import org.xml.sax.*;

import com.ctb.sax.element.*;


public class RemotePathOperation implements FieldOperation {

    String remoteArtPath;

    public RemotePathOperation(String localArtPath) {
        this.remoteArtPath = localArtPath;
    }

    static final String prefix = "iwts:";

    public String process(String input) throws SAXException {

        if (remoteArtPath == null) {
            return input;
        }
        // absolute paths remain unchanged
        if (input.startsWith("/")) {
            return input;
        }
        // remove relative path prefixes
        if (input.startsWith(prefix)) {
            input = input.substring(prefix.length());
        }
        while (input.startsWith("../")) {
            input = input.substring(3);
        }

        input = remoteArtPath + "/" + input;
        return input;
    }
}

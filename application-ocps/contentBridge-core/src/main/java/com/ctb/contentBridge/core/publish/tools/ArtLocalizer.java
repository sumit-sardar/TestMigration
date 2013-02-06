package com.ctb.contentBridge.core.publish.tools;


import java.io.*;

import org.jdom.*;
import org.xml.sax.*;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.util.ParseFilterProperties;



public class ArtLocalizer {

    static File localArtFolder = null;

    public static void localizeArt(Element element) throws IOException, SAXException, BusinessException {
        if (localArtFolder == null) {
            localArtFolder = ParseFilterProperties.instance().getImagesBaseCanonicalFile();
        }
        ImagePathMover mover = new ImagePathMover("JunkyFolder",
                localArtFolder.getCanonicalPath());

        mover.changeToLocal(element);
    }

    public static void setLocalImageArtPath(File folder) {
        localArtFolder = folder;
    }

}

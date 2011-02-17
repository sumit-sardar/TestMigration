package com.ctb.common.tools;


import java.io.*;

import org.jdom.*;
import org.xml.sax.*;

import com.ctb.util.*;


public class ArtLocalizer {

    static File localArtFolder = null;

    public static void localizeArt(Element element) throws IOException, SAXException {
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

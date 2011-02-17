package com.ctb.common.tools;


import org.jdom.*;

import com.ctb.sax.element.*;
import com.ctb.sax.replacement.*;
import com.ctb.util.iknowxml.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 8, 2003
 * Time: 5:41:00 PM
 * To change this template use Options | File Templates.
 */
public class ImagePathMover {

    String remoteImageArea;
    String localImageArea;

    public String getRemoteImageArea() {
        return remoteImageArea;
    }

    public String getLocalImageArea() {
        return localImageArea;
    }

    public ImagePathMover(String remoteImageArea) {
        this.remoteImageArea = remoteImageArea;
        this.localImageArea = remoteImageArea;

    }

    public ImagePathMover(String remoteImageArea, String localImageArea) {
        this.remoteImageArea = remoteImageArea;
        this.localImageArea = localImageArea;

    }

    public void changeToLocal(Element element) {
        try {
            FieldOperation absolutePathOperation = new AbsolutePathOperation(localImageArea);

            R2XmlTools.replaceAttributes(element, "//BMPPrint/@FileName",
                    absolutePathOperation);
            R2XmlTools.replaceAttributes(element, "//EPSPrint/@FileName",
                    absolutePathOperation);
            R2XmlTools.replaceAttributes(element, "//Flash/@FileName",
                    absolutePathOperation);
        } catch (Exception e) {
            throw new SystemException("Could not change image paths to local images: "
                    + e.getMessage(),
                    e);
        }
    }

    public void changeToRemote(Element element) {
        // move images from relative to absolute and remote, RemotePathOperation
        try {
            FieldOperation remotePathOperation = new RemotePathOperation(remoteImageArea);

            R2XmlTools.replaceAttributes(element, "//BMPPrint/@FileName",
                    remotePathOperation);
            R2XmlTools.replaceAttributes(element, "//EPSPrint/@FileName",
                    remotePathOperation);
            R2XmlTools.replaceAttributes(element, "//Flash/@FileName",
                    remotePathOperation);
        } catch (Exception e) {
            throw new SystemException("Could not change image paths to remote images: "
                    + e.getMessage(),
                    e);
        }
    }

}

package com.ctb.contentBridge.core.publish.tools;


import org.jdom.*;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.iknowxml.R2XmlTools;
import com.ctb.contentBridge.core.publish.sax.element.FieldOperation;
import com.ctb.contentBridge.core.publish.sax.replacement.AbsolutePathOperation;



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

    public void changeToLocal(Element element) throws SystemException {
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
                    + e.getMessage());
        }
    }

    public void changeToRemote(Element element) throws SystemException {
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
                    + e.getMessage());
        }
    }

}

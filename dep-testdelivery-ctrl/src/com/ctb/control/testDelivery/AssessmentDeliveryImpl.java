package com.ctb.control.testDelivery; 

import com.bea.control.*;
import com.ctb.bean.testDelivery.assessmentDeliveryData.ItemData;
import com.ctb.bean.testDelivery.assessmentDeliveryData.SubtestData;
import com.ctb.util.OASLogger;
import com.ctb.util.SimpleCache;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import noNamespace.AdssvcRequestDocument;
import noNamespace.AdssvcRequestDocument.AdssvcRequest;
import noNamespace.AdssvcResponseDocument;
import noNamespace.AdssvcResponseDocument.AdssvcResponse;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.DownloadItem;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.GetSubtest;
import noNamespace.ErrorDocument;
import org.apache.beehive.controls.api.bean.ControlImplementation;

/**
 * @author John_Wang
 */

/**
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation()
public class AssessmentDeliveryImpl implements AssessmentDelivery, Serializable
{ 
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.AssessmentDeliveryDB assessmentDelivery;

    static final long serialVersionUID = 1L;
    
    private static final String CACHE_TYPE_SUBTEST = "CACHE_TYPE_SUBTEST";
    private static final String CACHE_TYPE_ITEM = "CACHE_TYPE_ITEM";

    /**
     * @common:operation
     */
    public AdssvcResponseDocument getSubtest(AdssvcRequestDocument document)
    {
        AdssvcRequest request = document.getAdssvcRequest();
        OASLogger.getLogger("TestDelivery").debug(request.toString());
        int subtestId = (new Integer(request.getGetSubtest().getSubtestid())).intValue();
        String hash = request.getGetSubtest().getHash();

        AdssvcResponseDocument responseDoc = AdssvcResponseDocument.Factory.newInstance();
        AdssvcResponse adssvcResponse = responseDoc.addNewAdssvcResponse();
        GetSubtest getSubtest = adssvcResponse.addNewGetSubtest();
        getSubtest.setSubtestid(""+subtestId);

        try {
            SubtestData subtestData = (SubtestData) SimpleCache.checkCache(CACHE_TYPE_SUBTEST, ""+subtestId);
            
            if (subtestData == null || subtestData.getSubtestId() != subtestId || !subtestData.getHash().equals(hash)) {
                subtestData = assessmentDelivery.getSubtest(subtestId);
                if (subtestData == null)
                    throw new Exception("Subtest with subtest id '"+subtestId+"' not found in ADS");
                if (!subtestData.getHash().equals(hash))
                    throw new Exception("Incorrect hash for subtest id '"+subtestId+"' in ADS");
                boolean moreData = true;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream is = assessmentDelivery.getSubtestBlob(subtestId).getBinaryStream();
                while(moreData) {
                    byte [] buffer = new byte[128];
                    int read = is.read(buffer);
                    moreData = read > 0;
                    if(moreData) {
                        baos.write(buffer, 0, read);
                    }
                }
                subtestData.setSubtest(baos.toByteArray());
                SimpleCache.cacheResult(CACHE_TYPE_SUBTEST, "" + subtestId, subtestData);
                OASLogger.getLogger("TestDelivery").debug("******* save to cache "+CACHE_TYPE_SUBTEST+subtestId);
            }
            getSubtest.setContent(subtestData.getSubtest());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ErrorDocument.Error error = getSubtest.addNewError();
            error.setErrorElement(request.toString());
            error.setErrorDetail(e.getMessage());
        }
        return responseDoc;
    }

    /**
     * @common:operation
     */
    public AdssvcResponseDocument downloadItem(AdssvcRequestDocument document)
    {
        AdssvcRequest request = document.getAdssvcRequest();
        OASLogger.getLogger("TestDelivery").debug(request.toString());
        int itemId = (new Integer(request.getDownloadItem().getItemid())).intValue();
        String hash = request.getDownloadItem().getHash();

        AdssvcResponseDocument responseDoc = AdssvcResponseDocument.Factory.newInstance();
        AdssvcResponse adssvcResponse = responseDoc.addNewAdssvcResponse();
        DownloadItem downloadItem = adssvcResponse.addNewDownloadItem();
        downloadItem.setItemid(""+itemId);

        try {
        
            ItemData itemData = (ItemData) SimpleCache.checkCache(CACHE_TYPE_ITEM, ""+itemId);
            
            if (itemData == null || itemData.getItemId() != itemId || !itemData.getHash().equals(hash)) {
                itemData = assessmentDelivery.getItem(itemId);
                if (itemData == null)
                    throw new Exception("Subtest with item id '"+itemId+"' not found in ADS");
                if (!itemData.getHash().equals(hash))
                    throw new Exception("Incorrect hash for item id '"+itemId+"' in ADS");
                boolean moreData = true;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream is = assessmentDelivery.getItemBlob(itemId).getBinaryStream();
                while(moreData) {
                    byte [] buffer = new byte[128];
                    int read = is.read(buffer);
                    moreData = read > 0;
                    if(moreData) {
                        baos.write(buffer, 0, read);
                    }
                }
                itemData.setItem(baos.toByteArray());
                SimpleCache.cacheResult(CACHE_TYPE_ITEM, "" + itemId, itemData);
                OASLogger.getLogger("TestDelivery").debug("******* save to cache "+CACHE_TYPE_ITEM+itemId);
            }
            downloadItem.setContent(itemData.getItem());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ErrorDocument.Error error = downloadItem.addNewError();
            error.setErrorElement(request.toString());
            error.setErrorDetail(e.getMessage());
        }
        return responseDoc;
    }
    
}
package com.ctb.contentBridge.core.publish.tools;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.StringType;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.cvm.oas.MediaType;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemMediaCompositeId;
import com.ctb.contentBridge.core.publish.hibernate.persist.ItemMediaRecord;
import com.ctb.contentBridge.core.publish.media.Media;


/**
 * @author wmli
 */
public class MediaMapper {	
	public static final String CAB_MEDIA_PATH = "/oastd-web/versions/cab/1.0/";	
	// TODO - SLI - use TABE media path for now until the terranova media path set up properly.
	public static final String TERRANOVA_MEDIA_PATH = "/oastd-web/versions/terranova/1.0/";
	public static final String TABE_MEDIA_PATH = "/oastd-web/versions/tabe/1.0/";
	
	private static final String FIND_ALL_MEDIA_FOR_ITEM =
        "from "
            + ItemMediaRecord.class.getName()
            + " as itemMedia "
            + "where itemMedia.id.itemId = ?";

    public Media loadMedia(Session session, String itemId) {
        // load media from database
        try {
            Media media = new Media();
            media.setItemId(itemId);

            List itemMediaList =
                session.find(FIND_ALL_MEDIA_FOR_ITEM, itemId, new StringType());

            recordsToObject(session, itemMediaList.iterator(), media);

            return media;
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage());
        }

    }

    private void recordsToObject(
        Session session,
        Iterator mediaRecordIter,
        Media media) {
        for (Iterator iter = mediaRecordIter; iter.hasNext();) {
			ItemMediaRecord itemSetMediaRecord =
                (ItemMediaRecord) iter.next();

            if (itemSetMediaRecord
                .getId()
                .getMediaType()
                .equals(MediaType.XML_MEDIA_TYPE.getMediaType())) {
                media.setXml(itemSetMediaRecord.getMediaClob());
            } else if (
                itemSetMediaRecord.getId().getMediaType().equals(
                    MediaType.FLASH_ANSWER_MEDIA_TYPE.getMediaType())) {
                media.setAkSwf(itemSetMediaRecord.getMediaBlob());
            } else if (
                itemSetMediaRecord.getId().getMediaType().equals(
                    MediaType.PDF_ANSWER_MEDIA_TYPE.getMediaType())) {
                media.setAkPdf(itemSetMediaRecord.getMediaBlob());
            } else if (
                itemSetMediaRecord.getId().getMediaType().equals(
                    MediaType.PDF_MEDIA_TYPE.getMediaType())) {
                media.setPdf(itemSetMediaRecord.getMediaBlob());
            }

            try {
                session.evict(itemSetMediaRecord);
            } catch (HibernateException e) {
                throw new SystemException(e.getMessage());
            }
        }
    }

    public void saveMedia(Session session, Media media) {
        saveMedia(
            session,
            media.getItemId(),
            media.getXml(),
            MediaType.XML_MEDIA_TYPE);
        saveMedia(
            session,
            media.getItemId(),
            media.getAkSwf(),
            MediaType.FLASH_ANSWER_MEDIA_TYPE);
        saveMedia(
            session,
            media.getItemId(),
            media.getAkPdf(),
            MediaType.PDF_ANSWER_MEDIA_TYPE);
        saveMedia(
            session,
            media.getItemId(),
            media.getPdf(),
            MediaType.PDF_MEDIA_TYPE);

    }

    private void saveMedia(
        Session session,
        String itemId,
        Object lobData,
        MediaType mediaType) {
        try {
            ItemMediaCompositeId id = new ItemMediaCompositeId();
            id.setItemId(itemId);
            id.setMediaType(mediaType.getMediaType());

             ItemMediaRecord itemMediaRecord =
                (ItemMediaRecord) session.get(ItemMediaRecord.class, id);

            if (lobData == null && itemMediaRecord != null) {
                session.delete(itemMediaRecord);
            } else if (lobData != null) {
                saveOrUpdateMedia(
                    session,
                    id,
                    lobData,
                    mediaType,
                    itemMediaRecord);
            }

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage());
        }
    }

    private void saveOrUpdateMedia(
        Session session,
        ItemMediaCompositeId id,
        Object lobData,
        MediaType mediaType,
        ItemMediaRecord itemMediaRecord)
        throws HibernateException {

        if (itemMediaRecord == null) {
			itemMediaRecord = new ItemMediaRecord();
			itemMediaRecord.setCreatedBy(new Long(OASConstants.CREATED_BY));
			itemMediaRecord.setCreatedDateTime(new Date());
        } else {
			itemMediaRecord.setUpdatedBy(new Long(OASConstants.CREATED_BY));
			itemMediaRecord.setUpdatedDateTime(new Date());
        }

		itemMediaRecord.setId(id);
		itemMediaRecord.setMediaPath(MediaMapper.CAB_MEDIA_PATH);
		itemMediaRecord.setMimeType(mediaType.getMineType());

        if (mediaType.isBlob()) {
			itemMediaRecord.setMediaBlob((byte[]) lobData);
        } else {
			itemMediaRecord.setMediaClob((char[]) lobData);
        }

        session.saveOrUpdate(itemMediaRecord);
    }
}

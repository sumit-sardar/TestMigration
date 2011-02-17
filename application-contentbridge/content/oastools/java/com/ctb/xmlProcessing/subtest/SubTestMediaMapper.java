package com.ctb.xmlProcessing.subtest;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.LongType;

import com.ctb.common.tools.SystemException;
import com.ctb.common.tools.media.MediaType;
import com.ctb.hibernate.persist.ItemSetMediaCompositeId;
import com.ctb.hibernate.persist.ItemSetMediaRecord;

public class SubTestMediaMapper {
    protected static final Long USER_ID = new Long(2);

    private static final String FIND_ALL_MEDIA_FOR_ITEMSET =
        "from "
            + ItemSetMediaRecord.class.getName()
            + " as media where media.id.itemSetId = ?";

    public SubTestMedia loadMedia(Session session, Long itemSetId) {
        // load media from database
        try {

            SubTestMedia media = new SubTestMedia();
            media.setItemSetId(itemSetId);

            List itemSetMediaList =
                session.find(
                    FIND_ALL_MEDIA_FOR_ITEMSET,
                    itemSetId,
                    new LongType());

            recordsToObject(session, itemSetMediaList.iterator(), media);

            return media;
        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }

    }

    private void recordsToObject(
        Session session,
        Iterator mediaRecordIter,
        SubTestMedia media) {
        for (Iterator iter = mediaRecordIter; iter.hasNext();) {
            ItemSetMediaRecord itemSetMediaRecord =
                (ItemSetMediaRecord) iter.next();

            if (itemSetMediaRecord
                .getId()
                .getMediaType()
                .equals(MediaType.XML_MEDIA_TYPE.getMediaType())) {
                media.setPremadeTestXml(itemSetMediaRecord.getMediaClob());
            } else if (
                itemSetMediaRecord.getId().getMediaType().equals(
                    MediaType.FLASH_ANSWER_MEDIA_TYPE.getMediaType())) {
                media.setPremadeTestFlashAnswer(
                    itemSetMediaRecord.getMediaBlob());
            } else if (
                itemSetMediaRecord.getId().getMediaType().equals(
                    MediaType.FLASH_MOVIE_MEDIA_TYPE.getMediaType())) {
                media.setPremadeTestFlashMovie(
                    itemSetMediaRecord.getMediaBlob());
            } else if (
                itemSetMediaRecord.getId().getMediaType().equals(
                    MediaType.PDF_ANSWER_MEDIA_TYPE.getMediaType())) {
                media.setPremadeTestPDFAnswer(
                    itemSetMediaRecord.getMediaBlob());
            } else if (
                itemSetMediaRecord.getId().getMediaType().equals(
                    MediaType.PDF_CR_AK_ONLY.getMediaType())) {
                media.setPremadeTestPDFCRAnswer(
                    itemSetMediaRecord.getMediaBlob());
            } else if (
                itemSetMediaRecord.getId().getMediaType().equals(
                    MediaType.PDF_CR_ONLY.getMediaType())) {
                media.setPremadeTestPDFCRQuestions(
                    itemSetMediaRecord.getMediaBlob());
            } else if (
                itemSetMediaRecord.getId().getMediaType().equals(
                    MediaType.PDF_MEDIA_TYPE.getMediaType())) {
                media.setPremadeTestPDFQuestions(
                    itemSetMediaRecord.getMediaBlob());
            }

            try {
                session.evict(itemSetMediaRecord);
            } catch (HibernateException e) {
                throw new SystemException(e.getMessage(), e);
            }
        }
    }

    public void saveMedia(
        Session session,
        SubTestMedia media,
        String mediaPath) {
        saveMedia(
            session,
            media.getItemSetId(),
            media.getPremadeTestXml(),
            MediaType.XML_MEDIA_TYPE,
            mediaPath);
        saveMedia(
            session,
            media.getItemSetId(),
            media.getPremadeTestFlashAnswer(),
            MediaType.FLASH_ANSWER_MEDIA_TYPE,
            mediaPath);
        saveMedia(
            session,
            media.getItemSetId(),
            media.getPremadeTestFlashMovie(),
            MediaType.FLASH_MOVIE_MEDIA_TYPE,
            mediaPath);
        saveMedia(
            session,
            media.getItemSetId(),
            media.getPremadeTestPDFAnswer(),
            MediaType.PDF_ANSWER_MEDIA_TYPE,
            mediaPath);
        // new stuff for PDF
        //PDF CR ONLY AK
        saveMedia(
            session,
            media.getItemSetId(),
            media.getPremadeTestPDFCRAnswer(),
            MediaType.PDF_CR_AK_ONLY,
            mediaPath);
        //PDF CR ONLY QUESTIONS
        saveMedia(
            session,
            media.getItemSetId(),
            media.getPremadeTestPDFCRQuestions(),
            MediaType.PDF_CR_ONLY,
            mediaPath);
        //PDF ALL QUESTIONS
        saveMedia(
            session,
            media.getItemSetId(),
            media.getPremadeTestPDFQuestions(),
            MediaType.PDF_MEDIA_TYPE,
            mediaPath);
    }

    private void saveMedia(
        Session session,
        Long itemSetId,
        Object lobData,
        MediaType mediaType,
        String mediaPath) {
        try {
            ItemSetMediaCompositeId id = new ItemSetMediaCompositeId();
            id.setItemSetId(itemSetId);
            id.setMediaType(mediaType.getMediaType());

            ItemSetMediaRecord itemSetMediaRecord =
                (ItemSetMediaRecord) session.get(ItemSetMediaRecord.class, id);

            if (lobData == null && itemSetMediaRecord != null) {
                session.delete(itemSetMediaRecord);
            } else if (lobData != null) {
                saveOrUpdateMedia(
                    session,
                    id,
                    lobData,
                    mediaType,
                    itemSetMediaRecord,
                    mediaPath);
            }

        } catch (HibernateException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public void saveOrUpdateMedia(
        Session session,
        ItemSetMediaCompositeId id,
        Object lobData,
        MediaType mediaType,
        ItemSetMediaRecord itemSetMediaRecord,
        String mediaPath)
        throws HibernateException {

        if (itemSetMediaRecord == null) {
            itemSetMediaRecord = new ItemSetMediaRecord();
            itemSetMediaRecord.setCreatedBy(USER_ID);
            itemSetMediaRecord.setCreatedDateTime(new Date());
        } else {
            itemSetMediaRecord.setUpdatedBy(USER_ID);
            itemSetMediaRecord.setUpdatedDateTime(new Date());
        }

        itemSetMediaRecord.setId(id);
        itemSetMediaRecord.setMediaPath(mediaPath);
        itemSetMediaRecord.setMimeType(mediaType.getMineType());

        if (mediaType.isBlob()) {
            itemSetMediaRecord.setMediaBlob((byte[]) lobData);
        } else {
            itemSetMediaRecord.setMediaClob((char[]) lobData);
        }

        session.saveOrUpdate(itemSetMediaRecord);
    }
}
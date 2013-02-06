package com.ctb.contentBridge.core.publish.dao;

import com.ctb.contentBridge.core.publish.media.Media;
import com.ctb.contentBridge.core.publish.roundtrip.MediaReader;
import com.ctb.contentBridge.core.publish.tools.MediaMapper;

import net.sf.hibernate.Session;

/**
 * @author wmli
 */
public class DBMediaGateway implements MediaReader {
    Session session;

    public DBMediaGateway(Session session) {
        this.session = session;
    }

    public Media findMedia(String itemId) {
        return readMedia(itemId);
    }

    public Media readMedia(String itemId) {
        MediaMapper mapper = new MediaMapper();
        return mapper.loadMedia(session, itemId);
    }

    public void saveMedia(Media media) {
        MediaMapper mapper = new MediaMapper();
        mapper.saveMedia(session, media);
    }

}

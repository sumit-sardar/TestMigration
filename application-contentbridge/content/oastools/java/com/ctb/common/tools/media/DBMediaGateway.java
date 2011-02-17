package com.ctb.common.tools.media;

import net.sf.hibernate.Session;

import com.ctb.roundtrip.MediaReader;

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

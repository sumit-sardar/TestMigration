package com.ctb.xmlProcessing.subtest;


import net.sf.hibernate.Session;

import com.ctb.common.tools.media.MediaMapper;

/**
 * @author wmli
 */
public class DBSubTestMediaGateway { //extends DBLOBGateway implements ObjectFinder {
    Session session;

    public DBSubTestMediaGateway(Session session) {
        this.session = session;
    }

    public SubTestMedia findMedia(Long itemSetId) {
        SubTestMediaMapper mapper = new SubTestMediaMapper();
        return mapper.loadMedia(session, itemSetId);
    }

    public void saveMedia(SubTestMedia media) {
		this.saveMedia(media, MediaMapper.CAB_MEDIA_PATH);
    }

    public void saveMedia(SubTestMedia media, String mediaPath) {
		SubTestMediaMapper mapper = new SubTestMediaMapper();
        mapper.saveMedia(session, media, mediaPath);
    }
}

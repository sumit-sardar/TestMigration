package com.ctb.lexington.domain.score.scorer;

import com.ctb.lexington.db.data.ScoreMoveData;
import com.ctb.lexington.domain.score.ConnectionProvider;
import com.ctb.lexington.domain.score.event.common.Event;
import com.ctb.lexington.domain.score.event.common.EventRecipient;
import com.ctb.lexington.exception.CTBSystemException;
import com.ibatis.sqlmap.client.SqlMapSession;

public interface Scorer extends ConnectionProvider, EventRecipient {
    void notify(Event event) throws CTBSystemException;

    ScoreMoveData getResultHolder();

    void forceCloseAllConnections(boolean rollback);
    
    public SqlMapSession getIbatisSession();
}
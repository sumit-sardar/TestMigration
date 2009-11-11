package com.ctb.lexington.domain.score.scorer;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.domain.score.event.common.Event;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.db.data.ScoreMoveData;
import com.ibatis.sqlmap.client.SqlMapSession;

/**
 * A scorer that does nothing.  This scorer is used for product types that are either unknown
 * or that should not be scored.
 *
 * @author mnkamiya
 * @version $Id$
 */
public class DoNothingScorer implements Scorer {

	// scorer does nothing, don't create iBatis session
	public SqlMapSession getIbatisSession(){
		return null;
	}
	
	public void onEvent(Event event) {
	}
	
    public void notify(Event event) throws CTBSystemException {
    }

    public ScoreMoveData getResultHolder() {
        return null;
    }

    public Connection getOASConnection() throws SQLException {
        return null;
    }
    
    public Connection getIRSConnection() throws SQLException {
        return null;
    }

    public void close(boolean rollback, Connection connection) throws SQLException {

    }

    public void forceCloseAllConnections(boolean rollback) {
        
    }
}

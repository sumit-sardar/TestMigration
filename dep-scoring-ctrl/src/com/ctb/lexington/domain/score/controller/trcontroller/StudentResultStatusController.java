package com.ctb.lexington.domain.score.controller.trcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.irsdata.irstrdata.IrsTRItemFactData;
import com.ctb.lexington.db.mapper.trmapper.IrsTRItemFactMapper;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;

/**
 * @author Somenath_Chakroborti
 * 
 */
public class StudentResultStatusController {

	private ContextData context;
	private IrsTRItemFactMapper irsTRItemFactMapper;

	public StudentResultStatusController(Connection conn, ContextData context) {
		this.context = context;
		irsTRItemFactMapper = new IrsTRItemFactMapper(conn);
	}

	public void run() throws IOException, DataException, CTBSystemException,
			SQLException {
		Long priorResult = new Long(1).equals(context.getCurrentResultId()) ? new Long(
				2)
				: new Long(1);
		IrsTRItemFactData tascReadinessItemFactData = new IrsTRItemFactData();
		tascReadinessItemFactData.setStudentid(context.getStudentId());
		tascReadinessItemFactData.setSessionid(context.getSessionId());
		tascReadinessItemFactData.setCurrentResultid(priorResult);
		tascReadinessItemFactData.setAssessmentid(context.getAssessmentId());
		tascReadinessItemFactData.setTestCompletionTimestamp(context
				.getTestCompletionTimestamp());
		tascReadinessItemFactData.setProgramid(context.getProgramId());
		irsTRItemFactMapper
				.updateCurrentResultStatus(tascReadinessItemFactData);
	}
}
package com.ctb.lexington.domain.score.controller; 

import com.ctb.lexington.db.data.StudentDemographicData;
import com.ctb.lexington.db.irsdata.IrsDemographicData;
import com.ctb.lexington.domain.teststructure.ValidationStatus;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;
import java.io.IOException;
import java.sql.SQLException;

public interface TestResultController 
{
    public void run(ValidationStatus rosterValidationStatus) throws IOException, DataException, CTBSystemException, SQLException;

     public IrsDemographicData getIrsDemographics(StudentDemographicData data);
} 

--DROP PACKAGE PK_Students;

CREATE OR REPLACE PACKAGE PKG_BMTSYNC_Students AS

  	TYPE REF_CURSOR IS REF CURSOR;
	  
    PROCEDURE FetchStudentList 
	(
	    pNoOfMsgToFetch    IN  NUMBER,
	    pResultCursor OUT REF_CURSOR
    );
	
	/*
	*   Procedure to return Student Biographical Information
	*/
	PROCEDURE StudentDetails (
		pStudentID IN VARCHAR,
		pResultCursor OUT REF_CURSOR
	);

	/*
	*   Procedure to return Student Accomodation Information
	*/
	PROCEDURE StudentAccomodation (
		pStudentID IN VARCHAR,
		pResultCursor OUT REF_CURSOR
	);

	
	/*
	* Procedure to return Hierarchy 
	* to which the student ID belongs
	*/
	PROCEDURE Heirarchy (
	   pStudentID        IN VARCHAR,
	   pResultCursor     OUT REF_CURSOR
	);

	/********************************************************************
	* Procedure to return the Student Class Hierachy details
	*
	PROCEDURE HeirarchyClass  (
	   pStudentID    IN VARCHAR,
	   pResultCursor OUT REF_CURSOR
	);
	********************************************************************/
	
	/*
	*  Procedure to update the Student API export Status
	*/
	PROCEDURE updateStudentAPIStatus(
	    pStudentID     IN VARCHAR2,
		pAppName       IN VARCHAR2,
		pExportStatus  IN VARCHAR2,
		pErrorCode       IN VARCHAR2,
		pErrorMessage    IN VARCHAR2);	

	/*
	*  Procedure to insert/update the Student API Status table
	*  This procedure is called by the PKG_BMTSYNC.onChange_Student
	*  via Trigger TRG_STUDENT_WS
	*/
	PROCEDURE AddUpdateStudentAPIStatus(
	    pStudentID     IN VARCHAR2,
		pAppName       IN VARCHAR2,
		pExportStatus  IN VARCHAR2);		

END PKG_BMTSYNC_Students;
/




CREATE OR REPLACE PACKAGE BODY PKG_BMTSYNC_Students AS

    PROCEDURE FetchStudentList 
	(
	    pNoOfMsgToFetch    IN  NUMBER,
		pResultCursor OUT REF_CURSOR
    ) AS 
	   vCtr NUMBER := 1;
	   vStudentID   VARCHAR2(50);
	   vStudentIDs  VARCHAR2(32767);
	BEGIN
	   
	   WHILE vCtr <= pNoOfMsgToFetch LOOP
	       -- SET The Status to 'Inprogress' in BMTSYNC_Student_Status table
	       PKG_BMTSYNC_STUDENTQUEUE.FETCHSTUDENT_MSG(vStudentID);
		   IF vStudentID > 0 THEN
		   
			   IF vCtr = 1 THEN
				  vStudentIDs := vStudentID;
			   ELSE 
				  vStudentIDs := vStudentIDs || ', '||vStudentID;
			   END IF;
		   ELSE 
		      -- If the StudentID is NULL or 0, 
			  -- It means there are no messages in the queue and we don't need to loop through
		      vCtr := pNoOfMsgToFetch;
		   END IF;
		   
		   vCtr := vCtr + 1;
	   END LOOP;

	   
	   --DBMS_OUTPUT.PUT_LINE('Fecthed Student IDs: '||vStudentIDs);
	   
	   -- We get the student ids as a comma seperated values
	   -- Use the SplintString function to get the Student Id as rows of data
	   OPEN pResultCursor FOR
	   SELECT Column_Value AS oasStudentId from table(splitString(vStudentIDs));
	   COMMIT;	   
	   
	END FetchStudentList;
	

	/*
	*   Procedure to return Student Biographical Information
	*/
	PROCEDURE StudentDetails (
		pStudentID IN VARCHAR,
		pResultCursor OUT REF_CURSOR
	) AS
	BEGIN
	    OPEN pResultCursor FOR
		SELECT STD.Student_ID oasStudentId,
        NVL(STD.Customer_ID, 0) oasCustomerId,
        STD.User_Name StudentUserName,
        STD.First_Name FirstName,
        NVL(STD.Middle_Name, ' ') MiddleName,
        STD.Last_Name LastName,
        TO_CHAR(STD.BirthDate, 'mm/dd/yyyy') BirthDate,
        STD.Gender,
        STD.Grade,
        NVL(STD.Ext_Pin1, '') Ext_Pin1,
        NVL(STD.Ext_Pin2, '') Ext_Pin2,
		SA.SCREEN_MAGNIFIER AS SCREEN_MAGNIFIER, 
		SA.Screen_Reader AS Screen_Reader, 
		SA.Calculator AS Calculator, 
		SA.Test_Pause AS Test_Pause, 
		SA.Untimed_Test AS Untimed_Test,
		SA.Question_Background_Color, 
		SA.Question_Font_Color, 
		SA.Question_Font_Size,
		SA.Answer_Background_Color, 
		SA.Answer_Font_Color, 
		SA.Answer_Font_Size,
		SA.Highlighter AS Highlighter, 
		SA.Music_File_Id, 
		SA.Masking_Ruler AS Masking_Ruler, 
		SA.Magnifying_Glass AS Magnifying_Glass,
		SA.Extended_Time AS Extended_Time, 
		SA.Masking_Tool AS Masking_Tool, 
		SA.Microphone_Headphone AS Microphone_Headphone,
		SA.Extended_Time_Factor
        FROM Student STD, Student_Accommodation SA
        WHERE STD.STUDENT_ID = SA.Student_ID AND
		STD.STUDENT_ID = pStudentID;

		UPDATE BMTSYNC_Student_Status
		SET Export_Status = 'Inprogress'
		WHERE Student_ID = pStudentID;

		COMMIT;

	END StudentDetails;

	/*
	*   Procedure to return Student Accomodation Information
	*/
	PROCEDURE StudentAccomodation (
		pStudentID IN VARCHAR,
		pResultCursor OUT REF_CURSOR
	) AS
	BEGIN
		OPEN pResultCursor FOR
		SELECT NVL(SA.SCREEN_MAGNIFIER, 'F') AS SCREEN_MAGNIFIER, 
		NVL(SA.Screen_Reader, 'F') AS Screen_Reader, 
		NVL(SA.Calculator, 'F') AS Calculator, 
		NVL(SA.Test_Pause, 'F') AS Test_Pause, 
		NVL(SA.Untimed_Test, 'F') AS Untimed_Test,
		SA.Question_Background_Color, 
		SA.Question_Font_Color, 
		SA.Question_Font_Size,
		SA.Answer_Background_Color, 
		SA.Answer_Font_Color, 
		SA.Answer_Font_Size,
		NVL(SA.Highlighter, 'F') AS Highlighter, 
		SA.Music_File_Id, 
		NVL(SA.Masking_Ruler, 'F') AS Masking_Ruler, 
		NVL(SA.Magnifying_Glass, 'F') AS Magnifying_Glass,
		NVL(SA.Extended_Time, 'F') AS Extended_Time, 
		NVL(SA.Masking_Tool, 'F') AS Masking_Tool, 
		NVL(SA.Microphone_Headphone, 'F') AS Microphone_Headphone,
		SA.Extended_Time_Factor
		FROM Student_Accommodation SA 
		WHERE Student_Id = pStudentID;
		
	END StudentAccomodation;
	
	/*
	* Procedure to return Hierarchy parents
	* to which the stuident ID belongs
	*/
	 --pNodeCategoryName IN VARCHAR,
	
	PROCEDURE Heirarchy (
	   pStudentID        IN VARCHAR,
	   pResultCursor     OUT REF_CURSOR
	)  AS
	BEGIN

	    OPEN pResultCursor FOR
		SELECT   
		ONA.ANCESTOR_ORG_NODE_ID AS OAS_Heirarchy_ID,
		NVL(OND.ORG_NODE_CODE,  ' ') ORG_NODE_CODE,
		OND.ORG_NODE_NAME AS ORG_NODE_NAME, 
		ONC.CATEGORY_NAME AS CATEGORY_NAME,
		ONC.CATEGORY_LEVEL AS CATEGORY_LEVEL, 
		OND.CUSTOMER_ID AS CUSTOMER_ID
		FROM Org_Node_Ancestor ONA, Org_Node OND,
		Org_Node_Category ONC
		WHERE
		   ONA.ANCESTOR_ORG_NODE_ID = OND.ORG_NODE_ID AND
		   OND.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID AND
		   ONA.Org_Node_Id in (
		      SELECT Parent_Org_Node_Id FROM Org_Node_Parent
		      WHERE Org_Node_Id IN (
		        SELECT Org_Node_Id FROM Org_Node_Student ONS
		        WHERE ONS.Customer_Id = OND.CUSTOMER_ID AND
		        	ONS.STUDENT_ID = pStudentID )
		   ) 
        UNION
		SELECT ONS.ORG_NODE_ID AS OAS_Heirarchy_ID,
	    NVL(OND.ORG_NODE_CODE, ' ') AS ORG_NODE_CODE, 
		NVL(OND.ORG_NODE_NAME, ' ') AS ORG_NODE_NAME, 
		NVL(ONC.CATEGORY_NAME, ' ') AS CATEGORY_NAME,
		ONC.CATEGORY_LEVEL AS CATEGORY_LEVEL, 
		OND.CUSTOMER_ID AS CUSTOMER_ID 
	    FROM Student STD, Org_Node_Student ONS, Org_Node OND, ORG_NODE_CATEGORY ONC
		WHERE
		STD.STUDENT_ID = ONS.STUDENT_ID AND
		STD.CUSTOMER_ID = ONS.CUSTOMER_ID AND
		ONS.ORG_NODE_ID = OND.ORG_NODE_ID AND
	    OND.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID
	    AND STD.Student_ID = pStudentID 
 	    ORDER BY CATEGORY_LEVEL;

	END Heirarchy;

	/***************************************************************************************
	* Procedure to return the Student Class Hierachy details

	PROCEDURE HeirarchyClass  (
	   pStudentID    IN VARCHAR,
	   pResultCursor OUT REF_CURSOR
	) AS
	BEGIN
	    OPEN pResultCursor FOR
	    SELECT STD.STUDENT_ID, 
		ONS.ORG_NODE_ID Org_Node_Id,
	    NVL(OND.ORG_NODE_CODE, ' ') AS ORG_NODE_CODE, 
		NVL(OND.ORG_NODE_NAME, ' ') AS ORG_NODE_NAME, 
		NVL(ONC.CATEGORY_NAME, ' ') AS CATEGORY_NAME
	    FROM Student STD, Org_Node_Student ONS, Org_Node OND, ORG_NODE_CATEGORY ONC
		WHERE
		STD.STUDENT_ID = ONS.STUDENT_ID AND
		STD.CUSTOMER_ID = ONS.CUSTOMER_ID AND
		ONS.ORG_NODE_ID = OND.ORG_NODE_ID AND
	    OND.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID
	    AND STD.Student_ID = pStudentID;

	END HeirarchyClass;
	***************************************************************************************/
		
	/*
	*  Procedure to update the Student API export Status
	*/
	PROCEDURE updateStudentAPIStatus(
	    pStudentID     IN VARCHAR2,
		pAppName       IN VARCHAR2,
		pExportStatus  IN VARCHAR2,
		pErrorCode       IN VARCHAR2,
		pErrorMessage    IN VARCHAR2) AS
		
		--vStudentID            Student.Student_ID%TYPE;
		vNoOfAttempts         BMTSYNC_Student_Status.No_Of_Attempts%TYPE;
		vNext_Retry_DateTime  DATE; 
		vRetry                BMTSYNC_ERRORS.Retry_Error%TYPE;
		vExportStatus         BMTSYNC_Student_Status.Export_Status%TYPE;
	BEGIN
	    SELECT No_Of_Attempts INTO vNoOfAttempts 
		FROM BMTSYNC_Student_Status WHERE Student_ID = pStudentID;
		
		vExportStatus := pExportStatus;
		
		IF UPPER(vExportStatus) = 'FAILED' THEN 
		    --Check if the error need to be re-tryed
		    BEGIN
				SELECT NVL(Retry_Error, 'Y') INTO vRetry
				FROM  BMTSYNC_ERRORS
				WHERE Error_Code = pErrorCode;
			EXCEPTION 
			WHEN OTHERS THEN
				vRetry := 'Y';
			END;
			
			vNoOfAttempts := vNoOfAttempts + 1;			
			IF vRetry = 'Y' THEN
				-- Find the Next Retry time based on Retry latency
				BEGIN
					SELECT  SYSDATE +  Delay_in_Mins/1440 NEXT_RETRY_DATETIME INTO 
					vNext_Retry_DateTime
					FROM BMTSYNC_Retry_Frequency RF
					WHERE No_of_Attempt = vNoOfAttempts;
				EXCEPTION
				WHEN OTHERS THEN
				   vExportStatus := 'No Retry';
				   SELECT SYSDATE +  60/1440 INTO vNext_Retry_DateTime
					FROM DUAL;
				END;
			ELSE
			   vExportStatus := 'No Retry';
			END IF;

			
		END IF;
        
		   
		
	    IF UPPER(vExportStatus) = 'SUCCESS' THEN 
			UPDATE BMTSYNC_Student_Status
			SET App_Name = pAppName,
				Exported_On  = SYSDATE,
				Export_Status = vExportStatus,
				No_Of_Attempts = vNoOfAttempts,
				Error_Code    = pErrorCode,
				Error_Message = pErrorMessage,
				Next_Retry_DateTime = vNext_Retry_DateTime
			WHERE Student_ID = pStudentID 
			  AND UPPER(Export_Status) = 'INPROGRESS';
		ELSE
			UPDATE BMTSYNC_Student_Status
			SET App_Name = pAppName,
				Exported_On  = SYSDATE,
				Export_Status = vExportStatus,
				No_Of_Attempts = vNoOfAttempts,
				Error_Code    = pErrorCode,
				Error_Message = pErrorMessage,
				Next_Retry_DateTime = vNext_Retry_DateTime				
			WHERE Student_ID = pStudentID;
		END IF;
		COMMIT;
    EXCEPTION 
	WHEN NO_DATA_FOUND THEN
	    DBMS_OUTPUT.PUT_LINE('Insert');
	    INSERT INTO BMTSYNC_Student_Status (Student_ID, App_Name, Exported_ON, Export_Status, Error_Code, Error_Message, Next_Retry_DateTime)
	       VALUES (pStudentID, pAppName, SYSDATE, pExportStatus, pErrorCode, pErrorMessage, SYSDATE);
		COMMIT;

    WHEN OTHERS THEN	
		RAISE_APPLICATION_ERROR(-20001, 'PKG_Students.updateStudentAPIStatus FAILURE :' || SQLERRM(SQLCODE));
	END updateStudentAPIStatus;
    
	/*
	*  Procedure to insert/update the Student API Status table
	*  This procedure is called by the PKG_BMTSYNC.onChange_Student
	*  via Trigger TRG_BMTSYNC_STUDENT
	*/
	PROCEDURE AddUpdateStudentAPIStatus(
	    pStudentID     IN VARCHAR2,
		pAppName       IN VARCHAR2,
		pExportStatus  IN VARCHAR2) AS
		
		vNoOfAttempts         BMTSYNC_Student_Status.No_Of_Attempts%TYPE;
		vNext_Retry_DateTime  DATE; 
	BEGIN
	    SELECT No_Of_Attempts INTO vNoOfAttempts FROM BMTSYNC_Student_Status WHERE Student_ID = pStudentID;
		
		IF UPPER(pExportStatus) = 'NEW' THEN 
		   vNoOfAttempts := 0;
		   vNext_Retry_DateTime := SYSDATE;
		END IF;
        

		UPDATE BMTSYNC_Student_Status
		SET App_Name = pAppName,
			Exported_On  = SYSDATE,
			Export_Status = pExportStatus,
			No_Of_Attempts = vNoOfAttempts,
			Error_Code    = '',
			Error_Message = '',
			Next_Retry_DateTime = vNext_Retry_DateTime
		WHERE Student_ID = pStudentID;
		

		
    EXCEPTION 
	WHEN NO_DATA_FOUND THEN
	    DBMS_OUTPUT.PUT_LINE('Insert');
	    INSERT INTO BMTSYNC_Student_Status (Student_ID, App_Name, Exported_ON, Export_Status, Error_Code, Error_Message, Next_Retry_DateTime)
	       VALUES (pStudentID, pAppName, SYSDATE, pExportStatus, '', '', SYSDATE);

    WHEN OTHERS THEN	
		RAISE_APPLICATION_ERROR(-20001, 'PKG_Students.AddUpdateStudentAPIStatus FAILURE :' || SQLERRM(SQLCODE));
	END AddUpdateStudentAPIStatus;

	
END PKG_BMTSYNC_Students;
/

set TERMOUT on
PROMPT PKG_BMTSYNC_STUDENTS compiled
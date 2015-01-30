set termout off

declare
	vExists number := 0;

begin
	Select count(*) into vExists from user_tables where table_name = 'BMTSYNC_ERRORS';

	if (vExists = 0) then

		execute immediate 'CREATE TABLE BMTSYNC_ERRORS (
                        Error_Code      Number NOT NULL,
                        Error_Desc      VARCHAR2(200) NOT NULL,
                        Retry_Error     CHAR(1) NOT NULL
		)';

	end if;
end;
/

SET TERMOUT ON
PROMPT BMTSYNC_ERRORS table script complete;

DELETE FROM BMTSYNC_ERRORS;

INSERT INTO BMTSYNC_ERRORS VALUES (1, 'Request URI does not contain _method.', 'Y');	
INSERT INTO BMTSYNC_ERRORS VALUES (2, 'Requested student is not in the system.', 'N');	
INSERT INTO BMTSYNC_ERRORS VALUES (3, 'Request JSON is invalid', 'N');	
INSERT INTO BMTSYNC_ERRORS VALUES (4, 'Missing required field.', 'N');	
INSERT INTO BMTSYNC_ERRORS VALUES (5, 'Missing hierachy.', 'N');	
INSERT INTO BMTSYNC_ERRORS VALUES (6, 'Failed when create hierachy node.', 'Y');	
INSERT INTO BMTSYNC_ERRORS VALUES (7, 'Code exception, exception message + stack trace', 'N');	
INSERT INTO BMTSYNC_ERRORS VALUES (8, 'Failed to create student.', 'Y');	
INSERT INTO BMTSYNC_ERRORS VALUES (9, 'Request JSON student does not match URL student.', 'Y');	
INSERT INTO BMTSYNC_ERRORS VALUES (10, 'Could not find Request JSON.', 'N');	
INSERT INTO BMTSYNC_ERRORS VALUES (11, 'Student already exists.', 'Y');	
INSERT INTO BMTSYNC_ERRORS VALUES (12, 'BMT doesnot support data sync against this customer.', 'N');	
INSERT INTO BMTSYNC_ERRORS VALUES (13, 'GET is not supported in this API.', 'N');	
INSERT INTO BMTSYNC_ERRORS VALUES (14, 'Could not process delivery window.', 'N');	
INSERT INTO BMTSYNC_ERRORS VALUES (15, 'Test does not exist in BMT.', 'N');	
INSERT INTO BMTSYNC_ERRORS VALUES (16, 'Failed to create assignment.', 'Y');	
INSERT INTO BMTSYNC_ERRORS VALUES (17, 'Failed to create access code.', 'Y');	
INSERT INTO BMTSYNC_ERRORS VALUES (18, 'Can not reassign student when assignment already started.', 'N');
INSERT INTO BMTSYNC_ERRORS VALUES (999, 'Internal Server Error.', 'Y');	
commit;

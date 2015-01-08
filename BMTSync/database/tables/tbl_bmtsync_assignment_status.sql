set termout off

declare
	vExists number := 0;

begin
	Select count(*) into vExists from user_tables where table_name = 'BMTSYNC_ASSIGNMENT_STATUS';

	if (vExists = 0) then

		execute immediate 'CREATE TABLE BMTSYNC_ASSIGNMENT_STATUS (
                        Customer_ID           INTEGER NOT NULL,
                        Test_Admin_ID         INTEGER NOT NULL,
			Student_ID            INTEGER NOT NULL,
                        Roster_Id             INTEGER NOT NULL,
			App_Name              VARCHAR2(50) not null,
			Exported_On           DATE DEFAULT SYSDATE,
			Export_Status         VARCHAR2(10),
			No_Of_Attempts        INTEGER DEFAULT 0,
                        NEXT_RETRY_DATETIME   DATE,
			Error_Code            VARCHAR2(10),
			Error_Message         VARCHAR2(200) )  
                        PARTITION BY LIST(Export_Status) 
                        (
                        PARTITION Export_New VALUES ('New'),
                        PARTITION Export_Failed VALUES ('Failed'),
                        PARTITION Export_InProgress VALUES ('Inprogress'),
                        PARTITION Export_Other VALUES (DEFAULT)
		)';

	end if;
end;
/

SET TERMOUT ON
PROMPT BMTSYNC_ASSIGNMENT_STATUS table script complete;


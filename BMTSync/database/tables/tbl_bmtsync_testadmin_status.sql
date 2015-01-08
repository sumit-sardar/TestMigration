set termout off

declare
	vExists number := 0;

begin
	Select count(*) into vExists from user_tables where table_name = 'BMTSYNC_TESTADMIN_STATUS';

	if (vExists = 0) then

		execute immediate 'CREATE TABLE BMTSYNC_TESTADMIN_STATUS (
                        Customer_ID           INTEGER NOT NULL,
                        Test_Admin_ID         INTEGER NOT NULL,
			App_Name              VARCHAR2(50) not null,
			Exported_On           DATE DEFAULT SYSDATE,
			Export_Status         VARCHAR2(10),
			No_Of_Attempts        INTEGER DEFAULT 0,
                        NEXT_RETRY_DATETIME   DATE,
			Error_Code            VARCHAR2(10),
			Error_Message         VARCHAR2(200)
		)';

	end if;
end;
/

SET TERMOUT ON
PROMPT BMTSYNC_TESTADMIN_STATUS table script complete;

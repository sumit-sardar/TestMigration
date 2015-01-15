set termout off

declare
	vExists number := 0;

begin
	Select count(*) into vExists from user_tables where table_name = 'BMTSYNC_RETRY_FREQUENCY';

	if (vExists = 0) then

		execute immediate 'CREATE TABLE BMTSYNC_RETRY_FREQUENCY (
                        No_of_Attempt  NUMBER NOT NULL,
                        DELAY_IN_MINS  NUMBER NOT NULL                        

		)';

	end if;
end;
/

SET TERMOUT ON
PROMPT BMTSYNC_RETRY_FREQUENCY table script complete;


INSERT INTO BMTSYNC_RETRY_FREQUENCY VALUES (0, 1);
INSERT INTO BMTSYNC_RETRY_FREQUENCY VALUES (1, 1);
INSERT INTO BMTSYNC_RETRY_FREQUENCY VALUES (2, 1);
INSERT INTO BMTSYNC_RETRY_FREQUENCY VALUES (3, 5);
INSERT INTO BMTSYNC_RETRY_FREQUENCY VALUES (4, 15);
INSERT INTO BMTSYNC_RETRY_FREQUENCY VALUES (5, 60);


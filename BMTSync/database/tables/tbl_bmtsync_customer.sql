set termout off

declare
	vExists number := 0;

begin
	Select count(*) into vExists from user_tables where table_name = 'BMTSYNC_CUSTOMER';
dbms_output.put_line('vExists: '||vExists );
	if (vExists = 0) then

		execute immediate 'CREATE TABLE BMTSYNC_CUSTOMER (
			Customer_Id           INTEGER NOT NULL,
			Customer_Name        VARCHAR2(50) not null
		)';

	end if;
end;
/

SET TERMOUT ON
PROMPT BMTSYNC_CUSTOMER table script complete;
set termout off

declare
	vExists number := 0;

begin
	Select count(*) into vExists from user_tables where table_name = 'BMTSYNC_CUSTOMER';
dbms_output.put_line('vExists: '||vExists );
	if (vExists = 0) then

		execute immediate 'CREATE TABLE BMTSYNC_CUSTOMER (
			Customer_Id           INTEGER NOT NULL,
			Customer_Name        VARCHAR2(50) not null,
                        URL_ENDPOINT         VARCHAR2(2000)
		)';

	end if;
end;
/

INSERT INTO BMTSYNC_CUSTOMER(Customer_Id, Customer_Name, URL_ENDPOINT) VALUES 
(15357,'INDIANA', 'http://sync-gain-qa-elb.ec2-ctb.com/');

INSERT INTO BMTSYNC_CUSTOMER(Customer_Id, Customer_Name, URL_ENDPOINT) VALUES 
(16701, 'GEORGIA', 'http://sync-gain-content-elb.ec2-ctb.com/');


SET TERMOUT ON
PROMPT BMTSYNC_CUSTOMER table script complete;

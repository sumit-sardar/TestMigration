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

DELETE FROM BMTSYNC_CUSTOMER;

INSERT INTO BMTSYNC_CUSTOMER(Customer_Id, Customer_Name, URL_ENDPOINT) VALUES 
(7496,'INDIANA', 'http://insync-bmt.prod.ec2-ctb.com/');

INSERT INTO BMTSYNC_CUSTOMER(Customer_Id, Customer_Name, URL_ENDPOINT) VALUES 
(21264, 'GEORGIA', 'http://gasync-bmt.prod.ec2-ctb.com/');


INSERT INTO BMTSYNC_CUSTOMER(Customer_Id, Customer_Name, URL_ENDPOINT) VALUES 
(22446,'CTBQA ISTEP 2015', 'http://insync-bmt.prod.ec2-ctb.com/');

INSERT INTO BMTSYNC_CUSTOMER(Customer_Id, Customer_Name, URL_ENDPOINT) VALUES 
(22465, 'CTBQA GA 2015', 'http://gasync-bmt.prod.ec2-ctb.com/');

COMMIT;


SET TERMOUT ON
PROMPT BMTSYNC_CUSTOMER table script complete;

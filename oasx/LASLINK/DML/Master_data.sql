INSERT INTO product_resource VALUES (7000,'TDCLOGO','/resources/logoLL.jpg')
/

UPDATE score_lookup SET test_level = '2-3'  WHERE  FRAMEWORK_CODE = 'LLEAB' AND test_level = '2'
/
UPDATE score_lookup SET test_level = '4-5'  WHERE  FRAMEWORK_CODE = 'LLEAB' AND test_level = '3'
/
UPDATE score_lookup SET test_level = '6-8'  WHERE  FRAMEWORK_CODE = 'LLEAB' AND test_level = '4'
/
UPDATE score_lookup SET test_level = '9-12'  WHERE  FRAMEWORK_CODE = 'LLEAB' AND test_level = '5'
/

insert into job_status values (1, 'PENDING')
/
insert into job_status values (2, 'COMPLETE')
/
insert into job_status values (3, 'FAILED')
/
insert into job_status values (4, 'FILE GENERATION STARTED')
/
insert into job_status values (5, 'FILE GENERATION COMPLETED')
/
insert into job_status values (6, 'FILE GENERATION FAILED')
/
insert into job_status values (7, 'FILE TRANSFER STARTED')
/
insert into job_status values (8, 'FILE TRANSFER COMPLETED')
/
insert into job_status values (9, 'FILE TRANSFER FAILED')
/
insert into job_status values (10, 'TRANSFER INPROGRESS')
/
insert into job_status values (11, 'PROCESSING')
/

-- Changing tables resource_type and product_resource for OAS – Alternate URL - Part I-TAS

insert into resource_type values ('TDCINSTLIN', 'Test Delivery Client Installation For Linux')
/

insert into product_resource values (4000, 'TDCINSTPC', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.exe')
/
insert into product_resource values (4000, 'TDCINSTMAC', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.zip')
/
insert into product_resource values (4000, 'TDCINSTLIN', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.bin')
/

insert into product_resource values (7000, 'TDCINSTPC', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.exe')
/
insert into product_resource values (7000, 'TDCINSTMAC', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.zip')
/
insert into product_resource values (7000, 'TDCINSTLIN', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.bin')
/

insert into product_resource values (6100, 'TDCINSTPC', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.exe')
/
insert into product_resource values (6100, 'TDCINSTMAC', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.zip')
/
insert into product_resource values (6100, 'TDCINSTLIN', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.bin')
/

insert into product_resource values (3500, 'TDCINSTPC', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.exe')
/
insert into product_resource values (3500, 'TDCINSTMAC', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.zip')
/
insert into product_resource values (3500, 'TDCINSTLIN', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.bin')
/

insert into product_resource values (15, 'TDCINSTPC', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.exe')
/
insert into product_resource values (15, 'TDCINSTMAC', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.zip')
/
insert into product_resource values (15, 'TDCINSTLIN', 'http://oas.ctb.com/downloadfiles/InstallOnlineAsmt.bin')
/
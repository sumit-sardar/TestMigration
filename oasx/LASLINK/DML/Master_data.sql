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
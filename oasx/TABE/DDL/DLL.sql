/* New Table Added */
/* New table named customer_orgnode_license was created for license management.*/




CREATE TABLE customer_orgnode_license (

 org_node_id INTEGER ,
 customer_id NUMBER,
 product_id NUMBER,
 available NUMBER DEFAULT 0 NOT NULL,
 reserved  NUMBER DEFAULT 0 NOT NULL,
 consumed  NUMBER DEFAULT 0 NOT NULL,
 subtest_model VARCHAR2(1) DEFAULT 'F' NOT NULL,
 CONSTRAINT pk_orgnid_custid_prodid primary key(org_node_id,customer_id,product_id)
  )
/


/* Two new columns added in table customer_orgnode_license for license email notification */

ALTER TABLE customer_orgnode_license ADD license_after_last_purchase NUMBER
/

ALTER TABLE customer_orgnode_license ADD email_notify_flag VARCHAR2(1)
/

/* New cloumn added for removing harcoded item set ids from TMS for scoring */
ALTER TABLE product ADD( SCORABLE VARCHAR2(1))
/

/* New column extended_time added in table student_accomodation for student pacing */
ALTER TABLE student_accommodation ADD extended_time VARCHAR2(2)
/


/* New coloum extended_time added in test_roster for student pacing */
ALTER TABLE test_roster ADD extended_time NUMBER(2,1)
/

/* New column addded for do not score in test_roster*/
ALTER TABLE test_roster ADD DNS_STATUS CHAR(1)
/
ALTER TABLE test_roster ADD DNS_UPDATED_DATETIME DATE
/
ALTER TABLE test_roster ADD DNS_UPDATED_BY INTEGER
/

--Added for out of school user story
alter table student add out_of_school varchar2(3) default 'No'
/

-- Table added for pre scheduling
create table INCLUDE_TEST_SCHE_NODE
(
  CUSTOMER_ID     NUMBER not null,
  CORPORATE_ID    VARCHAR2(32) not null,
  SCHOOL_ID       VARCHAR2(32) not null,
  GRADE           VARCHAR2(4) not null,
  FORM_ASSIGNMENT VARCHAR2(10)
)
/
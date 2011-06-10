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
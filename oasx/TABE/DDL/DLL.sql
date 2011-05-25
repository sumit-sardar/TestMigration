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
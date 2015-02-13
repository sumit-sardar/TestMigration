create or replace TRIGGER OAS.SET_TMS_ADMIN_UPDATE
AFTER  UPDATE ON OAS.TEST_ADMIN REFERENCING
 NEW AS NEW
 OLD AS OLD
FOR EACH ROW
DECLARE
clusterId number :=1;
vExists number := 0;
BEGIN

  SELECT count(*) INTO vExists FROM BMTSYNC_CUSTOMER WHERE CUSTOMER_ID = :NEW.customer_id;
  IF (vExists > 0) THEN
    RETURN;
  END IF; 

   IF :NEW.test_admin_status = 'CU' THEN 
--    IF (:OLD.customer_id = 7496 OR :OLD.customer_id = 10714 OR :OLD.customer_id = 10384 OR :OLD.customer_id = 10730) THEN   -- For Prod environment
--    --IF (:OLD.customer_id = 7496 OR :OLD.customer_id = 10714 OR :OLD.customer_id = 10384 OR :OLD.customer_id = 10143) THEN  -- For QA environment
--      clusterId := 2;
--    ELSE
--      clusterId := 1;
--    END IF;

    insert into tms_prim_cache_prepop (test_roster_id, node_id, cluster_id) select test_roster_id, null as node_id, clusterId as cluster_id from test_roster ros where ros.test_admin_id = :NEW.test_admin_id;
   END IF; 

   EXCEPTION
     WHEN OTHERS THEN RAISE;
END SET_TMS_ADMIN_UPDATE;
/

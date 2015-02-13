create or replace TRIGGER OAS.SET_TMS_ROSTER_UPDATE
BEFORE  INSERT OR UPDATE OR DELETE ON OAS.TEST_ROSTER REFERENCING
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
  
--IF (:OLD.customer_id = 7496 OR :OLD.customer_id = 10714 OR :OLD.customer_id = 10384 OR  :OLD.customer_id = 10730) THEN  -- For Prod environment
----IF (:OLD.customer_id = 7496 OR :OLD.customer_id = 10714 OR :OLD.customer_id = 10384 OR  :OLD.customer_id = 10143) THEN  -- For QA environment
--  clusterId := 2;  --Indiana
--ELSE
--  clusterId := 1; --Oklahoma
--END IF;

IF NOT DELETING THEN
    IF not updating('TMS_UPDATE') THEN 
        :NEW.TMS_UPDATE := 'F';
        insert into tms_prim_cache_prepop (test_roster_id, cluster_id) values (:new.test_roster_id, clusterId);
     END IF;
ELSE
    insert into tms_prim_cache_prepop (test_roster_id, cluster_id) values (:old.test_roster_id, clusterId);
END IF;  

   EXCEPTION
     WHEN OTHERS THEN RAISE;
END SET_TMS_ROSTER_UPDATE;
/

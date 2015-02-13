create or replace TRIGGER OAS.SET_TMS_UPDATE
BEFORE  INSERT OR UPDATE ON OAS.STUDENT_ITEM_SET_STATUS REFERENCING
 NEW AS NEW
 OLD AS OLD
FOR EACH ROW
DECLARE
clusterId number :=1;
customerId number;
vExists number := 0;

BEGIN
  SELECT count(*) INTO vExists FROM BMTSYNC_CUSTOMER WHERE CUSTOMER_ID = (SELECT customer_id from TEST_ROSTER where TEST_ROSTER_ID = :NEW.test_roster_id);
  IF (vExists > 0) THEN
    RETURN;
  END IF; 
  
 IF not updating('TMS_UPDATE') THEN 
--    select customer_id into customerId from test_roster where test_roster_id = :new.test_roster_id;
-- 
--    IF (customerId = 7496 OR customerId = 10714 OR customerId = 10384 OR  customerId = 10730) THEN   -- For Prod environment
--    --IF (customerId = 7496 OR customerId = 10714 OR customerId = 10384 OR  customerId = 10143) THEN  -- For QA environment
--        clusterId := 2;
--    ELSE
--        clusterId := 1;
--    END IF;
    
    :NEW.TMS_UPDATE := 'F';
    insert into tms_prim_cache_prepop (test_roster_id, cluster_id) values (:new.test_roster_id, clusterId);
 END IF;


   EXCEPTION
     WHEN OTHERS THEN RAISE;
END SET_TMS_UPDATE;
/

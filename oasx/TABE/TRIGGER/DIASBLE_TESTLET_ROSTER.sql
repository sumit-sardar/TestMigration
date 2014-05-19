create or replace TRIGGER "DISABLE_TESTLET_ROSTER"
AFTER UPDATE
ON STUDENT_ITEM_SET_STATUS
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW

WHEN (NEW.COMPLETION_STATUS = 'CO')

DECLARE studentId NUMBER;

 sampleTest VARCHAR2(2);

 itemSetId NUMBER;

 rosterIdToDisable NUMBER;

 previousRosterSubject VARCHAR2(20);

 previousRosterLevel VARCHAR2(5);
 
 subject_list  VARCHAR2(200) := 'Language,Mathematics,Reading';

 deadlock_detected EXCEPTION;
  PRAGMA EXCEPTION_INIT(deadlock_detected, -60);
  resource_busy EXCEPTION;
  PRAGMA EXCEPTION_INIT(resource_busy, -54);

 BEGIN 
 
 itemSetId:= :NEW.ITEM_SET_ID;

 -- Get StudentId
 BEGIN
SELECT STUDENT_ID INTO studentID
FROM TEST_ROSTER
WHERE TEST_ROSTER_ID = :NEW.TEST_ROSTER_ID;

 EXCEPTION WHEN NO_DATA_FOUND THEN studentID := NULL;

 END;

 --Get itemSetId
 BEGIN
SELECT SAMPLE INTO sampleTest
FROM ITEM_SET
WHERE ITEM_SET_ID = itemSetId;

 EXCEPTION WHEN NO_DATA_FOUND THEN sampleTest := NULL;

 END;

 IF (sampleTest = 'F') THEN DBMS_OUTPUT.put_line('ID: '||studentID);

 DBMS_OUTPUT.put_line('SAMPLETEST: '||sampleTest);

 --Get level and subject
BEGIN
SELECT item_set_level,
       subject INTO previousRosterLevel,
                    previousRosterSubject
FROM
  (SELECT ROWNUM AS rn,
          item_set_level,
          subject
   FROM STUDENT_ITEM_SET_STATUS siss,
        TEST_ROSTER ros,
        item_set ii,
        test_admin ta
   WHERE siss.TEST_ROSTER_ID = ros.TEST_ROSTER_ID
     AND ros.STUDENT_ID = studentID
     AND ii.ITEM_SET_ID = siss.ITEM_SET_ID
     AND ii.subject=
       (SELECT subject
        FROM item_set_ancestor isa
        JOIN item_set ii ON ii.item_set_id = isa.item_set_id
        WHERE sample='F'
          AND ii.item_set_type='TD'
          AND ancestor_item_set_id=itemSetId)
     AND ii.sample='F'
     AND ii.item_set_level != 'L'
     AND siss.COMPLETION_STATUS = 'CO'
     AND ta.test_admin_id=ros.test_admin_id
     AND ta.test_catalog_id IN (select test_catalog_id from test_catalog where product_id in (4010,4009,4012,4011))
   ORDER BY siss.COMPLETION_DATE_TIME DESC)
WHERE rn =1;

 EXCEPTION WHEN NO_DATA_FOUND THEN previousRosterLevel := NULL;

 previousRosterSubject:= NULL;

 END;

 DBMS_OUTPUT.put_line('SUBJECT: '||previousRosterSubject);

 DBMS_OUTPUT.put_line('LEVEL: '||previousRosterLevel);

IF INSTR(',' || subject_list || ','
        ,',' || previousRosterSubject || ',') > 0 THEN
 --Get RosterId to disable
 BEGIN
SELECT testRosterId  INTO rosterIdToDisable
FROM
(SELECT ROWNUM AS rnum,siss.TEST_ROSTER_ID AS testRosterId
FROM STUDENT_ITEM_SET_STATUS siss,
     item_set ii,
     TEST_ROSTER ros,
     test_admin ta
WHERE ros.STUDENT_ID = studentId
  AND siss.TEST_ROSTER_ID = ros.TEST_ROSTER_ID
  AND siss.COMPLETION_STATUS not in ('CO')
  AND ii.ITEM_SET_ID = siss.ITEM_SET_ID
  AND ta.test_admin_id=ros.test_admin_id
  AND ta.test_catalog_id IN (SELECT test_catalog_id
          FROM TEST_CATALOG
          WHERE PRODUCT_ID =4201)
  AND ii.SUBJECT = previousRosterSubject
  AND ii.sample='F'
  AND ii.item_set_level != 'L'
  AND ITEM_SET_FORM IN
    (SELECT TESTLET_FORM
     FROM TESTLET_FORMS_BY_SUBJECT_LEVEL
     WHERE subject = previousRosterSubject
       AND TABE_LEVEL = previousRosterLevel
       AND TESTLET_FORM NOT IN
         (SELECT TESTLET_FORM
          FROM TESTLET_FORMS_BY_SUBJECT_LEVEL
          WHERE subject = previousRosterSubject
            AND TABE_LEVEL != previousRosterLevel))
 ) WHERE rnum =1;
 EXCEPTION WHEN NO_DATA_FOUND THEN rosterIdToDisable := NULL;
  
  
 END;
 
 ELSE 
rosterIdToDisable := NULL;

END IF; --compare subject

 DBMS_OUTPUT.put_line('RosterIdToDisable: '||rosterIdToDisable);

 ELSE DBMS_OUTPUT.put_line('Sample Test');

 IF rosterIdToDisable IS NOT NULL THEN

    UPDATE TEST_ROSTER SET ACTIVATION_STATUS = 'IN' WHERE TEST_ROSTER_ID = rosterIdToDisable;
   
 END IF;


 END IF; --sample test

 END;

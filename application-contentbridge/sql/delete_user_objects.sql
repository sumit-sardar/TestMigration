DECLARE
  CURSOR ld IS SELECT table_name, constraint_name FROM user_constraints
  	ORDER BY DECODE(constraint_type,'C',1,'R',2,'P',4,3);
  CURSOR lc IS SELECT object_name, object_type FROM user_objects;
  u VARCHAR2(40);
BEGIN
  DBMS_OUTPUT.enable(50000);
  SELECT USER INTO u FROM dual;
  IF u IN ('SYS','SYSTEM') THEN
    RAISE_APPLICATION_ERROR (-20000, 'DO NOT DROP SYS or SYSTEM, PLEASE!!!!!!!!!!!');
  END IF;
  FOR j IN ld LOOP
   BEGIN
     EXECUTE IMMEDIATE 'alter table ' || j.table_name || ' drop constraint ' || j.constraint_name;
   EXCEPTION
   WHEN OTHERS THEN
     NULL;
   END;
  END LOOP;
  FOR i IN lc LOOP
   BEGIN
     IF i.object_type = 'TABLE' THEN
       EXECUTE IMMEDIATE 'drop ' || i.object_type || ' ' || i.object_name||' cascade constraints';
     ELSE
       EXECUTE IMMEDIATE 'drop ' || i.object_type || ' ' || i.object_name;
     end IF;
   EXCEPTION
   WHEN OTHERS THEN
     NULL;
   END;
  END LOOP;
END;
/
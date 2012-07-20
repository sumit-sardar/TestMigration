CREATE OR REPLACE PROCEDURE SP_POC_SEQ_ID(in_TABLE_NAME IN VARCHAR2,o_SEQ_NO OUT INTEGER)
IS
   V_NEXTVAL INTEGER;
BEGIN
IF in_TABLE_NAME='CPM_ENROLLMENT_SITE' THEN
 SELECT CMP_ENROLLSITE_SEQ.NEXTVAL INTO V_NEXTVAL FROM DUAL;
 o_SEQ_NO:=V_NEXTVAL;
END IF;
END SP_POC_SEQ_ID;
/
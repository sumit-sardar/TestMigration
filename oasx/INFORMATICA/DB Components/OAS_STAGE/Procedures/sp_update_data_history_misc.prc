CREATE OR REPLACE PROCEDURE sp_update_data_history_misc(in_SESSION_NAME VARCHAR2)
IS
V_USER_ID NUMBER(38);
BEGIN
SELECT USER_ID INTO V_USER_ID FROM USERS WHERE USER_NAME= 'INFA_USER';
UPDATE DATA_IMPORT_HISTORY
SET UPDATED_BY=V_USER_ID,
UPDATED_DATE_TIME=SYSDATE
WHERE
IMPORT_FILENAME =in_SESSION_NAME AND UPDATED_BY IS NULL;
COMMIT;
END;
/

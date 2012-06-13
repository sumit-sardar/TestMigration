CREATE OR REPLACE PROCEDURE sp_update_data_history_A2(in_SESSION_NAME VARCHAR2)
IS
/*DESCRIPTION: This stored procedure updates the UPDATED_DATE_TIME field in the DATA_IMPORT_HISTORY table for the session name passed to it as input. The DATA_IMPORT_HISTORY table  is used to keep track of data history*/
/*AUTHOR: Wipro Offshore Team*/
/*CREATED DATE: 22nd February 2006*/
/*Variables to hold Customer_Id,Count,infa_user*/
V_USER_ID NUMBER(38);
BEGIN
SELECT USER_ID INTO V_USER_ID FROM USERS WHERE UPPER(USER_NAME)= 'SYSTEM';
UPDATE DATA_IMPORT_HISTORY
SET UPDATED_BY=V_USER_ID,
UPDATED_DATE_TIME=SYSDATE
WHERE
IMPORT_FILENAME =in_SESSION_NAME AND UPDATED_BY IS NULL;
COMMIT;
END;
/

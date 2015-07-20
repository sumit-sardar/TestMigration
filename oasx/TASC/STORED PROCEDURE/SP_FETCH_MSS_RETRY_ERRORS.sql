CREATE OR REPLACE PROCEDURE "SP_FETCH_MSS_RETRY_ERRORS" (fetchSize in number, temp_log_id OUT NUMBER) AS
BEGIN
DECLARE
  V_TMP_LOG_ID NUMBER;

begin
  SELECT SEQ_TMP_MSS_LOG_ID.NEXTVAL INTO V_TMP_LOG_ID FROM DUAL;

  INSERT INTO TMP_MSS_ERROR_LOG
    (INVOKE_LOG_KEY, TMP_ID)
    SELECT SCORING_INVOKE_LOG_KEY, V_TMP_LOG_ID
      FROM (SELECT SCORING_INVOKE_LOG_KEY, V_TMP_LOG_ID
              FROM SCORING_INVOKE_LOG A
             WHERE A.STATUS = 'Progress'
               AND A.INVOKE_COUNT < 5
               AND NOT EXISTS
             (SELECT 1
                      FROM TMP_MSS_ERROR_LOG B
                     WHERE A.SCORING_INVOKE_LOG_KEY = B.INVOKE_LOG_KEY)
             ORDER BY A.updated_date ASC)
     WHERE ROWNUM <= fetchSize;
     
     COMMIT;

     temp_log_id := V_TMP_LOG_ID;
END;
END;
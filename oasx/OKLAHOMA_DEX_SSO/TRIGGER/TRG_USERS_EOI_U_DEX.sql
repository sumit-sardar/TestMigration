CREATE OR REPLACE TRIGGER "TRG_USERS_EOI_U_DEX" 
AFTER UPDATE ON
USERS
REFERENCING  NEW AS NEW  OLD AS OLD
FOR EACH ROW 
when (NEW.ACTIVATION_STATUS <> 'IN')
DECLARE
  PRAGMA AUTONOMOUS_TRANSACTION;
  V_CUSTOMER_ID     INTEGER := 0;
  V_OK_EOI_CUSTOMER INTEGER := 0;
  V_OK_38_CUSTOMER  INTEGER := 0;
  V_RECORD_COUNT    INTEGER := 0;
  V_EOI_USER_ID     INTEGER := NULL;
  V_OK38_USER_COUNT INTEGER := NULL;
  V_OK_38_USER_ID   INTEGER := NULL;
  V_ADDRESS_ID_SEQ  INTEGER := NULL;
  V_EOI_ADDRESS_ID  INTEGER := NULL;

BEGIN

  V_EOI_USER_ID := :NEW.USER_ID;

  SELECT DISTINCT ORG.CUSTOMER_ID
    INTO V_CUSTOMER_ID
    FROM USER_ROLE UR, ORG_NODE ORG
   WHERE UR.USER_ID = V_EOI_USER_ID
     AND UR.ORG_NODE_ID = ORG.ORG_NODE_ID
     AND ORG.ACTIVATION_STATUS = 'AC'
     AND UR.ACTIVATION_STATUS = 'AC';

  SELECT COUNT(1)
    INTO V_RECORD_COUNT
    FROM OK_EOI_38_REPLICATE_CONFIG
   WHERE ACTIVATION_STATUS = 'AC';

  IF V_RECORD_COUNT = 1 THEN
  
    /*FETCHING THE OK-EOI AND OK3-8 CUSTOMERID*/
    SELECT OK.OK_EOI_CUSTOMER_ID, OK.OK_38_CUSTOMER_ID
      INTO V_OK_EOI_CUSTOMER, V_OK_38_CUSTOMER
      FROM OK_EOI_38_REPLICATE_CONFIG OK
     WHERE ACTIVATION_STATUS = 'AC';
  
    /*IF USER IS ASSOCIATED WITH OK-EOI CUSTOMER THEN ONLY PROCESS FURTHER LOGIC */
    IF V_CUSTOMER_ID = V_OK_EOI_CUSTOMER THEN
    
      /* CHECKING IF THE OK-EOI USER HAS ALREADY ANY OK3-8 USER ASSOCIATED WITH ITSELF */
      SELECT COUNT(1)
        INTO V_OK38_USER_COUNT
        FROM EOI_3TO8_USER_MAPPING OKMAP
       WHERE OKMAP.USER_ID_EOI = V_EOI_USER_ID
         AND OKMAP.ACTIVATION_STATUS = 'AC';
    
      /* IF MAPPING IS ALREADY PRESENT THEN INSERT CAN ONLY OCCUR IN USER_ROLE TABLE.
      AS THIS WILL ONLY HAPPEN WHEN ROLE IS UPDATED FOR OK-EOI CUSTOMER
      OR THE OK-EOI USER IS ASSOCIATED WITH ANOTHER ORG_NODE.
      ALSO ADDRESS CAN BE INSERTED/USER PROFILE CAN BE UPDATED*/
      IF V_OK38_USER_COUNT = 1 THEN
      
        SELECT OKMAP.USER_ID_3TO8
          INTO V_OK_38_USER_ID
          FROM EOI_3TO8_USER_MAPPING OKMAP
         WHERE OKMAP.USER_ID_EOI = V_EOI_USER_ID
           AND OKMAP.ACTIVATION_STATUS = 'AC';
      
        UPDATE USERS U
           SET U.PASSWORD                  = :NEW.PASSWORD,
               U.FIRST_NAME                = :NEW.FIRST_NAME,
               U.MIDDLE_NAME               = :NEW.MIDDLE_NAME,
               U.LAST_NAME                 = :NEW.LAST_NAME,
               U.EMAIL                     = :NEW.EMAIL,
               U.PASSWORD_EXPIRATION_DATE  = :NEW.PASSWORD_EXPIRATION_DATE,
               U.ACTIVE_SESSION            = :NEW.ACTIVE_SESSION,
               U.TIME_ZONE                 = :NEW.TIME_ZONE,
               U.CREATED_DATE_TIME         = :NEW.CREATED_DATE_TIME,
               U.PASSWORD_HINT_ANSWER      = :NEW.PASSWORD_HINT_ANSWER,
               U.PASSWORD_HINT_QUESTION_ID = :NEW.PASSWORD_HINT_QUESTION_ID,
               U.RESET_PASSWORD            = :NEW.RESET_PASSWORD,
               U.UPDATED_DATE_TIME         = :NEW.UPDATED_DATE_TIME,
               U.UPDATED_BY                = :NEW.UPDATED_BY,
               U.CREATED_BY                = :NEW.CREATED_BY,
               U.LAST_LOGIN_DATE_TIME      = :NEW.LAST_LOGIN_DATE_TIME,
               U.PREFIX                    = :NEW.PREFIX,
               U.SUFFIX                    = :NEW.SUFFIX,
               U.PREFERRED_NAME            = :NEW.PREFERRED_NAME,
               U.EXT_SCHOOL_ID             = :NEW.EXT_SCHOOL_ID,
               U.DATA_IMPORT_HISTORY_ID    = :NEW.DATA_IMPORT_HISTORY_ID,
               U.EXT_PIN1                  = :NEW.EXT_PIN1,
               U.EXT_PIN2                  = :NEW.EXT_PIN2,
               U.EXT_PIN3                  = :NEW.EXT_PIN3,
               U.ACTIVATION_STATUS         = :NEW.ACTIVATION_STATUS,
               U.DISPLAY_NEW_MESSAGE       = :NEW.DISPLAY_NEW_MESSAGE
         WHERE U.USER_ID = V_OK_38_USER_ID;
      
        /* This block will only be executed if the address was not entered first and 
        *  later the address got entered.
        */
        IF :OLD.ADDRESS_ID IS NULL AND :NEW.ADDRESS_ID IS NOT NULL THEN
        
          SELECT SEQ_ADDRESS_ID.NEXTVAL INTO V_ADDRESS_ID_SEQ FROM DUAL;
          V_EOI_ADDRESS_ID := :NEW.ADDRESS_ID;
          /*
          * INSERT INTO ADDRESS TABLE FIRST
          *
          */
          INSERT INTO ADDRESS
            (ADDRESS_ID,
             STREET_LINE1,
             STREET_LINE2,
             STREET_LINE3,
             CITY,
             STATEPR,
             COUNTRY,
             ZIPCODE,
             PRIMARY_PHONE,
             CREATED_DATE_TIME,
             FAX,
             CREATED_BY,
             UPDATED_BY,
             UPDATED_DATE_TIME,
             DATA_IMPORT_HISTORY_ID,
             SECONDARY_PHONE,
             ZIPCODE_EXT,
             PRIMARY_PHONE_EXT)
            (SELECT V_ADDRESS_ID_SEQ,
                    STREET_LINE1,
                    STREET_LINE2,
                    STREET_LINE3,
                    CITY,
                    STATEPR,
                    COUNTRY,
                    ZIPCODE,
                    PRIMARY_PHONE,
                    CREATED_DATE_TIME,
                    FAX,
                    CREATED_BY,
                    UPDATED_BY,
                    UPDATED_DATE_TIME,
                    DATA_IMPORT_HISTORY_ID,
                    SECONDARY_PHONE,
                    ZIPCODE_EXT,
                    PRIMARY_PHONE_EXT
               FROM ADDRESS A
              WHERE A.ADDRESS_ID = V_EOI_ADDRESS_ID);
        
          /* AFTER INSERTING THE NEW ADDRESS INFORMATION, ASSOCIATE THE ADDRESS INFO WITH
          *  USER-PROFILE FOR OK3-8
          */
          UPDATE USERS U
             SET U.ADDRESS_ID = V_ADDRESS_ID_SEQ
           WHERE U.USER_ID = V_OK_38_USER_ID;
        
        END IF;
      END IF; -- IF MAPPING IS PRESENT.
      COMMIT;
    END IF; -- EOI CUSTOMER DATA REPLICATION ENDS
  END IF; -- CONFIG TABLE CHECK END

EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.put_line('TRIGGER EXCEPTION WHEN RECORD UPDATED.');
END;
/

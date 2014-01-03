CREATE OR REPLACE TRIGGER "TRG_USER_ROLE_EOI_I_DEX"
  AFTER INSERT ON USER_ROLE
  REFERENCING NEW AS NEW OLD AS OLD
  FOR EACH ROW

DECLARE
  PRAGMA AUTONOMOUS_TRANSACTION;
  V_CUSTOMER_ID             INTEGER := 0;
  V_ORG_NODE_CATEGORY_ID    INTEGER := 0;
  V_TOP_EOI_ORG_CATEGORY_ID INTEGER := 0;
  V_TOP_38_ORG_CATEGORY_ID  INTEGER := 0;
  V_ORG_NODE_CODE           VARCHAR2(32) := '';
  V_OK_EOI_CUSTOMER         INTEGER := 0;
  V_OK_38_CUSTOMER          INTEGER := 0;
  V_USER_ID_SEQ             INTEGER := NULL;
  V_ADDRESS_ID_SEQ          INTEGER := NULL;
  V_ADDRESS_ID              INTEGER := NULL;
  V_OK_38_ORG_NODE_ID       INTEGER := NULL;
  V_RECORD_COUNT            INTEGER := 0;
  V_USER_NAME_EOI           VARCHAR2(32) := '';
  V_USER_NAME_38            VARCHAR2(32) := '';
  V_USER_NAME_38_NEW        VARCHAR2(32) := '';
  V_EOI_ORG_NODE_ID         INTEGER := NULL;
  V_EOI_USER_ID             INTEGER := NULL;
  V_EOI_ROLE_ID             INTEGER := NULL;
  V_OK38_USER_COUNT         INTEGER := NULL;
  V_OK_38_USER_ID           INTEGER := NULL;
  V_LOGICAL_ERROR           INTEGER := 0;
  V_USER_NAME_PRESENT       INTEGER := 0;
  V_EOI_USER_COUNT          INTEGER := NULL;
  V_ORG_RECORD_PRESENT      INTEGER := NULL;

BEGIN
  
  V_EOI_ORG_NODE_ID := :NEW.ORG_NODE_ID;
  V_EOI_USER_ID     := :NEW.USER_ID;
  V_EOI_ROLE_ID     := :NEW.ROLE_ID;

  SELECT COUNT(1)
    INTO V_RECORD_COUNT
    FROM OK_EOI_38_REPLICATE_CONFIG
   WHERE ACTIVATION_STATUS = 'AC';

  /*CHECKING IF ONLY 1 ROW IS THERE IN CONFIG TABLE WITH 'AC' ACTIVATION STATUS*/
  IF V_RECORD_COUNT = 1 THEN
  
    /*FETCHING THE OK-EOI AND OK3-8 CUSTOMERID*/
    SELECT OK.OK_EOI_CUSTOMER_ID, OK.OK_38_CUSTOMER_ID
      INTO V_OK_EOI_CUSTOMER, V_OK_38_CUSTOMER
      FROM OK_EOI_38_REPLICATE_CONFIG OK
     WHERE ACTIVATION_STATUS = 'AC';
  
    SELECT COUNT(1)
      INTO V_ORG_RECORD_PRESENT
      FROM ORG_NODE
     WHERE ORG_NODE_ID = V_EOI_ORG_NODE_ID
     AND   ACTIVATION_STATUS = 'AC';
  
    IF V_ORG_RECORD_PRESENT <> 0 THEN
    
      /*FETCHING DETAILS OF THE ORGANISATION FOR WHICH THE USER WAS CREATED*/
      SELECT ORG.CUSTOMER_ID,
             ORG.ORG_NODE_CATEGORY_ID,
             DECODE(ORG.ORG_NODE_CODE, NULL, 'NULL', ORG.ORG_NODE_CODE)
        INTO V_CUSTOMER_ID, V_ORG_NODE_CATEGORY_ID, V_ORG_NODE_CODE
        FROM ORG_NODE ORG
       WHERE ORG.ORG_NODE_ID = V_EOI_ORG_NODE_ID
         AND ORG.ACTIVATION_STATUS = 'AC';
      /*IF USER IS ASSOCIATED WITH OK-EOI CUSTOMER THEN ONLY PROCESS FURTHER LOGIC */
      IF V_CUSTOMER_ID = V_OK_EOI_CUSTOMER THEN
      
        /*FIRST FETCH ALL ORG-NODE  DETAILS OF OK3-8 RELATED TO ORG_NODE OF OK-EOI*/
        IF V_ORG_NODE_CODE = 'NULL' THEN
          SELECT ORG_NODE_CATEGORY_ID
            INTO V_TOP_EOI_ORG_CATEGORY_ID
            FROM (SELECT ORG_NODE_CATEGORY_ID
                    FROM ORG_NODE_CATEGORY ONC
                   WHERE ONC.CUSTOMER_ID = V_OK_EOI_CUSTOMER
                   ORDER BY CATEGORY_LEVEL)
           WHERE ROWNUM = 1;
        
          SELECT ORG_NODE_CATEGORY_ID
            INTO V_TOP_38_ORG_CATEGORY_ID
            FROM (SELECT ORG_NODE_CATEGORY_ID
                    FROM ORG_NODE_CATEGORY ONC
                   WHERE ONC.CUSTOMER_ID = V_OK_38_CUSTOMER
                   ORDER BY CATEGORY_LEVEL)
           WHERE ROWNUM = 1;
        
          /*
          * If this condition satisfies then it means that user is associated with a
          * org that is of top-level.
          */
          IF V_TOP_EOI_ORG_CATEGORY_ID = V_ORG_NODE_CATEGORY_ID THEN
          
            SELECT ORG.ORG_NODE_ID
              INTO V_OK_38_ORG_NODE_ID
              FROM ORG_NODE ORG
             WHERE ORG.CUSTOMER_ID = V_OK_38_CUSTOMER
               AND ORG.ORG_NODE_CATEGORY_ID = V_TOP_38_ORG_CATEGORY_ID
               AND ORG.ACTIVATION_STATUS = 'AC';
          
            V_LOGICAL_ERROR := 1;
          
          ELSE
            /*IF CONTROL ENTERS THE ELSE PART, THIS MEANS AN EOI-ORG_NODE_CODE IS NULL OTHER THAN TOP NODE.
            THEN WE WILL NOT PROCESS ANYTHING.HENCE TRACKING THIS ISSUE WITH THIS VARIABLE.
            THE VALUE OF THIS VARIABLE SHOULD NEVER BE 2.*/
            V_LOGICAL_ERROR := 2;
          
          END IF;
        
        ELSE
        
          SELECT ORG.ORG_NODE_ID
            INTO V_OK_38_ORG_NODE_ID
            FROM ORG_NODE ORG
           WHERE ORG.CUSTOMER_ID = V_OK_38_CUSTOMER
             AND UPPER(ORG.ORG_NODE_CODE) = UPPER(V_ORG_NODE_CODE)
             AND ORG.ACTIVATION_STATUS = 'AC'
             AND ROWNUM = 1;
        
        END IF;
      
        /*IF THIS VALUE IS 2 , THEN DONOT PROCESS ANYTHING FURTHER AS ORG-CODE IS NULL AND THIS EOI-ORGNODE
        IS NOT TOP ORGNODE.*/
        IF V_LOGICAL_ERROR <> 2 THEN
        
          /* CHECKING IF THE OK-EOI USER HAS ALREADY ANY OK3-8 USER ASSOCIATED WITH ITSELF */
          SELECT COUNT(1)
            INTO V_OK38_USER_COUNT
            FROM EOI_3TO8_USER_MAPPING OKMAP
           WHERE OKMAP.USER_ID_EOI = V_EOI_USER_ID
             AND OKMAP.ACTIVATION_STATUS = 'AC';
        
          /* IF MAPPING IS ALREADY PRESENT THEN INSERT ONLY INTO USER_ROLE TABLE.
          AS THIS WILL ONLY HAPPEN WHEN ROLE IS UPDATED FOR OK-EOI CUSTOMER
          OR THE OK-EOI USER IS ASSOCIATED WITH ANOTHER ORG_NODE*/
          IF V_OK38_USER_COUNT = 1 THEN
          
            SELECT OKMAP.USER_ID_3TO8
              INTO V_OK_38_USER_ID
              FROM EOI_3TO8_USER_MAPPING OKMAP
             WHERE OKMAP.USER_ID_EOI = V_EOI_USER_ID
               AND OKMAP.ACTIVATION_STATUS = 'AC';
          
            INSERT INTO USER_ROLE
              (USER_ID,
               ROLE_ID,
               ORG_NODE_ID,
               CREATED_BY,
               CREATED_DATE_TIME,
               UPDATED_BY,
               UPDATED_DATE_TIME,
               ACTIVATION_STATUS,
               DATA_IMPORT_HISTORY_ID)
            VALUES
              (V_OK_38_USER_ID,
               :NEW.ROLE_ID,
               V_OK_38_ORG_NODE_ID,
               :NEW.CREATED_BY,
               :NEW.CREATED_DATE_TIME,
               :NEW.UPDATED_BY,
               :NEW.UPDATED_DATE_TIME,
               :NEW.ACTIVATION_STATUS,
               :NEW.DATA_IMPORT_HISTORY_ID);
          
          ELSE
          
            SELECT COUNT(1)
              INTO V_EOI_USER_COUNT
              FROM USERS
             WHERE USER_ID = V_EOI_USER_ID;
          
            IF V_EOI_USER_COUNT = 1 THEN
            
              /* FRESH INSERTION IS TAKING PLACE.. CREATE A NEW OK3-8 USER */
              SELECT SEQ_USER_ID.NEXTVAL INTO V_USER_ID_SEQ FROM DUAL;
            
              SELECT DECODE(ADDRESS_ID, NULL, 0, ADDRESS_ID)
                INTO V_ADDRESS_ID
                FROM USERS
               WHERE USER_ID = V_EOI_USER_ID;
            
              -- This checks if address is present or not at all.
              IF V_ADDRESS_ID <> 0 THEN
              
                SELECT SEQ_ADDRESS_ID.NEXTVAL
                  INTO V_ADDRESS_ID_SEQ
                  FROM DUAL;
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
                    WHERE A.ADDRESS_ID =
                          (SELECT ADDRESS_ID
                             FROM USERS
                            WHERE USER_ID = V_EOI_USER_ID));
              
              END IF;
            
              /*PREPARE USER_NAME*/
              SELECT USER_NAME
                INTO V_USER_NAME_EOI
                FROM USERS
               WHERE USER_ID = V_EOI_USER_ID;
            
              V_USER_NAME_38     := V_USER_NAME_EOI || '_38';
              V_USER_NAME_38_NEW := V_USER_NAME_38;
            
              SELECT COUNT(1)
                INTO V_USER_NAME_PRESENT
                FROM USERS
               WHERE USER_NAME = V_USER_NAME_38;
            
              /*THIS USERNAME ALREADY EXISTS */
              IF V_USER_NAME_PRESENT <> 0 THEN
              
                FOR I IN 1 .. 999 LOOP
                  V_USER_NAME_38_NEW := V_USER_NAME_38 || '_' || TO_CHAR(I);
                
                  SELECT COUNT(1)
                    INTO V_USER_NAME_PRESENT
                    FROM USERS
                   WHERE USER_NAME = V_USER_NAME_38_NEW;
                
                  IF V_USER_NAME_PRESENT = 0 THEN
                    EXIT;
                  END IF;
                END LOOP;
              
              END IF;
            
              /*
              * INSERT INTO USERS TABLE 
              *
              */
              INSERT INTO USERS
                (USER_ID,
                 USER_NAME,
                 PASSWORD,
                 FIRST_NAME,
                 MIDDLE_NAME,
                 LAST_NAME,
                 EMAIL,
                 PASSWORD_EXPIRATION_DATE,
                 ADDRESS_ID,
                 ACTIVE_SESSION,
                 TIME_ZONE,
                 CREATED_DATE_TIME,
                 PASSWORD_HINT_ANSWER,
                 PASSWORD_HINT_QUESTION_ID,
                 RESET_PASSWORD,
                 UPDATED_DATE_TIME,
                 UPDATED_BY,
                 CREATED_BY,
                 LAST_LOGIN_DATE_TIME,
                 PREFIX,
                 SUFFIX,
                 PREFERRED_NAME,
                 EXT_SCHOOL_ID,
                 DATA_IMPORT_HISTORY_ID,
                 EXT_PIN1,
                 EXT_PIN2,
                 EXT_PIN3,
                 ACTIVATION_STATUS,
                 DISPLAY_USER_NAME,
                 DISPLAY_NEW_MESSAGE)
                (SELECT V_USER_ID_SEQ               AS USER_ID,
                        V_USER_NAME_38_NEW          AS USER_NAME,
                        U.PASSWORD                  AS PASSWORD,
                        U.FIRST_NAME                AS FIRST_NAME,
                        U.MIDDLE_NAME               AS MIDDLE_NAME,
                        U.LAST_NAME                 AS LAST_NAME,
                        U.EMAIL                     AS EMAIL,
                        U.PASSWORD_EXPIRATION_DATE  AS PASSWORD_EXPIRATION_DATE,
                        V_ADDRESS_ID_SEQ            AS ADDRESS_ID,
                        U.ACTIVE_SESSION            AS ACTIVE_SESSION,
                        U.TIME_ZONE                 AS TIME_ZONE,
                        U.CREATED_DATE_TIME         AS CREATED_DATE_TIME,
                        U.PASSWORD_HINT_ANSWER      AS PASSWORD_HINT_ANSWER,
                        U.PASSWORD_HINT_QUESTION_ID AS PASSWORD_HINT_QUESTION_ID,
                        U.RESET_PASSWORD            AS RESET_PASSWORD,
                        U.UPDATED_DATE_TIME         AS UPDATED_DATE_TIME,
                        U.UPDATED_BY                AS UPDATED_BY,
                        U.CREATED_BY                AS CREATED_BY,
                        U.LAST_LOGIN_DATE_TIME      AS LAST_LOGIN_DATE_TIME,
                        U.PREFIX                    AS PREFIX,
                        U.SUFFIX                    AS SUFFIX,
                        U.PREFERRED_NAME            AS PREFERRED_NAME,
                        U.EXT_SCHOOL_ID             AS EXT_SCHOOL_ID,
                        U.DATA_IMPORT_HISTORY_ID    AS DATA_IMPORT_HISTORY_ID,
                        U.EXT_PIN1                  AS EXT_PIN1,
                        U.EXT_PIN2                  AS EXT_PIN2,
                        U.EXT_PIN3                  AS EXT_PIN3,
                        U.ACTIVATION_STATUS         AS ACTIVATION_STATUS,
                        V_USER_NAME_38_NEW          AS DISPLAY_USER_NAME,
                        U.DISPLAY_NEW_MESSAGE       AS DISPLAY_NEW_MESSAGE
                   FROM USERS U
                  WHERE U.USER_ID = V_EOI_USER_ID);
            
              /* 
               * Insert into user_role table
               *
              */
            
              INSERT INTO USER_ROLE
                (USER_ID,
                 ROLE_ID,
                 ORG_NODE_ID,
                 CREATED_BY,
                 CREATED_DATE_TIME,
                 UPDATED_BY,
                 UPDATED_DATE_TIME,
                 ACTIVATION_STATUS,
                 DATA_IMPORT_HISTORY_ID)
              VALUES
                (V_USER_ID_SEQ,
                 :NEW.ROLE_ID,
                 V_OK_38_ORG_NODE_ID,
                 :NEW.CREATED_BY,
                 :NEW.CREATED_DATE_TIME,
                 :NEW.UPDATED_BY,
                 :NEW.UPDATED_DATE_TIME,
                 :NEW.ACTIVATION_STATUS,
                 :NEW.DATA_IMPORT_HISTORY_ID);
            
              /**
              * Now create the mapping of OK EOI and OK 3-8 customer
              *
              */
            
              INSERT INTO EOI_3TO8_USER_MAPPING
              VALUES
                (V_USER_NAME_EOI,
                 :NEW.USER_ID,
                 V_USER_NAME_38_NEW,
                 V_USER_ID_SEQ,
                 'AC',
                 SYSDATE);
            
            ELSE
              /*Changes for Defect #76124*/
              /* This block of code gets executed when USER is created through DEX.
              *  DEX application creates users by Single Transaction Approach. Hence 
              *  the user data will not be available to be fetched as data is not 
              *  committed in these Tables. To capture this data a workaround code is 
              *  implemented here. :: 
              * EOI user-id will be inserted in a table. Now a scheduled oracle job will call
              * a procedure which will replicate the data of users of OKEOI to OK3-8.
              * Those users will be replicated whose Ids are mentioned in this table.
              */
            
              INSERT INTO OK_EOI_38_USER_REPLICATE
                (OK_EOI_USER_ID,
                 OK_EOI_ROLE_ID,
                 OK_EOI_ORG_NODE_ID,
                 OK_38_ORG_NODE_ID,
                 REPLICATION_STATUS,
                 CREATED_DATE_TIME,
                 UPDATED_DATE_TIME)
              VALUES
                (V_EOI_USER_ID,
                 V_EOI_ROLE_ID,
                 V_EOI_ORG_NODE_ID,
                 V_OK_38_ORG_NODE_ID,
                 'NEW',
                 SYSDATE,
                 NULL);
            
            END IF; -- INSERTION ENDS  
          END IF; -- CHECKING IF EOI USER ENTRY HAS BEEN MADE ALREADY.. DEX EXCEPTION WAS OCCURING HERE
        END IF; -- CHECKING OF DATA ERROR ENDS.
        COMMIT;
      END IF; -- EOI CUSTOMER DATA REPLICATION ENDS  
    END IF; -- DATA IS PRESENT IN ORG-NODE TABLE. I.E. DATA IS COMMITTED.
  END IF; -- CONFIG TABLE CHECK END

  /*<<escapeTrigger>>
  dbms_output.put_line('escaped');*/
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.put_line('TRIGGER EXCEPTION WHEN RECORD INSERTED in TRG_USER_ROLE_EOI_I_DEX' ||
                         sqlcode || '~' || sqlerrm);
END;
/

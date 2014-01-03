CREATE OR REPLACE PROCEDURE SP_OK_EOI_38_USER_REPLICATE IS

  CURSOR CUR_USER_DETAILS IS
    SELECT U.OK_EOI_USER_ID,
           U.OK_EOI_ROLE_ID,
           U.OK_EOI_ORG_NODE_ID,
           U.OK_38_ORG_NODE_ID
      FROM OK_EOI_38_USER_REPLICATE U
     WHERE UPPER(U.REPLICATION_STATUS) = 'NEW';

  V_EOI_USER_ID       INTEGER := 0;
  V_EOI_ROLE_ID       INTEGER := 0;
  V_EOI_ORG_NODE_ID   INTEGER := 0;
  V_OK_38_ORG_NODE_ID INTEGER := 0;
  V_EOI_USER_COUNT    INTEGER := NULL;
  V_USER_ID_SEQ       INTEGER := 0;
  V_ADDRESS_ID        INTEGER := NULL;
  V_ADDRESS_ID_SEQ    INTEGER := NULL;
  V_USER_NAME_EOI     VARCHAR2(32) := '';
  V_USER_NAME_38      VARCHAR2(32) := '';
  V_USER_NAME_38_NEW  VARCHAR2(32) := '';
  V_USER_NAME_PRESENT INTEGER := 0;
  V_USER_PRESENT      INTEGER := NULL;

BEGIN

  FOR V_USER_DETAILS IN CUR_USER_DETAILS LOOP
  
    V_EOI_USER_ID       := V_USER_DETAILS.OK_EOI_USER_ID;
    V_EOI_ROLE_ID       := V_USER_DETAILS.OK_EOI_ROLE_ID;
    V_EOI_ORG_NODE_ID   := V_USER_DETAILS.OK_EOI_ORG_NODE_ID;
    V_OK_38_ORG_NODE_ID := V_USER_DETAILS.OK_38_ORG_NODE_ID;
  
    SELECT COUNT(1)
      INTO V_EOI_USER_COUNT
      FROM USERS
     WHERE USER_ID = V_EOI_USER_ID;
  
    IF V_EOI_USER_COUNT = 1 THEN
    
      SELECT COUNT(1)
        INTO V_USER_PRESENT
        FROM EOI_3TO8_USER_MAPPING U
       WHERE U.USER_ID_EOI = V_EOI_USER_ID
         AND U.ACTIVATION_STATUS = 'AC';
    
      IF V_USER_PRESENT = 0 THEN
      
        /* FRESH INSERTION IS TAKING PLACE.. CREATE A NEW OK3-8 USER */
        SELECT SEQ_USER_ID.NEXTVAL INTO V_USER_ID_SEQ FROM DUAL;
      
        SELECT DECODE(ADDRESS_ID, NULL, 0, ADDRESS_ID)
          INTO V_ADDRESS_ID
          FROM USERS
         WHERE USER_ID = V_EOI_USER_ID;
      
        -- This checks if address is present or not at all.
        IF V_ADDRESS_ID <> 0 THEN
        
          SELECT SEQ_ADDRESS_ID.NEXTVAL INTO V_ADDRESS_ID_SEQ FROM DUAL;
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
          (SELECT V_USER_ID_SEQ,
                  UR.ROLE_ID,
                  V_OK_38_ORG_NODE_ID,
                  UR.CREATED_BY,
                  UR.CREATED_DATE_TIME,
                  UR.UPDATED_BY,
                  UR.UPDATED_DATE_TIME,
                  UR.ACTIVATION_STATUS,
                  UR.DATA_IMPORT_HISTORY_ID
             FROM USER_ROLE UR
            WHERE UR.USER_ID = V_EOI_USER_ID
              AND UR.ROLE_ID = V_EOI_ROLE_ID
              AND UR.ORG_NODE_ID = V_EOI_ORG_NODE_ID
              AND UR.ACTIVATION_STATUS = 'AC');
      
        /**
        * Now create the mapping of OK EOI and OK 3-8 customer
        *
        */
      
        INSERT INTO EOI_3TO8_USER_MAPPING
        VALUES
          (V_USER_NAME_EOI,
           V_EOI_USER_ID,
           V_USER_NAME_38_NEW,
           V_USER_ID_SEQ,
           'AC',
           SYSDATE);
      
        UPDATE OK_EOI_38_USER_REPLICATE U
           SET U.REPLICATION_STATUS = 'SUCCESS',
               U.UPDATED_DATE_TIME  = SYSDATE
         WHERE U.OK_EOI_USER_ID = V_EOI_USER_ID
           AND U.OK_EOI_ROLE_ID = V_EOI_ROLE_ID
           AND U.OK_EOI_ORG_NODE_ID = V_EOI_ORG_NODE_ID
           AND U.OK_38_ORG_NODE_ID = V_OK_38_ORG_NODE_ID;
      
      ELSIF V_USER_PRESENT = 1 THEN
        /*MAPPED USER PRESENT,LINK THAT USER WITH THIS ORG-NODE.
        */
      
        SELECT M.USER_ID_3TO8
          INTO V_USER_ID_SEQ
          FROM EOI_3TO8_USER_MAPPING M
         WHERE M.USER_ID_EOI = V_EOI_USER_ID
           AND M.ACTIVATION_STATUS = 'AC';
      
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
          (SELECT V_USER_ID_SEQ,
                  UR.ROLE_ID,
                  V_OK_38_ORG_NODE_ID,
                  1,
                  SYSDATE,
                  NULL,
                  NULL,
                  'AC',
                  UR.DATA_IMPORT_HISTORY_ID
             FROM USER_ROLE UR
            WHERE UR.USER_ID = V_USER_ID_SEQ
              AND ROWNUM = 1);
      
        UPDATE OK_EOI_38_USER_REPLICATE U
           SET U.REPLICATION_STATUS = 'LINKED',
               U.UPDATED_DATE_TIME  = SYSDATE
         WHERE U.OK_EOI_USER_ID = V_EOI_USER_ID
           AND U.OK_EOI_ROLE_ID = V_EOI_ROLE_ID
           AND U.OK_EOI_ORG_NODE_ID = V_EOI_ORG_NODE_ID
           AND U.OK_38_ORG_NODE_ID = V_OK_38_ORG_NODE_ID;
      
      END IF;
      COMMIT;
    END IF;
  END LOOP;

EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.put_line('ERROR' || SQLCODE || '~' || SQLERRM);
END SP_OK_EOI_38_USER_REPLICATE;
/

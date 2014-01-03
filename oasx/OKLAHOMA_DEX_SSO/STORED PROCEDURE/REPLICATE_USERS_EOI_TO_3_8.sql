CREATE OR REPLACE PROCEDURE REPLICATE_USERS_EOI_TO_3_8(CustomerIDEOI  IN org_node.customer_id%TYPE,
                                                       CustomerID3to8 IN org_node.customer_id%TYPE) IS

  CUSTOMER_ID_EOI CONSTANT org_node.customer_id%TYPE := CustomerIDEOI; --Hard coded
  CUSTOMER_ID_3_8 CONSTANT org_node.customer_id%TYPE := CustomerID3to8; --Hard coded

  noOrgIn3to8   INTEGER := 0;
  noMappedUser  INTEGER := 0;
  userPresent38 INTEGER := 0;
  orgNodeId3to8 org_node.org_node_id%TYPE;
  userId3to8    users.user_id%TYPE;

  newAddressId address.address_id%TYPE;
  newUserId    users.user_id%TYPE;

  newUserName38    users.user_name%TYPE;
  newNewUserName38 users.user_name%TYPE;

  org_node_rec  org_node%ROWTYPE;
  user_role_rec user_role%ROWTYPE;
  users_rec     users%ROWTYPE;
  address_rec   address%ROWTYPE;

  isAssociationPresent INTEGER := 0;

  /* This cursor holds all the org_node_id in EOI customer */
  CURSOR org_node_cur IS
    SELECT ONO.*
      FROM ORG_NODE ONO
     WHERE ONO.CUSTOMER_ID = CUSTOMER_ID_EOI
       AND ONO.ACTIVATION_STATUS = 'AC'
       AND (ONO.ORG_NODE_CODE IS NOT NULL OR
           ONO.ORG_NODE_ID IN
           (SELECT ONO.ORG_NODE_ID
               FROM ORG_NODE ONO, ORG_NODE_CATEGORY ONC
              WHERE ONO.CUSTOMER_ID = CUSTOMER_ID_EOI
                AND ONO.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID
                AND ONC.CATEGORY_LEVEL =
                    (SELECT MIN(CATEGORY_LEVEL)
                       FROM ORG_NODE_CATEGORY
                      WHERE CUSTOMER_ID = CUSTOMER_ID_EOI)));

  /* This cursor holds all the user_role entries in EOI customer for the org_node_code present in 3-8 Customer  */
  CURSOR user_role_cur(orgNodeId1 IN org_node.org_node_id%TYPE, customerId1 IN org_node.customer_id%TYPE) IS
    SELECT ur.*
      FROM ORG_NODE ONO, user_role ur
     WHERE ONO.CUSTOMER_ID = customerId1
       AND ono.org_node_id = orgNodeId1
       AND ur.org_node_id = ono.org_node_id
       AND ur.activation_status = 'AC';

BEGIN
  IF NOT org_node_cur%ISOPEN THEN
    OPEN org_node_cur;
  END IF;
  LOOP
    FETCH org_node_cur
      INTO org_node_rec;
    EXIT WHEN org_node_cur%NOTFOUND;
  
    noOrgIn3to8 := 0;
    /* Checks whether the org_node_code is present for 3to8 Customer or not */
    IF org_node_rec.Org_Node_Code IS NOT NULL THEN
      SELECT COUNT(1)
        INTO noOrgIn3to8
        FROM ORG_NODE ONO
       WHERE ONO.ORG_NODE_CODE = org_node_rec.Org_Node_Code
         AND ONO.CUSTOMER_ID = CUSTOMER_ID_3_8;
    ELSE
      noOrgIn3to8 := 1;
    END IF;
  
    IF noOrgIn3to8 > 0 THEN
    
      IF org_node_rec.Org_Node_Code IS NOT NULL THEN
        SELECT ONO.ORG_NODE_ID
          INTO orgNodeId3to8
          FROM ORG_NODE ONO
         WHERE ONO.ORG_NODE_CODE = org_node_rec.Org_Node_Code
           AND ONO.CUSTOMER_ID = CUSTOMER_ID_3_8
           AND ROWNUM = 1;
      ELSE
        SELECT ONO.ORG_NODE_ID
          INTO orgNodeId3to8
          FROM ORG_NODE ONO, ORG_NODE_CATEGORY ONC
         WHERE ONO.CUSTOMER_ID = CUSTOMER_ID_3_8
           AND ONO.ORG_NODE_CATEGORY_ID = ONC.ORG_NODE_CATEGORY_ID
           AND ONC.CATEGORY_LEVEL =
               (SELECT MIN(CATEGORY_LEVEL)
                  FROM ORG_NODE_CATEGORY
                 WHERE CUSTOMER_ID = CUSTOMER_ID_3_8);
      END IF;
    
      IF NOT user_role_cur%ISOPEN THEN
        OPEN user_role_cur(org_node_rec.org_node_id, CUSTOMER_ID_EOI);
      END IF;
      LOOP
        FETCH user_role_cur
          INTO user_role_rec;
        EXIT WHEN user_role_cur%NOTFOUND;
      
        noMappedUser := 0;
        SELECT COUNT(1)
          INTO noMappedUser
          FROM eoi_3to8_user_mapping umap
         WHERE umap.user_id_eoi = user_role_rec.user_id
           AND umap.activation_status = 'AC';
      
        IF noMappedUser = 0 THEN
          /* Check if any mapped user is not present */
          /* Create new internal user */
          SELECT *
            INTO users_rec
            FROM users
           WHERE user_id = user_role_rec.user_id;
        
          newAddressId := NULL;
          IF users_rec.address_id IS NOT NULL THEN
            SELECT *
              INTO address_rec
              FROM address
             WHERE address_id = users_rec.address_id;
          
            SELECT SEQ_ADDRESS_ID.NEXTVAL INTO newAddressId FROM DUAL;
          END IF;
        
          SELECT SEQ_USER_ID.NEXTVAL INTO newUserId FROM DUAL;
        
          IF users_rec.address_id IS NOT NULL THEN
          
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
            VALUES
              (newAddressId,
               address_rec.STREET_LINE1,
               address_rec.STREET_LINE2,
               address_rec.STREET_LINE3,
               address_rec.CITY,
               address_rec.STATEPR,
               address_rec.COUNTRY,
               address_rec.ZIPCODE,
               address_rec.PRIMARY_PHONE,
               sysdate,
               address_rec.FAX,
               address_rec.CREATED_BY,
               address_rec.UPDATED_BY,
               sysdate,
               address_rec.DATA_IMPORT_HISTORY_ID,
               address_rec.SECONDARY_PHONE,
               address_rec.ZIPCODE_EXT,
               address_rec.PRIMARY_PHONE_EXT);
          END IF;
        
          newUserName38    := users_rec.USER_NAME || '_38';
          newNewUserName38 := newUserName38;
        
          SELECT COUNT(1)
            INTO userPresent38
            FROM USERS
           WHERE USER_NAME = newNewUserName38;
        
          /*THIS USERNAME ALREADY EXISTS */
          IF userPresent38 <> 0 THEN
          
            FOR I IN 1 .. 999 LOOP
              newNewUserName38 := newUserName38 || '_' || TO_CHAR(I);
            
              SELECT COUNT(1)
                INTO userPresent38
                FROM USERS
               WHERE USER_NAME = newNewUserName38;
            
              IF userPresent38 = 0 THEN
                EXIT;
              END IF;
            END LOOP;
          
          END IF;
        
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
          VALUES
            (newUserId,
             newNewUserName38,
             users_rec.PASSWORD,
             users_rec.FIRST_NAME,
             users_rec.MIDDLE_NAME,
             users_rec.LAST_NAME,
             users_rec.EMAIL,
             users_rec.PASSWORD_EXPIRATION_DATE,
             newAddressId,
             users_rec.ACTIVE_SESSION,
             users_rec.TIME_ZONE,
             users_rec.CREATED_DATE_TIME,
             users_rec.PASSWORD_HINT_ANSWER,
             users_rec.PASSWORD_HINT_QUESTION_ID,
             users_rec.RESET_PASSWORD,
             users_rec.UPDATED_DATE_TIME,
             users_rec.UPDATED_BY,
             users_rec.CREATED_BY,
             users_rec.LAST_LOGIN_DATE_TIME,
             users_rec.PREFIX,
             users_rec.SUFFIX,
             users_rec.PREFERRED_NAME,
             users_rec.EXT_SCHOOL_ID,
             users_rec.DATA_IMPORT_HISTORY_ID,
             users_rec.EXT_PIN1,
             users_rec.EXT_PIN2,
             users_rec.EXT_PIN3,
             users_rec.ACTIVATION_STATUS,
             newNewUserName38,
             users_rec.DISPLAY_NEW_MESSAGE);
        
          /* Create mapping between EOI and 3-8 users */
        
          INSERT INTO eoi_3to8_user_mapping
            (username_eoi,
             user_id_eoi,
             username_3to8,
             user_id_3to8,
             activation_status,
             last_updated)
          VALUES
            (users_rec.USER_NAME,
             users_rec.USER_ID,
             newNewUserName38,
             newUserId,
             'AC',
             sysdate);
        
          userId3to8 := newUserId;
        ELSE
          SELECT umap.user_id_3to8
            INTO userId3to8
            FROM eoi_3to8_user_mapping umap
           WHERE umap.user_id_eoi = user_role_rec.user_id
             AND umap.activation_status = 'AC';
        END IF;
        /* Associate the internal user (newly created or previously created) with this org in 3-8 */
        SELECT COUNT(1)
          INTO isAssociationPresent
          FROM USER_ROLE UR
         WHERE UR.USER_ID = userId3to8
           AND UR.ROLE_ID = user_role_rec.role_id
           AND UR.ORG_NODE_ID = orgNodeId3to8;
      
        IF isAssociationPresent = 0 THEN
          INSERT INTO User_Role
            (user_id,
             role_id,
             org_node_id,
             created_by,
             created_date_time,
             updated_by,
             updated_date_time,
             activation_status,
             data_import_history_id)
          VALUES
            (userId3to8,
             user_role_rec.role_id,
             orgNodeId3to8,
             user_role_rec.created_by,
             SYSDATE,
             user_role_rec.updated_by,
             SYSDATE,
             user_role_rec.activation_status,
             user_role_rec.data_import_history_id);
        END IF;
      
      END LOOP;
      IF user_role_cur%ISOPEN THEN
        CLOSE user_role_cur;
      END IF;
    END IF;
  
  END LOOP;

  IF org_node_cur%ISOPEN THEN
    CLOSE org_node_cur;
  END IF;

  COMMIT;
END REPLICATE_USERS_EOI_TO_3_8;
/

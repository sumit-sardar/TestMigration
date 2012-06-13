CREATE OR REPLACE FUNCTION FN_GETCRRESPONSE(TEST_ROSTER_ID IN NUMBER,
                                            ITEM_SET_ID    IN NUMBER,
                                            ITEM_ID        IN VARCHAR2)
  RETURN NUMBER IS

  CHARSET CLOB;

  G_TEST_ROSTER_ID NUMBER;
  G_ITEM_SET_ID    NUMBER;
  G_ITEM_ID        VARCHAR2(40);

  RESPONSEDATA   OAS.ITEM_RESPONSE_CR.CONSTRUCTED_RESPONSE%TYPE := ''; --varchar2(32767);
  ACTUALRESPONSE OAS.ITEM_RESPONSE_CR.CONSTRUCTED_RESPONSE%TYPE;
  P_DELIM        VARCHAR2(10);
  POS            NUMBER := 0;
  POS1           NUMBER := 0;
  RESPONSEDATA1  OAS.ITEM_RESPONSE_CR.CONSTRUCTED_RESPONSE%TYPE;
  RESPONSEDATA2  OAS.ITEM_RESPONSE_CR.CONSTRUCTED_RESPONSE%TYPE;
  RESPONSEDATA3  OAS.ITEM_RESPONSE_CR.CONSTRUCTED_RESPONSE%TYPE;
  RES_POS        NUMBER := 1;
  V_PARAGRAPH    VARCHAR2(3);
  V_PARAGRPAH1   VARCHAR2(4);
  V1             VARCHAR2(4000);
  V_INSTR        number := 1950;
  V_SP_POS       NUMBER := 0;
  RES_LENGTH     number := 0;
  ---Entry  for special character

  TAB          CHAR(1) := CHR(9);
  DOUBLE_QUOTE CHAR(1) := CHR(34);
  SINGLE_QUOTE CHAR(1) := CHR(39);
  MASK         VARCHAR2(80) := '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';

  V_SPECIAL_CHARS VARCHAR2(80) := '`~^\|' || DOUBLE_QUOTE || SINGLE_QUOTE || TAB;

  CURSOR CR_RESPONSE(IN_TEST_ROSTER_ID OAS.ITEM_RESPONSE.TEST_ROSTER_ID%TYPE, IN_ITEM_SET_ID OAS.ITEM_RESPONSE.ITEM_SET_ID%TYPE, IN_ITEM_ID OAS.ITEM.ITEM_ID%TYPE) IS
    SELECT CONSTRUCTED_RESPONSE, TEST_ROSTER_ID
      FROM OAS.ITEM_RESPONSE_CR
     WHERE TEST_ROSTER_ID = IN_TEST_ROSTER_ID
       AND ITEM_SET_ID = IN_ITEM_SET_ID
       AND ITEM_ID = IN_ITEM_ID;

  --cursor rowtype declaration
  CURSOR_CR_RESPONSE CR_RESPONSE%ROWTYPE;
BEGIN
  G_TEST_ROSTER_ID := TEST_ROSTER_ID;
  G_ITEM_SET_ID    := ITEM_SET_ID;
  G_ITEM_ID        := ITEM_ID;

  OPEN CR_RESPONSE(G_TEST_ROSTER_ID, G_ITEM_SET_ID, G_ITEM_ID);

  FETCH CR_RESPONSE
    INTO CURSOR_CR_RESPONSE;
  IF CR_RESPONSE%NOTFOUND THEN
    ACTUALRESPONSE := '![CDATA[]]';
    update test_resp set resp = ACTUALRESPONSE;
    commit;
    RETURN 1;
  END IF;

  CLOSE CR_RESPONSE;

  ACTUALRESPONSE := CURSOR_CR_RESPONSE.CONSTRUCTED_RESPONSE;

  select length(ACTUALRESPONSE) into RES_LENGTH from dual;

  if (RES_LENGTH > 2950) then
  
    While RES_LENGTH > 2950 loop
      --V1 := SUBSTR(CURSOR_CR_RESPONSE.CONSTRUCTED_RESPONSE, RES_POS, 1999);
    
      V_SP_POS     := INSTR(CURSOR_CR_RESPONSE.CONSTRUCTED_RESPONSE,
                            '%20',
                            RES_POS + 2950,
                            1);
                            
      /*dbms_output.put_line( RES_LENGTH || ' ---'||RES_POS || ' --' || V_SP_POS || '--' ||V_INSTR);                      
      V1 := SUBSTR(CURSOR_CR_RESPONSE.CONSTRUCTED_RESPONSE,RES_POS,V_SP_POS -RES_POS-1);*/
      
      /* update test_resp set resp = V1;
       commit;    */                 
      RESPONSEDATA := RESPONSEDATA || TRANSLATE(UTL_URL.UNESCAPE(SUBSTR(CURSOR_CR_RESPONSE.CONSTRUCTED_RESPONSE,
                                                                        RES_POS,
                                                                        V_SP_POS - RES_POS -1),
                                                                 
                                                                 CHARSET),
                                                MASK || V_SPECIAL_CHARS,
                                                MASK);
      RES_LENGTH := RES_LENGTH - (V_SP_POS -RES_POS -1 ) ;                                           
      RES_POS    := V_SP_POS;
      
      --V_INSTR    := V_SP_POS -2 + 1950;
    
    end loop;
    RESPONSEDATA := RESPONSEDATA || TRANSLATE(UTL_URL.UNESCAPE(SUBSTR(CURSOR_CR_RESPONSE.CONSTRUCTED_RESPONSE,
                                                                      RES_POS,
                                                                      RES_LENGTH),
                                                               
                                                               CHARSET),
                                              MASK || V_SPECIAL_CHARS,
                                              MASK);
  else
    RESPONSEDATA := UTL_URL.UNESCAPE(CURSOR_CR_RESPONSE.CONSTRUCTED_RESPONSE,
                                     CHARSET);
    RESPONSEDATA := TRANSLATE(RESPONSEDATA, MASK || V_SPECIAL_CHARS, MASK);
  end if;
  P_DELIM := '[CDATA[';

  V_PARAGRAPH    := '<p>';
  V_PARAGRPAH1   := '</p>';
  ACTUALRESPONSE := '';
  POS            := INSTR(RESPONSEDATA, V_PARAGRAPH, 1, 1);

  /* Searching for <p> and </p> tag and replace with </br> tag */
  IF (POS != 0) THEN
    RESPONSEDATA := REPLACE(RESPONSEDATA, V_PARAGRAPH, '<BR>');
    RESPONSEDATA := REPLACE(RESPONSEDATA, V_PARAGRPAH1, '<BR>');
  END IF;

  POS := INSTR(RESPONSEDATA, P_DELIM, 1, 1);

  IF (POS = 0) THEN
    ACTUALRESPONSE := '<![CDATA[]]>';
    update test_resp set resp = ACTUALRESPONSE;
    commit;
    RETURN 1;
  END IF;
  /* Loop is used here because one test roster id may have more than one item id's CR response */
  WHILE (POS != 0) LOOP
    POS  := POS + 7;
    POS1 := INSTR(RESPONSEDATA, ']', POS, 1);
  
    ACTUALRESPONSE := ACTUALRESPONSE ||
                      SUBSTR(RESPONSEDATA, POS, POS1 - POS);
  
    POS := 0;
    POS := INSTR(RESPONSEDATA, P_DELIM, POS1, 1); /*if no CDATA string, then come out of the loop */
  
    IF (POS != 0) THEN
      ACTUALRESPONSE := ACTUALRESPONSE || '<BR>' || '<BR>' || '<BR>';
      RESPONSEDATA   := SUBSTR(RESPONSEDATA, POS); /* replace the  response data with the next item id data*/
      POS            := INSTR(RESPONSEDATA, P_DELIM, 1, 1);
    
    END IF;
  
  END LOOP;
  update test_resp set resp = ACTUALRESPONSE;
  commit;
  RETURN 1;

/*exception

  when others then
    return 0;*/
  
END;
/

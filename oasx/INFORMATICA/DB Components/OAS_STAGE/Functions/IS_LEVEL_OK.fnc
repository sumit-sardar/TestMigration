create or replace function IS_LEVEL_OK(levle_1_code varchar2,levle_2_code varchar2, LEVEL_3_CODE varchar2 ) return number
  
  is
 
  v_LEVEL_1_CODE  VARCHAR2(30);
  v_LEVEL_2_CODE varchar2(30);
  v_LEVEL_3_CODE VARCHAR2(30);
  bOK number := 0;
  v_b_levle1 boolean := FALSE;
  v_b_levle2 boolean := FALSE;
  v_b_levle3 boolean := FALSE;
  
  
 FUNCTION BOOLEAN_TO_VARCHAR(FLAG IN BOOLEAN)
RETURN VARCHAR2 IS
BEGIN
RETURN
CASE FLAG
WHEN TRUE THEN 'TRUE'
WHEN FALSE THEN 'FALSE'
ELSE 'NULL'
END;
END;
  
  begin
     
     v_LEVEL_1_CODE := levle_1_code;
     v_LEVEL_2_CODE := levle_2_code;
     v_LEVEL_3_CODE := LEVEL_3_CODE;
 
     if  REGEXP_LIKE( v_LEVEL_1_CODE,'^[A-Z0-9[:blank:]]+$')
        

         then
              v_b_levle1:=true;
     else
              v_b_levle1:=false;

     end if;
     
     if REGEXP_LIKE(v_LEVEL_2_CODE,'^[A-Z0-9]+$')
      

        then
            v_b_levle2:=true;
        else
            v_b_levle2:=false;

     end if;
     
     if REGEXP_LIKE(v_LEVEL_3_CODE,'^[0-9]+$')
        and v_LEVEL_3_CODE >=0 and v_LEVEL_3_CODE <=9999

     then

         v_b_levle3 :=true;
     else
         v_b_levle3 :=false;

     end if; 
     
     dbms_output.put_line ('v_b_levle1' || BOOLEAN_TO_VARCHAR(v_b_levle1));
     dbms_output.put_line ('v_b_levle2' || BOOLEAN_TO_VARCHAR(v_b_levle2));
     dbms_output.put_line ('v_b_levle3' || BOOLEAN_TO_VARCHAR(v_b_levle3));
     

      if v_b_levle1 and v_b_levle2 and v_b_levle3

     then

         bOK := 1;
     else
         bOK := 0;
     END IF;

    

    

return bOK;
  
end IS_LEVEL_OK;
/

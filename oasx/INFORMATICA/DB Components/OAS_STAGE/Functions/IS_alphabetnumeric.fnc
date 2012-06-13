create or replace function IS_alphabetnumeric(LEVEL_1_CODE IN VARCHAR2) return boolean
 is
  Result boolean  := false;
  l_regular_expr   VARCHAR2(40)  := '^(nirmala_){1}[0-9]';
  l_string VARCHAR2(32676);
begin

  
    /*l_string := translate(LEVEL_1_CODE, '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
                             , '9999999999XXXXXXXXXXXXXXXXXXXXXXXXXX');
     dbms_output.put_line(l_string);                         
    FOR i IN 1.. length(LEVEL_CODE)
    
     LOOP
     dbms_output.put_line(substr(l_string, i, 1));
       IF substr(l_string, i, 1) = 'X'
       THEN
           Result := true;
       ELSE IF substr(l_string, i, 1) = '9' 
       THEN
          Result := true;
       ELSE
         Result := false;
       END IF;
     
      END IF;
      
   END LOOP;*/
   
   
   /*IF REGEXP_LIKE(LEVEL_1_CODE, '[:alpha:]','i')*/
   
   IF REGEXP_LIKE(LEVEL_1_CODE,l_regular_expr)
   THEN
   Result := true;
   
   end if;
  return Result;
end IS_alphabetnumeric;
/

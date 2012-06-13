CREATE OR REPLACE FUNCTION F_UDF(LEVEL_DATA_IN VARCHAR2) RETURN VARCHAR2 IS
  RESULT           VARCHAR2(278);
  POS              NUMBER;
  V_STUDEN_BARCODE VARCHAR2(1);
  V_MATH_CODE      VARCHAR2(4);
BEGIN
  -- catching the barcode value (barcode will be blank for onlne student)
  V_STUDEN_BARCODE := SUBSTR(LEVEL_DATA_IN,
                             (INSTR(LEVEL_DATA_IN, ',', 1, 7) + 1),
                             1);

  V_STUDEN_BARCODE := REPLACE(V_STUDEN_BARCODE, ',', '@');

  -- catching the UDF value
  POS := INSTR(LEVEL_DATA_IN, ',', 1, 12);

  IF V_STUDEN_BARCODE = '@' THEN
    -- online student
  
    IF SUBSTR(LEVEL_DATA_IN, (POS + 131), 1) = ' ' THEN
      -- Reading test code
    
      RESULT := SUBSTR(LEVEL_DATA_IN, (POS + 4), 146) || LPAD(' ', 10, ' ');
    
    ELSE
      -- Math test code
    
      RESULT := SUBSTR(LEVEL_DATA_IN, (POS + 4), 136);
    
    END IF;
  
  ELSE
    --precode student
    RESULT := SUBSTR(LEVEL_DATA_IN, (POS + 4), 184); --****this is added****--- 
  
  END IF;

  RETURN(RESULT);
END;
/

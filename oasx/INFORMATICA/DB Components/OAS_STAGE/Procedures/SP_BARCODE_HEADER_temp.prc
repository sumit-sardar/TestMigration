CREATE OR REPLACE PROCEDURE SP_BARCODE_HEADER_temp(in_CUSTOMER_ID IN NUMBER ,in_PRECODE_DATA IN VARCHAR2)
IS
   V_OCCURENCE_NO  INTEGER;
   V_DEMO_HEADER   VARCHAR2(4000);
   V_DATA   VARCHAR2(100);
   CUST   NUMBER;
   V_DEMODATA   VARCHAR2(4000);
   V_FIELD_WIDTH  NUMBER(38);
tec1 varchar2(101);
--tec2 varchar2(101);
--tec3 varchar2(101);
--tec4 varchar2(101);
--tec5 varchar2(101);
--tec6 varchar2(101);
--tec7 varchar2(101);
o_HDR_INFO varchar2(4000);
   BEGIN
   V_DEMO_HEADER:=in_PRECODE_DATA;
--dbms_output.put_line('V_DEMO_HEADER'||V_DEMO_HEADER);
    --   SELECT CUSTOMER_ID INTO CUST FROM STG_PARAM_VERIFY_TB WHERE SNO=1;
    FOR i IN 1..LENGTH(V_DEMO_HEADER) LOOP
        V_OCCURENCE_NO:=INSTR(V_DEMO_HEADER,',',1);
   IF V_OCCURENCE_NO>0 THEN
        V_DATA:=SUBSTR(V_DEMO_HEADER,1,(INSTR(V_DEMO_HEADER,',',1)-1));
--dbms_output.put_line('v_data'||v_data);
        IF UPPER(V_DATA) != 'INTERNAL PRECODE ID' THEN
--dbms_output.put_line('CUSTOMER_ID'||TO_CHAR(in_CUSTOMER_ID));
--dbms_output.put_line('V_DATA'||TO_CHAR(V_DATA));
       SELECT FIELD_WIDTH INTO V_FIELD_WIDTH FROM PRECODE_OAS_MAP_TB WHERE UPPER(PRECODE_FIELD_NAME)=UPPER(V_DATA) AND CUSTOMER_ID=in_CUSTOMER_ID;
       V_DEMODATA := V_DEMODATA||','||V_DATA||' ('||V_FIELD_WIDTH||')';
        END IF;
          V_DEMO_HEADER:=SUBSTR(V_DEMO_HEADER,V_OCCURENCE_NO+1);
          ELSE
                 V_OCCURENCE_NO:=0;
                      IF UPPER(V_DEMO_HEADER) != 'INTERNAL PRECODE ID' THEN
             SELECT FIELD_WIDTH INTO V_FIELD_WIDTH FROM PRECODE_OAS_MAP_TB WHERE UPPER(PRECODE_FIELD_NAME)=UPPER(V_DEMO_HEADER) AND CUSTOMER_ID=in_CUSTOMER_ID;
        V_DEMODATA := V_DEMODATA||','||V_DEMO_HEADER||' ('||V_FIELD_WIDTH||')';
               END IF;
          EXIT WHEN V_OCCURENCE_NO =0;
          END IF;
 END LOOP;
     COMMIT;
o_HDR_INFO:=SUBSTR(V_DEMODATA,(INSTR(V_DEMODATA,',',1)+1));
tec1:=substr(o_HDR_INFO,1,100);
--tec2:=substr(o_HDR_INFO,101,200);
--tec3:=substr(o_HDR_INFO,201,300);
--tec4:=substr(o_HDR_INFO,301,400);
--tec5:=substr(o_HDR_INFO,401,500);
--tec6:=substr(o_HDR_INFO,501,600);
--tec7:=substr(o_HDR_INFO,601,700);
dbms_output.put_line('1'||tec1);
--d-bms_output.put_line('2'||tec2);
--dbms_output.put_line('3'||tec3);
--dbms_output.put_line('4'||tec4);
--dbms_output.put_line('5'||tec5);
--dbms_output.put_line('6'||tec6);
--dbms_output.put_line('7'||tec7);
END SP_BARCODE_HEADER_temp;
/

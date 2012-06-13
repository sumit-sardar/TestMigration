CREATE OR REPLACE PROCEDURE NODE_PATH(IN_CONTRACT_NAME IN VARCHAR) IS
NODE_NAME VARCHAR2(30);
NODE   NUMBER(10);
NODE_PATH VARCHAR2(2000);
BMI_STR VARCHAR2(2000);
BMI_SNO NUMBER;
NODE_TYPE varchar2(100);
UNIQUE_COLUMN VARCHAR2(100);
TYPE NODE_ROOT IS TABLE OF MONARCH_INITIAL_LOAD.UNIQUE_COL2%TYPE INDEX BY BINARY_INTEGER;
NODE_ROOT2 NODE_ROOT;

--IN_NODE IN NUMBER,
CURSOR C1 IS SELECT HIERARCHY_PK,NODEKEY,PARENT_NODE_KEY,LEVEL_NO,NODE_LEVEL,UNIQUE_COL1,UNIQUE_COL2 FROM MONARCH_INITIAL_LOAD 
WHERE VALUE_TYPE='V' AND NODEKEY <>1 ORDER BY NODEKEY DESC;
CURSOR C2(HIER_PK NUMBER) IS SELECT * FROM MONARCH_ADD_CONTACT WHERE HIERARCHY_PK=HIER_PK;

BEGIN
--DBMS_OUTPUT.PUT_LINE(IN_NODE);
FOR C1_REC IN C1 LOOP
	NODE_PATH:='/ORGANIZATION';
	IF LENGTH(C1_REC.PARENT_NODE_KEY) >0 THEN
	   SELECT UNIQUE_COL2,PARENT_NODE_KEY INTO NODE_NAME,NODE FROM MONARCH_INITIAL_LOAD WHERE VALUE_TYPE='V' AND NODEKEY=C1_REC.PARENT_NODE_KEY;
	   FOR I IN 1..15 LOOP
	   	   IF LENGTH(NODE_NAME)>0 AND LENGTH(NODE)>0 THEN
		   	  NODE_ROOT2(I):=NODE_NAME;
			  SELECT UNIQUE_COL2,PARENT_NODE_KEY INTO NODE_NAME,NODE FROM MONARCH_INITIAL_LOAD WHERE VALUE_TYPE='V' AND NODEKEY=NODE;	  
		   ELSE
		   	  EXIT;
		   END IF;
	   END LOOP;
	END IF;
	FOR I IN REVERSE 1..NODE_ROOT2.COUNT LOOP
	NODE_PATH :=NODE_PATH||'/'||NODE_ROOT2(I);
	--DBMS_OUTPUT.PUT_LINE(NODE_PATH);
	END LOOP;
	Select NVL(max(sno)+1,1) INTO BMI_SNO from stg_bmi;
	BMI_STR:='I|ORGANIZATION|'||IN_CONTRACT_NAME||'|'||C1_REC.UNIQUE_COL2||'|'||NODE_PATH||'|||A|||';
	INSERT INTO STG_BMI VALUES(BMI_SNO,BMI_STR);
	SELECT M1.NODE_LEVEL INTO NODE_TYPE FROM MONARCH_INITIAL_LOAD M1,MONARCH_INITIAL_LOAD M2 WHERE 
	M1.LEVEL_NO=M2.LEVEL_NO AND M2.NODEKEY=8 AND M1.VALUE_TYPE='C';
	BMI_SNO:=BMI_SNO+1;
	BMI_STR:='S|OBJECT_ATTRIBUTE|ORGANIZATION|'||NODE_PATH||'/'||C1_REC.UNIQUE_COL2||'|EXT_orgType|'||lower(node_type)||'|||||'||IN_CONTRACT_NAME;
	INSERT INTO STG_BMI VALUES(BMI_SNO,BMI_STR);
	BMI_SNO:=BMI_SNO+1;
	BMI_STR:='S|OBJECT_ATTRIBUTE|ORGANIZATION|'||NODE_PATH||'/'||C1_REC.UNIQUE_COL2||'|EXT_orgLabel|'||Initcap(node_type)||'|||||'||IN_CONTRACT_NAME;
	INSERT INTO STG_BMI VALUES(BMI_SNO,BMI_STR);
	BMI_SNO:=BMI_SNO+1;
	BMI_STR:='S|OBJECT_ATTRIBUTE|ORGANIZATION|'||NODE_PATH||'/'||C1_REC.UNIQUE_COL2||'|EXT_site_nbr|'||C1_REC.UNIQUE_COL1||'|||||'||IN_CONTRACT_NAME;
	INSERT INTO STG_BMI VALUES(BMI_SNO,BMI_STR);
	BMI_SNO:=BMI_SNO+1;
	BMI_STR:='S|OBJECT_ATTRIBUTE|ORGANIZATION|'||NODE_PATH||'/'||C1_REC.UNIQUE_COL2||'|EXT_cmpPhaseFlags|'||'!W WRIGHT MS!!!!!!1!!!'||'|||||'||IN_CONTRACT_NAME;
	INSERT INTO STG_BMI VALUES(BMI_SNO,BMI_STR);
	FOR C2_REC IN C2(C1_REC.HIERARCHY_PK) LOOP
	IF C2_REC.BILLING='Y' THEN 
		 UNIQUE_COLUMN:=C2_REC.ADDRESS_PK||'_'||C2_REC.HIERARCHY_PK||'_Bill';
		 BMI_SNO:=BMI_SNO+1;
		 BMI_STR:= 'I|ADDRESS|'||UNIQUE_COLUMN||'|||'||IN_CONTRACT_NAME||'|'||'ENB'||c2_rec.first_name||'||'||C2_REC.LAST_NAME||'|'||C2_REC.ADDRESSLINE1||'|||'||C2_REC.CITY||'|'||C2_REC.STATE||'|'||C2_REC.ZIP||'|'||C2_REC.COUNTRY||'|'||C2_REC.TELEPHONENO||'|TRUE|FALSE||Billing Address|4|0|||';
		 INSERT INTO STG_BMI VALUES(BMI_SNO,BMI_STR);
		 BMI_SNO:=BMI_SNO+1;
		 BMI_STR:='I|ORG_ADDRESS|'||UNIQUE_COLUMN||'|'||IN_CONTRACT_NAME||'|'||NODE_PATH||'/'||C1_REC.UNIQUE_COL2;
		 INSERT INTO STG_BMI VALUES(BMI_SNO,BMI_STR);
    ELSIF C2_REC.MAT_SHIP1='Y' THEN 
		 UNIQUE_COLUMN:=C2_REC.ADDRESS_PK||'_'||C2_REC.HIERARCHY_PK||'_Ship1';
		 BMI_SNO:=BMI_SNO+1;
		 BMI_STR:= 'I|ADDRESS|'||UNIQUE_COLUMN||'|||'||IN_CONTRACT_NAME||'|'||'ENB'||c2_rec.first_name||'||'||C2_REC.LAST_NAME||'|'||C2_REC.ADDRESSLINE1||'|||'||C2_REC.CITY||'|'||C2_REC.STATE||'|'||C2_REC.ZIP||'|'||C2_REC.COUNTRY||'|'||C2_REC.TELEPHONENO||'|FALSE|TRUE||Shipping1 Address|4|0|||';
		 INSERT INTO STG_BMI VALUES(BMI_SNO,BMI_STR);
		 BMI_SNO:=BMI_SNO+1;
		 BMI_STR:='I|ORG_ADDRESS|'||UNIQUE_COLUMN||'|'||IN_CONTRACT_NAME||'|'||NODE_PATH||'/'||C1_REC.UNIQUE_COL2;
		 INSERT INTO STG_BMI VALUES(BMI_SNO,BMI_STR);
	ELSIF C2_REC.MAT_SHIP2='Y' THEN 
		 UNIQUE_COLUMN:=C2_REC.ADDRESS_PK||'_'||C2_REC.HIERARCHY_PK||'_Ship2';
		 BMI_SNO:=BMI_SNO+1;
		 BMI_STR:= 'I|ADDRESS|'||UNIQUE_COLUMN||'|||'||IN_CONTRACT_NAME||'|'||'ENB'||c2_rec.first_name||'||'||C2_REC.LAST_NAME||'|'||C2_REC.ADDRESSLINE1||'|||'||C2_REC.CITY||'|'||C2_REC.STATE||'|'||C2_REC.ZIP||'|'||C2_REC.COUNTRY||'|'||C2_REC.TELEPHONENO||'|FALSE|TRUE||Shipping2 Address|4|0|||';
		 INSERT INTO STG_BMI VALUES(BMI_SNO,BMI_STR);
		 BMI_SNO:=BMI_SNO+1;
		 BMI_STR:='I|ORG_ADDRESS|'||UNIQUE_COLUMN||'|'||IN_CONTRACT_NAME||'|'||NODE_PATH||'/'||C1_REC.UNIQUE_COL2;
		 INSERT INTO STG_BMI VALUES(BMI_SNO,BMI_STR);
	END IF;
	END LOOP;
	COMMIT;
	
--	DBMS_OUTPUT.PUT_LINE(BMI_STR);--C1_REC.UNIQUE_COL2||'   '||NODE_PATH);

END LOOP;
END NODE_PATH;
/

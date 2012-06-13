create or replace procedure SP_CHECK_LEVELCODE(LEVEL_CODE VARCHAR2,test OUT boolean )
 as 
 

begin

test := IS_alphabetnumeric(LEVEL_CODE);
 


end SP_CHECK_LEVELCODE;
/

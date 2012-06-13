create or replace function f_test_group_code(level_data_in varchar2) return varchar2 is
  Result varchar2(10);
  commodityCode varchar2(7);
  pos number;
  i number := 0;
  rest stg_eiss_tb.level_data%type;
begin

 rest := level_data_in ;
 WHILE i < 12 LOOP
  pos := instr(rest, ',');
  rest := substr(rest, pos + 1);
  i := i + 1;
 END LOOP;

 commodityCode := f_commodity_code(level_data_in);
 commodityCode:=substr(commodityCode,1,5);
 -- 20134= Math, 20133= Reading

 if( commodityCode = '20134' ) then
   Result := substr(rest, 145, 4);
 else
   Result := substr(rest, 130, 4);
 end if;

 return(Result);
end ;
/

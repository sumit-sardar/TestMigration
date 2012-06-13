create or replace function f_commodity_code(level_data_in varchar2) return varchar2 is
  Result varchar2(7);
  pos number;

begin
 pos := instr(level_data_in, ',', 1, 8 );
 Result := substr(level_data_in, pos + 1, 7);
 return(Result);
end ;
/

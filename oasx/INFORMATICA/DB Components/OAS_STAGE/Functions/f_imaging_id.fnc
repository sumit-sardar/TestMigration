create or replace function f_imaging_id(level_data_in varchar2) return varchar2 is
  Result varchar2(26);
  pos1 number;
  pos2 number;

begin
 pos1 := instr(level_data_in, ',', 1, 11);
 pos2 := instr(level_data_in, ',', 1, 12);
 Result := substr(level_data_in, pos1 + 1, pos2 - pos1 - 1);
 Result := rpad(Result, 26, ' ');
 return(Result);
end ;
/

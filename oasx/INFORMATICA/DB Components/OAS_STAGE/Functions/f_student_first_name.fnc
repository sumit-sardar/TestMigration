create or replace function f_student_first_name(level_data_in varchar2) return varchar2 is
  Result varchar2(12);
  pos1 number;
  pos2 number;
begin

 pos1 := instr(level_data_in, '",');
 pos2 := instr(level_data_in, '",', 1, 2);

 Result := substr(level_data_in, pos1 + 3, pos2 - pos1- 3);
 Result := rpad(Result, 12, ' ');
 return(Result);
end ;
/

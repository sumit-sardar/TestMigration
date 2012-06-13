create or replace function f_student_last_name(level_data_in varchar2) return varchar2 is
  Result varchar2(17);
  pos number;

begin
 pos := instr(level_data_in, '","');
 Result := substr( substr(level_data_in, 5, pos - 5), 0, 17);
 Result := rpad( Result, 17, ' ');
 return(Result);
end ;
/

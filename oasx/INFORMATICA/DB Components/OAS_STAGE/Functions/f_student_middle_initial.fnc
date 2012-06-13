create or replace function f_student_middle_initial(level_data_in varchar2) return varchar2 is
  Result varchar2(1);
  pos number;
begin
 pos := instr(level_data_in, '","', 1, 2);
 Result := substr(level_data_in, pos + 3, 1);
 if (Result = '"' ) Then
    Result := ' ';
 end if;
 return(Result);
end ;
/

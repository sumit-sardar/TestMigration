create or replace function f_student_birthdate(level_data_in varchar2) return varchar2 is
  Result varchar2(6);
  pos number;
begin

 pos := instr(level_data_in, '",', 1, 3);
 Result := substr(level_data_in, pos + 2, 6);

 If substr(Result, 1, 1) = ',' Then
    Result := '      ';
 end if;

 Result := substr(Result, 3) || substr(Result, 1, 2);

 return(Result);
end ;
/

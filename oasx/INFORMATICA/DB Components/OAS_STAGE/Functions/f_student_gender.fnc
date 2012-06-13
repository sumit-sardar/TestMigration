create or replace function f_student_gender(level_data_in varchar2) return varchar2 is
  Result varchar2(1);
  pos number;
  rest varchar2(2000);
begin

 rest := level_data_in ;

 pos := instr(rest, '",', 1, 3);
 rest := substr(rest, pos + 2);

 pos := instr(rest, ',');

 Result := substr(rest, pos + 1, 1);

 if Result = ',' Then
    Result := ' ' ;
 end if;

 return(Result);
end ;
/

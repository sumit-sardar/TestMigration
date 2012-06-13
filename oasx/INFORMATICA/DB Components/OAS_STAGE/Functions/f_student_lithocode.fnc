create or replace function f_student_lithocode(level_data_in varchar2) return varchar2 is
  Result varchar2(8);
  pos number;
  i number := 0;
  rest varchar2(2000);
begin
 rest := level_data_in ;
 WHILE i < 6 LOOP
  pos := instr(rest, ',');
  rest := substr(rest, pos + 1);
  i := i + 1;
 END LOOP;

  Result := substr(rest, pos, 8);
  return(Result);
end ;
/

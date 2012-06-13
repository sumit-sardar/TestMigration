create or replace function f_district_number(level_data_in varchar2) return varchar2 is
  Result varchar2(2);
  pos number;
  i number := 0;
  rest varchar2(2000);
begin
 rest := level_data_in ;
 WHILE i < 12 LOOP
  pos := instr(rest, ',');
  rest := substr(rest, pos + 1);
  i := i + 1;
 END LOOP;

  Result := substr(rest, pos + 34, 2);
  return(Result);
end f_district_number;
/

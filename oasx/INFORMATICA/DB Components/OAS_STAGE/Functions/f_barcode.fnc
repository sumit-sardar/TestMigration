create or replace function f_barcode(level_data_in varchar2) return varchar2 is
  Result varchar2(8);
  pos1 number;
  pos2 number;

begin
 pos1 := instr(level_data_in, ',', 1, 7);
 pos2 := instr(level_data_in, ',', 1, 8);
 Result := substr(level_data_in, pos1 + 1, pos2 - pos1 - 1);
 Result := lpad(Result, 8, '0');
 return( nvl( Result,  '        ' ) );
end ;
/

create or replace function f_gh_grade(level_data_in varchar2) return varchar2 is
  Result varchar2(2);
  pos number;

begin
 pos := length(level_data_in);
 Result := substr(level_data_in, pos - 1,2);

 Result := replace(Result, ',' , '');

 if( length(Result) = 1 ) then
   if( Result = 'K' ) then
     Result := 'K ';
   else
     Result := lpad(Result, 1, '0');
   end if;
 end if;

 if( Result not in ('10', '11', '12', '13', 'AD') ) then
   Result := '11';
 end if;

 return(Result);
end ;
/

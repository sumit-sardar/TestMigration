create or replace procedure Update_CPM_Enrollment_Side
is
v_value varchar2(1000);
v_startpos integer;
v_endpos integer;
v_equalpos integer;
v_attribute varchar2(50);
v_length integer;
v_counter integer :=0;
v_val varchar2(100);
map_poc_typ_rec stg_map_poc_tb%rowtype;
v_monarchid varchar2(100) :='3456';
var_sql varchar2(400);
v_Custid varchar2(50) :='FLORIDA';
--mappingRecord mappingRecord%rowtype;
cursor Data_Crs is
select MONARCH_ID, CUSTOMER_NAME, TEMP from stg_temp_data;
begin
for Data_Crs_record in Data_Crs loop
v_length:=length(Data_Crs_record.TEMP);
v_counter :=0;
if v_length > 0 then
  v_counter:=1;
loop
 if v_counter=1 then
  v_startpos:=1;
 else
  v_startpos:=v_endpos+2;
 end if;
 v_endpos:=instr(Data_Crs_record.TEMP,',',1,v_counter);
 if v_endpos=0 then
   v_endpos:=v_length;
 else
  v_endpos:=v_endpos-1;
 end if;
 v_equalpos:=instr(Data_Crs_record.TEMP,'=',1,v_counter);
 v_attribute:=substr(Data_Crs_record.TEMP,v_startpos, v_equalpos-v_startpos);
 v_val:=substr(Data_Crs_record.TEMP, v_equalpos+1, v_endpos-v_equalpos);
---- to update the value in table
var_sql := 'select * from  stg_map_poc_tb where customer_name=''' || Data_Crs_record.CUSTOMER_NAME ||
 ''' and XML_attribute_name=''' || v_attribute ||'''';
execute immediate (var_sql) into map_poc_typ_rec;
var_sql:='';
 var_sql:='update '|| map_poc_typ_rec.target_table_name ||' set '|| map_poc_typ_rec.target_column_name ||
 '='|| v_val|| ' where site_nbr=' || Data_Crs_record.MONARCH_ID;
 execute immediate (var_sql);
 exit when v_endpos=v_length;
  v_counter:=v_counter+1;
 end loop;
 end if;
 end loop;
commit;
end;
/

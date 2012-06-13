create or replace procedure sp_clob is
X clob:='TEMP-VAR';
begin

 IF replace(x,'-','*')='TEMP*VAR' then
  insert into data_import_history (created_date_time,created_by,activation_status,customer_id,import_filename,data_import_history_id,load_code)
 select sysdate,100,'AC',3015,'TEST',seq_data_import_history_id.nextval,120 from dual;
 commit;
end if;
   
 

end sp_clob;
/

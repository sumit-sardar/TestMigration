create or replace procedure date_dim_pre_load_load is
  -- var_1 Number :=0;
  start_date      date;
  end_date        date;
  processing_date date;
  temp_date       date;
begin
  start_date      := to_date('01/01/1970', 'mm/dd/yyyy');
  end_date        := to_date('12/31/2036', 'mm/dd/yyyy');
  processing_date := start_date;

  insert into date_dim_pre_load values (processing_date);

  while processing_date < end_date loop
    select processing_date + 1 into temp_date from dual;
    --var_1 := var_1 +1;
  
    insert into date_dim_pre_load values (temp_date);
  
    processing_date := temp_date;
  end loop;

  commit;
  --dbms_output.put_line( 'Selected Number of days =' || var_1);
end date_dim_pre_load_load;
/

create or replace procedure SP_TEST(my_job_id in out varchar2) is
begin
  my_job_id := '100';
  insert into t2 values (my_job_id, 200);

end SP_TEST;
/

create or replace procedure delete_child_site_id(in_parent_site_id in number,
                                                 out_flag          out varchar2) as
  cnt    integer := 0;
  v_flag varchar2(3);
begin
  select count(*)
    into cnt
    from cpm_enrollment_site
   where parent_site_sys_id = in_parent_site_id;

  if (cnt != 0) then
    delete from cpm_enrollment_site_TEMP@COMPASS
     where parent_site_sys_id = in_parent_site_id;
    commit;
    v_flag := 'Y';
  else
    v_flag := 'Y';
  end if;
  out_flag := v_flag;

end;
/

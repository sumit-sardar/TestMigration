create or replace procedure wipeout_scoring_after_reset(student_list    in clob,
                                                        sessionid       in session_dim.sessionid%type,
                                                        contentareaid   in content_area_dim.content_areaid%type,
                                                        contentareaname in content_area_dim.name%type) is

  cursor getprimaryobjectives(in_contentara_id content_area_dim.content_areaid%type) is
    select prim_objid
      from prim_obj_dim
     where content_areaid = in_contentara_id;

  cursor getsecobjectives(in_primobj_id prim_obj_dim.prim_objid%type) is
    select sec_objid from sec_obj_dim where prim_objid = in_primobj_id;

  cursor getitemcount(in_sec_objid sec_obj_dim.sec_objid%type) is
    select itemid from item_dim where sec_objid = in_sec_objid;

  cursor getconetentareaid(contentareaname varchar2, v_product_id varchar2, v_sessdionid number, v_studentid varchar2) is
    select distinct cad.content_areaid
      from content_area_dim cad, laslink_content_area_fact lcaf
     where lcaf.content_areaid = cad.content_areaid
       and cad.name = contentareaname
       and cad.content_areaid like v_product_id
       and lcaf.studentid = v_studentid
       and lcaf.sessionid = v_sessdionid;

  cursor getstudentids is
    select distinct trim(substr(txt,
                                instr(txt, ',', 1, level) + 1,
                                instr(txt, ',', 1, level + 1) -
                                instr(txt, ',', 1, level) - 1)) as student_id
      from (select ',' || v_student_id || ',' as txt
              from wipe_out_score_temp)
    connect by level <= length(txt) - length(replace(txt, ',', '')) - 1;

  v_length        integer := 0;
  v_length2       integer := 0;
  v_count         integer := 2000;
  res_pos         number := 1;
  v_sp_pos        number := 0;
  v_temp_stu_list varchar2(32767) := '';
  type v_content_arr_tab is table of varchar2(128);
  v_content_arr   v_content_arr_tab := v_content_arr_tab();
  v_product_id    varchar2(32) := 0;
  v_contentareaid content_area_dim.content_areaid%type := null;
  --v_sql_str       varchar2(4000) := '';

begin

  execute immediate 'truncate TABLE wipe_out_score_temp';
  select length(student_list) into v_length from dual;

  /*dbms_output.put_line(' V_LENGTH-' || v_length || 'student_list' ||
  student_list || 'sessionid' || sessionid ||
  'contentareaid' || contentareaid || 'TEMP' ||
  to_char(student_list) ||'contentareaname'||contentareaname);*/
  -----------------
  v_length2 := v_length;

  if contentareaname is not null then
  
    if contentareaname = 'Listening' then
      v_content_arr.extend;
      v_content_arr(1) := 'Listening';
      v_content_arr.extend;
      v_content_arr(2) := 'Comprehension';
      v_content_arr.extend;
      v_content_arr(3) := 'Oral';
    elsif contentareaname = 'Speaking' then
      v_content_arr.extend;
      v_content_arr(1) := 'Speaking';
      v_content_arr.extend;
      v_content_arr(2) := 'Productive';
      v_content_arr.extend;
      v_content_arr(3) := 'Oral';
    elsif contentareaname = 'Reading' then
      v_content_arr.extend;
      v_content_arr(1) := 'Reading';
      v_content_arr.extend;
      v_content_arr(2) := 'Comprehension';
      v_content_arr.extend;
      v_content_arr(3) := 'Literacy';
    elsif contentareaname = 'Writing' then
      v_content_arr.extend;
      v_content_arr(1) := 'Writing';
      v_content_arr.extend;
      v_content_arr(2) := 'Productive';
      v_content_arr.extend;
      v_content_arr(3) := 'Literacy';
    end if;
  
    if contentareaid is not null then
      v_product_id := substr(to_char(contentareaid), 1, 4) || '%';
    
      if v_length > v_count then
      
        while v_length > v_count loop
          v_sp_pos        := instr(student_list, ',', res_pos + v_count, 1);
          v_temp_stu_list := substr(student_list,
                                    res_pos,
                                    (v_sp_pos - res_pos));
        
          res_pos  := v_sp_pos + 1;
          v_length := v_length2 - res_pos;
        
          insert into wipe_out_score_temp values (v_temp_stu_list);
          commit;
          for cur in getstudentids loop
          
            for i in 1 .. v_content_arr.count loop
              open getconetentareaid(v_content_arr(i),
                                     v_product_id,
                                     sessionid,
                                     to_number(cur.student_id));
            
              fetch getconetentareaid
                into v_contentareaid;
            
              if getconetentareaid%found then
                fetch getconetentareaid
                  into v_contentareaid;
              
                /* execute immediate 'select distinct cad.content_areaid
                 from content_area_dim cad, laslink_content_area_fact lcaf
                where lcaf.content_areaid = cad.content_areaid
                  and cad.name = ' || '''' ||
                                         v_content_arr(i) || '''' || '
                  and cad.content_areaid like ' || '''' ||
                                         v_product_id || ' % ' || '''' ||
                                         ' and lcaf.studentid in (' || v_temp_stu_list ||
                                         ') and  lcaf.sessionid = ' || sessionid
                       
                         into v_contentareaid;*/
              
                for col1 in getprimaryobjectives(v_contentareaid) loop
                  if col1.prim_objid is not null then
                    for col2 in getsecobjectives(col1.prim_objid) loop
                      if col2.sec_objid is not null then
                        for col3 in getitemcount(col2.sec_objid) loop
                          if col3.itemid is not null then
                            /*DELTE FROM ITEM FACT */
                            delete from laslink_item_fact lif
                             where lif.itemid = col3.itemid
                               and lif.sessionid = sessionid
                               and lif.studentid = cur.student_id;
                          end if;
                        end loop;
                        /*DELETE FROM SEC OBJECTIVE FACT */
                        delete from laslink_sec_obj_fact lsof
                         where lsof.sec_objid = col2.sec_objid
                           and lsof.sessionid = sessionid
                           and lsof.studentid = cur.student_id;
                      
                        /*DELETE OVERALL ALSO FROM SEC OBJECTIVE FACT */
                        delete from laslink_sec_obj_fact lsof
                         where lsof.sessionid = sessionid
                           and lsof.studentid = cur.student_id
                           and lsof.sec_objid in
                               (select sec_objid
                                  from sec_obj_dim
                                 where name = 'Overall Academic');
                      end if;
                    end loop;
                    /*DELETE FROM PRIM OBJECTIVE FACT */
                    delete from laslink_prim_obj_fact lpof
                     where lpof.prim_objid = col1.prim_objid
                       and lpof.sessionid = sessionid
                       and lpof.studentid = cur.student_id;
                  end if;
                end loop;
                /*DELETE FROM CONTENT AREA OBJECTIVE FACT */
                delete from laslink_content_area_fact lcaf
                 where lcaf.content_areaid = v_contentareaid
                   and lcaf.sessionid = sessionid
                   and lcaf.studentid = cur.student_id;
              end if;
              close getconetentareaid;
            end loop;
            /*DELETE FROM COMPOSITE FACT */
            delete from laslink_composite_fact lcf
             where lcf.sessionid = sessionid
               and lcf.studentid = cur.student_id;
          end loop;
        
          execute immediate 'truncate TABLE wipe_out_score_temp';
        end loop;
      
        v_temp_stu_list := substr(student_list, res_pos);
      
        if v_temp_stu_list is not null and length(v_temp_stu_list) > 0 then
        
          insert into wipe_out_score_temp values (v_temp_stu_list);
          commit;
          for cur in getstudentids loop
          
            for i in 1 .. v_content_arr.count loop
              open getconetentareaid(v_content_arr(i),
                                     v_product_id,
                                     sessionid,
                                     to_number(cur.student_id));
              fetch getconetentareaid
                into v_contentareaid;
              if getconetentareaid%found then
                fetch getconetentareaid
                  into v_contentareaid;
              
                /*   execute immediate 'select distinct cad.content_areaid
                 from content_area_dim cad, laslink_content_area_fact lcaf
                where lcaf.content_areaid = cad.content_areaid
                  and cad.name = ' || '''' ||
                                         v_content_arr(i) || '''' || '
                  and cad.content_areaid like ' || '''' ||
                                         v_product_id || ' % ' || '''' ||
                                         ' and lcaf.studentid in (' || v_temp_stu_list ||
                                         ') and  lcaf.sessionid = ' || sessionid
                       
                         into v_contentareaid;*/
                for col1 in getprimaryobjectives(v_contentareaid) loop
                  if col1.prim_objid is not null then
                    for col2 in getsecobjectives(col1.prim_objid) loop
                      if col2.sec_objid is not null then
                        for col3 in getitemcount(col2.sec_objid) loop
                          if col3.itemid is not null then
                            /*DELTE FROM ITEM FACT */
                            delete from laslink_item_fact lif
                             where lif.itemid = col3.itemid
                               and lif.sessionid = sessionid
                               and lif.studentid = cur.student_id;
                          end if;
                        end loop;
                        /*DELETE FROM SEC OBJECTIVE FACT */
                        delete from laslink_sec_obj_fact lsof
                         where lsof.sec_objid = col2.sec_objid
                           and lsof.sessionid = sessionid
                           and lsof.studentid = cur.student_id;
                      
                        /*DELETE OVERALL ALSO FROM SEC OBJECTIVE FACT */
                        delete from laslink_sec_obj_fact lsof
                         where lsof.sessionid = sessionid
                           and lsof.studentid = cur.student_id
                           and lsof.sec_objid in
                               (select sec_objid
                                  from sec_obj_dim
                                 where name = 'Overall Academic');
                      end if;
                    end loop;
                    /*DELETE FROM PRIM OBJECTIVE FACT */
                    delete from laslink_prim_obj_fact lpof
                     where lpof.prim_objid = col1.prim_objid
                       and lpof.sessionid = sessionid
                       and lpof.studentid = cur.student_id;
                  end if;
                end loop;
                /*DELETE FROM CONTENT AREA OBJECTIVE FACT */
                delete from laslink_content_area_fact lcaf
                 where lcaf.content_areaid = v_contentareaid
                   and lcaf.sessionid = sessionid
                   and lcaf.studentid = cur.student_id;
              end if;
              close getconetentareaid;
            end loop;
            /*DELETE FROM COMPOSITE FACT */
            delete from laslink_composite_fact lcf
             where lcf.sessionid = sessionid
               and lcf.studentid = cur.student_id;
          
          end loop;
          execute immediate 'truncate TABLE wipe_out_score_temp';
        end if;
      
      else
        execute immediate 'truncate TABLE wipe_out_score_temp';
        v_temp_stu_list := substr(student_list, 1);
      
        insert into wipe_out_score_temp values (v_temp_stu_list);
        commit;
      
        for cur in getstudentids loop
        
          for i in 1 .. v_content_arr.count loop
            /* dbms_output.put_line(' TO_NUMBER(cur.student_id)' ||
            to_number(cur.student_id) ||
            'v_content_arr(i)' || v_content_arr(i) ||
            'v_product_id' || v_product_id ||
            'sessionid' || sessionid);*/
          
            open getconetentareaid(v_content_arr(i),
                                   v_product_id,
                                   sessionid,
                                   cur.student_id);
            fetch getconetentareaid
              into v_contentareaid;
          
            if getconetentareaid%found then
            
              fetch getconetentareaid
                into v_contentareaid;
            
              /*v_sql_str := 'select distinct cad.content_areaid from content_area_dim cad, laslink_content_area_fact lcaf where lcaf.content_areaid = cad.content_areaid and cad.name = ' || '''' ||
                           v_content_arr(i) || '''' ||
                           ' and cad.content_areaid like ' || '''' ||
                           v_product_id || '%' || '''' ||
                           ' and lcaf.studentid in (' || v_temp_stu_list ||
                           ') and  lcaf.sessionid = ' || sessionid;
              dbms_output.put_line('sql >> ' || v_sql_str);
              
              execute immediate v_sql_str
                into v_contentareaid;*/
            
              for col1 in getprimaryobjectives(v_contentareaid) loop
              
                if col1.prim_objid is not null then
                
                  for col2 in getsecobjectives(col1.prim_objid) loop
                  
                    if col2.sec_objid is not null then
                    
                      for col3 in getitemcount(col2.sec_objid) loop
                      
                        if col3.itemid is not null then
                        
                          /*DELTE FROM ITEM FACT */
                          delete from laslink_item_fact lif
                           where lif.itemid = col3.itemid
                             and lif.sessionid = sessionid
                             and lif.studentid = cur.student_id;
                        
                        end if;
                      end loop;
                      /*DELETE FROM SEC OBJECTIVE FACT */
                      delete from laslink_sec_obj_fact lsof
                       where lsof.sec_objid = col2.sec_objid
                         and lsof.sessionid = sessionid
                         and lsof.studentid = cur.student_id;
                    
                      /*DELETE OVERALL ALSO FROM SEC OBJECTIVE FACT */
                      delete from laslink_sec_obj_fact lsof
                       where lsof.sessionid = sessionid
                         and lsof.studentid = cur.student_id
                         and lsof.sec_objid in
                             (select sec_objid
                                from sec_obj_dim
                               where name = 'Overall Academic');
                    
                    end if;
                  end loop;
                  /*DELETE FROM PRIM OBJECTIVE FACT */
                  delete from laslink_prim_obj_fact lpof
                   where lpof.prim_objid = col1.prim_objid
                     and lpof.sessionid = sessionid
                     and lpof.studentid = cur.student_id;
                
                end if;
              end loop;
              /*DELETE FROM CONTENT AREA FACT */
              delete from laslink_content_area_fact lcaf
               where lcaf.content_areaid = v_contentareaid
                 and lcaf.sessionid = sessionid
                 and lcaf.studentid = cur.student_id;
            
            end if;
            close getconetentareaid;
          end loop;
        
          /*DELETE FROM COMPOSITE FACT */
          delete from laslink_composite_fact lcf
           where lcf.sessionid = sessionid
             and lcf.studentid = cur.student_id;
        end loop;
        execute immediate 'truncate TABLE wipe_out_score_temp';
      end if;
    
      -----------------
      --end loop;
    end if;
  end if;
  commit;
exception
  when others then
    rollback;
    execute immediate 'truncate TABLE wipe_out_score_temp';
    dbms_output.put_line(dbms_utility.format_error_backtrace);
end wipeout_scoring_after_reset;
/
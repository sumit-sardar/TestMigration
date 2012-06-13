create or replace procedure sp_opunit_assignment_monarch is

-- Declare the variables
   v_dist_id   stg_district_opunit.district_org_id%type; 
   v_cust_id stg_param_winscr_tb.customer_id%type;
   v_test_roster_id  stg_test_roster.test_roster_id%type;
   v_teacher_id     stg_district_opunit.district_org_id%type; 
   v_roster_count             stg_district_opunit.roster_count%type;
   v_opunit                   stg_district_opunit.opunit%type;
   flag boolean;
   
   
-- Declare the cursors
--cursor for  stg_param_winscr_tb table
    cursor cur_cust_id 
    is 
       SELECT customer_id 
       FROM stg_param_winscr_tb
       WHERE sno = 1;

-- cursor to get stg_test_roster data
   /* CHANGE FOR MONARCH COMPLETENESS  TO GET TEACHER_ID */

    cursor cur_stg_test_roster 
    is
      SELECT str.test_roster_id as test_roster_id, str.student_id, str.opunit, tr.org_node_id as class_id
      from stg_test_roster str , test_roster tr 
     where str.test_roster_id = tr.test_roster_id
       and str.opunit is null
       order by tr.org_node_id;
      
-- cursor to have start opunit
   cursor cur_exp_opunit
   is
   select value 
   from stg_export_parms 
   where upper(parameter)='START_OPUNIT' and customer_id = v_cust_id ; 

--cursor rowtype declaration
cursor_opunit_rec cur_exp_opunit%rowtype;

    cursor cur_exp_last_opunit
    is
    select value 
    from stg_export_parms 
    where upper(parameter)='LAST_OPUNIT' and customer_id = v_cust_id ; 

--cursor rowtype declaration
cursor_last_opunit_rec cur_exp_last_opunit%rowtype;


--cursor for fetching the ditrict_id   
    cursor cur_district_org_id(p_test_roster_id stg_test_roster.test_roster_id%type)
    is
    select  ong.org_node_id 
            from 
                  TEST_ROSTER tr,
    
                  org_node ons,
                  org_node ong,
                  org_node_ancestor ona ,
                  org_node_category onc
    
                  where 
                  tr.test_roster_id = p_test_roster_id 
                  and
                  ons.org_node_id = tr.org_node_id
                  and
                  ona.org_node_id =  tr.org_node_id
                  and
                  ong.org_node_id = ona.ancestor_org_node_id 
                  and
                  onc.org_node_category_id = ong.org_node_category_id
                  and
                  onc.category_level = ( select value from stg_export_parms where upper(parameter)='CATEGORY_LEVEL' and customer_id = v_cust_id);
             
              
--cursor rowtype declaration

 /* CHANGE FOR MONARCH COMPLETENESS  TO GET TEACHER_ID */
cursor_dist_orgid_rec cur_district_org_id%rowtype;

    cursor cur_district_opunit(p_district_id stg_district_opunit.district_org_id%type) 
    is 
    select STG_DISTRICT_OPUNIT_ID,district_org_id,roster_count,opunit  
      from stg_district_opunit 
      where district_org_id = p_district_id
            AND
            opunit = (SELECT MAX(opunit) FROM stg_district_opunit WHERE district_org_id = p_district_id)
            AND rownum=1  -- This has added in the condition to get latest record inserted into the table for the paricular district id which is the ascending order of roster_count
                          -- roster_count = (SELECT MIN(ROSTER_COUNT) FROM stg_district_opunit WHERE district_org_id = p_district_id)
            order by roster_count; 
      
  --cursor rowtype declaration
cr_dop_rec cur_district_opunit%rowtype;            
              
begin    
        --Get the customer_id
         open cur_cust_id; 
              fetch cur_cust_id into v_cust_id;
         
         
         if cur_cust_id%Notfound then
            raise_application_error (-20001,'The specified customer_id does not exist');
         end if;
         
         close cur_cust_id; 
       
        -- Get the start opunit
          open cur_exp_opunit;
            fetch cur_exp_opunit into cursor_opunit_rec; 
             
             
         if cur_exp_opunit%notfound then
            raise_application_error (-20001,'start_opunit value not set' || v_cust_id);
         end if;
         close cur_exp_opunit;
       
        -- Get the start opunit
        open cur_exp_last_opunit;
            fetch cur_exp_last_opunit into cursor_last_opunit_rec; 
             
             
        if cur_exp_last_opunit%notfound then
            raise_application_error (-20001,'last_opunit value not set' || v_cust_id);
        end if;
        close cur_exp_last_opunit;
        
        
        --loop through the stg_test_roster records
        
        for cur_stg_testroster1 in cur_stg_test_roster
        
        loop
            
      
            v_test_roster_id := cur_stg_testroster1.test_roster_id;
            /* CHANGE FOR MONARCH COMPLETENESS  TO GET TEACHER_ID */
            -- First time assign the value of class_id into v_teacher_id variable
            if(v_teacher_id is null) then 
                  v_teacher_id := cur_stg_testroster1.class_id;
            else
            
            --In 2nd Iteration of the loop compare with the v_teacher_id variable.If comparison results true,then flag value is true
            --If compariosn results false, then flag value is false and assign the class_id value in variable v_teacher_id
                if ( v_teacher_id = cur_stg_testroster1.class_id ) then
                        flag :=true;
                else 
                     flag := false;
                     v_teacher_id := cur_stg_testroster1.class_id;
                end if;
            end if;
            
            --fetch district id 
            
             open cur_district_org_id(v_test_roster_id);
                   fetch cur_district_org_id into cursor_dist_orgid_rec; 
                  
             if cur_district_org_id%notfound then
                 raise_application_error (-20001,'No organistaion_id exists for the test_roster_id ' || v_test_roster_id);
             end if;
                    
             close cur_district_org_id;
             
             --Fetch roster count from stg_district_opunit
             open cur_district_opunit(cursor_dist_orgid_rec.org_node_id);
                   fetch  cur_district_opunit into  cr_dop_rec;
            
            
            if cur_district_opunit%notfound then
      --this part is executed if dist_id is not present in stg_dist_opunit table
                  v_roster_count:=1;
             
             
                  insert into stg_district_opunit 
                        (STG_DISTRICT_OPUNIT_ID,
                        CUSTOMER_ID ,         
                        DISTRICT_ORG_ID,       
                        OPUNIT,                
                        ROSTER_COUNT)
        
                        values(
                        seq_STG_DISTRICT_OPUNIT_ID.nextval,
                        v_cust_id, 
                        cursor_dist_orgid_rec.org_node_id,
                        cursor_opunit_rec.value,v_roster_count);
             
                 update stg_test_roster set opunit= cursor_opunit_rec.value
                 where 
                 test_roster_id = cur_stg_testroster1.test_roster_id;
                 
                 update test_roster set opunit = cursor_opunit_rec.value 
                 where 
                 test_roster_id = cur_stg_testroster1.test_roster_id;
             
             
           else 
      
                if( cr_dop_rec.roster_count < 3000) then
                   --this part is executed if roster_count < 2999                  
                    
                                     v_roster_count:= cr_dop_rec.roster_count + 1 ;
                                     
                                     update stg_district_opunit set roster_count= v_roster_count
                                     where 
                                     STG_DISTRICT_OPUNIT_ID=cr_dop_rec.STG_DISTRICT_OPUNIT_ID;
                                     
                
                                     update stg_test_roster set opunit=  cr_dop_rec.opunit
                                     where 
                                     test_roster_id = cur_stg_testroster1.test_roster_id;
                                     
                                     update test_roster set opunit = cr_dop_rec.opunit
                                     where 
                                     test_roster_id = cur_stg_testroster1.test_roster_id;
                                     
                               
                               
                  else
              --this part is executed if roster_count > 3000     
              
                              /* CHANGE FOR MONARCH COMPLETENESS */
                                -- flag value is assigned by checking teacher_id.
                                --If it is true, then the opunit value will not be changed and a new record will be inserted into stg_district_opunit table 
                               if (flag) then
                                  cursor_opunit_rec.value := cr_dop_rec.opunit;
                                  v_opunit := cr_dop_rec.opunit;
                               else 
                                    --If flag value  is false , then the opunit value will be incremented to 1  and a new record will be inserted into stg_district_opunit table 
                                    cursor_opunit_rec.value := cr_dop_rec.opunit  +1;
                                    v_opunit :=cr_dop_rec.opunit  +1;
                               end if;
                               
                                /* END OF CHANGE FOR MONARCH COMPLETENESS */
                               
                               v_roster_count :=1;
                               v_dist_id := cr_dop_rec.district_org_id;
                               
                               --Check opunit should not exceed the range
                               
                               if (v_opunit <= cursor_last_opunit_rec.value) then 
                               
                                   insert into stg_district_opunit values(
                                          seq_STG_DISTRICT_OPUNIT_ID.nextval, 
                                          v_cust_id,v_dist_id,cursor_opunit_rec.value,v_roster_count,null);
                                   
                                   update stg_test_roster set opunit=   v_opunit
                                   where 
                                   test_roster_id = cur_stg_testroster1.test_roster_id;
                                   
                                   update test_roster set opunit =   v_opunit
                                   where  
                                   test_roster_id = cur_stg_testroster1.test_roster_id;
                                   
                                   
                                else 
                                
                                     raise_application_error(-20001,'Invalid opunit range encountered - '||SQLCODE||' -ERROR- '||SQLERRM);
      
                                end if;
                              
                 end if;
                 
                 
        end if;
      
      --closing the cursors
          
      close cur_district_opunit;
      
      commit; 
      
      --closing outer cursor
      end loop;
        
      EXCEPTION
           WHEN OTHERS THEN
           
              if cur_district_opunit%ISOPEN then
                 close cur_district_opunit;
              end if ; 
              if cur_district_org_id%ISOPEN then
                 close cur_district_org_id;
              end if;
              if cur_exp_opunit%ISOPEN then
                 close cur_exp_opunit;
              end if;       
              if cur_stg_test_roster%ISOPEN then
                 close cur_stg_test_roster;
               end if;  
              if cur_cust_id%ISOPEN then   
                 close cur_cust_id;
               end if;  
      
     raise_application_error(-20001,'An error was encountered - '||SQLCODE||' -ERROR- '||SQLERRM);
     
end;
/

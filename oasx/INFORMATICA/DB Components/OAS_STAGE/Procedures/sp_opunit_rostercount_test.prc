create or replace procedure sp_opunit_rostercount_test is


v_dist_id                  stg_district_opunit.district_org_id%type; 
v_cust_id                  stg_param_winscr_tb.customer_id%type;
v_roster_count             stg_district_opunit.roster_count%type;
v_opunit                   stg_district_opunit.opunit%type;
v_test_roster_id           stg_test_roster.test_roster_id%type;

--cursor for extracting test_roster_id from stg_test_roster table
cursor cur_testroster is 
 select test_roster_id,opunit from stg_test_roster where opunit is null;

 
--cursor for  stg_district_opunit table
cursor cur_district_opunit(p_district_id stg_district_opunit.district_org_id%type) 
is 
select STG_DISTRICT_OPUNIT_ID,district_org_id,roster_count,opunit  
  from stg_district_opunit 
  where district_org_id = p_district_id
        AND 
        opunit = (SELECT MAX(opunit) FROM stg_district_opunit WHERE district_org_id = p_district_id);

--cursor rowtype declaration
cursor_dop_rec cur_district_opunit%rowtype;




--cursor for  stg_param_winscr_tb table
cursor cur_cust_id 
is 
   SELECT customer_id 
   FROM stg_param_winscr_tb
   WHERE sno = 1;
   
  
  
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
cursor_dist_orgid_rec cur_district_org_id%rowtype;

--cursor for stg_export_parms table
cursor cur_exp_opunit
is
select value from stg_export_parms where upper(parameter)='START_OPUNIT' and customer_id = v_cust_id ; 

--cursor rowtype declaration
cursor_opunit_rec cur_exp_opunit%rowtype;


--cursor for stg_export_parms table TO get opunit range
cursor cur_exp_last_opunit
is
select value from stg_export_parms where upper(parameter)='LAST_OPUNIT' and customer_id = v_cust_id ; 

--cursor rowtype declaration
cursor_last_opunit_rec cur_exp_last_opunit%rowtype;

   
begin 

 ---open the cursor of getting customer id
 
 open  cur_cust_id;
       fetch cur_cust_id into  v_cust_id;
 
 
 if cur_cust_id%notfound then
  raise_application_error (-20001,'The specified customer_id does not exist');
 end if;
 close cur_cust_id;
 
 --  dbms_output.put_line ('customer' || v_cust_id);

 open cur_exp_opunit;
      fetch cur_exp_opunit into cursor_opunit_rec; 
       
       
 if cur_exp_opunit%notfound then
  raise_application_error (-20001,'start_opunit value not set' || v_cust_id);
 end if;
 close cur_exp_opunit;
 
 open cur_exp_last_opunit;
      fetch cur_exp_last_opunit into cursor_last_opunit_rec; 
       
       
 if cur_exp_last_opunit%notfound then
  raise_application_error (-20001,'last_opunit value not set' || v_cust_id);
 end if;
 close cur_exp_last_opunit;
 
       
for cur_testroster1 in cur_testroster
loop
      

            v_test_roster_id :=cur_testroster1.test_roster_id;
 --           dbms_output.put_line ('v_test_roster_id' || v_test_roster_id);
       
        
            open cur_district_org_id(v_test_roster_id);
             fetch cur_district_org_id into cursor_dist_orgid_rec; 
            
            if cur_district_org_id%notfound then
              raise_application_error (-20001,'No organistaion_id exists for the test_roster_id ' || v_test_roster_id);
            end if;
              
            close cur_district_org_id;
              
           
            
  --          dbms_output.put_line ('v_org_node_id' || cursor_dist_orgid_rec.org_node_id);       
       
        /*if cur_get_orgNode_id%No*/
         
            open cur_district_opunit(cursor_dist_orgid_rec.org_node_id);
             fetch  cur_district_opunit into  cursor_dop_rec; --v_STG_DISTRICT_OPUNIT_ID,v_dist_id ,v_roster_count,  v_opunit;
         
 --           dbms_output.put_line ('v_roster_count...' || cursor_dop_rec.roster_count);    
 --           dbms_output.put_line ('opunit' || cursor_dop_rec.opunit);    
             
      
      
      
      
if cur_district_opunit%notfound then
--this part is executed if dist_id is not present in stg_dist_opunit table
       v_roster_count:=1;
 --      dbms_output.put_line ('inside cur_dist_opunit%notfound loop');
      
       


       
       insert into stg_district_opunit values(
              seq_STG_DISTRICT_OPUNIT_ID.nextval,
              v_cust_id, 
              cursor_dist_orgid_rec.org_node_id,
              cursor_opunit_rec.value,v_roster_count);
       
       update stg_test_roster set opunit= cursor_opunit_rec.value
       where 
       test_roster_id = cur_testroster1.test_roster_id;
       
       update test_roster set opunit = cursor_opunit_rec.value 
       where 
       test_roster_id = cur_testroster1.test_roster_id;
       
       
       
       
else 

    if( cursor_dop_rec.roster_count < 3000) then
       --this part is executed if roster_count < 2999                  
        --                 dbms_output.put_line ('v_roster_count---this is in else if part of <2999 ' || cursor_dop_rec.roster_count);               
                         v_roster_count:= cursor_dop_rec.roster_count + 1 ;
                         
                         update stg_district_opunit set roster_count= v_roster_count
                         where 
                         STG_DISTRICT_OPUNIT_ID=cursor_dop_rec.STG_DISTRICT_OPUNIT_ID;
                         
     --                    dbms_output.put_line ('opunit value from cursor ' || cursor_opunit_rec.value || cur_testroster1.test_roster_id);
                         update stg_test_roster set opunit=  cursor_dop_rec.opunit
                         where 
                         test_roster_id = cur_testroster1.test_roster_id;
                         
                         update test_roster set opunit = cursor_dop_rec.opunit
                         where 
                         test_roster_id = cur_testroster1.test_roster_id;
                          
                  
                        

    else
        --this part is executed if roster_count > 300                     
   --                      dbms_output.put_line ('else part of <2999- will insert recs into all 3 tables');         
                        
                         cursor_opunit_rec.value := cursor_dop_rec.opunit  +1;
                         v_opunit :=cursor_dop_rec.opunit  +1;
                         v_roster_count:=1;
                         v_dist_id := cursor_dop_rec.district_org_id;
                         
                         --Check opunit should not exceed the range
                         
                         if (v_opunit <= cursor_last_opunit_rec.value) then 
                         
                             insert into stg_district_opunit values(
                                    seq_STG_DISTRICT_OPUNIT_ID.nextval, 
                                    v_cust_id,v_dist_id,cursor_opunit_rec.value,v_roster_count);
                             
                             update stg_test_roster set opunit=   v_opunit
                             where 
                             test_roster_id = cur_testroster1.test_roster_id;
                             
                             update test_roster set opunit =   v_opunit
                             where  
                             test_roster_id = cur_testroster1.test_roster_id;
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
      if cur_testroster%ISOPEN then
         close cur_testroster;
       end if;  
      if cur_cust_id%ISOPEN then   
         close cur_cust_id;
       end if;  
      
 raise_application_error(-20001,'An error was encountered - '||SQLCODE||' -ERROR- '||SQLERRM);


end;
/

CREATE OR REPLACE PROCEDURE Sp_Monarch_Student_Count_ISTEP(in_org_node_id in test_roster.org_node_id%type,
                                                                in_grade in stg_eiss_tb.grade%type,in_opunit in stg_eiss_tb.opunit%type,
                                                                v_parameter in varchar2,
                                                                v_count out number) IS
   
   --p_opunit stg_eiss_tb.opunit%type;
   --p_org_node_id test_roster.org_node_id%type;
   --p_grade stg_eiss_tb.grade%type;
   --p_parameter varchar2(50);
   /*Cursor for extracting OU records*/
   CURSOR school_case_count  IS
      -- passing parameter school_id and opunit
        select count(se.st_id)
          from stg_eiss_tb se
         where se.sh_id = in_org_node_id
           and se.level_type = 'ST'
           and se.opunit = in_opunit
         group by opunit;
       

       
       
-------  NOTE : opunit column is added in all the cursors during ISTEP 2008  ----------------------------
       
   /*Cursor for extracting SH records*/
   CURSOR SGL_COUNT IS
       
         select count(tr.student_id) 
                from test_roster tr,test_admin ta,test_catalog tc,student s,product p
                where tr.org_node_id = in_org_node_id 
                and s.grade = in_grade
                and s.student_id = tr.student_id 
                and tr.test_admin_id = ta.test_admin_id
                and ta.test_catalog_id = tc.test_catalog_id 
                and tc.product_id = p.product_id;
         
         
    CURSOR GIS_COUNT IS
      select count(tr.student_id)
        from test_roster tr, student s
       where tr.org_node_id = in_org_node_id
         and tr.student_id = s.student_id
         and s.grade = in_grade
         and tr.test_completion_status not in ('SC', 'NT');
      
  BEGIN                                   
      --p_opunit := in_opunit;
      --p_org_node_id := in_org_node_id;
      --p_grade := in_grade;
      --p_parameter := v_parameter;
      
      /*
      * No of student test records exported for this school in the particular opUnit
      */
      
      if ( v_parameter = 'school_case_count') then 
        open school_case_count;
            fetch school_case_count into v_count;
         
         
             if school_case_count%Notfound then
                raise_application_error (-20001,'The specified customer_id does not exist');
             end if;
         
         close school_case_count; 
        
        end if; 
        
        /*
        * No of students scheduled under this particular class for this particular Grade
        */
        
        if ( v_parameter = 'sgl_count') then 
         open SGL_COUNT;
            fetch SGL_COUNT into v_count;
         
         
             if SGL_COUNT%Notfound then
                raise_application_error (-20001,'The specified customer_id does not exist');
             end if;
         
          close SGL_COUNT; 
        
        end if;
        
        /*
        * No of students attempted for the test 
        */
        
        if ( v_parameter = 'gis_count') then 
         open GIS_COUNT;
            fetch GIS_COUNT into v_count;
         
         
             if GIS_COUNT%Notfound then
                raise_application_error (-20001,'The specified customer_id does not exist');
             end if;
         
          close GIS_COUNT; 
        
        end if;  
      
    
END Sp_Monarch_Student_Count_ISTEP;
/

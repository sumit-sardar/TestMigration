create or replace procedure assign_llespanol_demographics (las_customer_id integer)
as
  g_id integer;
  counter integer;
  sortCounter integer;
begin

     -- ETHNICITY
    select seq_customer_demographic_id.nextval into g_id from dual;
   
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'Ethnicity', 'Ethnicity', 'SINGLE', 1, 'T', 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'mexicano', 'mexicano', 1, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'mexicano-americano', 'mexicano-americano', 2, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'cubano', 'cubano', 3, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'cubano-mexicano', 'cubano-mexicano', 4, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'puertorriqueno', 'puertorriqueno', 5, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'dominicano', 'dominicano', 6, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'centroamericano', 'centroamericano', 7, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'sudaamericano', 'sudaamericano', 8, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'otro', 'otro', 9, 'T', 1, sysdate);

    -- HOME LANGUAGE
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'Home Language', 'Home Language', 'SINGLE', 2, 'T', 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, '00', '00', 1, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, '01', '01', 2, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, '02', '02', 3, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, '03', '03', 4, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, '04', '04', 5, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, '05', '05', 6, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, '06', '06', 7, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, '07', '07', 8, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, '08', '08', 9, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, '09', '09', 10, 'T', 1, sysdate);
      
    for counter in 10..99
    loop
    sortCounter := counter + 1;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);    
    end loop;
      
      
    -- USA SCHOOL ENROLLMENT
     select seq_customer_demographic_id.nextval into g_id from dual;
     
   insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'USA School Enrollment', 'USA School Enrollment', 'SINGLE', 3, 'T', 'T', 1, sysdate);
    
     for counter in 1900..2020
    loop
    sortCounter := counter - 1899;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);
    end loop;  
    
   
    
    -- MOBILITY
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'Mobility', 'Mobility', 'SINGLE', 4, 'T', 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'K', 'K', 1, 'T', 1, sysdate);
    
    for counter in 1..12
    loop
    sortCounter := counter + 1;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);
    
    end loop;
    
    
   
    
    
    -- PROGRAM PARTICIPATION
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'Program Participation', 'Program Participation', 'MULTIPLE',5, 'T', 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'ESEA Title 1', 'ESEA Title 1', 1, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'English Language Learner (ESEA Title III)', 'English Language Learner (ESEA Title III)', 2, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Gifted and Talented', 'Gifted and Talented', 3, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Indian Education', 'Indian Education', 4, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Migrant Education', 'Migrant Education', 5, 'T', 1, sysdate);
    
    
    
    -- SPECIAL EDUCATION
     select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'Special Education', 'Special Education', 'MULTIPLE', 6, 'T', 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'IEP', 'IEP', 1, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, '504', '504', 2, 'T', 1, sysdate);
    
    
    -- DISABILITY
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'Disability', 'Disability', 'SINGLE', 7, 'T', 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'A', 'A', 1, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'D', 'D', 2, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'HI', 'HI', 3, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'MU', 'MU', 4, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'OI', 'OI', 5, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'OHI', 'OHI', 6, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'SED', 'SED', 7, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'LN', 'LN', 8, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'SLI', 'SLI', 9, 'T', 1, sysdate); 
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'TBI', 'TBI', 10, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'VI', 'VI', 11, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'ME', 'ME', 12, 'T', 1, sysdate);
    
    
    --ACCOMMODATIONS
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'Accommodations', 'Accommodations', 'MULTIPLE', 21, 'T', 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'DC-S', 'DC-S', 1, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'DC-L', 'DC-L', 2, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'DC-RD', 'DC-RD', 3, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'DC-WR', 'DC-RW', 4, 'T', 1, sysdate);
        
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RQE-S', 'RQE-S', 5, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RQE-L', 'RQE-L', 6, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RQE-RD', 'RQE-RD', 7, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RQE-WR', 'RQE-RW', 8, 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RPE-S', 'RPE-S', 9, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RPE-L', 'RPE-L', 10, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RPE-RD', 'RPE-RD', 11, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RPE-WR', 'RPE-RW', 12, 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RSR-S', 'RSR-S', 13, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RSR-L', 'RSR-L', 14, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RSR-RD', 'RSR-RD', 15, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RSR-WR', 'RSR-RW', 16, 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'SA-S', 'SA-S', 17, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'SA-L', 'SA-L', 18, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'SA-RD', 'SA-RD', 19, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'SA-WR', 'SA-RW', 20, 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'ASM-S', 'ASM-S', 21, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'ASM-L', 'ASM-L', 22, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'ASM-RD', 'ASM-RD', 23, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'ASM-WR', 'ASM-RW', 24, 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RDNL-S', 'RDNL-S', 25, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RDNL-L', 'RDNL-L', 26, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RDNL-RD', 'RDNL-RD', 27, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RDNL-WR', 'RDNL-RW', 28, 'T', 1, sysdate);
    
    
    --SPECIAL CODES
     select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'SPECIAL CODES-K', 'SPECIAL CODES-K', 'SINGLE', 11, 'T', 'T', 1, sysdate);
    
    for counter in 0..9
    loop
    sortCounter := counter + 1;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);
    
    end loop;
    
    
    
    select seq_customer_demographic_id.nextval into g_id from dual;
     
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'SPECIAL CODES-L', 'SPECIAL CODES-L', 'SINGLE', 12, 'T', 'T', 1, sysdate);
     
    for counter in 0..9
    loop
    sortCounter := counter + 1;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);
    
    end loop;
    
    
    
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'SPECIAL CODES-M', 'SPECIAL CODES-M', 'SINGLE', 13, 'T', 'T', 1, sysdate);
     
    for counter in 0..9
    loop
    sortCounter := counter + 1;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);
    
    end loop;
    
    
    
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'SPECIAL CODES-N', 'SPECIAL CODES-N', 'SINGLE', 14, 'T', 'T', 1, sysdate);
     
    for counter in 0..9
    loop
    sortCounter := counter + 1;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);
    
    end loop;
    
     
    
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'SPECIAL CODES-O', 'SPECIAL CODES-O', 'SINGLE', 15, 'T', 'T', 1, sysdate);
     
    for counter in 0..9
    loop
    sortCounter := counter + 1;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);
    
    end loop;
    
    
    
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'SPECIAL CODES-P', 'SPECIAL CODES-P', 'SINGLE', 16, 'T', 'T', 1, sysdate);
     
    for counter in 0..9
    loop
    sortCounter := counter + 1;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);
    
    end loop;
    
    
    
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'SPECIAL CODES-Q', 'SPECIAL CODES-Q', 'SINGLE', 17, 'T', 'T', 1, sysdate);
     
    for counter in 0..9
    loop
    sortCounter := counter + 1;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);
    
    end loop;
        
    
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'SPECIAL CODES-R', 'SPECIAL CODES-R', 'SINGLE', 18, 'T', 'T', 1, sysdate);
     
    for counter in 0..9
    loop
    sortCounter := counter + 1;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);
    end loop;
    
        
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'SPECIAL CODES-S', 'SPECIAL CODES-S', 'SINGLE', 19, 'T', 'T', 1, sysdate);
     
    for counter in 0..9
    loop
    sortCounter := counter + 1;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);   
    end loop;   
    
    
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'SPECIAL CODES-T', 'SPECIAL CODES-T', 'SINGLE', 20, 'T', 'T', 1, sysdate);
     
    for counter in 0..9
    loop
    sortCounter := counter + 1;
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, counter, counter, sortCounter, 'T', 1, sysdate);    
    end loop;
        
end;
/

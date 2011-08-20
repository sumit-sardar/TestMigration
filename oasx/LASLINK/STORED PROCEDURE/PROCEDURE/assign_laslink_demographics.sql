create or replace procedure assign_laslink_demographics (las_customer_id integer)
as
  g_id integer;
  counter integer;
  sortCounter integer;
   jvalue varchar2(200);
begin

     -- ETHNICITY
    select seq_customer_demographic_id.nextval into g_id from dual;
   
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'Ethnicity', 'Ethnicity', 'SINGLE', 1, 'T', 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'American Indian or Alaska Native', 'American Indian or Alaska Native', 1, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'African American or Black, Not Hispanic', 'African American or Black, Not Hispanic', 2, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Asian', 'Asian', 3, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Pacific Islander', 'Pacific Islander', 4, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Hispanic or Latino', 'Hispanic or Latino', 5, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'White, Not Hispanic', 'White, Not Hispanic', 6, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Multiethnic', 'Multiethnic', 7, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Other', 'Other', 8, 'T', 1, sysdate);

    -- HOME LANGUAGE
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'Home Language', 'Home Language', 'SINGLE', 2, 'T', 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'English', '00', 1, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Albanian', '01', 2, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Amharic', '02', 3, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Arabic', '03', 4, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Armenian', '04', 5, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Assyrian', '05', 6, 'T', 1, sysdate);
    /*insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, '06', '06', 7, 'T', 1, sysdate);*/
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Bengali', '07', 8, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Bosnian', '08', 9, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Burmese', '09', 10, 'T', 1, sysdate);
      
    for counter in 10..99
    loop
     jvalue := LabelName(counter);
       if jvalue IS NOT NULL  THEN
         sortCounter := counter + 1;
         insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
         values (g_id, jvalue, counter, sortCounter, 'T', 1, sysdate);  
       END IF;     
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
    values (g_id, 'Autism', 'A', 1, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Deafness', 'D', 2, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Hearing Impairment', 'HI', 3, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Multiple Disabilities', 'MU', 4, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Orthopedic Impairment', 'OI', 5, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Other Health Impairments', 'OHI', 6, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Serious Emotional Disturbance', 'SED', 7, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Learning Disability', 'LN', 8, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Speech or Language Impairment', 'SLI', 9, 'T', 1, sysdate); 
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Traumatic Brain Injury', 'TBI', 10, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Visual Impairment', 'VI', 11, 'T', 1, sysdate);
     insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Mental Retardation', 'ME', 12, 'T', 1, sysdate);
    
    
    --ACCOMMODATIONS
    select seq_customer_demographic_id.nextval into g_id from dual;
    
    insert into customer_demographic (customer_demographic_id, customer_id, label_code, label_name, value_cardinality, sort_order, import_editable, visible, created_by, created_date_time) 
    values (g_id, las_customer_id, 'Accommodations', 'Accommodations', 'MULTIPLE', 21, 'T', 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Clarify directions in English - Speaking', 'DC-S', 1, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Clarify directions in English - Listening', 'DC-L', 2, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Clarify directions in English - Reading', 'DC-RD', 3, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Clarify directions in English - Writing', 'DC-WR', 4, 'T', 1, sysdate);
        
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Read questions/text in English - Speaking', 'RQE-S', 5, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Read questions/text in English - Listening', 'RQE-L', 6, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Read questions/text in English - Reading', 'RQE-RD', 7, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Read questions/text in English - Writing', 'RQE-WR', 8, 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Rephrase questions in English - Speaking', 'RPE-S', 9, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Rephrase questions in English - Listening', 'RPE-L', 10, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Rephrase questions in English - Reading', 'RPE-RD', 11, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Rephrase questions in English - Writing', 'RPE-WR', 12, 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Use scribe to record responses - Speaking', 'RSR-S', 13, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Use scribe to record responses - Listening', 'RSR-L', 14, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Use scribe to record responses - Reading', 'RSR-RD', 15, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Use scribe to record responses - Writing', 'RSR-WR', 16, 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Spelling aids provided - Speaking', 'SA-S', 17, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Spelling aids provided - Listening', 'SA-L', 18, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Spelling aids provided - Reading', 'SA-RD', 19, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Spelling aids provided - Writing', 'SA-WR', 20, 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Mark responses in Student Book - Speaking', 'ASM-S', 21, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Mark responses in Student Book - Listening', 'ASM-L', 22, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Mark responses in Student Book - Reading', 'ASM-RD', 23, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'Mark responses in Student Book - Writing', 'ASM-WR', 24, 'T', 1, sysdate);
    
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RDNL - Speaking', 'RDNL-S', 25, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RDNL - Listening', 'RDNL-L', 26, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RDNL - Reading', 'RDNL-RD', 27, 'T', 1, sysdate);
    insert into customer_demographic_value (customer_demographic_id, value_name, value_code, sort_order, visible, created_by, created_date_time) 
    values (g_id, 'RDNL - Writing', 'RDNL-WR', 28, 'T', 1, sysdate);
    
    
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

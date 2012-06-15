-- Inserting values in tabe_cat_objective table
insert into tabe_cat_objective values (3118, 'Recall Information', 30212, 60)
/
insert into tabe_cat_objective values (3119, 'Words in Context/Construct Meaning', 30212, 106)
/
insert into tabe_cat_objective values (3120, 'Evaluate/Extend Meaning and Interpret Graphic Information', 30212, 78)
/

insert into tabe_cat_objective values (2478, 'Usage', 30412, 63)
/
insert into tabe_cat_objective values (2479, 'Sentence Formation and Paragraph Development', 30412, 76)
/
insert into tabe_cat_objective values (2481, 'Capitalization, Punctuation, and Writing Conventions', 30412, 93)
/

insert into tabe_cat_objective values (2498, 'Number and Number Operations', 30292, 68)
/
insert into tabe_cat_objective values (2500, 'Computation and Estimation', 30292, 76)
/
insert into tabe_cat_objective values (2501, 'Measurement', 30292, 46)
/
insert into tabe_cat_objective values (2502, 'Geometry and Spatial Sense', 30292, 46)
/
insert into tabe_cat_objective values (2504, 'Data Analysis, Statistics, Probability', 30292, 86)
/
insert into tabe_cat_objective values (2505, 'Patterns, Functions, Algebra', 30292, 45)
/
insert into tabe_cat_objective values (2506, 'Problem Solving and Reasoning', 30292, 33)
/

insert into tabe_cat_objective values (3125, 'Whole Numbers', 30173, 60)
/
insert into tabe_cat_objective values (3126, 'Decimals, Fractions, Percents', 30173, 60)
/
insert into tabe_cat_objective values (3130, 'Integers', 30173, 60)
/
insert into tabe_cat_objective values (3131, 'Algebraic Operations', 30173, 60)
/

-- inserting value for tabe cat in product_type_code table
insert into product_type_code values ('TA', 'Tabe Adaptive')
/

-- updating the time

select item_set_id, item_set_name,time_limit  from item_set where  item_set_id in( select item_set_id from item_set_product where product_id = 8001) and adaptive = 'T'
/
update item_set set time_limit = 2400 where item_set_id = 30212
/
update item_set set time_limit = 1800 where item_set_id = 30412
/
update item_set set time_limit = 1200 where item_set_id = 30173
/
update item_set set time_limit = 1800 where item_set_id = 30292
/

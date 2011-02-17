insert into product (product_id, created_date_time, product_description, version, created_by, 
    activation_status, product_type, product_name, scoring_item_set_level, content_area_level, 
    preview_item_set_level, internal_display_name) values 
    (3000,sysdate,'Curriculum Framework for the Unit Testing WV','1.0',1,'AC','CF',
     'i-know Curriculum Framework for Unit Testing WV',4,3,4, 'CCSUNITWV');

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (3001,sysdate,'CCSUNITWV Reading Language Arts',3000,'1.0',1,'AC','ST','READING LANGUAGE ARTS', 'CCSUNITWV Reading Language Arts',4,3,4);
        
insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (3002,sysdate,'CCSUNITWV Mathematics',3000,'1.0',1,'AC','ST','MATHEMATICS','CCSUNITWV Mathematics',4,3,4);
        

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (3003,sysdate,'CCSUNITWV Science',3000,'1.0',1,'AC','ST','SCIENCE','CCSUNITWV Science',4,3,4);

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (3004,sysdate,'CCSUNITWV Social Studies',3000,'1.0',1,'AC','ST','SOCIAL STUDIES','CCSUNITWV  Social Studies',4,3,4);


insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    1,'Root',1,sysdate,3000);
    
insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    2,'Content Area',1,sysdate,3000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    3,'Grade',1,sysdate,3000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    4,'Standard',1,sysdate,3000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    5,'Benchmark',1,sysdate,3000);

    
commit;  
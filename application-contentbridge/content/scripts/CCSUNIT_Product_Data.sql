delete item_set_parent where item_set_id in (
  select item_set_id from item_set where item_set_category_id in (
    select item_set_category_id from item_set_category where framework_product_id=2000
 )); 

delete item_set_ancestor where item_set_id in (
  select item_set_id from item_set where item_set_category_id in (
    select item_set_category_id from item_set_category where framework_product_id=2000
 )); 

delete item_set where item_set_category_id in (
  select item_set_category_id from item_set_category where framework_product_id=2000
);

delete item_set_category where framework_product_id=2000;

delete product where product_id >=2000 and product_id <= 2005;

insert into product (product_id, created_date_time, product_description, version, created_by, 
    activation_status, product_type, product_name, scoring_item_set_level, content_area_level, 
    preview_item_set_level, internal_display_name) values 
    (2000,sysdate,'Curriculum Framework for the Unit Testing','1.0',1,'AC','CF',
     'i-know Curriculum Framework for Unit Testing',4,3,4, 'CCSUNIT');

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (2001,sysdate,'CCSUNIT Reading',2000,'1.0',1,'AC','ST','READING', 'CCSUNIT Reading',4,3,4);
    
insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (2002,sysdate,'CCSUNIT Writing',2000,'1.0',1,'AC','ST','WRITING', 'CCSUNIT Writing',4,3,4);
    
insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (2003,sysdate,'CCSUNIT Mathematics',2000,'1.0',1,'AC','ST','MATHEMATICS','CCSUNIT Mathematics',4,3,4);
        

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (2004,sysdate,'CCSUNIT Science',2000,'1.0',1,'AC','ST','SCIENCE','CCSUNIT Science',4,3,4);

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
    created_by, activation_status, product_type, product_name, internal_display_name, 
    scoring_item_set_level, content_area_level, preview_item_set_level) values 
    (2005,sysdate,'CCSUNIT Language',2000,'1.0',1,'AC','ST','LANGUAGE','CCSUNIT Language',4,3,4);


insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    1,'Root',1,sysdate,2000);
    
insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    2,'Grade',1,sysdate,2000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    3,'Content Area',1,sysdate,2000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    4,'Strand',1,sysdate,2000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    5,'Standard',1,sysdate,2000);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    6,'Benchmark',1,sysdate,2000);


insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
    created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
    7,'Skill',1,sysdate,2000);

commit;

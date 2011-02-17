insert into product (product_id, created_date_time, product_description, version, created_by, 
	activation_status, product_type, product_name, scoring_item_set_level, content_area_level, 
	preview_item_set_level, internal_display_name) values (1100,sysdate,'Curriculum Framework for the State of Texas',
	'1.0',1,'AC','CF','i-know Curriculum Framework for Texas',4,3,4, 'TX');

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
	created_by, activation_status, product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (1101,sysdate,
	'TX English Language Arts',1100,'1.0',1,'AC','ST','ENGLISH LANGUAGE ARTS',
	'TX English Language Arts',4,3,4);
	
insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
	created_by, activation_status, product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (1102,sysdate,
	'TX Mathematics',1100,'1.0',1,'AC','ST','MATHEMATICS','TX Mathematics',4,3,4);
		

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
	created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
	1,'Root',1,sysdate,1100);
	
insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
	created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
	2,'Grade',1,sysdate,1100);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
	created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
	3,'Content Area',1,sysdate,1100);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
	created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
	4,'Strand',1,sysdate,1100);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
	created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
	5,'Standard',1,sysdate,1100);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
	created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
	6,'Benchmark',1,sysdate,1100);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
	created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
	7,'Skill',1,sysdate,1100);
commit;

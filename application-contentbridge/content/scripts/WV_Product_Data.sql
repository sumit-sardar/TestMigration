insert into product (product_id, created_date_time, product_description, version, created_by, 
	activation_status, product_type, product_name, scoring_item_set_level, content_area_level, 
	preview_item_set_level, internal_display_name) values (1300,sysdate,'Curriculum Framework for the State of West Virginia',
	'1.0',1,'AC','CF','West Virginia Instructional Item Bank',4,2,4, 'WV');

insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
	created_by, activation_status, product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (1301,sysdate,
	'WV Reading Language Arts',1300,'1.0',1,'AC','ST','READING LANGUAGE ARTS',
	'WV Reading Language Arts',4,2,4);
	
insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
	created_by, activation_status, product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (1302,sysdate,
	'WV Mathematics',1300,'1.0',1,'AC','ST','MATHEMATICS',
	'WV Mathematics',4,2,4);
	
insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
	created_by, activation_status, product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (1303,sysdate,
	'WV Social Studies',1300,'1.0',1,'AC','ST','SOCIAL STUDIES','WV Social Studies',4,2,4);
	
insert into product (product_id, created_date_time, product_description, parent_product_id, version, 
	created_by, activation_status, product_type, product_name, internal_display_name, 
	scoring_item_set_level, content_area_level, preview_item_set_level) values (1304,sysdate,
	'WV Science',1300,'1.0',1,'AC','ST','SCIENCE','WV Science',4,2,4);
		

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
	created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
	1,'Root',1,sysdate,1300);
	
insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
	created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
	2,'Content Area',1,sysdate,1300);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
	created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
	3,'Grade',1,sysdate,1300);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
	created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
	4,'Standard',1,sysdate,1300);

insert into item_set_category (item_set_category_id, item_set_category_level, item_set_category_name, 
	created_by, created_date_time, framework_product_id) values (seq_item_set_category_id.nextval,
	5,'Objective',1,sysdate,1300);

commit;

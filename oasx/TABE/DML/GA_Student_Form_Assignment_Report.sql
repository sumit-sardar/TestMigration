
select system.org_node_name District,
       school.org_node_name School,
       class.org_node_name "Class/Teacher",
       adm.test_admin_name "Test Session Name",
       stu.grade Grade,
       stu.last_name "Last Name",
       stu.first_name "First Name",
       stu.ext_pin1 "Student ID",
       acco.screen_reader "Screen Reader Flag",
       tr.form_assignment "Test Form",
       tr.updated_date_time "Last Modified Time"
  from org_node               class,
       org_node               grade,
       org_node               school,
       org_node               system,
       org_node_parent        class_parent,
       org_node_parent        grade_parent,
       org_node_parent        school_parent,
       student                stu,
       student_accommodation  acco,
       test_roster            tr,
       test_admin             adm,
       customer_configuration cc
 where cc.customer_id = adm.customer_id
   and adm.test_admin_id = tr.test_admin_id
   and tr.student_id = stu.student_id
   and stu.student_id = acco.student_id
   and tr.org_node_id = class.org_node_id
   and class.org_node_id = class_parent.org_node_id
   and class_parent.parent_org_node_id = grade.org_node_id
   and grade.org_node_id = grade_parent.org_node_id
   and grade_parent.parent_org_node_id = school.org_node_id
   and school.org_node_id = school_parent.org_node_id
   and school_parent.parent_org_node_id = system.org_node_id
   and stu.activation_status = 'AC'
   and tr.activation_status = 'AC'
   and class.activation_status = 'AC'
   and grade.activation_status = 'AC'
   and school.activation_status = 'AC'
   and system.activation_status = 'AC'
   --and stu.created_by <> 1
   --and stu.precode_id is null
   and acco.screen_reader = 'T'
   and tr.form_assignment = '1T'
   and adm.product_id = 37
   and cc.customer_configuration_name = 'GA_Customer'
   --and cc.customer_id = 
order by  system.org_node_name,
          school.org_node_name,
          grade.org_node_name,
          class.org_node_name
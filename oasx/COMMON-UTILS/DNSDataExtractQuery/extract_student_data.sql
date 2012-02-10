/*Data of students marked as DNS.*/
SELECT 
nd2.org_node_name AS Corp_Name
,nd2.org_node_code AS Corp_Number
,nd1.org_node_name AS School_Name
,nd1.org_node_code AS School_Number
,nd.Org_Node_Name AS Teacher_Name
,ad.test_admin_name AS Test_Name
,st.grade AS Grade
,st.first_name||', '||st.last_name AS Student_Name
,st.birthdate AS Birthdate
,sc.test_completion_status_desc AS Testing_Status
,us.first_name AS User_First_Name
,us.last_name AS User_Last_Name
,us.user_name AS User_Login_Id
,tr.dns_updated_datetime AS DNS_Updated_DateTime
FROM 
test_roster tr
,org_node_student ns
,org_node nd
,org_node nd1
,org_node nd2
,org_node_parent op
,org_node_parent op1
,test_admin ad
,student st
,test_completion_status_code sc
,users us
WHERE 
tr.dns_status = 'Y'
AND tr.student_id=ns.student_id
AND ns.org_node_id=nd.org_node_id
AND tr.test_admin_id=ad.test_admin_id
AND st.student_id=tr.student_id
AND tr.test_completion_status=sc.test_completion_status
AND us.user_id=tr.dns_updated_by
AND nd.org_node_id=op.org_node_id
AND nd1.org_node_id=op.parent_org_node_id
AND nd1.org_node_id=op1.org_node_id
AND nd2.org_node_id=op1.parent_org_node_id
AND st.customer_id = <customer Id>

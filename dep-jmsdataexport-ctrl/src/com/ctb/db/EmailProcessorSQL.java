package com.ctb.db;

public interface EmailProcessorSQL {
	
	String GET_CUSTOMER_EMAIL_BY_USERNAME = "select c.customer_id as customerId , c.email_type as emailType , c.reply_to as replyTo , c.subject as subject , c.email_body as emailBody from customer_email_config c where c.customer_id = (select distinct node.customer_id  from users u  , user_role role  , org_node node  where u.user_name = ?  and u.user_id = role.user_id  and role.org_node_id = node.org_node_id) and c.email_type = ?";
	
	String GET_USER_DETAIL_BY_USER_NAME = " select  users.user_id as userId,  users.user_name as userName,  users.email from  users, password_hint_question phq where  phq.password_hint_question_id (+) = users.password_hint_question_id  and users.user_name = ?  and users.activation_status = 'AC'";

}

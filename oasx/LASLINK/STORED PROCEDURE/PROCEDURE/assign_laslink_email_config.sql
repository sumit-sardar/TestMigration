create or replace procedure assign_laslink_email_config (customer_id integer)
as
begin

    Insert into OAS.CUSTOMER_EMAIL_CONFIG
       (CUSTOMER_ID, EMAIL_TYPE, SUBJECT, EMAIL_BODY, CREATED_BY, CREATED_DATE_TIME)
     Values
       (customer_id , 1, 'OAS User Login', 'Welcome to the Online Assessment System (OAS),provided by CTB/McGraw-Hill (www.ctb.com).  Your account has been set up and your username is:  <#userid#>'
       ||chr(13)||chr(13)||'Access OAS at the following URL: https://oas.ctb.com/'
       ||chr(13)||chr(13)||'For security purposes, your password will be sent to you in a separate email.'
       ||chr(13)||'Please watch your inbox for this message.'
       ||chr(13)||chr(13)||'For any questions about set up or access, just call your Account Manager at'
       ||chr(13)||'(888) 630-1102.', 1, sysdate);
       
     Insert into OAS.CUSTOMER_EMAIL_CONFIG
       (CUSTOMER_ID, EMAIL_TYPE, SUBJECT, EMAIL_BODY, CREATED_BY, CREATED_DATE_TIME)
     Values
       (customer_id , 2, 'Password', 'Your Online Assessment System (OAS) password is <#password#>'
       ||chr(13)||chr(13)||'Your username has been sent in a separate email.', 1, sysdate);
       
     Insert into OAS.CUSTOMER_EMAIL_CONFIG
       (CUSTOMER_ID, EMAIL_TYPE, SUBJECT, EMAIL_BODY, CREATED_BY, CREATED_DATE_TIME)
     Values
       (customer_id , 3, 'Password Notification', 'Your Online Assessment System (OAS) password has been changed.'
       ||chr(13)||chr(13)||'If you are unaware of this change and/or have not authorized or requested it, please contact your OAS Administrator.', 1, sysdate);

end; 
/

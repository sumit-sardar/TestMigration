--@C:\workspaces\database\DB_Install_Script.sql
set termout on
spool bmytsync_deploy.log

conn oas/pwd@oasr5p_ewdc

PROMPT CREATING BMTSYNC TYPES
@types/student_message_typ.sql
@types/bmtsync_assignment_typ.sql
@types/bmtsync_testadmin_typ.sql

PROMPT CREATING BMTSYNC TABLES
@tables/tbl_bmtsync_customer.sql
@tables/tbl_bmtsync_errors.sql
@tables/tbl_bmtsync_retryfrequency.sql
@tables/tbl_bmtsync_student_status.sql
@tables/tbl_bmtsync_assignment_status.sql
@tables/tbl_bmtsync_testadmin_status.sql

PROMPT CREATE QUEUE TABLES
@tables/tbl_create_queuetables.sql

PROMPT CREATING BMTSYNC PACKAGES
@pkg/pkg_bmtsync_queueTable.sql
@pkg/pkg_bmtsync_createmessage.sql
@pkg/pkg_bmtsync_onchange.sql;
@pkg/pkg_bmtsync_students.sql
@pkg/pkg_bmtsync_studentQueue.sql
@pkg/pkg_bmtsync_assignment.sql
@pkg/pkg_bmtsync_assignmentqueue.sql;
@pkg/pkg_bmtsync_testadmin.sql
@pkg/pkg_bmtsync_testadminqueue.sql;
@pkg/pkg_bmtsync_teststatus.sql

PROMPT CREATING BMTSYNC TRIGGERS
@triggers/trg_bmtsync_assignment.sql;
@triggers/trg_bmtsync_org_node_student.sql;
@triggers/trg_bmtsync_student_accomodation.sql;
@triggers/trg_bmtsync_student.sql;

PROMPT SCHEDULING BMTSYNC JOBS
@jobs/job_bmtsync_createassignmentmessage.sql
@jobs/job_bmtsync_createstudentmessage.sql
@jobs/job_bmtsync_createtestadminmessage.sql

commit;
disconnect

set termout off
spool off

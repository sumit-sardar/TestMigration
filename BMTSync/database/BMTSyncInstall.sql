--@C:\workspaces\database\DB_Install_Script.sql
set termout on
spool bmytsync_deploy.log

conn oas/oasr5d@oasr51d.ctb

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


PROMPT SCHEDULING BMTSYNC JOBS
@jobs/job_bmtsync_createassignmentmessage.sql
@jobs/job_bmtsync_createstudentmessage.sql
@jobs/job_bmtsync_createtestadminmessage.sql


commit;
disconnect

set termout off
spool off

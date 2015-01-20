set termout on
spool bmytsync_uninstall.log

conn oas/pwd@oasr5p_ewdc

PROMPT DROPPING BMTSYNC TRIGGERS
DROP TRIGGER TRG_BMTSYNC_TESTADMIN;
DROP TRIGGER TRG_BMTSYNC_TESTROSTER;
DROP TRIGGER TRG_BMTSYNC_TESTADMIN_ITEMSET;
DROP TRIGGER TRG_BMTSYNC_SISS;
DROP TRIGGER TRG_BMTSYNC_ORGNODE_STUDENT;
DROP TRIGGER TRG_BMTSYNC_STUDENT_ACCO;
DROP TRIGGER TRG_BMTSYNC_STUDENT;

PROMPT DROPPING BMTSYNC PACKAGES
DROP PACKAGE PKG_BMTSYNC_QUEUETABLE;
DROP PACKAGE PKG_BMTSYNC_CREATEMESSAGE;
DROP PACKAGE PKG_BMTSYNC_ONCHANGE;
DROP PACKAGE PKG_BMTSYNC_TESTSTATUS;
DROP PACKAGE PKG_BMTSYNC_ASSIGNMENT;
DROP PACKAGE PKG_BMTSYNC_Students;
DROP PACKAGE PKG_BMTSYNC_ASSIGNMENTQUEUE;
DROP PACKAGE PKG_BMTSYNC_TESTADMINQUEUE;
DROP PACKAGE PKG_BMTSYNC_TESTADMIN;
DROP PACKAGE PKG_BMTSYNC_STUDENTQUEUE;

PROMPT DROPPING BMTSYNC TABLES
DROP TABLE BMTSYNC_TESTADMIN_STATUS;
DROP TABLE BMTSYNC_ASSIGNMENT_STATUS;
DROP TABLE BMTSYNC_STUDENT_STATUS;
DROP TABLE BMTSYNC_ERRORS;
DROP TABLE BMTSYNC_CUSTOMER;
DROP TABLE BMTSYNC_RETRY_FREQUENCY;

PROMPT DROPPING BMTSYNC TYPES
DROP TYPE STUDENT_MESSAGE_TYP;
DROP TYPE BMTSYNC_ASSIGNMENT_TYP;
DROP TYPE BMTSYNC_TESTADMIN_TYP;

--DROP THE JOBS MANUALLY
PROMPT UNSCHEDULE THE BMTSYNC JOBS MANUALLY

commit;
disconnect

set termout off
spool off

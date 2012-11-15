UPDATE "OAS"."CUSTOMER_REPORT_BRIDGE" 
SET DISPLAY_NAME = 'Group Immediate Scores', 
DESCRIPTION = 'Displays the immediate scores obtained by the students of a group in standard format.',
REPORT_NAME = 'GroupImmediateScores'
WHERE CUSTOMER_ID='&Please_enter_the_CUSTOMER_ID' 
#!/bin/bash

cd `dirname $0`

ORACLE_BASE="/oracle"
ORACLE_HOME="${ORACLE_BASE}/product/9.0.1"
ORACLE_LIB="${ORACLE_HOME}/lib"
NLS_SORT="WEST_EUROPEAN"
NLS_CHARACTERSET="UTF8"
NLS_COMP="ANSI"
NLS_LANGUAGE="American_America.UTF8"
LD_LIBRARY_PATH=":/usr/local/lib:${ORACLE_LIB}"
PATH=/usr/mount/bin:/usr/sbin/:/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:/${ORACLE_HOME}/bin:/web/sites/tools:/usr/local/bin/oracle:/${ORACLE_HOME}/lib
OASPRODAUTH=`/export/home/oasdev/operations/cronAuth.sh`
export ORACLE_BASE ORACLE_HOME ORACLE_LIB NLS_SORT NLS_CHARACTERSET NLS_COMP NLS_LANGUAGE LD_LIBRARY_PATH PATH OASPRODAUTH

BASEFILENAME=TABE_ERROR_DETAILS_`date '+%Y%m%d%H%M%S'`
QUERYFILE1=$BASEFILENAME.sql
RESULTFILE1=$BASEFILENAME.xls


########
cat >$QUERYFILE1 <<EndOfText
set feedback off
set echo off
set termout off
set pages 50000
set linesize 5000
set markup html on
col ROSTER_STATUS format a20
col SISS_STATUS format a20
col ERROR_STATUS format a20
col CREATED_DATETIME format a20
spool $RESULTFILE1
SELECT TEM.PRODUCT_NAME PRODUCT_NAME,
		 TEM.CUSTOMER_ID CUSTOMER_ID,
		 TEM.STUDENT_ID STUDENT_ID,
		 TEM.TEST_ADMIN_ID TEST_ADMIN_ID,
		 TEM.TEST_ROSTER_ID TEST_ROSTER_ID,
		 TEM.ROSTER_STATUS ROSTER_STATUS,
		 TEM.TD_ITEM_SET TD_ITEM_SET,
		 TEM.SISS_STATUS SISS_STATUS,
		 TEM.RAW_SCORE RAW_SCORE,
		 TEM.ERROR_TYPE_FLAG ERROR_TYPE_FLAG,
		 TEM.ERROR_STATUS ERROR_STATUS,
		 TEM.CREATED_DATETIME CREATED_DATETIME,
		 TEM.COMMENTS COMMENTS
	FROM TABE_ERROR_MONITOR TEM
	WHERE TRUNC(TEM.CREATED_DATETIME) = TRUNC(SYSDATE) - 1
	 AND TEM.EMAIL_FLAG = 'N';
spool off
exit;
EndOfText
########
sqlplus -s oas/qoasr5@new_oasr5t.ctb @$QUERYFILE1


EmailErrorCount=`sqlplus -s /nolog  <<ENDPOS
  set echo off
  conn oas/qoasr5@new_oasr5t.ctb
  set feedback off
  set serveroutput on
  set head off
  declare
    Daily_Error_Count varchar2(4000) := NULL;
  begin
  UPDATE TABE_ERROR_MONITOR TEM
   SET TEM.EMAIL_FLAG = 'Y'
 WHERE TRUNC(TEM.CREATED_DATETIME) = TRUNC(SYSDATE) - 1
   AND TEM.EMAIL_FLAG = 'N';
 Daily_Error_Count := SQL%ROWCOUNT;
 COMMIT;
  dbms_output.put_line(Daily_Error_Count);
  end;
  /
  exit;
  ENDPOS`
  
 
TotalErrorCount=`echo $EmailErrorCount | awk '{print $2}'`


if [ "$TotalErrorCount" != "0" ]; then
echo "Sending Mail..."

/usr/lib/sendmail -t -oi <<EndOfText
From: operations@dagobah.mhe.mhc (OAS Operations)
Reply-To: operations@dagobah.mhe.mhc (OAS Operations)
To: somenath.c@tcs.com
To: saswata.jis@gmail.com
To: suvam194@gmail.com
Subject: TABE Error Details Notification 
Mime-Version: 1.0
Content-Type: multipart/mixed; boundary="boundary-line" 

--boundary-line
Content-Type: application/vnd.ms-excel; name="$RESULTFILE1"
Content-Transfer-Encoding: 7bit
Content-Disposition: attachment; filename="$RESULTFILE1"

`cat $RESULTFILE1 | grep -v '^$'`
--boundary-line

See attached for the details of TABE error for the previous day.

- OAS Development Support

EndOfText

fi

rm $QUERYFILE1
rm $RESULTFILE1

exit

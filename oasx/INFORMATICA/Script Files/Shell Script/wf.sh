#!/usr/bin/bash
domain=Domain_mcsoas504
umask 0002

export PATH=/bin:.:/usr/kerberos/bin:/usr/local/bin:/usr/bin:/usr/X11R6/bin
#export PATH=/bin:.:/usr/kerberos/bin:/usr/local/bin:/bin:/usr/bin:/usr/X11R6/bin
PATH=$PATH:$HOME/bin

export PATH
unset USERNAME


# LANG=C is required for informatica installation
export LANG=C
alias rm="rm -i"
alias cp="cp -i"
alias mv="mv -i"

export PWX_HOME=/u01/app/informatica/PowerCenter8.1.1
export INFA_HOME=/u01/app/informatica/PowerCenter8.1.1
export PM_HOME=/u01/app/informatica/PowerCenter8.1.1
export ODBCHOME=/u01/app/informatica/PowerCenter8.1.1/ODBC5.1 
export ODBCINI=$ODBCHOME/odbc.ini

export ORACLE_BASE=/u01/app/oracle
export ORACLE_SID=cqar5p
export ORACLE_HOME=/u01/app/oracle/product/10.2.0.4.4
export NLS_LANG=AMERICAN_AMERICA.UTF8
export PATH=/u01/app/oracle/product/10.2.0.4.4/bin:/u01/app/oracle/product/10.2.0.4.4/opatch/OPatch:$PATH

export PATH=/u01/app/informatica/PowerCenter8.1.1:/u01/app/informatica/PowerCenter8.1.1/server/tomcat/bin:$PWX_HOME:$ODBCHOME:$PATH:.
export CLASSPATH=.:/u01/app/oracle/product/10.2.0.4.4/jdbc/lib/classes12.zip:$CLASSPATH
#export LD_LIBRARY_PATH=/usr/lib64:/usr/lib:/lib:/u01/app/oracle/product/10.2.0.4.4/lib32
export LD_LIBRARY_PATH=/usr/lib:/lib:/u01/app/oracle/product/10.2.0.4.4/lib
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/u01/app/informatica/PowerCenter8.1.1:$ODBCHOME/lib:$PWX_HOME:$INFA_HOME/server/bin:$INFA_HOME/server/lib:$INFA_HOME/server/bin:$INFA_HOME/server/tomcat/lib:$INFA_HOME/server/tomcat/server/lib:$INFA_HOME/java/jre/lib

export TNS_ADMIN=$ORACLE_HOME:/network/admin


set -o vi
export PS1="$SYSNAME"'${PWD#${PWD%/*/*/*}/}($ORACLE_SID)>'

integration_service=oasprodr5_Integration_Service
user_name=ayan_bandyopadhyay
passwd=ayan321
log_file=/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/auto_GA.log



echo "PATH= $PATH" >> $log_file 
/appl/informatica/PowerCenter8.1.1/server/bin/pmcmd startworkflow -sv $integration_service -d $domain -u $user_name -p $passwd -wait wf_eiss_GA >> $log_file
/appl/informatica/PowerCenter8.1.1/server/bin/pmcmd  getworkflowdetails -sv $integration_service -d $domain -u $user_name -p $passwd wf_eiss_GA >>  $log_file

if [ `find /appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/EISS -iname 'eiss_ISTEP.dat' -mmin -720` ];
then
/appl/informatica/PowerCenter8.1.1/server/bin/pmcmd startworkflow -sv $integration_service -d $domain -u $user_name -p $passwd -wait wf_MONARCH_Completeness_Check_GA >> $log_file;
/appl/informatica/PowerCenter8.1.1/server/bin/pmcmd  getworkflowdetails -sv $integration_service -d $domain -u $user_name -p $passwd wf_MONARCH_Completeness_Check_GA >>  $log_file;
fi


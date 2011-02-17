#!/usr/bin/ksh

# Set up the environment. Note that we're depending on the foo.properties
# file to use the thin JDBC driver: if you want to use the OCI driver, 
# you'll need to set ORACLE_HOME and probably  some other things as well.
export JAVA_HOME=/usr/java
export PATH=/bin:/opt/bin:/opt/X11/bin:/usr/mount/bin:/usr/local/bin:/usr/bin:.:/usr/openv/netbackup/bin:/usr/ccs/bin:/usr/ucb:/usr/openwin/bin:/oracle/product/9.2.0.1/bin:/web/sites/oastest.ctb.com/weblogic610/bin:/usr/local/bin:/usr/java/j2sdkee1.3.1/bin

prod_dir=/appl/publishing/deployments/production/outbound
archive_dir=/appl/publishing/deployments/production/deployed
fail_dir=/appl/publishing/deployments/production/failed
job_log=ctb_deploy.log
process_log=/tmp/prod_publish.log
fail=failed
success=succeeded

#Check for directories in the publish folder
cd $prod_dir

if [ ! -d * ]; then 
   echo nothing
   exit 1 
fi

today=`date +%Y%m%d`
mkdir $archive_dir/$today
mkdir $fail_dir/$today

#
# The script appends to ctb_deploy.log everything it wants to communicate.
# If a prod_publish.log file appears in /tmp, that's because ctb_deploy.log
# wasn't writeable.
#

for folder in *; do
   cd $folder
   job_status=$success
   for i in `find .`; do
     if [ ! -w $i ]; then
       echo "$folder: $i is not writeable: this should not happen" >> $process_log
       job_status=$fail
     fi
   done
   if [ ! -x ./deployToProduction ] ; then
     echo "deployToProduction is not executable: this should not happen" >> $job_log
     job_status=$fail
   else
     ./deployToProduction
     if [ $? -ne 0 ] ; then
       echo "deployToProduction exited with non-zero status: this should not happen" >> $job_log
       job_status=$fail
     fi
   fi
   cd $prod_dir
   if [ $job_status = $fail ] ; then
     mv $folder $fail_dir/$today
   else
     mv $folder $archive_dir/$today
   fi
done
exit 0


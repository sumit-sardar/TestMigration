#!/usr/bin/ksh

# Set the Java environment variables
export JAVA_HOME=/usr/java
export J2EE_RELEASE=/usr/java/j2sdkee1.3.1
export CLASSPATH=/export/home/iknowusr/ItemDetailReport.jar:/usr/j2sdkee1.3.1/lib/j2ee.jar:/oracle/product/9.0.1/jdbc/lib/classes12.
jar:/usr/java/jre/lib/ext:/usr/java/jre/lib/ext/xerces.jar:/oracle/product/9.0.1/jdbc/lib/classes12.jar:/oracle/product/9.2.0.1/jdbc
/lib/classes12.jar:/export/data/inbound/scripts:/web/sites/lexington-1-0/java:/web/sites/lexington-1-0/build/classes:/usr/java/j2sdk
ee1.3.1/lib/j2ee.jar:/export/cim:.
export ORACLE_HOME=/oracle/product/9.2.0.1
export ORACLE_LIB=/oracle/product/9.2.0.1/lib
export ORACLE_BIN=/oracle/product/9.2.0.1/bin
export PATH=/bin:/opt/bin:/opt/X11/bin:/usr/mount/bin:/usr/local/bin:/usr/bin:.:/usr/openv/netbackup/bin:/usr/ccs/bin:/usr/ucb:/usr/
openwin/bin:/oracle/product/9.2.0.1/bin:/web/sites/oastest.ctb.com/weblogic610/bin:/usr/local/bin:/usr/java/j2sdkee1.3.1/bin
export LD_LIBRARY_PATH=/oracle/oracle32/lib:/usr/openwin/lib:/usr/openwin/lib/X11:/usr/dt/lib:/opt/lib:/opt/lib/X11:/oracle/product/
9.2.0.1/lib


java -classpath ItemDetailReport.jar ItemDetailReport ContentQA reportList.txt
java -classpath ItemDetailReport.jar ItemDetailReport Production reportList.txt
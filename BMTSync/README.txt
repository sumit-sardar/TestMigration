You ened to install two custom Maven libraries

ojdbc14.jar - available in the lib folder.  
This is the standard Oracle JDBC Driver

aqapi.jar - available in the lib folder.  
This is the Oracle AQ Messaging driver, part of teh Oracle Containers for J2EE.  Get the pack here and you can find the source JAR.
http://www.oracle.com/technetwork/middleware/ias/index-099846.html


oc4j_extended_101350
 +- /rdbms/jlib/aqapi.jar
 +- jdbc/lib/ojdbc14dms.jar


mvn install:install-file -Dfile=3rdparty/ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dpackaging=jar -Dversion=10.1.3.5
mvn install:install-file -Dfile=3rdparty/aqapi.jar -DgroupId=com.oracle -DartifactId=aqapi -Dpackaging=jar -Dversion=10.1.3.5


call mvn install:install-file -Dfile="%OCPS_HOME%\assets\jars\classes12.jar" -DgroupId=com.oracle -DartifactId=classes12 -Dversion=8.1.7 -Dpackaging=jar -DgeneratePom=true

call mvn install:install-file -Dfile="%OCPS_HOME%\assets\jars\jsch-0.1.24.jar" -DgroupId=jsch -DartifactId=jsch-0.1.24 -Dversion=0.1.24 -Dpackaging=jar -DgeneratePom=true

call mvn install:install-file -Dfile="%OCPS_HOME%\assets\jars\edtFTPj-1.5.3.jar" -DgroupId=edtFTPj -DartifactId=edtFTPj-1.5.3 -Dversion=1.5.3 -Dpackaging=jar -DgeneratePom=true

pause


@echo off
cd /d %~dp0
set "CURRENT_DIR=%cd%"
cd %CURRENT_DIR%
rem start call "storage\apache-cassandra-0.7.6-2\bin\cassandra.bat"
rem start java -cp ./storage/hsqldb/lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:OAS --dbname.0 oas
cd .\server\apache-tomcat-6.0.29\bin\
rem sleep 5
start call "startup.bat"
rem cd ..\..\..
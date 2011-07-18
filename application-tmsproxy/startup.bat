@echo off
c:
cd c:\oas\application-tmsproxy
rem start call "storage\apache-cassandra-0.7.6-2\bin\cassandra.bat"
cd .\server\apache-tomcat-6.0.29\bin\
sleep 5
start call "startup.bat"
cd ..\..\..
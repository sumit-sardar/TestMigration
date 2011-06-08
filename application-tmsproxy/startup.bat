@echo off
start call "storage\apache-cassandra-0.7.6-2\bin\cassandra.bat"
cd .\server\apache-tomcat-6.0.29\bin\
start call "startup.bat"
@echo off
cd /d %~dp0
set "CURRENT_DIR=%cd%"
cd %CURRENT_DIR%
cd .\server\apache-tomcat-6.0.29\bin\
start call "startup.bat"
@echo off
@setlocal

set ANT_HOME=
set BUILDLIB=.\lib\build-only
set LIB=.\lib


SET LOCALCLASSPATH=
    for %%c in (%BUILDLIB%\*.zip %BUILDLIB%\*.jar %LIB%\*.jar) do call append.bat %%c

SET LOCALCLASSPATH=.\lib\app\bc4jdomorcl.jar;%LOCALCLASSPATH%
SET LOCALCLASSPATH=%JAVA_HOME%\lib\tools.jar;%LOCALCLASSPATH%
SET LOCALCLASSPATH=.\build\classes;%LOCALCLASSPATH%
SET LOCALCLASSPATH=.\lib\build-only\optional.jar;%LOCALCLASSPATH%
SET LOCALCLASSPATH=.\lib\build-only\junit.jar;%LOCALCLASSPATH%
SET LOCALCLASSPATH=.;%LOCALCLASSPATH%

echo %LOCALCLASSPATH%
java -cp %LOCALCLASSPATH% junit.swingui.TestRunner %*

@endlocal




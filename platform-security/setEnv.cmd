@rem *************************************************************************
@rem This script is used to set up your environment for development with 
@rem WebLogic Server. It simply calls the commEnv.cmd script under 
@rem C:\bea\weblogic81\common\bin.  Add domain specific configuration in this script below.
@rem *************************************************************************

set WL_HOME=C:\bea10_3_0\wlserver_10.3
@rem Set Production Mode.  When this is set to true, the server starts up in
@rem production mode.  When set to false, the server starts up in development
@rem mode.  If it is not set, it will default to false.
set PRODUCTION_MODE=

@rem Set JAVA_VENDOR to java virtual machine you want to run on server side.
set JAVA_VENDOR=Sun

@rem Set JAVA_HOME to java virtual machine you want to run on server side.
set JAVA_HOME=C:\bea10_3_0\jdk160_05

call "%WL_HOME%\common\bin\commEnv.cmd"

@rem set JAVA_VM=
set CLASSPATH=%WEBLOGIC_CLASSPATH%;%POINTBASE_CLASSPATH%;%JAVA_HOME%\jre\lib\rt.jar;%WL_HOME%\server\lib\webservices.jar

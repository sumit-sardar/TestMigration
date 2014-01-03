create database link DBL_ADS_PROD
  connect to ads identified by ads7pr8
  using '(DESCRIPTION =

    (ADDRESS = (PROTOCOL = TCP)(HOST = 172.28.169.116)(PORT = 1521))

    (LOAD_BALANCE = yes)  

    (CONNECT_DATA = (SERVER = DEDICATED) (SERVICE_NAME = oasr5p_ewdc))

  )';


create database link DBL_OAS_PROD
  connect to oas identified by oas7pr8
  using '(DESCRIPTION =

    (ADDRESS = (PROTOCOL = TCP)(HOST = 172.28.169.116)(PORT = 1521))

    (LOAD_BALANCE = yes)  

    (CONNECT_DATA = (SERVER = DEDICATED) (SERVICE_NAME = oasr5p_ewdc))

  )';
BEGIN
  -- Job defined by an existing program and schedule. 
 DBMS_SCHEDULER.create_job (
    job_name        => 'MONITOR_TABE_LOCATOR_ERROR',
    job_type        => 'PLSQL_BLOCK',
    job_action      => 'BEGIN SP_TABE_LOCATOR_ERROR_MONITOR; END;',
    start_date      => SYSTIMESTAMP,
    repeat_interval => 'freq=MINUTELY; interval = 30',
    end_date        => NULL,
    enabled         => TRUE,
    comments        => 'This job will track the TABE locator error.Runs every 30 minutes');

END;
/
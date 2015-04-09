BEGIN
DBMS_SCHEDULER.create_job (
    job_name        => 'MONITOR_ROSTER_STATUS_TABE',
    job_type        => 'PLSQL_BLOCK',
    job_action      => 'BEGIN SP_TABE_ROSTER_STATUS_ISSUE; END;',
    start_date      => SYSTIMESTAMP,
    repeat_interval => 'freq=MINUTELY;  interval = 30',
    end_date        => NULL,
    enabled         => TRUE,
    comments        => 'This job will track the roster status mismatch error.Runs every 30 minutes'
);
END;
/
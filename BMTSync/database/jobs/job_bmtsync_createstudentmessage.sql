--fires every 21 seconds
DECLARE
   VARIABLE jobno number;
BEGIN

  dbms_job.submit(job => :jobno,
                  what => 'DECLARE BEGIN PKG_BMTSYNC_CREATEMESSAGE.CreateStudentMessage; END;',
                  next_date => SYSDATE,
                  interval => 'SYSDATE + .35/1440');

  commit;

  
end;
/

/*
exec dbms_job.run( :jobno );

BEGIN
   DBMS_JOB.CHANGE(301, null, null, 'SYSDATE + .34/1440');
   COMMIT;
END;
*/

--fires every 21 (.35/1440)seconds. 

   VARIABLE jobno number;
BEGIN

  dbms_job.submit(job => :jobno,
                  what => 'DECLARE BEGIN PKG_BMTSYNC_CREATEMESSAGE.CreateAssignmentMessage; END;',
                  next_date => SYSDATE,
                  interval => 'SYSDATE + .35/1440');

  commit;

  
end;
/

/*
exec dbms_job.run( :jobno );

BEGIN
   DBMS_JOB.CHANGE(321, null, null, 'SYSDATE + .35/1440');
   COMMIT;
END;
*/

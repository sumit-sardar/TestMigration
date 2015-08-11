--fires every 21 (.35/1440)seconds. 
--DECLARE
   VARIABLE jobno number;
BEGIN

  dbms_job.submit(job => :jobno,
                  what => 'DECLARE BEGIN PKG_BMTSYNC_FIXEXPIRED.FixExpired_Messages_Wrapper; END;',
                  next_date => SYSDATE,
                  interval => 'SYSDATE + 10/1440');

  commit;

  
end;
/


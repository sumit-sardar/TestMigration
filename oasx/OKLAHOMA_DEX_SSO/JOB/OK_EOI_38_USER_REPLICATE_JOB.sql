DECLARE
  jobno NUMBER;
BEGIN
  DBMS_JOB.SUBMIT(job       => jobno,
                  what      => 'begin SP_OK_EOI_38_USER_REPLICATE; end;',
                  next_date => sysdate ,
                  interval  => 'SYSDATE + 2/1440');
  COMMIT;
END;
/
DECLARE
  jobno NUMBER;
BEGIN
  DBMS_JOB.SUBMIT(job       => jobno,
                  what      => 'begin PKG_LAS_LM_TRG.SP_MANTAIN_AVAILABILITY_JOB; end;',
                  next_date =>  sysdate,
                  interval  => 'SYSDATE + 2/1440');
 END;
/
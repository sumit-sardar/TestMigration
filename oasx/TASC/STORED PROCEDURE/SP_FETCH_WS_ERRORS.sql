create or replace PROCEDURE SP_FETCH_WS_ERRORS(fetchSize in number) AS
BEGIN
declare
  cursor c1          is select * from (select rowid FROM WS_ERROR_LOG WHERE STATUS = 'Progress' 
                      order by updated_date asc) FOR UPDATE SKIP LOCKED ;

  type   tRowidTbl   is table of rowid index by pls_integer;
         vRowidTbl   tRowidTbl;
begin
  open  c1;
      fetch c1 bulk collect into vRowidTbl LIMIT fetchSize;
  close c1;
    forall i in vRowidTbl.FIRST..vRowidTbl.LAST
       insert into TMP_WS_ERROR_LOG_ROWNUM (row_id) values (vRowidTbl(i)) ;
end;
END;
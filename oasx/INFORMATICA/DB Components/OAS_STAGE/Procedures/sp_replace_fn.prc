create or replace procedure sp_replace_fn
is
v_nn varchar2(20);
a number;
b number;
c varchar2(10);
begin
a:=instr('abc <asa> qqwq','<',1,1);
b:=instr('abc <asa> qqwq','>',1,1);
c:=substr('abc <asa> qqwq',a,b);
v_nn:=replace('abc <asa> qqwq',c,'#');
dbms_output.put_line(v_nn);
end;
/

create or replace procedure p1 is
begin
for i in 1 to 3 loop
dbms_ouput.put_line(i);
for j in i+1 to 3 loop
dbms_ouput.put_line(j);
for k in j+1 to 3 loop
dbms_ouput.put_line(j);
end loop;
end loop;
end loop;
end p1;
/

create or replace procedure proc_test (in_param in varchar2, out_param in out varchar2) is
casual_use number;
Begin
        casual_use := 10;
        out_param := '10';
End proc_test;
/

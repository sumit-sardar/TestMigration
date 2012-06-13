CREATE OR REPLACE PROCEDURE SP_SPLIT_HEADER IS
--cursor c1 is select precode_data from stg_student_demographic_fl where precode_data like 'INTERNAL%';
str1 varchar2(2000);
BEGIN
STR1:='CREATE TABLE XX (NAME VARCHAR2(20))';
EXECUTE IMMEDIATE 'CREATE TABLE XX (NAME VARCHAR2(20))';
end SP_SPLIT_HEADER;
/

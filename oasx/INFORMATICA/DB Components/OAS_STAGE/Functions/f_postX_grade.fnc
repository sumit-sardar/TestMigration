create or replace function f_postX_grade(level_data_in varchar2,grade_in varchar2,grade_pos_in number ) return varchar2 is
  Result_grade varchar2(2);
  v_level_data varchar2(500);
  v_grade_position number;
  v_grade varchar2(2);
  v_udf_grade varchar2(2);
  v_studen_barcode varchar2(1);

begin
v_level_data:=level_data_in;
-- catching the grade position in the post export file
v_grade_position:=grade_pos_in;
-- catching the grade from student demo table (i,e., additional student grade)
v_grade:=grade_in;
-- catching the grade from UDF string (i,e., student bio grade)
v_udf_grade:= substr(level_data_in,(instr(level_data_in,',',1,12)+36),2);
-- catching the barcode value (barcode will be blank for onlne student)
v_studen_barcode:=substr(level_data_in,(instr(level_data_in,',',1,7)+1),1);

v_studen_barcode:= replace(v_studen_barcode,',','@');



	if v_grade_position=7 then -- grade position 7-8

   	   if v_grade=v_udf_grade then

	   	  Result_grade:=v_udf_grade;
	   else

	   	   Result_grade:=v_grade;

	  end if;
	end if;



	if v_grade_position=46 then -- grade position 46-47

	   if v_studen_barcode='@' then -- online student

   	   	  	 Result_grade:=v_udf_grade;

	   else		--precode student

	   		 Result_grade:='';

	   end if;

	end if;



 return(Result_grade);

end;
/

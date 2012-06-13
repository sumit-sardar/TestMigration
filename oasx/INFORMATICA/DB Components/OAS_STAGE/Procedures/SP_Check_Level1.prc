create or replace procedure SP_Check_Level1(
v_Customer in out varchar2,
v_Level1 in integer,
v_Level2 in integer,
v_Level3 in integer,
v_Level4 in integer,
v_NO_OF_LEVEL in INTEGER,
Flag out integer)
is
  v_LevelCount_Table integer;
  v_LevelCount integer;
  begin
      Flag:=0;
        -- To count the number of rows present for specific customer and file type
     -- select count(*) into v_LevelCount from mapping_tb
     --where customer_name=v_Customer and file_type=v_Filetype;
     -- To count the number of rowms present with the parametes specified in the procedure
     if v_NO_OF_LEVEL = 1 then
         if (v_Level1 IS NULL) then
         Flag := 1;
      end if;
     elsif v_NO_OF_LEVEL = 2 then
      if (v_Level1 is null) or (v_Level2 is null) then
         Flag := 1;
      end if;
     elsif v_NO_OF_LEVEL = 3 then
         if (v_Level1 is null) or (v_Level2 is null) or (v_Level3 is null) then
         Flag := 1;
         end if;
     elsif v_NO_OF_LEVEL = 4 then
      if (v_Level1 is null) or (v_Level2 is null) or (v_Level3 is null) or (v_Level4 is null) then
         Flag := 1;
      end if;
     else
      Flag :=0;
     end if;
end;
/

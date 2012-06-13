CREATE OR REPLACE Procedure Sp_Update_Attributes_On_Level Is
  Var_Sql Varchar2(300) := '';
  Cursor C1 Is
    Select Distinct Cpm_Enrollment_Site.Site_Sys_Id,
                    Cpm_Enrollment_Site.Site_Nbr,
                    Abc.Level_No,
                    Abc.Col1,
                    Abc.Col2,
                    Abc.Target_Column_Name,
                    Abc.Target_Table_Name
      From Cpm_Enrollment_Site,
           (Select x.Nodekey,
                   x.Level_No,
                   x.Col1,
                   x.Col2,
                   y.Target_Column_Name,
                   y.Target_Table_Name
              From Monarch_Attribute_Load x, Stg_Attr_Level_Map_Tb y
             Where x.Level_No = y.Node_Level
               And Upper(y.Xml_Attribute_Name) = Upper(x.Col1)
             Order By x.Level_No) Abc
     Where To_Number(Cpm_Enrollment_Site.Site_Type) =
           To_Number(Abc.Level_No)
       And Cpm_Enrollment_Site.Site_Nbr = Abc.Nodekey;
Begin
  For C1_Rec In C1 Loop
    ---- TO UPDATE THE VALUE IN TABLE
    Var_Sql := 'UPDATE ' || C1_Rec.Target_Table_Name || ' SET ' ||
               C1_Rec.Target_Column_Name || '=' || '''' || C1_Rec.Col2 || '''' ||
               ' WHERE SITE_NBR =' || C1_Rec.Site_Nbr ||
               ' AND SITE_SYS_ID =' || C1_Rec.Site_Sys_Id;
    Dbms_Output.Put_Line(Var_Sql);
    Execute Immediate (Var_Sql);
  End Loop;
  Commit;
End;
/

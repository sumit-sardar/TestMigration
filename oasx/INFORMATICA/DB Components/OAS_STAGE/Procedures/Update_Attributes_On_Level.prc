CREATE OR REPLACE Procedure Update_Attributes_On_Level Is
  Var_Sql Varchar2(300) := '';
  Cursor C1 Is
    Select x.Level_No,
           x.Col1,
           x.Col2,
           y.Target_Column_Name,
           y.Target_Table_Name
      From Monarch_Attribute_Load x, Stg_Attr_Level_Map_Tb y
     Where x.Level_No = y.Node_Level
       And Upper(y.Xml_Attribute_Name) = Upper(x.Col1)
     Order By x.Level_No;
  Cursor C2(Level_No Integer) Is
    Select Site_Sys_Id
      From Cpm_Enrollment_Site
     Where To_Number(Site_Type) = Level_No;
Begin
  For C1_Rec In C1 Loop
    For C2_Rec In C2(C1_Rec.Level_No) Loop
      ---- TO UPDATE THE VALUE IN TABLE
      Var_Sql := 'UPDATE ' || C1_Rec.Target_Table_Name || ' SET ' ||
                 C1_Rec.Target_Column_Name || '=' || '''' || C1_Rec.Col2 || '''' ||
                 ' WHERE SITE_SYS_ID =' || C2_Rec.Site_Sys_Id;
      Dbms_Output.Put_Line(Var_Sql);
      --EXECUTE IMMEDIATE (VAR_SQL);
    End Loop;
  End Loop;
  Commit;
End;
/

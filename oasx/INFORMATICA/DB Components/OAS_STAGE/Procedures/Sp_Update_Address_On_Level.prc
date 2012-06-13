CREATE OR REPLACE Procedure Sp_Update_Address_On_Level Is
  Var_Sql Varchar2(300) := '';
  Cursor C1 Is
    Select S3.Site_Sys_Id,
           S2.Column_Value,
           S2.Node_Key,
           S1.Target_Column_Name
      From Stg_Address_Map S1, Stg_Address_Tb S2, Cpm_Enrollment_Site S3
     Where Upper(S1.Administrator_Name) = Upper(S2.Administrator_Name)
       And Upper(S1.Frame_Work) = Upper(S2.Frame_Work)
       And Upper(S1.Source_Column_Name) = Upper(S2.Column_Name)
       And Upper(S1.Source_Type) = Upper(S2.Column_Type)
       And S1.Node_Level = S2.Node_Level
       And S2.Column_Value Is Not Null
       And S3.Site_Nbr = S2.Node_Key
     Order By Node_Key;
Begin
  For C1_Rec In C1 Loop
    ---- TO UPDATE THE VALUE IN TABLE
    Var_Sql := 'UPDATE CPM_ENROLLMENT_SITE SET ' ||
               C1_Rec.Target_Column_Name || '=' || '''' ||
               C1_Rec.Column_Value || '''' || ' WHERE SITE_SYS_ID =' ||
               C1_Rec.Site_Sys_Id;
    Dbms_Output.Put_Line(Var_Sql);
    Execute Immediate (Var_Sql);
  End Loop;
  Commit;
End;
/

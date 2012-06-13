CREATE OR REPLACE Procedure Update_Attributes Is
  Var_Sql Varchar2(300) := '';
  Cursor C1 Is
    Select Cpm_Enrollment_Site.Site_Sys_Id,
           Abc.Unique_Id,
           Abc.Attribute_Name,
           Abc.Attribute_Value,
           Abc.Target_Column_Name,
           Abc.Target_Table_Name
      From Cpm_Enrollment_Site,
           (Select Ab.Unique_Id,
                   Ab.Attribute_Name,
                   Ab.Attribute_Value,
                   Stg_Map_Poc_Tb.Target_Column_Name,
                   Stg_Map_Poc_Tb.Target_Table_Name
              From Stg_Map_Poc_Tb,
                   (Select x.Unique_Id, y.Attribute_Name, y.Attribute_Value
                      From Stg_Xml_Model2 x, Stg_Xml_Model2 y
                     Where x.Pk_Node = y.Fk_Nodeinstance) Ab
             Where Upper(Stg_Map_Poc_Tb.Xml_Attribute_Name) =
                   Upper(Ab.Attribute_Name)
               And Stg_Map_Poc_Tb.Unique_Id = Ab.Unique_Id
               And Stg_Map_Poc_Tb.Customer_Name = 'FLORIDA'
             Order By Ab.Unique_Id) Abc
     Where Cpm_Enrollment_Site.Site_Nbr = Abc.Unique_Id;
Begin
  For C1_Rec In C1 Loop
    ---- TO UPDATE THE VALUE IN TABLE
    Var_Sql := 'UPDATE ' || C1_Rec.Target_Table_Name || ' SET ' ||
               C1_Rec.Target_Column_Name || '=' || '''' ||
               C1_Rec.Attribute_Value || '''' || ' WHERE SITE_NBR =' ||
               C1_Rec.Unique_Id || ' AND SITE_SYS_ID =' ||
               C1_Rec.Site_Sys_Id;
    Dbms_Output.Put_Line(Var_Sql);
    Execute Immediate (Var_Sql);
  End Loop;
  Commit;
End;
/

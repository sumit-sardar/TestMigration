CREATE OR REPLACE Procedure Sp_Eiss_Org(Cust_Id In Number, Prod_Id Number) Is
  i Number := 0;
  Cursor c Is
    Select Org_Node.Org_Node_Name, Org_Node.Org_Node_Code
      From Org_Node, Org_Node_Category
     Where Org_Node.Customer_Id = Cust_Id
       And Org_Node_Category.Customer_Id = Cust_Id
       And Upper(Org_Node_Category.Category_Name) = 'DISTRICT'
       And Org_Node_Category.Org_Node_Category_Id =
           Org_Node.Org_Node_Category_Id;
Begin
  i := 0;
  For C1 In c Loop
    i := i + 1;
    Insert Into Eiss_Org_Bc_Tpe
    Values
      (Cust_Id,
       Prod_Id,
       1,
       1,
       2,
       i,
       Nvl(C1.Org_Node_Code, C1.Org_Node_Name),
       C1.Org_Node_Name,
       500);
  End Loop;
  Commit;
End Sp_Eiss_Org;
/

CREATE OR REPLACE Procedure Sp_Update_Data_History(In_Session_Name Varchar2) Is
  /*DESCRIPTION: This stored procedure updates the UPDATED_DATE_TIME field in the DATA_IMPORT_HISTORY table for the session name passed to it as input. The DATA_IMPORT_HISTORY table  is used to keep track of data history*/
  /*AUTHOR: Wipro Offshore Team*/
  /*CREATED DATE: 22nd February 2006*/
  /*Variables to hold Customer_Id,Count,infa_user*/
  v_User_Id Number(38);
Begin
  Select User_Id
    Into v_User_Id
    From Users
   Where Upper(User_Name) = 'SYSTEM'; /* changed user name 04/19/07 */
  Update Data_Import_History
     Set Updated_By = v_User_Id, Updated_Date_Time = Sysdate
   Where Import_Filename = In_Session_Name
     And Updated_By Is Null;
  Commit;
End;
/

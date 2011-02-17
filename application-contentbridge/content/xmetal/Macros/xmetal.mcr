<?xml version="1.0"?>
<!DOCTYPE MACROS SYSTEM "macros.dtd">
<MACROS> 

<MACRO name="Change Comment menu" hide="true" lang="VBScript">
<![CDATA[
' ************************************************************
' Sub ChangeCommentMenu ()
' ************************************************************
' DESCRIPTION
' In Insert Menu, Change the caption of "&Comment" to "&XML Comment".
' The reason for changing this is because there's also a tag name "Comment"
' in the DTD.
'
' PARAMETERS
' None
'
' RETURN VALUE
' None
'
' HISTORY
' 1. 20030227 COREL: David Ngo
'    Official Release
' ************************************************************
Sub ChangeCommentMenu()
   On Error Resume Next 
      Dim i
      Dim objControl
      Dim objBar
      Dim objMenu
      Dim strFunctionName

      set objBar = Application.CommandBars.item( "Menu bar" )
      set objMenu = objBar.Controls.item( "Insert" )

      strFunctionName = "ChangeCommentMenu()"

      If ( Not objMenu Is Nothing ) Then
         set objControl = objMenu.Controls.item(5)
         If ( Not objControl Is Nothing ) Then
            objControl.Caption = "&XML Comment                         F8"
         End If
      End If 
      set objBar = Nothing
      set objMenu = Nothing
      set objControl = Nothing
   If ( Err.Number <> 0 ) Then   
      Application.Alert ("Error: " & Err.Number & "(" & strFunctionName & "), " & Err.Description & VBCRLF & "Unable to Change Comment in Insert Menu to XML Comment.")
      set objBar = Nothing
      set objMenu = Nothing
      set objControl = Nothing
   End If
End Sub
Err.Clear
Call ChangeCommentMenu
]]>
</MACRO>

<MACRO name="On_Application_Deactivate" lang="JScript"><![CDATA[


// ************************************************************
// Sub OnApplicationDeactivate ()
// ************************************************************
// DESCRIPTION
// Move the cursor position out of the Attribute Inspector when user
// switching application while editing it. Otherwise, it will causes XMetal
// to crash. The cursor will get restore to its position before entering
// Attribute's Inspector.
//
// PARAMETERS
// None
//
// RETURN VALUE
// None
//
// HISTORY
// 1. 20030226 COREL: David Ngo
//    Official Release
// ************************************************************
function OnApplicationDeactivate () {
   if ((Application.ActiveDocument) && (Application.ActiveDocument.nodeType != 9)) {
      var rng = Application.ActiveDocument.Range;
      rng.Select();
   }
}
OnApplicationDeactivate ();
]]></MACRO>

<MACRO name="LIBRARY - General Application Functions" hide="true" lang="VBScript">
<![CDATA[
' ************************************************************
' Sub InitializeEnvironment()
' ************************************************************
' DESCRIPTION
' Stores the currently logged in user name.
'
' PARAMETERS
' None
'
' RETURN VALUE
' None
'
' HISTORY
' 1. 20010425 SoftQuad: David Ngo
'    Pre-release
' 2. 20010914 SoftQuad: Roehl Sioson 
'    Official Release
' ************************************************************

Sub InitializeEnvironment()

	On Error Resume Next

	Dim objNetwork
	Dim strFunctionName

	strFunctionName = "InitializeEnvironment()"

	Set objNetwork = CreateObject( "WScript.Network" )
	Call Application.CustomProperties.Add( "Application.UserName", objNetwork.username )

	' Garbage Collection

	Set objNetwork = Nothing

	' Default error trap.

	If ( Err.Number <> 0 ) Then
           Application.Alert ("Error: " & Err.Number & "(" & strFunctionName & "), " & Err.Description)
	End If

End Sub
' ************************************************************
' Function ConfigureCTBResourceManager()
' ************************************************************
' DESCRIPTION
' Adds the CTB tab to the Resource Manager and removes the 
' Desktop and Assets tab.
'
' PARAMETERS
' None
'
' RETURN VALUE
' None
'
' HISTORY
' 1. 20020919 Corel: David Ngo
'    Initial-release
' ************************************************************
' ************************************************************

Sub ConfigureCTBResourceManager()

	On Error Resume Next

	Dim strFunctionName

	strFunctionName = "ConfigureCTBResourceManager()"
'	Call ResourceManager.AddTab( "KMS", "AttributeUI.Component" )
	Call ResourceManager.RemoveTab( "Desktop" )
	Call ResourceManager.RemoveTab( "Assets" )

	' Default error trap.

	If ( Err.Number <> 0 ) Then
           Application.Alert ("Error: " & Err.Number & "(" & strFunctionName & "), " & Err.Description)
	End If

End Sub

' ************************************************************
' Sub AddCTBToolbarItems()
' ************************************************************
' DESCRIPTION
' Adds a menu item for the About CTB macro
'
' This function is called from the "On_Application_Open_Complete"
' macro.
'
'
' PARAMETERS
' None
'
' RETURN VALUE
' None
'
' NOTES
'
' HISTORY
' 1. 20030203 COREL: David Ngo
'    Official Release
' ************************************************************

Sub AddCTBToolbarItems()

	On Error Resume Next

	Dim i
	Dim objAboutCTBItem
	Dim objElementListButton
	Dim objControl
	Dim objHelpMenu
	Dim objViewMenu
	Dim objBar
	Dim strFunctionName
	Dim strSpacing

	strFunctionName = "AddCTBToolbarItems()"

	Set objBar = Application.CommandBars.item( "Menu bar" )

	' Add About CTB Menu item on Help Menu
	Set objHelpMenu = objBar.Controls.item( "Help" )
	If ( Not objHelpMenu Is Nothing ) Then
		Set objAboutCTBItem = Nothing
		For i = 1 To objHelpMenu.Controls.Count
			Set objControl = objHelpMenu.Controls.item(i)
			If ( objControl.Caption = "About XMetaL Integration for CTB/Mcgraw-Hill" ) Then
				Set objAboutCTBItem = objControl
			End If
		Next
		If ( objAboutCTBItem Is Nothing) Then
			Set objAboutCTBItem = objHelpMenu.Controls.Add(5)
			If ( Not objAboutCTBItem Is Nothing ) Then
				objAboutCTBItem.Caption = "About XMetaL Integration for CTB/Mcgraw-Hill"
				objAboutCTBItem.DescriptionText = "View the CTB XMetaL Customization Number"
				objAboutCTBItem.TooltipText = "View the CTB XMetaL Customization Number"
				objAboutCTBItem.OnAction = "About CTB"
				objAboutCTBItem.Enabled = True
				objAboutCTBItem.FaceId = 0
			End If
		End If
	End If  
End Sub
]]>
</MACRO>
<MACRO name="LIBRARY - General Application JScript Functions" hide="true" lang="JScript"><![CDATA[

// ************************************************************
// Function GetCurrentDate()
// ************************************************************
// DESCRIPTION
// Gives the date and time.
//
// PARAMETERS
// None
//
// RETURN VALUE
// String  The date and time.
//
// HISTORY
// 1. 20010425 SoftQuad: David Ngo
//    Pre-release
// 2. 20010914 SoftQuad: Roehl Sioson 
//    Official Release
// ************************************************************

function GetCurrentDate() {

       var strResult = "";
       
       var d, day, strDay, strMonth, s, month, strReturn;
       var strDay = new Array("Sunday", "Monday", "Tuesday");
       var strDay = strDay.concat("Wednesday","Thursday", "Friday");
       var strDay = strDay.concat("Saturday");
       var strMonth = new Array("January", "February", "March", "April", "May", "June");
       var strMonth = strMonth.concat ("July", "August", "September", "October", "November", "December");
       d = new Date();
       day = d.getDay();
       //s += (d.getMonth() + 1) + "/";
       month = (d.getMonth());
       s = d.getDate() + ", ";
       s += d.getYear();
       strReturn = (strDay[day] + ", " + strMonth[month] + " " + s);
       return strReturn;
       d = null;
}
]]></MACRO>

<MACRO name="About CTB" lang="VBScript" hide="true" id="1000">
<!--
' ************************************************************
' DESCRIPTION
' Displays a Message Box containing the current Customization Number
'
' HISTORY
' 1. 20030203 COREL: David Ngo
'    Official Release
' ************************************************************
-->
<![CDATA[

Sub ShowAboutCTB ()

	On Error Resume Next

	Dim objVersionProp
	Dim strMessage
	Dim strTitle
	Dim strFunctionName
	Dim strVersion
	
	strFunctionName = "ShowAboutCTB"

	Set objVersionProp = Application.CustomProperties.item( "CTBCustomizationNumber" )
	If ( Not objVersionProp Is Nothing ) Then
		strVersion = objVersionProp.value
	Else 
		strVersion = "No version number was found in xmlcus.ini"
	End If	

	strTitle = "XMetaL CTB-McgrawHill Customization"
        strMessage = "This is a customized version of XMetaL" &_
		" built for the CTB-McgrawHill System." & _
		vbCRLF & vbCRLF & _
		" Version Number: " & strVersion
			
	Call Application.MessageBox ( strMessage, 0, strTitle )

	' Garbage Collection
	Set objVersionProp = Nothing

	' Default error trap.
	If ( Err.Number <> 0 ) Then
           Application.Alert ("Error: " & Err.Number & "(" & strFunctionName & "), " & Err.Description)
	End If

End Sub

Call ShowAboutCTB()

]]>
</MACRO> 


<MACRO name="MakeReplaceText" key="Ctrl+Alt+Z" lang="VBScript" id="1127">
<![CDATA[
 ' SoftQuad Script Language VBSCRIPT:
 if Application.Documents.Count = 0 Then
   Application.Alert("No Open Document")
 
 Else 

   If Application.ActiveDocument.ViewType = 2 Then
     Application.Alert("This macro doesn't work in Plain Text mode")
   Else 
     txt = Selection.Text
     Selection.Delete
     Selection.InsertReplaceableText(txt)
   End If
 End If
 
]]>
</MACRO>
 
<MACRO name="Refresh Macros" key="Ctrl+Alt+R" lang="JScript" id="1270" tooltip="" desc="">
<![CDATA[
 Application.RefreshMacros();
 Application.Alert("Macros have been refreshed");
]]>
</MACRO>

<MACRO lang="JScript" name="Open Document Macros" id="1272">
<![CDATA[
 var count = Application.Documents.Count;
 if (count == 0) {
   Application.Alert("No Open Document");
 }
 else {
   var mpath = ActiveDocument.MacroFile;
   Documents.Open(mpath, 1);  // open in tags on view
 }
]]>
</MACRO>

<MACRO name="Open Application Macros" lang="JScript" id="1274">
<![CDATA[
 var mpath = Application.MacroFile;
 Documents.Open(mpath, 1); // open in tags on view
]]>
</MACRO>

<MACRO name="On_Update_UI" hide="true" lang="JScript">
<![CDATA[
// this will only work if no On_Update_UI macro is defined for the DTD
if (Selection.IsInsertionPoint && ActiveDocument.ViewType == 1) {
// this should only apply to the tags-on view, and allow selection of the top-level element
   if (Selection.ContainerNode == null) {
      Selection.MoveRight();
   }
   if (Selection.ContainerNode == null) {
      Selection.MoveLeft();
   }
}
]]>
</MACRO>

<MACRO name="On_Application_Activate" lang="JScript" hide="true"><![CDATA[
// Reset the Status Text and the Cursor in case a different document changed them and they are stuck.
function OnApplicationActivate()
{
   Application.SetStatusText("");
   Application.SetCursor(0);
}
OnApplicationActivate();
]]></MACRO> 

<MACRO name="On_Mouse_Over_xxx" lang="JScript" hide="true"><![CDATA[
// Reset the Status Text and the Cursor in case a different document changed them and they are stuck.
function OnMouseOver()
{
   Application.SetStatusText("");
   Application.SetCursor(0);
}
OnMouseOver();
]]></MACRO> 

<MACRO name="On_DTD_Open_Complete" key="" hide="true" lang="VBScript"><![CDATA[
Function doaddElements()
  ' In the macro the new document is NOT the active document
  ' so to get at the document type information do
  Set docType = Application.NewDocumentType

  ' root element
  Dim rootElem
  rootElem = docType.name
' Application.Alert rootElem
  If rootElem = "Article" And Not docType.hasElementType("Annotation") Then
    ' add Annotation element
    docType.addElement "Annotation", "Annotation" , True , False
    ' add attribute UserName
    docType.addAttribute "Annotation", "UserName", "", 0, 0 
    ' add attribute Time
    docType.addAttribute "Annotation", "Time", "", 0, 0 
    ' add attribute Initials
    docType.addAttribute "Annotation", "Initials", "", 0, 0 
    ' add attribute Comment
    docType.addAttribute "Annotation", "Comment", "", 0, 0 
    
    'add Annotation element  to the other elements inclusion list 
    If docType.hasElementType("Annotation") And docType.hasElementType("Article") Then
      docType.addElementToInclusions "Annotation", "Article"
    End If
  End If
End Function

doaddElements()
]]>
</MACRO>
<MACRO name="On_Application_Open" hide="true" lang="VBScript">
<![CDATA[
   Application.Run ("LIBRARY - General Application Functions")
   Application.Run ("LIBRARY - General Application JScript Functions")
   Application.Run ("LIBRARY - Enter Key Functions")
   Application.Run ("LIBRARY - Space Key Functions")
   Application.Run ("LIBRARY - Upload MultiPart-FormData File")
   Application.Run ("LIBRARY - INI Files")
   Call InitializeEnvironment
   Call ConfigureCTBResourceManager
   Call ReadIniFile
]]></MACRO>

<MACRO name="On_Application_Open_Complete" lang="VBScript" hide="true"><![CDATA[


	ResourceManager.Visible = True
	Call AddCTBToolbarItems
        Application.Run ("Change Comment menu")

]]></MACRO> 
</MACROS>


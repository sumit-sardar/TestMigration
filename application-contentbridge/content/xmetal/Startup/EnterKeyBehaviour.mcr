<?xml version="1.0"?>
<!DOCTYPE MACROS SYSTEM "macros.dtd">
<MACROS>
<MACRO name="LIBRARY - Enter Key Functions" hide="true" lang="VBScript">
<!--
' ************************************************************
' DESCRIPTION
' This macro is a library macro.  It contains functions for
' key binding (Enter Key).
'
' HISTORY
' 1. 20010921 SoftQuad: Roehl Sioson
'    Official Release
' ************************************************************
-->
<![CDATA[

' ************************************************************
' Sub EnterKeyBehaviour()
' ************************************************************
' DESCRIPTION
' Determines what happens when the user types the Enter key.
' 
' PARAMETERS
' None
'
' RETURN VALUE
' None
'
' NOTES
' 1. Pre-release: "Read-Only Selection Handler"
'    This function is not designed to work on a read-only
'    selection.  The handler will fail with an alert.
'
' 2. Pre-release: "Plain Text Handler"
'    This function will insert a linebreak when in Plain
'    Text mode.
'
' 3. Pre-release: "In Document Body Handler"
'    This function is not designed to work outside of the
'    document body.  The handler will fail with an alert.
'
' HISTORY
' 1. 20010921 SoftQuad: Roehl Sioson
'    Official Release
'
' 2. 20011101 SoftQuad: Roehl Sioson
'    Had to re-organize the handler sequence: the Plain Text
'    handler should be firing second, after the read-only
'    handler is triggered.
'
' 3. 20020424 Corel: Yas Etessam
'    DTD modification: Update element names
' ************************************************************

Sub EnterKeyBehaviour()

	On Error Resume Next

	Dim strFunctionName
	strFunctionName = "EnterKeyBehaviour()"

	If ( ActiveDocument.ViewType = sqViewPlainText ) Then
		Selection.TypeText( vbCRLF )
		Exit Sub
	End If

	If ( Selection.ReadOnly = True ) Then
           Application.Alert (strFunctionName & "  This portion of the document is read-only.")
		Exit Sub
	End If

	If ( Selection.IsParentElement( ".MARKSEC" ) ) Then
		Selection.TypeText( vbCRLF & vbCRLF )
		Exit Sub
	End If

	If ( Selection.CanInsertText ) Then
'		If ( Selection.IsParentElement( "Comment" ) ) Then
'			Selection.TypeText( vbCRLF & vbCRLF )
'			Exit Sub
'		End If
                If ( Selection.IsParentElement ("DistractorRationale")) Then 
		   Call Selection.InsertElement( "BR" )
                   Selection.SelectAfterContainer
		   Exit Sub
                ElseIf ( Selection.IsParentElement( "SelectedResponse" ) ) Then
		   Do While ( Selection.ContainerName <> "SelectedResponse" )
		      Selection.SelectElement
		   Loop
                   Selection.Collapse(sqCollapseEnd)
                   Selection.InsertElement("AnswerChoice")
                   Selection.Select()
                   Selection.PasteString ("<?xm-replace_text {Add AnswerChoice text here.}?>")
		   Exit Sub
		End If
'		If ( IsRangeInfluencedByTag( Selection, "Formatting", "Type", "FixedText" ) ) Then
'			Selection.TypeText( vbCRLF & vbCRLF )
'			Exit Sub
'		End If
'		If ( Selection.IsParentElement( "ListItem" ) ) Then
'			While ( Selection.ContainerName <> "ListItem" )
'				Selection.SplitContainer
'				Selection.MoveLeft	
'			Wend
'			Selection.SplitContainer
'			Exit Sub
'		End If
		Call Selection.InsertElement( "BR" )
                Selection.SelectAfterContainer
	End If

	' Default error trap.

	If ( Err.Number <> 0 ) Then
           Application.Alert ("Error...")
	End If

End Sub

]]>
</MACRO>

<MACRO name="_Enter Key Behaviour" hide="false" key="Enter" lang="VBScript">
<!--
' ************************************************************
' DESCRIPTION
' Determines what happens when the user types the Enter key.
'
' HISTORY
' 1. 20010921 SoftQuad: Roehl Sioson
'    Official Release
' ************************************************************
-->
<![CDATA[ 

Call EnterKeyBehaviour

]]>
</MACRO> 
</MACROS>

<?xml version="1.0"?>
<!DOCTYPE MACROS SYSTEM "macros.dtd">
<MACROS>
<MACRO name="LIBRARY - Space Key Functions" hide="true" lang="JScript">
<!--
// ************************************************************
// DESCRIPTION
// This macro is a library macro.  It contains functions for
// key binding (Space Key).
//
// HISTORY
// 1. 20030220 Corel: David Ngo
//    Official Release
// ************************************************************
-->
<![CDATA[

// ************************************************************
// function SpaceKey()
// ************************************************************
// DESCRIPTION
// Determines what happens when the user types the Space key.
// 
// PARAMETERS
// None
//
// RETURN VALUE
// None
//
// NOTES
// 1. Pre-release: "Read-Only Selection Handler"
//    This function is not designed to work on a read-only
//    selection.  The handler will fail with an alert.
//
// 2. Pre-release: "Plain Text Handler"
//    This function will insert a linebreak when in Plain
//    Text mode.
//
// HISTORY
// ************************************************************

function spacekey () {
   if (ActiveDocument.ViewType == sqViewPlainText) {
      Selection.TypeText (" ");
   } else if (Selection.ReadOnly == true ) {
      return;
   } else if (Selection.IsParentElement(".MARKSEC" )) {
      Selection.TypeText (" ");
   } else {
      if (Selection.CanInsertText) {
         var rng = ActiveDocument.Range;
         rng.MoveLeft (sqExtend);
         var strRng = rng.Text;
         strRng = strRng.replace (/\s+/g, " "); 
         if ((strRng == " ") || (strRng == "&nbsp;") || (strRng == " &nbsp;") ||(rng.ContainerNode.NodeName != Selection.ContainerNode.NodeName)) {
            Selection.InsertNBSP();
         } else {
            rng = ActiveDocument.Range;
            rng.MoveRight (SqExtend);
            strRng = rng.Text;
            strRng = strRng.replace (/\s+/g, " "); 
            if ((strRng == " ") || (strRng == "&nbsp;") || (strRng == " &nbsp;")) {
               Selection.InsertNBSP();
            } else {
               Selection.TypeText (" ");
               Selection.Select();
            }
         }
      }
   }
}
function convertSpacesIntoNBSP() {
   var elemList = ActiveDocument.getElementsByTagName ("Item");
   var rng = ActiveDocument.Range;
   var strText = "";
   var ans = 0;
   for (var i=0; i< elemList.length; i++) {
      rng.SelectNodeContents (elemList.item(i));
      strText = rng.Text;
      if (/  /.test(strText)) {
         strText = strText.replace (/(  )( *)/g, " &nbsp;$2");
         strText = strText.replace (/  /g, "&nbsp;&nbsp;");
         strText = strText.replace (/\&nbsp; /g, "&nbsp;&nbsp;");
         if (rng.CanPaste(strText)) {
            rng.PasteString(strText);
         }
      }
   }
   var root=ActiveDocument.documentElement;
   if (root) ProcessPermissions(ActiveDocument, root);
}
]]>
</MACRO>

<MACRO name="_Space Key Behaviour" hide="false" key="Space" lang="JScript">
<![CDATA[
function doSpaceKey () {
   spacekey();
}
doSpaceKey ();
]]>
</MACRO>
</MACROS>

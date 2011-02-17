<?xml version="1.0"?>
<!-- Copyright 2001-2003, Corel Corporation -->

<!DOCTYPE MACROS SYSTEM "macros.dtd">
<MACROS> 

<!-- 	Ari Susal, Corel Corp.
	Modifications 9/13/2002:
		(1) Turned Plain Text View on again;
		(2) Turned on FindInsertLocation for drops to active docs.
	Modification 11/01/02:
		(1) Turned OFF Plain Text View for final Phase 1 delivery.
20030124: David Ngo (Corel).
   Add Drag   Drop for Flash   EPS Images (*.swf|*.eps).
20030130: David Ngo (Corel).
   Change gSaveToServer = true, this will enable the "Save To TexmXML..." for
   file menu.
20030204: David Ngo
   Restore gSaveToServer = false, change this flag does not seems to make a different.
-->


<MACRO name="On_Macro_File_Load" lang="JScript" id=""><![CDATA[
//globals
var gDisableSourceView = true;
var gCloseCancelled = false;
var gSaveToServer = false;


function G_OnViewChange()
{
  // Warn user of Plain Text view-mode support issue for Interwoven Integration...
  var curDoc = ActiveDocument;
  if (curDoc && (curDoc.ViewType == sqViewPlainText) && (getAreaVPath(curDoc.FullName) != null))
  {
    Application.Alert("Switching to Plain Text View is not supported by the Interwoven integration.\n"
                    + "To prevent integration problems, please Undo this operation\n"
                    + "through menu command Edit Undo.", "Interwoven to XMetaL Interface Tool");
  }
  if (curDoc && (curDoc.ViewType == sqViewNormal || curDoc.ViewType == sqViewTagsOn))
  {
    if (IsInterwoven(curDoc))
    {
      var root=curDoc.documentElement;
      if (root) ProcessPermissions(curDoc, root);
    }
  }
}

function CreateDocCachePath()
{
    var cwXMTS=cwXMTSConnect();
    var appDataPath=cwXMTS.getAppDataPath();
    cwXMTS=null;

    var fso=new ActiveXObject("Scripting.FileSystemObject");
    if( !fso.FolderExists(appDataPath) ) 
		fso.CreateFolder(appDataPath);
    if( !fso.FolderExists(appDataPath + "\\Corel") ) 
		fso.CreateFolder(appDataPath + "\\Corel");
    if( !fso.FolderExists(appDataPath + "\\Corel\\XMTeamXML") ) 
		fso.CreateFolder(appDataPath+ "\\Corel\\XMTeamXML");
    if( !fso.FolderExists(appDataPath + "\\Corel\\XMTeamXML\\cTemp") ) 
		fso.CreateFolder(appDataPath+ "\\Corel\\XMTeamXML\\cTemp");
    fso=null;
}

function GetDocCachePath()
{
  var cwXMTS=cwXMTSConnect();
  var docCachePath=cwXMTS.getAppDataPath() + "\\Corel\\XMTeamXML\\cTemp"
  cwXMTS=null;
  return docCachePath;
}


function RemoveIWTS(filename)
{
	if( !Application.FileExists(filename) ) return -1;
	var xml=Application.FileToString(filename);
    var atts=xml.match(/areaVPath=\"([^\"]*)\".*\?>/);
    if( atts == null ) return -1;
	var areapath=atts[1];

	var refre=/(iwts:[^\"]*)/i;
	var refs;
	while( (refs=refre.exec(xml)) != null )
	{
		var ref1=refs[0];
		var newref=ref1.substr(ref1.lastIndexOf("/")+1);
		xml=xml.replace(refre,newref);
	}
    var fso=new ActiveXObject("Scripting.FileSystemObject");
	var reu=/encoding=\"UTF-16\"/i;
	var inUnicode=reu.test(xml);
    var os=fso.OpenTextFile(filename,2,true,inUnicode);
    os.write(xml);
    os.close();
    fso=null; os=null;
}


function SetReadOnly(node, bValue)
// bValue is true or false
// returns the current readonly setting of node
{
  var rng = ActiveDocument.Range;
  rng.SelectNodeContents(node);
  var bRet = rng.ReadOnlyContainer;
  rng.ReadOnlyContainer = bValue;
  return bRet;
}

function cwXMTSConnect() {
  // Connect to cwXMTS
  try {
    var cwXMTS = ResourceManager.ControlInTab("TeamXML");
  }
  catch(exception) {
    Application.Alert("cwXMTS error: "+ exception.description, "Interwoven to XMetaL Interface Tool");
    cwXMTS = null;
  }
  return cwXMTS;
}

function newFileSystemObject() {

  try {
    var fso = new ActiveXObject("Scripting.FileSystemObject");
  }
  catch(exception) {
    Application.Alert("Script Error: " + exception.description + "\nFailed to invoke Scripting.FileSystemObject\nYou need to get Windows Scripting Host from the Microsoft site.", "Interwoven to XMetaL Interface Tool");
    fso = null;
    return null;
  }
  
  return fso;
}

function PutAttr(doc, tagname, attrname, value) {
  var root = doc.documentElement;
  if (root) {
    var child = root.firstChild;
    while (child && (child.nodeName != tagname)) {
      child = child.nextSibling;
    }
    if (!child) {
      // Create the new PI node
      var newPI = doc.createProcessingInstruction(tagname, "");
      var firstChild = root.firstChild;
      var ro = SetReadOnly(root, false);
      root.insertBefore(newPI, firstChild);
      child = newPI;
      SetReadOnly(root, ro);
    }
    if (child) {
      var newAttr = attrname + "=\"" + value + "\" ";
      var str = child.nodeValue;
      var attrstr = attrname + "=\"";
      var start = str.indexOf(attrstr);
      if (start == -1) {
        var ro = SetReadOnly(root, false);
        child.nodeValue = newAttr + str;
        SetReadOnly(root, ro);
        return true;
      }
      else {
        var removestr = str.substr(start);
        var end = removestr.indexOf("\" ", attrstr.length);
        removestr = removestr.substring(0, end + 2);
        var newstr = str.replace(removestr, newAttr);
        var ro = SetReadOnly(root, false);
        child.nodeValue = newstr;
        SetReadOnly(root, ro);
        return true;
      }
    }
  }
  
  return false;
}

function RemoveAttr(doc, tagname, attrname) {
  var root = doc.documentElement;
  if (root) {
    var child = root.firstChild;
    while (child && (child.nodeName != tagname)) {
      child = child.nextSibling;
    }
    if (child) {
      var str = child.nodeValue;
      var attrstr = attrname + "=\"";
      var start = str.indexOf(attrstr);
      if (start > -1) {
        var removestr = str.substr(start);
        var end = removestr.indexOf("\"", attrstr.length);
        removestr = removestr.substring(0, end + 1);
        var newstr = str.replace(removestr, "");
        var ro = SetReadOnly(root, false);
        child.nodeValue = newstr;
        newstr = newstr.replace(/(^\s+)|(\s+$)/g, "");
        if (newstr == "") // after removing last value, remove processing instruction
        {
          root.removeChild(child);
        }
        SetReadOnly(root, ro);

      }
      return true;
    }
  }
  return false;
}
        
function GetNodeAttrValue(node, attrname) {
  var str = node.nodeValue;
  var attrstr = attrname + "=\"";
  var start = str.indexOf(attrstr);
  if (start > -1) {
    start = start + attrstr.length;
    str = str.substr(start);
    var end = str.indexOf("\"");
    str = str.substring(0, end);
    return str;
  }
  return null;
}  

function GetAttrValue(doc, tagname, attrname) {
  var root = doc.documentElement;
  if (root) {
    var child = root.firstChild;
    while (child && (child.nodeName != tagname)) {
      child = child.nextSibling;
    }
    if (child) {
      var str = GetNodeAttrValue(child, attrname);
      return str;
    }
  }
  return null;
}

function UpdateToolBar()
{
    var cwXMTS=null;
    try{ cwXMTS=ResourceManager.ControlInTab("TeamXML"); } catch(e){}
    if( (Application.ActiveDocument == null) || (cwXMTS == null) || !cwXMTS.IsLoggedIn ) 
    {
        Application.DisableMacro("Interwoven_Save");
        Application.DisableMacro("Interwoven_Save_As");
        DisableToolBar();
    }
	else if( getAreaVPath(Application.ActiveDocument.FullName) != null )
    {
        EnableToolBar();
    }
    else
    {
//        Application.DisableMacro("Interwoven_Save");
        DisableToolBar();
    }
    cwXMTS=null;
}

function getAreaVPath(filepath)
{
    vpath=null;
    try
    {
        if( Application.FileExists(filepath) )
        {
            var xml=Application.FileToString(filepath);
            var atts=xml.match(/areaVPath=\"([^\"]*)\".*\?>/);
            if( atts != null ) vpath=atts[1];
        }
    }
    catch(e){}
    return vpath;
}


function IsInterwoven(doc) {
  var isiwts=false;
  try
  {
	var filepath=doc.FullName;
	if( filepath > "" )
    {
		var xml=Application.FileToString(filepath);
		var atts=xml.match(/areaVPath=\"([^\"]*)\".*\?>/);
		if( atts != null ) isiwts=true;
    }
  }
  catch(e){}
  return isiwts;
}

function findDoc(fullname) {
  var doc;
  var count = Application.Documents.Count;
  // Iterate through Documents collection to find file
  var found = false;
  var x = 1;
  while (!found && (x <= count)) {
    doc = Application.Documents.item(x);
    if (doc.FullName == fullname) {
      found = true;
    }
    x++;
  }

  if (!found) {
    return null;
  }
  
  return doc;
}

function getUniqueFileName(folder)
{

  var locFileName = Application.UniqueFileName(folder, "IW", "xml");

  return locFileName;
}

function getUniqueFolder()
{

  var fso = newFileSystemObject();
  var locFolderName = GetDocCachePath() + "\\IW" + fso.GetTempName();
  try {
    var locFolder = fso.CreateFolder(locFolderName);
  }
  catch (exception) {
    Application.Alert("getUniqueFolder Error: " + exception.description + "\nFailed to create " + locFolderName, "Interwoven to XMetaL Interface Tool");
    fso = null;
    return null;
  }
//  addFolderToList(locFolderName);
  fso = null;
  return locFolderName;
}

function addFolderToList(folderName)
{
    // Add this folder to the list of folders to delete when XMetaL exits.
    var curAppProps = Application.CustomProperties;
    var foldersProp = curAppProps.item("IW_DeleteFolders");
    var folders = folderName;
    if (foldersProp) {
      folders += "," + foldersProp.value ;
      foldersProp.Delete();
    }
    curAppProps.Add("IW_DeleteFolders", folders);
}

function GetComponentNodes(doc, nd, lstComponentNodes) {
  // Read the DOM tree
  // If this is a DOMProcessingInstruction node, check if for component node
  if (nd.nodeType==7) {
    
    // iw_document, iw_component, are for component nodes
    var name = nd.nodeName;
    if (name == "iw_document" || name == "iw_component")
    {
      var componentNode = nd.parentNode;
      var newIndex = lstComponentNodes.length;
      lstComponentNodes[newIndex] = componentNode;
    }
    
  }
  
  // Process this node's children
  if (nd.hasChildNodes()) {
    var child = nd.firstChild;
    lstComponentNodes = GetComponentNodes(doc, child, lstComponentNodes);
  }
  
  // Continue with this node's siblings
  var sibling = nd.nextSibling;
  if (sibling) {
    lstComponentNodes = GetComponentNodes(doc, sibling, lstComponentNodes);
  }
  
  return lstComponentNodes;
}

function ProcessPermissions(doc, nd) {
  // Read the DOM tree
  // If this is a DOMProcessingInstruction node, check read-only permission
  if (nd.nodeType==7) {
    var rng = doc.Range;
    
    // Nobody should see iw_document, iw_component, and interwoven PIs
    var name = nd.nodeName;
    if (name == "interwoven" || name == "iw_document" || name == "iw_component")
    {
      rng.SelectNodeContents(nd);
	  if (rng.HiddenContainer != true)
		rng.HiddenContainer = true;
    }
    // Nobody should modify iw_reference
    else if (name == "iw_reference")
    {
      rng.SelectNodeContents(nd);
      rng.ReadOnlyContainer = true;
    }
    
    var perm = GetNodeAttrValue(nd, "perm");
    if (perm) {
      rng.SelectBeforeNode(nd);
      // If there is no "w" then it is read-only
      if (perm.search(/w/i) < 0) {
        rng.ReadOnlyContainer = true;
      }
      if (perm.search(/r/i) < 0) {
        // Collapse tags
        rng.CollapsedContainerTags = true;
      }
    }
  }
  
  // Process this node's children
  if (nd.hasChildNodes()) {
    var child = nd.firstChild;
    ProcessPermissions(doc, child);
  }
  
  // Continue with this node's siblings
  var sibling = nd.nextSibling;
  if (sibling) {
    ProcessPermissions(doc, sibling);
  }
}

function InitializeClosure(doc)
{
  gSaveToServer = false; // if nothing has changed then no point uploading to server
  var saveWish = 0; 
  {
    if (!doc.Saved) doc.Save();
   
    // has document changed since the last time it was uploaded to the server?
    var changed = GetAttrValue(doc, "interwoven", "changed");
    if (changed == "true")
    {
        saveWish = Application.MessageBox("Would you like to save " + doc.Title + " on the server?\nIt will be deleted locally when XMetaL closes.", 32+3, "Interwoven to XMetaL Interface Tool");
        if (saveWish == 2) return false; // Cancel
        else if (saveWish == 6) gSaveToServer = true;  // Yes 
        else if (saveWish == 7) gSaveToServer = false; // No
    }
  }
  return true;
}   

function PrepareForClosing(doc)
{
  // By the time we get here we know we have an interwoven document.
    if (gSaveToServer)
    {
      // Make sure this document has already been saved to Interwoven
      if (!SaveToServer(doc)) return false;
    }

  // get the Interwoven filename
  var fileVPath = GetAttrValue(doc, "interwoven", "fileVPath");
  if (fileVPath)
  {

    // Delete document's property from application properties
    var fileVPathpropName = "fileVPath_" + fileVPath;
    var curAppProps = Application.CustomProperties;
    var curProp = curAppProps.item(fileVPathpropName);
    if (curProp)
    {
      // Delete property
      curProp.Delete();
    }
  }
  ActiveDocument.Close(sqDoNotSaveChanges);
  return true;

}

function MarkComponentsUnchanged(doc)
{
  // See if this is an Interwoven document
  var fileVPath = GetAttrValue(doc, "interwoven", "fileVPath");
  if (fileVPath == null) return;

  // Sanity check
  var root = doc.documentElement;
  if (root == null) return;

  // Get the list of component nodes
  var lstComponentNodes = new Array();
  lstComponentNodes = GetComponentNodes(ActiveDocument, root, lstComponentNodes);

  for (var iNode = 0; iNode < lstComponentNodes.length; iNode++)
  {
    var nd = lstComponentNodes[iNode];

    // Find the iw_component or iw_document PI
    var child = nd.firstChild;
    while (child && child.nodeName != "iw_component" && child.nodeName != "iw_document")
      child = child.nextSibling;
      
    // Sanity check: No PI found for this component node
    if (!child) break;
    
    // Can't and shouldn't update anything if the node is read-only
    var rng = ActiveDocument.Range;
    rng.SelectBeforeNode(child);
    if (rng.ReadOnly == true) break;
    
    var changedAttr = "changed=\"false\"";
    var str = child.nodeValue;
    var attrstr = "changed=\"";
    var start = str.indexOf(attrstr);
    if (start == -1) {
      child.nodeValue = str + " " + changedAttr;
    }
    else {
      var removestr = str.substr(start);
      var end = removestr.indexOf("\"", attrstr.length);
      removestr = removestr.substring(0, end + 1);
      var newstr = str.replace(removestr, changedAttr);
      child.nodeValue = newstr;
    }
  }
}

function SaveNewToServer(doc)
{
  if (doc.ViewType != sqViewNormal && doc.ViewType != sqViewTagsOn)
    doc.ViewType = sqViewNormal;

  var retValue = true;

  // get the Interwoven directory if already chosen
  var areaVPath=GetAttrValue(doc, "interwoven", "areaVPath");
  var dirID=(areaVPath == null)? "":areaVPath + "/";
  var newdoctype = GetAttrValue(doc, "interwoven", "fileKind");
  if( newdoctype == null ) newdoctype="";

  // Take out the PI attributes
    if (!RemoveAttr(doc, "interwoven", "fileKind")) {
//      Application.Alert("Failed removing fileKind attribute.", "Interwoven to XMetaL Interface Tool");
    }

    if (!RemoveAttr(doc, "interwoven", "areaVPath")) {
//      Application.Alert("Failed removing areaPath attribute.", "Interwoven to XMetaL Interface Tool");
    }

  if (!RemoveAttr(doc, "interwoven", "new")) {
//    Application.Alert("Failed removing new attribute.", "Interwoven to XMetaL Interface Tool");
  }
  if (!RemoveAttr(doc, "interwoven", "changed")) {
    // Brand new files that have never been saved locally don't have a changed attribute.
    // Application.Alert("Failed removing changed attribute.", "Interwoven to XMetaL Interface Tool");
  }
  // Get rid of the property so that "changed" attr isn't put back in document upon saving.
  var curDocProps = doc.CustomDocumentProperties;
  var changedProp = curDocProps.item("setChangedFlag");
  if (changedProp) {
    changedProp.Delete();
  }
  doc.Save();  // So that our inserted PIs aren't sent to the server
// David Ngo 20030409 - Started
// Need to exit when user's canceling save dialog.
   if( !ActiveDocument.Saved ) {
       ActiveDocument.Saved = false;
       return;
   }
// David Ngo 20030409 - Ended

  // Save the old information for later
  var oldPath = doc.Path;
  var oldFullName = doc.FullName;
  
  // Connect to cwXMTS
  var cwXMTS = cwXMTSConnect(); 
  if (cwXMTS == null) return false;        

  // Save on the server
  try {
    var fileVPath = cwXMTS.saveNewFile(doc.FullName, dirID, newdoctype);
  }
  catch(exception) {
    Application.Alert("Failed saveNewFile: " + exception.description, "Interwoven to XMetaL Interface Tool");
    retValue = false;
  }
  cwXMTS = null;
   
  var newdoc = doc;
  if (retValue && fileVPath != "")
  {
	doc.Close(sqDoNotSaveChanges);
	return true;
  }

  if (fileVPath == "") {
    // saveNewFile dialog cancelled or error
	// Application.Alert("SaveNewFile error: " + cwXMTS.XErrInfo, "Interwoven to XMetaL Interface Tool");
    retValue = false;
  }

  if (!retValue) // failure saving
  {
    // put back the fileKind and new attributes
    if (newdoctype != "") {
		PutAttr(newdoc, "interwoven", "fileKind", newdoctype);
    }
	if (areaVPath != null) {
		PutAttr(newdoc, "interwoven", "areaVPath", areaVPath);
	}
  }

  // Make sure changed attributes indicates that the document hasn't been modified yet.
  var curDocProps = newdoc.CustomDocumentProperties;
  if (!retValue)
	curDocProps.Add("setChangedFlag", "true"); // error saving so still keep track as changed.
  else
  {
    curDocProps.Add("setChangedFlag", "false");
    MarkComponentsUnchanged(newdoc);
  }
  newdoc.Save();
  curDocProps.Add("setChangedFlag", "true");
  newdoc.Saved = true;

  var root=newdoc.documentElement;
  if (root) ProcessPermissions(newdoc, root);
  newdoc.ClearAllChangedStates();
 
  return retValue;
}

function SaveToServer(doc)
{
  if (doc.ViewType != sqViewNormal && doc.ViewType != sqViewTagsOn)
    doc.ViewType = sqViewNormal;

  var retValue = true;
  
  // get the Interwoven filename
  var fileVPath = GetAttrValue(doc, "interwoven", "fileVPath");
  if (fileVPath) {

    // This will update all the component changed attributes
    //doc.Save();
    
    // Connect to cwXMTS
    var cwXMTS = cwXMTSConnect();
    if (cwXMTS == null) return false;        

    // Take out the PI attributes
    if (!RemoveAttr(doc, "interwoven", "fileVPath")) {
//      Application.Alert("Failed to remove fileVPath.", "Interwoven to XMetaL Interface Tool");
    }
    if (!RemoveAttr(doc, "interwoven", "changed")) {
//      Application.Alert("Failed to remove changed attribute.", "Interwoven to XMetaL Interface Tool");
    }
    // Get rid of the property so that "changed" attr isn't put back in document upon saving.
    var curDocProps = doc.CustomDocumentProperties;
    var changedProp = curDocProps.item("setChangedFlag");
    if (changedProp) {
      changedProp.Delete();
    }

    // existing catalog on the server
    doc.Save();  // So that our inserted PIs aren't sent to the server
	var ret=-1;
	if( doc.Saved )
	{
		try {
		  var rng = ActiveDocument.Range;
		  rng.SelectAll();
		  ret = cwXMTS.SaveFile(doc.FullName, fileVPath);
		}
		catch(exception) {
		  Application.Alert("Failed SaveFile: " + exception.description, "Interwoven to XMetaL Interface Tool");
		  retValue = false;
		}
		if (ret == 0)
		{
			doc.Close(sqDoNotSaveChanges);
			return 0;
		}	
		else
		{
			var XErrInfo = cwXMTS.XErrInfo;
			Application.Alert("SaveFile error: " + XErrInfo, "Interwoven to XMetaL Interface Tool");
			retValue = false;
		}
	}
    cwXMTS = null;
    
    // Put attributes back in the document
    if (!PutAttr(doc, "interwoven", "fileVPath", fileVPath)) {
//      Application.Alert("Failed to save fileVPath in document.", "Interwoven to XMetaL Interface Tool");
    }
    
    // Make sure changed attributes indicates that the document hasn't been modified yet.
    curDocProps.Add("setChangedFlag", "true"); // error saving so still keep track as changed.
//    doc.Save();
    doc.ClearAllChangedStates();
  }
  else
  {
    Application.Alert("SaveToServer: fileVPath not found; not a TeamXML file", "Interwoven to XMetaL Interface Tool");
  }

  return retValue;

}

function SaveAllToServerAndClose(bCloseOthers)
{
  // bCloseOthers is true if non-interwoven documents should also be closed.
  // bCloseOthers is false if non-interwoven documents should be left alone.
  var docu;
  var count = Application.Documents.Count;
  // Iterate through Documents collection
  var doc_array = new Array();
  
  // Get all the open documents
  for (var x = 1; x <= count; x++)
  {
    doc_array[x-1] = Application.Documents.item(x);
  }
  
  // Close all the open documents
  for (x = 0; x < count; x++)
  {
    docu = doc_array[x];
    docu.Activate();
    // Must be in tags-on or normal view for next test.
    if (docu.ViewType != sqViewNormal && docu.ViewType != sqViewTagsOn)
      docu.ViewType = sqViewNormal;
    
    // See if this is an Interwoven document
    var bInterwoven = IsInterwoven(docu);
    if (bInterwoven || bCloseOthers)
    {
      Application.Run("File_Close");
    }
    if (gCloseCancelled == true) return false;
  }
  return true;
}

function SaveToServerAs(doc)
{
  var retValue = false;

  // Connect to cwXMTS
  var cwXMTS = cwXMTSConnect();
  if (cwXMTS == null) return false;        
  
  // Save the old information for later
  var oldPath = doc.Path;
  var oldFullName = doc.FullName;
  var oldfileVPath = GetAttrValue(doc, "interwoven", "fileVPath");
  if( oldfileVPath == null ) oldfileVPath="";
  
  // Take out the PI attributes

  if (!RemoveAttr(doc, "interwoven", "fileVPath")) {
//    Application.Alert("Failed removing fileVPath attribute.", "Interwoven to XMetaL Interface Tool");
  }
  if (!RemoveAttr(doc, "interwoven", "changed")) {
//    Application.Alert("Failed removing changed attribute.", "Interwoven to XMetaL Interface Tool");
  }
  // Get rid of the property so that "changed" attr isn't put back in document upon saving.
  var curDocProps = doc.CustomDocumentProperties;
  var changedProp = curDocProps.item("setChangedFlag");
  if (changedProp) {
    changedProp.Delete();
  }

  doc.Save();
  if( doc.Saved )
  {
	  // Save on the server
	  try {
		var fileVPath = cwXMTS.saveNewFile(oldFullName, oldfileVPath, "");
	  }
	  catch(exception) {
		Application.Alert("Failed saveNewFile: " + exception.description, "Interwoven to XMetaL Interface Tool");
	  }
	  if (fileVPath != "")
	  {
		retValue=true;
	  }
  }
  cwXMTS = null;
 
  
  // Check fileVPath for validity
  if (retValue)
  {
	doc.Close(sqDoNotSaveChanges);
	return true;
  }
  else // recover from failure to save
  {
    // Put the fileVPath in the document
    if( oldfileVPath != "" ) PutAttr(doc, "interwoven", "fileVPath", oldfileVPath);

    // Make sure changed attributes indicates that the document hasn't been modified yet.
    var curDocProps = doc.CustomDocumentProperties;
    curDocProps.Add("setChangedFlag", "true"); // error saving so still keep track as changed.
//    doc.Save();
	return false;
  }
}

function CanRunMacros()
{
  if (Application.ActiveDocument && ActiveDocument.ViewType == sqViewPlainText) {
    Application.Alert("Plain Text View is not supported by the Interwoven integration.\nSwitch to Tags On or Normal view and try again.", "Interwoven to XMetaL Interface Tool");
    return false;
  }

  if (Application.ActiveDocument && ActiveDocument.ViewType != sqViewNormal && ActiveDocument.ViewType != sqViewTagsOn) {
    Application.Alert("Use Tags On or Normal view for Interwoven operations.", "Interwoven to XMetaL Interface Tool");
    return false;
  }

  return true;
}

function EnableToolBar()
{
    try
    {
        var iwBar=Application.CommandBars.item("TeamXML");
        var iwBarCtrls=iwBar.Controls;
        iwBarCtrls.item(4).Enabled=true;
        var fileVPath=GetAttrValue(ActiveDocument,"interwoven","fileVPath");
        if( fileVPath == null )
        {
            iwBarCtrls.item(1).Enabled=false;
            iwBarCtrls.item(2).Enabled=false;
            iwBarCtrls.item(3).Enabled=false;
            iwBarCtrls.item(4).Enabled=true;
            iwBarCtrls.item(5).Enabled=false;
        }
        else
        {
            iwBarCtrls.item(1).Enabled=true;
            iwBarCtrls.item(2).Enabled=true;
            iwBarCtrls.item(3).Enabled=true;
            iwBarCtrls.item(4).Enabled=true;
            iwBarCtrls.item(5).Enabled=true;
        }
    }
    catch(e){}
}

function DisableToolBar()
{
	try
	{
		var iwBar=Application.CommandBars.item("TeamXML");
		var iwBarCtrls=iwBar.Controls;
		iwBarCtrls.item(1).Enabled=false;
		iwBarCtrls.item(2).Enabled=false;
		iwBarCtrls.item(3).Enabled=false;
		iwBarCtrls.item(4).Enabled=false;
		iwBarCtrls.item(5).Enabled=false;
	}
	catch(e){}
}
]]></MACRO> 


<MACRO name="On_Application_Open_Complete" key="" lang="JScript" tooltip="" desc=""><![CDATA[
  try 
  {		
		Application.ResourceManager.AddTab("TeamXML", "cwXMTS.TSExplore"); 
		Application.ResourceManager.SelectTab("TeamXML");
		CreateDocCachePath();
		Application.AcceptDropFormat("cwXMTS vpath","IWVPath");
  } 
catch (e)
{
	Application.alert("Unable to load Interwoven TeamXML integration. Please contact your system administrator for help.");
}
  

]]></MACRO> 

<MACRO name="On_Application_Open" key="" lang="JScript" tooltip="" desc=""><![CDATA[
if (gDisableSourceView)
  Application.DisablePlainTextView();

]]></MACRO> 

<MACRO name="On_Application_Close" key="" lang="JScript" tooltip="" desc=""><![CDATA[

function DeleteFolder(locFolderName) {
  var fso = newFileSystemObject();
  if (!fso) return;

  if (!fso.FolderExists(locFolderName)) {
    fso = null;
    return false;
  }
  else {
    try {
      fso.DeleteFolder(locFolderName);
    }
    catch (exception) {
      Application.Alert("FileSystemObject Error: " + exception.description + "\nFailed to delete " + locFolderName, "Interwoven to XMetaL Interface Tool");
      fso = null;
      return false;
    }
  }
  
  return true;
}

function onApplicationClose() {

// Delete all the folders and their files that were used to store the interwoven documents locally.

  var curAppProps = Application.CustomProperties;
  var foldersProp = curAppProps.item("IW_DeleteFolders");
  if (foldersProp) {
    var folders = foldersProp.value;
    var folder = new Array();
    var folder = folders.split(",");
    for(var count = 0; count < folder.length; count++)
    {
      var str = folder[count];
      if (str != "")
      {
        DeleteFolder(str);
      }
    }
  }
  var docCachePath=GetDocCachePath();
  DeleteFolder(docCachePath);
}

//Application.Alert("onApplicationClose");
onApplicationClose();

// Clean up before exiting
fso = null;
curAppProps = null;
foldersProp = null;
folders = null;
folder = null;
count = null;
str = null;

ResourceManager.RemoveAllTabs();

]]></MACRO>
<MACRO name="On_Drop_IWVPath" lang="JScript"><![CDATA[

function stripOffXmlPi(xmlComponentStr)
{
  var regExp = /<\?xml.+\?>/;
  var matches = xmlComponentStr.match(regExp);
  var strippedStr;
  if (matches != null)
    strippedStr = xmlComponentStr.substr(matches.lastIndex);
  else
    strippedStr = xmlComponentStr;
  return strippedStr;
}

function stripOffDOCTYPE(xmlComponentStr)
{
  var regExp = /<!DOCTYPE.+>/;
  var matches = xmlComponentStr.match(regExp);
  var strippedStr;
  if (matches != null)
    strippedStr = xmlComponentStr.substr(matches.lastIndex);
  else
    strippedStr = xmlComponentStr;
  return strippedStr;
}

function parseForTopLevelElementName(xmlComponentStr)
{
  // Return root element name from beginning of component...
  var regExp   = /<([a-zA-Z][^>\s]*)[^>]*>/;
  var matches  = xmlComponentStr.match(regExp);
  if (matches) {
    return matches[1];
  }
  else
    return null;
}

function PasteComponent(Dp,component)
{
//DN 20020124: David Ngo (Corel)
//DN Uncommented FindInsertLocation test below.
//DN    if( !Dp.CanPaste(component) )
//DN	{
//DN//		var elemName=parseForTopLevelElementName(component);
//DN//		if( (elemName == null) || elemName == "" || 
//DN//			(!Dp.FindInsertLocation(elemName,true) && !Dp.FindInsertLocation(elemName,false)) )
//DN//		{
//DN			return false;
//DN//		}
//DN	}
    if( !Dp.CanPaste(component) ) {
       var elemName=parseForTopLevelElementName(component);
       if( (elemName == null) || elemName == "" || (!Dp.FindInsertLocation(elemName,true) && !Dp.FindInsertLocation(elemName,false)) ) {
          return false;
       }
    }
    Dp.PasteString(component);
    Dp.Select();
    return true;
}

// get imgURL attribute name from ctm file.
function GetImageRef(imgElemName)
{
	var attrName="";
	var dtdPath=ActiveDocument.RulesFile;
	var ctmPath=dtdPath.substr(0,dtdPath.lastIndexOf(".")+1) + "ctm";
	try
	{
		var xmlCtm=Application.FileToString(ctmPath);
		xmlCtm=xmlCtm.replace(/ctm\.dtd/,Application.Path + "\\Rules\\ctm.dtd");
		var xmlDoc=new ActiveXObject("MSXML2.DOMDocument");
		xmlDoc.async=false;
		if( !xmlDoc.loadXML(xmlCtm) ) throw xmlDoc.parseError.reason;
		var imgNode=xmlDoc.selectSingleNode("DTDExtensions/Images/Image[./Name/text() = '" + imgElemName + "']");
		if( imgNode != null )
		{
			var srcAttrNode=imgNode.selectSingleNode("Source-Attribute");
			if( srcAttrNode != null ) attrName=srcAttrNode.text;
		}
	}
	catch(e){}
	return attrName;
}

function PasteImage(Dp,imgRef)
{
	var imgElemName=Dp.ContainerName;
	var attrName=GetImageRef(imgElemName);
	if( attrName != "" )
		Dp.ContainerAttribute(attrName)=imgRef;
	else
		Dp.InsertImage(imgRef);
// 20030409 David Ngo - Start
//          Add code to update Graphic ID when drop BMP image.
        if ((matchArray = (imgRef.match (/.*\/(.+)\.(bmp)$/i))) != null) {
           nd = Dp.ContainerNode;
           if (nd.ParentNode) {
              nd = nd.ParentNode;
              if (nd.NodeName == "Graphic") {
                 nd.setAttribute("ID", matchArray[1]);
              }
           }
        }
// 20030409 David Ngo - End
	return true;
}

// 20030123: David Ngo (COREL), Start
// Add this for Drag and Drop Flash & EPS tags for images.
function PasteUnsupportedImage (Dp, imgRef, tagName) {
   var imgElemName=Dp.ContainerName;
   var rng = ActiveDocument.Range;
   var matchArray;
   if (rng.FindInsertLocation(tagName)) {
      rng.InsertElement(tagName);
      rng.Select();
      var nd = rng.ContainerNode;
      if (nd.NodeName == tagName) {
         nd.setAttribute("FileName", imgRef);
      }
      if ((matchArray = (imgRef.match (/.*\/(.+)\.(swf|eps)$/i))) != null) {
         nd = nd.ParentNode;
         if (nd.NodeName == "Graphic") {
            nd.setAttribute("ID", matchArray[1]);
         }
      }
   }
   return true;
}
// 20030123: David Ngo (Corel), End

function onDrop() {
  var dptr = Application.DropDataObject;
  var cwXMTS=ResourceManager.ControlInTab("TeamXML");
  var vpath=cwXMTS.getDropVPath(dptr);
  vpath=vpath.replace(/\0/g,"");
//  Application.Alert("vpath=" + vpath);
  if( vpath == ""  ) 
  {
    Application.Alert(cwXMTS.XErrInfo, "Interwoven to XMetaL Interface Tool");
    return;
  }

  var Dp=Application.DropPoint; 
  if( Dp == null )      // new document window?
  {
    var curAppProps = Application.CustomProperties;
    curAppProps.Add("IW_Open", vpath);
    Application.Run("IW_Open");
    var openProp = curAppProps.item("IW_Open");
    openProp.Delete();
    return;
  }


    // CW: check if file area paths matches for re-use.
    var compAreaPath=cwXMTS.ExtractAreaPath(vpath);
    var isWA=cwXMTS.IsWorkAreaPath(compAreaPath);
    if( !isWA )
    {
        Application.Alert("ERROR: TeamXML does not support re-use of component\n" +
            "files from staging area or editions.", "Interwoven to XMetaL Interface Tool");
        return;
    }
    var docAreaPath="";
    var fileVPath = GetAttrValue(ActiveDocument,"interwoven","fileVPath");
    if( fileVPath != null )
        docAreaPath=cwXMTS.ExtractAreaPath(fileVPath);
    else
    {
        var areaVPath=GetAttrValue(ActiveDocument,"interwoven","areaVPath");
        if( areaVPath != null )
            docAreaPath=areaVPath;
        else
        {
            var cont=Application.MessageBox("WARNING: This is not a TeamXML file.\n" +
                "TeamXML does not support re-use of component\n" + 
                "files in different workareas. Do you want to continue\n" + 
                "and restrict saving this document to \n" + 
                "\"" + compAreaPath.substr(compAreaPath.lastIndexOf("/")+1) + "\" only?", 
                32+4, "Interwoven to XMetaL Interface Tool");
	        if( cont != 6 ) return;
            PutAttr(ActiveDocument,"interwoven","areaVPath",compAreaPath);
            docAreaPath=compAreaPath;
        }
    }
//Application.Alert("docAreaPath=" + docAreaPath + "\ncompAreaPath=" + compAreaPath);
    if( compAreaPath != docAreaPath )
    {
	    Application.Alert("ERROR: TeamXML does not support re-use of\n" + 
		    "component files in different workareas.",
		    "Interwoven to XMetaL Interface Tool");
        return;
    }

    // CW: check if selection exists for paste over-write.
	var rng=ActiveDocument.Range;
    if( rng != null )
	{
		if( rng.Contains(Dp,true) )
			Dp=ActiveDocument.Range;
		else
		{
			rng.MoveRight();
			if( rng.IsEqual(Dp) )
				Dp=ActiveDocument.Range;
			else
			{
				rng=ActiveDocument.Range;
				rng.MoveLeft();
				if( rng.IsEqual(Dp) )
					Dp=ActiveDocument.Range;
			}
		}
	}

    // CW: inserting images? ========================================================================
// 20030124: David Ngo (COREL), Start
// Desc: Replacing this section of code to allow Drag & Drop Flash & EPS images (*.swf | *.eps).
//       Drag & Drop these two images only inserting the tag and its attributes, not the actual
//       images.
//DN    var isImgVPath=/.*\.(gif|jpe?g|bmp)$/i;
//DN    if( isImgVPath.test(vpath) )
//DN    {
//DN		var relvpath=cwXMTS.ExtractAreaRelPath(vpath);
//DN		var imgRef="iwts:/" + relvpath;
//DN		if( !PasteImage(Dp,imgRef) )
//DN		{
//DN			var imgfname=imgRef.substr(imgRef.lastIndexOf("/")+1);
//DN			Application.Alert("Unable to paste image " + imgfname + " in current location.", 
//DN				"Interwoven to XMetaL Interface Tool");
//DN		}
//DN        return;
//DN    }
   var isImgVPath=/.*\.(gif|jpe?g|bmp|swf|eps)$/i;
   if( isImgVPath.test(vpath) ) {
      var relvpath=cwXMTS.ExtractAreaRelPath(vpath);
      var imgRef="iwts:/" + relvpath;
      if ((/.*\.(swf)$/i).test (imgRef) == true) {
         PasteUnsupportedImage (Dp, imgRef, "Flash");
      } else if ((/.*\.(eps)$/i).test (imgRef) == true) {
         PasteUnsupportedImage (Dp, imgRef, "EPSPrint");
      } else {
         if( !PasteImage(Dp,imgRef) ) {
	    var imgfname=imgRef.substr(imgRef.lastIndexOf("/")+1);
            Application.Alert("Unable to paste image " + imgfname + " in current location.", "Interwoven to XMetaL Interface Tool");
         }
      }
      return;
    }
// 20030124: David Ngo (COREL), End
    // CW: inserting XML ============================================================================
    var gmode=5743;	 // read component with descendent choice, with annotations, but no prolog
    var locFolderName = getUniqueFolder();
    if (locFolderName == null) {
      Application.Alert("On_Drop_IWVPath Error: Failure to get unique local folder",
        "Interwoven to XMetaL Interface Tool");
      return;
    }

    var ret = 0;
    try {
    //Application.Alert("vpath=" + vpath +", dirPath=" + locFolderName + ", gmode=" + gmode)
      ret =  cwXMTS.getFile(vpath, locFolderName, gmode);
    }
    catch(exception) {
      Application.Alert("Failed getFile: " + exception.description, "Interwoven to XMetaL Interface Tool");
      cwXMTS = null;
      return;
    }
    if (ret != 0)
    {
      var XErrInfo = cwXMTS.XErrInfo;
      Application.Alert("getFile error: " + XErrInfo, "Interwoven to XMetaL Interface Tool");
      cwXMTS = null;
      return;
    }
    cwXMTS = null;

    // Open the document in XMetaL
    var docFileName=vpath;
    docFileName=docFileName.substr(docFileName.lastIndexOf("/")+1);
    var filename = locFolderName + "\\" + docFileName;

    if (Application.ReadableFileExists(filename)) {
    // This will return all the contents of the 
    // file in string format
      var component= Application.FileToString(filename);
    } 
    else {
      Application.Alert("Unable to read " + filename + ".", "Interwoven to XMetaL Interface Tool");
      return;
    }

	if( !PasteComponent(Dp,component) )
	{
        Application.Alert("Unable to insert component:\n" + component.substr(0,512) + "...", 
			"Interwoven to XMetaL Interface Tool");
		return;
	}
    var root=ActiveDocument.documentElement;
    if( root ) ProcessPermissions(ActiveDocument,root);
}

onDrop();
  
]]></MACRO>

<MACRO name="IW_NewXML" key="" lang="JScript" tooltip="" desc=""><![CDATA[

  function onNewXML() {
    // Find the folder in which to new a document
    var curAppProps = Application.CustomProperties;
    var dirID = curAppProps.item("IW_NewXML");
    if (dirID) {
      var NewdirID = dirID.value;
      curAppProps.Add("IW_NewXML", ""); // return success unless proven otherwise!

      // Use the view that the user is already using because he probably likes it.
      var curDoc = Application.ActiveDocument;
      var viewtype;
      if (curDoc && (curDoc.ViewType == sqViewNormal || curDoc.ViewType == sqViewTagsOn))
        viewtype = curDoc.ViewType;
      else viewtype = sqViewNormal;

      var doc = Documents.OpenTemplate();
      if (doc) {
        doc.ViewType = viewtype;

        var locFolderName = getUniqueFolder();
        if (locFolderName == null) {
          Application.Alert("Failure to get unique local folder", "Interwoven to XMetaL Interface Tool");
          curAppProps.Add("IW_NewXML", "Failure to get unique local folder");
          return;
        }

        var locFilename = getUniqueFileName(locFolderName);
        docTitle = locFilename.substr(locFilename.lastIndexOf("/")+1);

        // Add fileVPath to the custom properties to keep track
        var docID = NewdirID + "/" + docTitle;
        var fileVPathpropName = "fileVPath_" + docID;
        var curProp = curAppProps.item(fileVPathpropName);

        if (curProp) {
          // property defined.  This document already exists and is open!

          var existingDoc = findDoc(curProp.value);

          if (existingDoc) {
            Application.Alert(docTitle + " is already opened.", "Interwoven to XMetaL Interface Tool");
            existingDoc.Activate();
            doc.Close(sqDoNotSaveChanges);
            curAppProps.Add("IW_NewXML", docID + " already exists and is open.");
            return;
          }
        }
        doc.SaveAs(locFilename, false); 
        curAppProps.Add(fileVPathpropName, locFilename);

        // Put the areaVPath in the document
        var cwXMTS=cwXMTSConnect();
        var areaPath=cwXMTS.ExtractAreaPath(NewdirID + "/");
        if (!PutAttr(doc, "interwoven", "areaVPath", areaPath)) {
//          Application.Alert("Failed to save areaVPath in document.", "Interwoven to XMetaL Interface Tool");
          curAppProps.Add("IW_NewXML", "Failed to save areaVPath in document.");
        }
        cwXMTS=null;

        // This document has not been saved to the server so make note...
        if (!PutAttr(doc, "interwoven", "new", "true")) {
//          Application.Alert("Failed to save new attribute in document.", "Interwoven to XMetaL Interface Tool");
          curAppProps.Add("IW_NewXML", "Failed to save new attribute in document.");
        }

        var root=doc.documentElement;
        if (root)
        {
          ProcessPermissions(doc, root);
        }

        // Add changed attribute.
        var curDocProps = doc.CustomDocumentProperties;
        curDocProps.Add("setChangedFlag", "true");
        doc.Save();


      }
      else {
        // Cancelled
        // Application.Alert("Failed to open template.", "Interwoven to XMetaL Interface Tool");
        curAppProps.Add("IW_NewXML", "Failed to open template.");
      }
      
    }
    else {
//      Application.Alert("Failed to receive IW_NewXML dirID property from cwXMTS.", "Interwoven to XMetaL Interface Tool");
      curAppProps.Add("IW_NewXML", "Failed to receive IW_NewXML dirID property from cwXMTS.");
    }
  }

onNewXML();

]]></MACRO>

<MACRO name="IW_Open" key="" lang="JScript" tooltip="" desc=""><![CDATA[

  function onOpen()
  {
    // Find the document to open
    var curAppProps = Application.CustomProperties;
    var fileVPath = curAppProps.item("IW_Open");
    if (fileVPath) {

      // See if this document is already open in XMetaL
      var docID = fileVPath.value;
      curAppProps.Add("IW_Open", ""); // return success unless proven otherwise!
      var fileVPathpropName = "fileVPath_" + docID;
      var curProp = curAppProps.item(fileVPathpropName);

      if (curProp) {
        // property defined.  See if document is really open, then focus on it.
        
        var doc = findDoc(curProp.value);

        if (doc) {
          Application.Alert(docID.substr(docID.lastIndexOf("/")+1) + " is already opened.", 
            "Interwoven to XMetaL Interface Tool");
          doc.Activate();
          curAppProps.Add("IW_Open", docID + " is already opened.");
          return;
        }
      }

      // Connect to cwXMTS and open this fileVPath
      var cwXMTS = cwXMTSConnect();
      if (cwXMTS == null) {
        curAppProps.Add("IW_Open", "Failure to connect to cwXMTS");
        return;
      }

      var locFolderName = getUniqueFolder();
      if (locFolderName == null) {
        curAppProps.Add("IW_Open", "Failure to get unique local folder");
        cwXMTS = null;
        return;
      }
      
      try {
        var ret = cwXMTS.getFile(docID, locFolderName);
      }
      catch(exception) {
        Application.Alert("Failed getFile: " + exception.description, "Interwoven to XMetaL Interface Tool");
        curAppProps.Add("IW_Open", "Failed getFile: " + exception.description);
        cwXMTS = null;
        return;
      }
      if (ret != 0)
      {
        var XErrInfo = cwXMTS.XErrInfo;
        Application.Alert("getFile error: " + XErrInfo, "Interwoven to XMetaL Interface Tool");
        curAppProps.Add("IW_Open", "getFile error: " + XErrInfo);
        cwXMTS = null;
        return;
      }

      var areaVPath=cwXMTS.ExtractAreaPath(docID);
      cwXMTS = null;


      // Open the document in XMetaL
      var docFileName=docID;
      docFileName=docFileName.substr(docFileName.lastIndexOf("/")+1);
      var filename = locFolderName + "\\" + docFileName;

	   // CW: record filevpath using text editing to avoid readability problem.
	   try
	   {
            var xml=Application.FileToString(filename);
            var elems=xml.match(/(<[a-zA-Z][^>]*>)/);
            if( elems != null )
            {
                var re=new RegExp(elems[1]);
                var xmld=xml.replace(re,elems[1] + "<?interwoven fileVPath=\"" + docID + 
                    "\" areaVPath=\"" + areaVPath + "\" ?>");


				var enc=xml.match(/encoding=\"([^\"]+)\"/i);
				if( (enc == null) || (enc[1] == "UTF-8") )
				{
					var sutil=new ActiveXObject("CWUtil.StringUtil");
					sutil.StringToUTF8File(xmld,filename);
				}
				else	// assume in unicode encoding otherwise
				{
					var fso=new ActiveXObject("Scripting.FileSystemObject");
					var os=fso.OpenTextFile(filename,2,true,true);
					os.write(xmld);
					os.close();
					fso=null; os=null;
				}
                xml=null; xmld=null;
            }

	   }
	   catch(e){ Application.Alert("Unable to record fileVPath.\n" + e.description, "Interwoven to XMetaL Interface Tool"); }

       var doc = Documents.Open(filename);
       if (doc) {
        // Add fileVPath to the custom properties to keep track
        curAppProps.Add(fileVPathpropName, filename);

        if (doc.ViewType != sqViewNormal && doc.ViewType != sqViewTagsOn)
        {
          Application.Alert("Failed to open "+docFileName+" in Normal or Tags On View.\nPlain Text View is not supported by the Interwoven integration.", "Interwoven to XMetaL Interface Tool");
          return;
        }

        // Put the fileVPath in the document
//        if (!PutAttr(doc, "interwoven", "fileVPath", docID)) {
//          Application.Alert("Failed to save fileVPath in document.", "Interwoven to XMetaL Interface Tool");
//          curAppProps.Add("IW_Open", "Failed to save fileVPath in document.");
//        }

        var root = doc.documentElement;
        if (root)
        {
          ProcessPermissions(doc, root);
		  Selection.MoveToElement();
        }  


        // Make sure changed attribute indicates that the document hasn't been modified yet.
        var curDocProps = doc.CustomDocumentProperties;
        curDocProps.Add("setChangedFlag", "false");
        MarkComponentsUnchanged(doc);
        doc.Save();
        curDocProps.Add("setChangedFlag", "true");
        doc.Saved = true;


        var curAppProps = Application.CustomProperties;
        var fileVPathpropName = "fileVPath_" + fileVPath;
        curAppProps.Add(fileVPathpropName, doc.FullName);
        doc.ClearAllChangedStates();

      }
      else {
        Application.Alert("Failed to open document "+docFileName, "Interwoven to XMetaL Interface Tool");
        curAppProps.Add("IW_Open", "Failed to open " + filename);
      }
      
    }
    else {
      Application.Alert("Failed to receive IW_Open fileVPath property from cwXMTS.", "Interwoven to XMetaL Interface Tool");
      curAppProps.Add("IW_Open", "Failed to receive IW_Open fileVPath property from cwXMTS.");
    }
  
  }

onOpen();

]]></MACRO> 

<MACRO name="On_Application_Before_Document_Save" key="" lang="JScript" tooltip="" desc=""><![CDATA[
function MarkAsChanged(nd)
{
  // Find the iw_component or iw_document PI
  var child = nd.firstChild;
  while (child && child.nodeName != "iw_component" && child.nodeName != "iw_document")
    child = child.nextSibling;

  // Sanity check: Just return if it is not there.
  if (!child) return;

  // Add changed=true to attributes of PI
  var changedAttr = "changed=\"true\"";
  var str = child.nodeValue;
  
  // Find the changed attribute
  var attrstr = "changed=\"";
  var start = str.indexOf(attrstr);
  
  // If it is not there add changed attribute to end
  if (start == -1) {
    child.nodeValue = str + " " + changedAttr;
  }
  
  // Otherwise replace the changed attribute
  else {
    var removestr = str.substr(start);
    var end = removestr.indexOf("\"", attrstr.length);
    removestr = removestr.substring(0, end + 1);
    var newstr = str.replace(removestr, changedAttr);
    child.nodeValue = newstr;
  }
}

function MarkChangedComponents(doc)
{
  // See if this is an Interwoven document
  var fileVPath = GetAttrValue(doc, "interwoven", "fileVPath");
  if (fileVPath == null) return;

  // Sanity check
  var root = doc.documentElement;
  if (root == null) return;

  // Get the list of changed nodes
  var ChngNodes = doc.ChangedNodes;
  var len = ChngNodes.length;
  var lstChngNodes = new Array();
  var lstIndex = 0;
  for (var iNode = 0; iNode < len; iNode++)
  {
    var nd = ChngNodes.item(iNode);
    if ((nd != null) && (nd.nodeType == 1))
    {
      lstChngNodes[lstIndex++] = nd;
    }
  }

  // Get the list of component nodes
  var lstComponentNodes = new Array();
  lstComponentNodes = GetComponentNodes(ActiveDocument, root, lstComponentNodes);
    
  // Now find the components that the changed element nodes are in and mark them
  len = lstChngNodes.length;
  for (iNode = 0; iNode < len; iNode++)
  {
    var nd = lstChngNodes[iNode];
    // The changed node is a new one or it's content or attributes
    // have been changed.
    // Find the closest ancestor that is in our list of component nodes.
    var foundIt = false;
    var parent = nd;
    while (parent && !foundIt)
    {
      for(var j = 0; j < lstComponentNodes.length; j++)
      {
        if (lstComponentNodes[j] == parent)
        {
          // Found the ancestor.
          // mark as changed.
          MarkAsChanged(parent);
          foundIt = true;
          break;
        }
      }
      parent = parent.parentNode;
    }
  }
  doc.ClearAllChangedStates();
  lstComponentNodes = null;
  lstChngNodes = null;
}
  

function onDocumentSave()
{
  var curDocProps=ActiveDocument.CustomDocumentProperties;
  var curProp = curDocProps.item("setChangedFlag");
  if (curProp) {
    if (ActiveDocument.ViewType == sqViewNormal || ActiveDocument.ViewType == sqViewTagsOn) {
      if (curProp.value == "true") {

        MarkChangedComponents(ActiveDocument);
        PutAttr(ActiveDocument, "interwoven", "changed", "true");
      }
      else if (curProp.value == "false") {
        PutAttr(ActiveDocument, "interwoven", "changed", "false");
      }
    }
  }
}

onDocumentSave();

]]></MACRO> 

<MACRO name="On_Application_Document_Open_Complete" lang="JScript"><![CDATA[

function onApplicationDocumentOpenComplete()
{
  var fileVPath = GetAttrValue(ActiveDocument, "interwoven", "fileVPath");

  if (fileVPath) {
    var curAppProps = Application.CustomProperties;
    var fileVPathpropName = "fileVPath_" + fileVPath;
    curAppProps.Add(fileVPathpropName, ActiveDocument.FullName);
    
  }
  var root=ActiveDocument.documentElement;
  if (root) ProcessPermissions(ActiveDocument, root);
  ActiveDocument.ClearAllChangedStates();
}

if (ActiveDocument.ViewType == sqViewNormal || ActiveDocument.ViewType == sqViewTagsOn)
{
  onApplicationDocumentOpenComplete();
}
    
]]></MACRO>


<MACRO name="On_View_Change" lang="JScript"><![CDATA[
	G_OnViewChange();
]]></MACRO>

<MACRO name="File_New" lang="JScript"><![CDATA[
  Documents.OpenTemplate();
]]></MACRO>

<MACRO name="File_Close" lang="JScript"><![CDATA[
gCloseCancelled = false;
if (ActiveDocument)
{
  if (ActiveDocument.ViewType != sqViewNormal && ActiveDocument.ViewType != sqViewTagsOn)
    ActiveDocument.ViewType = sqViewNormal;

  // interwoven document
  if (IsInterwoven(ActiveDocument)) {
    if (!InitializeClosure(ActiveDocument))
    {
      gCloseCancelled = true;
    }
    else
    {
      if (!PrepareForClosing(ActiveDocument))
      {
        gCloseCancelled = true;
      }
    }
  }
  // some other document
  else {
    ActiveDocument.Close(sqPromptToSaveChanges);
  }
}

]]></MACRO>


<MACRO name="File_CloseAll" key="" lang="JScript" tooltip="" desc=""><![CDATA[
SaveAllToServerAndClose(true);
]]></MACRO> 

<MACRO name="File_Exit" key="" lang="JScript" tooltip="" desc=""><![CDATA[
function onFileExit()
{
  if (!SaveAllToServerAndClose(false))
  {
    var exitWish = Application.MessageBox("You cancelled or errors encountered.\nExit anyway?", 32+4, "Interwoven to XMetaL Interface Tool");
    if (exitWish == 7) {  // Don't exit.
      return;
    }
    Application.Alert("Folders containing unsaved documents are in\n"+Application.Path+"\\document folder.", "Interwoven to XMetaL Interface Tool");
  }
  Application.Quit(sqPromptToSaveChanges);
}

//Application.Alert("File_Exit");
onFileExit();

]]></MACRO> 

<MACRO name="On_Default_CommandBars_Complete" key="" lang="JScript"><![CDATA[


  function findFileMenuControls()
  {
    // Get menu bar and add item to file menu...
	var fileMenuId=-2147483647;
	var fileMenu=Application.CommandBars.FindControl(sqcbcTypePopup,fileMenuId);
    if (fileMenu == null) {
      Application.Alert("Unable to find File Menu!", "Interwoven to XMetaL Interface Tool");
      return null;
    }
    return fileMenu.Controls;
  }
  
  ///////////////////////////////////////////////////////////////////////////////////

  function findHelpMenuControls()
  {
    // Get menu bar and add item to Help menu...
	var helpMenuId=-2147483639;   // Id for XM prior to XM4.0.
    try {
      var exeName=Application.ExeName.toLowerCase();
      var vidx=exeName.lastIndexOf("xmetal");
      if (vidx >= 0) {
	 var ver=exeName.substr(vidx+6,1);
	 if (ver > 3) {
	   helpMenuId=-2147483640; // Id for XM for XM4.0 and up(?).
	 }
      }
     } catch(e) {
	// we'll assume prior to XM 4.0
    }

	var helpMenu=Application.CommandBars.FindControl(sqcbcTypePopup,helpMenuId);
    if (helpMenu == null) {
      Application.Alert("Unable to find Help Menu!", "Interwoven to XMetaL Interface Tool");
      return null;
    }
    return helpMenu.Controls;  
  }
    
  ///////////////////////////////////////////////////////////////////////////////////

  function areInterwovenMenuItemsPresent()
  {
    var fileCtls = findFileMenuControls();
    if (fileCtls == null) {
      return false;
    }
    var fileCtl;
    for (var i = 1; i <= fileCtls.Count; i++) {
      fileCtl = fileCtls.Item(i);
      if (fileCtl.OnAction.lastIndexOf("Interwoven_") != -1) {
        return true;
      }
    }
    return false;
  }
  
  ///////////////////////////////////////////////////////////////////////////////////

  function areInterwovenHelpMenuItemsPresent()
  {
    var helpCtls = findHelpMenuControls();
    if (helpCtls == null) {
      return false;
    }
    var helpCtl;
    for (var i = 1; i <= helpCtls.Count; i++) {
      helpCtl = helpCtls.Item(i);
      if (helpCtl.OnAction.lastIndexOf("Interwoven_") != -1) {
        return true;
      }
    }
    return false;
  }
     
  ///////////////////////////////////////////////////////////////////////////////////

  function Interwoven_AddMenuItems()
  {
    // Insert Interwoven menuitems after "Save All" menuitem...
    var fileCtls = findFileMenuControls();
    if (fileCtls == null) {
      return -1;
    }
    var fileCtl  = null;
    var insertIx = -1;
	var saveAllId=33012;
    for (var i = 1; i <= fileCtls.Count; i++) {
      fileCtl = fileCtls.Item(i);
      if (fileCtl.Id == saveAllId) {
        insertIx = i + 1;
		break;
      }
    }
    if (insertIx < 0) {
      insertIx = 1;
    }
    
    // Interwoven File menu items:
    //    save to TeamXML...
    //    save to TeamXML as...
    //    
    // Add new "save to TeamXML..." menuitem and set properties...
    var saveCtl = fileCtls.Add(sqcbcTypePopup, -1, insertIx, "interwoven-save");
    if (saveCtl != null) {
      saveCtl.Caption    = "Save to TeamXML...";
      saveCtl.DescriptionText = "Save document to Interwoven server with existing name";
      saveCtl.BeginGroup = true;
      saveCtl.OnAction   = "Interwoven_Save";
      saveCtl.FaceId     = Application.MakeFaceId("Integration (Custom)", 7, 6);
      saveCtl.Enabled=false;
    } else {
      Application.Alert("Unable to create Interwoven_Save MENUITEM control!", "Interwoven to XMetaL Interface Tool");
      return -1;
    }
    ++insertIx;

    saveCtl = fileCtls.Add(sqcbcTypePopup, -1, insertIx, "interwoven-save-as");
    if (saveCtl != null) {
      saveCtl.Caption    = "Save to TeamXML as...";
      saveCtl.DescriptionText = "Save document to Interwoven server with new name";
      saveCtl.OnAction   = "Interwoven_Save_As";
      saveCtl.FaceId     = 0;
    } else {
      Application.Alert("Unable to create Interwoven_Save_As MENUITEM control!", "Interwoven to XMetaL Interface Tool");
      return -1;
    }
    ++insertIx;

    // Return last menuitem insertion position...
    return insertIx;
  }
  
  /////////////////////////////////////////////////////////////////////////////////// 
    
  function Interwoven_AddHelpMenuItems()
  {
    // Insert Interwoven menuitems after "About" menuitem...
    var helpCtls = findHelpMenuControls();
    if (helpCtls == null) {
      return -1;
    }
    var helpCtl  = null;
    var insertIx = -1;
	var aboutId=57664;
    for (var i = 1; i <= helpCtls.Count; i++) {
      helpCtl = helpCtls.Item(i);
      if (helpCtl.Id == aboutId) {
        insertIx = i + 1;
		break;
      }
    }
    if (insertIx < 0) {
      insertIx = 1;
    }
       
    // Add Interwoven Help menu item:
    //    About Interwoven...
    //    Interwoven Help...
    //    
    var AboutCtl = helpCtls.Add(sqcbcTypePopup, -1, insertIx, "interwoven-about");
    if (AboutCtl != null) {
      AboutCtl.Caption    = "About XMetaL/TeamXML Integration...";
      AboutCtl.BeginGroup = true;
      AboutCtl.OnAction   = "Interwoven_About";
      AboutCtl.FaceId     = Application.MakeFaceId("Interwoven", 1, 1);
    } else {
      Application.Alert("Unable to create Interwoven_About MENUITEM control!", "Interwoven to XMetaL Interface Tool");
      return -1;
    }
    ++insertIx;
   
    var HelpCtl  = helpCtls.Add(sqcbcTypePopup, -1, insertIx, "interwoven-help");
    if (HelpCtl != null) {
      HelpCtl.Caption    = "XMetaL/TeamXML Integration Help...";
      HelpCtl.OnAction   = "Interwoven_Help";
      HelpCtl.FaceId     = 0;
    } else {
      Application.Alert("Unable to create TeamXML_Integration_Help MENUITEM control!", "Interwoven to XMetaL Interface Tool");
      return -1;
    }
    ++insertIx;
   
    // Return last menuitem insertion position...
    return insertIx;
  }
     
  ///////////////////////////////////////////////////////////////////////////////////

  function isInterwovenToolbarPresent()
  {
    var cmdBars = Application.CommandBars;
    var interwovenBar = null;
   
    for (var i = 1; i <= cmdBars.Count; i++) {
      if (cmdBars.Item(i).Name == "TeamXML") {
        interwovenBar = cmdBars.Item(i);
        break;
      }
    }
    return interwovenBar;
  }

  ///////////////////////////////////////////////////////////////////////////////////

  function Interwoven_AddToolbarItem
            (toolbarCtls,
             faceID,
             onAction,
             tooltipText,
             descText,
       beginGroup)
  {
    var toolbarCtl = toolbarCtls.Add();
    if (toolbarCtl == null) {
      Application.Alert("Unable to create " + onAction + " toolbar button!", "Interwoven to XMetaL Interface Tool");
      return;
    }
    toolbarCtl.FaceId   = Application.MakeFaceId("Interwoven", 1, faceID);
    toolbarCtl.OnAction = onAction;
    toolbarCtl.TooltipText = tooltipText;
    toolbarCtl.DescriptionText = descText;
    toolbarCtl.BeginGroup = beginGroup;
    toolbarCtl.Enabled=false;
  }

  
  ///////////////////////////////////////////////////////////////////////////////////

  function Interwoven_AddToolbarItems(cmdBar)
  {
    // Append Interwoven CheckIn toolbar button...
    var toolbarCtls = cmdBar.Controls;
    if (toolbarCtls == null) {
      return;
    }
    Interwoven_AddToolbarItem
        (toolbarCtls, 8, "Interwoven_Save",
         "Save to TeamXML",
         "Save document to Interwoven server with existing name", false);
    Interwoven_AddToolbarItem
        (toolbarCtls, 2, "Interwoven_Lock",
         "Lock Current Document",
         "Lock document on Interwoven server", false);
    Interwoven_AddToolbarItem
        (toolbarCtls, 3, "Interwoven_Unlock",
         "Unlock Current Document",
         "Unlock document on Interwoven server", false);
    Interwoven_AddToolbarItem
        (toolbarCtls, 4, "Interwoven_Search",
         "Search in TeamXML",
         "Search documents on Interwoven server", false);
    Interwoven_AddToolbarItem
        (toolbarCtls, 5, "Interwoven_Properties",
         "Current Document Properties",
         "Current Document Properties on Interwoven server", false);
  }
  
  ///////////////////////////////////////////////////////////////////////////////////

  function MakeInterwovenToolbar()
  {
    var interwovenBar = isInterwovenToolbarPresent();
    if (interwovenBar != null) {
        interwovenBar.Enabled=false;
        return;
    }
    interwovenBar = Application.CommandBars.Add("TeamXML", sqcbPosTop);
    if (interwovenBar == null) {
      Application.Alert("Unable to create Interwoven toolbar!", "Interwoven to XMetaL Interface Tool");
      return;
    }
    Interwoven_AddToolbarItems(interwovenBar);
    interwovenBar.Enabled=false;
  }

  ///////////////////////////////////////////////////////////////////////////////////

  function MakeInterwovenMenuItems()
  {
    if (!areInterwovenMenuItemsPresent()) {
      Interwoven_AddMenuItems();
    }
  }

  ///////////////////////////////////////////////////////////////////////////////////
     
  function MakeInterwovenHelpMenuItems()
  {
    if (!areInterwovenHelpMenuItemsPresent()) {
      Interwoven_AddHelpMenuItems();
    }
  }
     

  MakeInterwovenMenuItems();
  MakeInterwovenHelpMenuItems();
  MakeInterwovenToolbar();
]]></MACRO> 

<MACRO name="Interwoven_Save" key="" lang="JScript"><![CDATA[
function doInterwovenSave()
{
	if (ActiveDocument && ActiveDocument.ViewType != sqViewUnspecified && CanRunMacros())
	{ 
	  // CW: Make sure it's saved locally first
	  if( ActiveDocument.name == "" )
	  {
		var tmpname=getUniqueFolder();
		ActiveDocument.SaveAs(tmpname + "\\Untitle1.xml",false);
		if( !ActiveDocument.Saved ) return;
	  }
	  SaveToServer(ActiveDocument);
	}
}
doInterwovenSave();
]]></MACRO> 


<MACRO name="Interwoven_Save_As" key="" lang="JScript"><![CDATA[
function doInterwovenSaveAs()
{
	if (ActiveDocument && ActiveDocument.ViewType != sqViewUnspecified && CanRunMacros())
	{
	  // CW: Make sure it's saved locally first
	  if( ActiveDocument.name == "" )
	  {
// David Ngo 20030409 - Started
                  ActiveDocument.Saved = false;
// David Ngo 20030409 - Ended
		  var tmpname=getUniqueFolder();
		  ActiveDocument.SaveAs(tmpname + "\\Untitle1.xml",false);
		  if( !ActiveDocument.Saved ) return;
	  }

	  // Make sure this document has already been saved to Interwoven
	  var fileVPath = GetAttrValue(ActiveDocument, "interwoven", "fileVPath");
	  if (fileVPath == null) {
		  SaveNewToServer(ActiveDocument);
	  }
	  else {
		SaveToServerAs(ActiveDocument);
	  }
	}
}
doInterwovenSaveAs();
]]></MACRO> 

<MACRO name="Interwoven_Lock" key="" lang="JScript"><![CDATA[
if (ActiveDocument && ActiveDocument.ViewType != sqViewUnspecified && CanRunMacros())
{ 
  // get the Interwoven filename
  var fileVPath = GetAttrValue(ActiveDocument, "interwoven", "fileVPath");
  if (fileVPath) {
    // Connect to cwXMTS
    var cwXMTS = cwXMTSConnect();
    if (cwXMTS != null) {
      try {
        cwXMTS.lockFile(fileVPath);
      }
      catch(exception) {
        Application.Alert("Failed lockFile: " + exception.description, "Interwoven to XMetaL Interface Tool");
      }
      cwXMTS = null;
    }
  }
  else
    Application.Alert("Current document is not an Interwoven document.", "Interwoven to XMetaL Interface Tool");
  
}
]]></MACRO> 

<MACRO name="Interwoven_Unlock" key="" lang="JScript"><![CDATA[
if (ActiveDocument && ActiveDocument.ViewType != sqViewUnspecified && CanRunMacros())
{ 
  // get the Interwoven filename
  var fileVPath = GetAttrValue(ActiveDocument, "interwoven", "fileVPath");
  if (fileVPath) {
    // Connect to cwXMTS
    var cwXMTS = cwXMTSConnect();
    if (cwXMTS != null) {
      try {
        cwXMTS.unlockFile(fileVPath);
      }
      catch(exception) {
        Application.Alert("Failed unlockFile: " + exception.description, "Interwoven to XMetaL Interface Tool");
      }
      cwXMTS = null;
    }
  }
  else
    Application.Alert("Current document is not an Interwoven document.", "Interwoven to XMetaL Interface Tool");
  
}
]]></MACRO> 

<MACRO name="Interwoven_Properties" key="" lang="JScript"><![CDATA[
if (ActiveDocument && ActiveDocument.ViewType != sqViewUnspecified && CanRunMacros())
{ 
  // get the Interwoven filename
  var fileVPath = GetAttrValue(ActiveDocument, "interwoven", "fileVPath");
  if(fileVPath) {
    // Connect to cwXMTS
    var cwXMTS = cwXMTSConnect();
    if (cwXMTS != null) {
      try {
        cwXMTS.showProperties(fileVPath);
      }
      catch(exception) {
        Application.Alert("Failed showProperties: " + exception.description, "Interwoven to XMetaL Interface Tool");
      }
      cwXMTS = null;
    }
  }
  else
    Application.Alert("Current document is not an Interwoven document.", "Interwoven to XMetaL Interface Tool");
  
}
]]></MACRO> 

<MACRO name="Interwoven_Search" key="" lang="JScript"><![CDATA[
if (ActiveDocument && ActiveDocument.ViewType != sqViewUnspecified && CanRunMacros())
{ 
  // Connect to cwXMTS
  var cwXMTS = cwXMTSConnect();
  if (cwXMTS != null) {
	  try {
		cwXMTS.doSearch();
	  }
	  catch(exception) {
		Application.Alert("Failed doSearch: " + exception.description, "Interwoven to XMetaL Interface Tool");
	  }
	  cwXMTS = null;
  }
}
]]></MACRO> 

<MACRO name="Interwoven_About" key="" lang="JScript"><![CDATA[
function about()
{
  // Connect to cwXMTS
  var cwXMTS = cwXMTSConnect();
  if (cwXMTS == null) {
    return;
  }
  try {
    cwXMTS.showAbout();
  }
  catch(exception) {
    Application.Alert("Failed showAbout: " + exception.description, "Interwoven to XMetaL Interface Tool");
  }
  cwXMTS = null;
}
about();

]]></MACRO> 

<MACRO name="Interwoven_Help" key="" lang="JScript"><![CDATA[
function help()
{
  // Connect to cwXMTS
  var cwXMTS = cwXMTSConnect();
  if (cwXMTS == null) {
    return;
  }
  try {
    cwXMTS.showHelp();
  }
  catch(exception) {
    Application.Alert("Failed showHelp: " + exception.description, "Interwoven to XMetaL Interface Tool");
  }
  cwXMTS = null;
}
help();
]]></MACRO> 


<MACRO name="On_Update_UI" lang="JScript" hide="true" id="144"><![CDATA[

// Check if the view is Tags On and if so, adjust the selection out of the 
// top-level
if (Selection.IsInsertionPoint && ActiveDocument.ViewType == sqViewTagsOn) {
   if (Selection.ContainerNode == null) {
      Selection.MoveRight();
   }
   if (Selection.ContainerNode == null) {
      Selection.MoveLeft();
   }
}
UpdateToolBar(); // ????????

]]></MACRO> 


<MACRO name="On_Application_Resolve_Image_URL" lang="JScript" hide="true"><![CDATA[
function ResolveImageURL()
{
	var url=Application.ResolveURL;
	var re=/^iwts:.*/i;	// test for interwoven URL prefix
	if( !re.test(url) )	return 0;
	url=url.substr(5);	// remove prefix
	try
	{
		var doc=Application.ActiveDocument;
		var areaPath=GetAttrValue(doc,"interwoven","areaVPath");
		if( areaPath == null ) return -1;

		var netDrive="";		
		var wshShell=new ActiveXObject("WScript.Shell");
		if( wshShell != null )
		{
		  try
              {
                netDrive=wshShell.regRead("HKCU\\Software\\Softquad\\XM_TeamXML\\TeamXMLDrive");
                netDrive=netDrive.replace(/^(\s)* | (\s)*$/,"");
                var isDrive=/[a-z]:/i;
                if( !isDrive.test(netDrive) ) netDrive="";
              } catch(e2){}
		}
		if( netDrive != "" )
		{
			var newURL=netDrive + areaPath + url
			Application.ResolveURL=newURL.replace(/\//g,"\\");
		}
		else
		{
    	      var cwXMTS = ResourceManager.ControlInTab("TeamXML");
            var docPath=doc.Path;
            if( docPath == null ) docPath == "";

            // check if opened a TXML document (not from new or local store).
            // if new or from local store, download all files into general cache.
		var docCachePath=GetDocCachePath();
            var rexp=docCachePath;
            rexp=rexp.replace(/\\/g,"\\\\");
            var reAppDocPath=new RegExp(rexp,"i");
            if( docPath.search(reAppDocPath) == -1 ) docPath=docCachePath;
			var imgfpath=docPath + "\\" + url.substr(url.lastIndexOf("/")+1);
			var autoMode=4357;		// read component with descendents and annotations, but no prolog
			
			if( !Application.FileExists(imgfpath) ) 
			cwXMTS.getSimpleFile(areaPath + url,docPath);
			
			Application.ResolveURL=imgfpath;
		}
	}
	catch(e){ return -1; } 
}
ResolveImageURL();
]]></MACRO> 


<MACRO name="On_Application_Resolve_Entity" lang="JScript" hide="true"><![CDATA[
function ResolveEntityURL()
{
	var url=Application.ResolveEntityInfo.systemID;
	var re=/^iwts:.*/i;	// test for interwoven URL prefix
	if( !re.test(url) )	return 0;
	url=url.substr(5);	// remove prefix
	try
	{
		var docPath=Application.ResolveEntityInfo.BasePath;
	      docPath=Application.URLToPath(docPath);
		var areaPath=getAreaVPath(docPath);
		if( areaPath == null ) return -1;

		var netDrive="";
		var wshShell=new ActiveXObject("WScript.Shell");
		if( wshShell != null )
		{
		   try
               {
                 netDrive=wshShell.regRead("HKCU\\Software\\Softquad\\XM_TeamXML\\TeamXMLDrive"); 
                 netDrive=netDrive.replace(/^(\s)* | (\s)*$/,"");
                 var isDrive=/[a-z]:/i;
                 if( !isDrive.test(netDrive) ) netDrive="";
               } catch(e2){ }
		}
		if( netDrive != "" )
		{
			var newURL=netDrive + areaPath + url
			Application.ResolveEntityInfo.Filename=newURL.replace(/\//g,"\\");
		}
		else
		{
            // check if opened a TXML document (not from new or local store).
            // if new or from local store, download all files into general cache.
    	      var cwXMTS=ResourceManager.ControlInTab("TeamXML");
			var docCachePath=GetDocCachePath();
            var rexp=docCachePath;
            rexp=rexp.replace(/\\/g,"\\\\");
            var reAppDocPath=new RegExp(rexp,"i");
            if( docPath.search(reAppDocPath) == -1 ) docPath=docCachePath + "\\Untitled1.xml";
			var entfpath=docPath.substr(0,docPath.lastIndexOf("\\")+1) + url.substr(url.lastIndexOf("/")+1);
			var autoMode=4357;		// read component with descendents and annotations, but no prolog
			if( !Application.FileExists(entfpath) )
			{
				var dirPath=docPath.substr(0,docPath.lastIndexOf("\\"));
				cwXMTS.getSimpleFile(areaPath + url,dirPath);
				// CW: here, we assume that any associated files to DTD that must be downloaded
				// are sitting in the sub-folder "filename.dtd+"
				var isDTD=/.*\.dtd$/i;
				var isXSD=/.*\.xsd$/i;
				if( isDTD.test(entfpath) || isXSD.test(entfpath) )
				{
					var dtdURL=areaPath + url.substr(0,url.lastIndexOf("/")+1) +
						url.substr(url.lastIndexOf("/")+1) + "+";
					cwXMTS.getSimpleFile(dtdURL,dirPath);
				}
			}
			var url2=docPath.substr(0,docPath.lastIndexOf("\\")+1) + url.substr(url.lastIndexOf("/")+1);
			Application.ResolveEntityInfo.Filename=url2;
		}
	}
	catch(e){ return -1; } 
}
ResolveEntityURL();
]]></MACRO> 

</MACROS> 

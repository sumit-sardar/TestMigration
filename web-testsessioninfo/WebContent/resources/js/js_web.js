/**
 * js_web.js
 */
 
function getSafeElement( elementId ) {
	var element = document.getElementById(elementId);
	if (element == null) {
		var elements = document.getElementsByName(elementId);
		if (elements != null) {
			element = elements[0];
		}
	}
	return element;
}

var submitToActionDone = false; 
function submitToAction(toAction, subtestId, status, ctrl) {

    if (!submitToActionDone) {
 
 		ctrl.disabled = true;
 		submitToActionDone = true;
 
	    document.body.style.cursor = 'wait';
	    
	    var actionElement = getSafeElement('{actionForm.actionElement}');
	    actionElement.value = subtestId;
	    
	    var currentAction = getSafeElement('{actionForm.currentAction}');
	    currentAction.value = status;
	    
	    actionElement.form.action = toAction;
	    actionElement.form.submit();
    
    }
    return false;
}

function submitFormAndSwitchPage(action)
{
    document.forms[0].action = action;
    document.forms[0].submit();
}    


var clientInstalled = false;
var systemRequirementWindow = null;
var termsWindow = null;
var privacyWindow = null;
var COPPAWindow = null;

function checkForJavaVersion()
{
//var javaVersion = document.applets[0].javaVersion;
//alert(javaVersion);
    
    var isValidJavaVersion = document.applets[0].isValidJavaVersion;
    if (isValidJavaVersion == 'false') {
        showElement(getElement("showJavaVersionError", "div"));
        hideElement(getElement("showContent", "div"));
    }
}

function checkForDownloadManager(){
    if ( checkForPCNotIE() ) {
        showElement(getElement("showPCNotIEError", "div"));
        hideElement(getElement("showContent", "div"));
    }
    else 
    if ( isMacOS() ) {
        showElement(getElement("showMacOSError", "div"));
        hideElement(getElement("showContent", "div"));
    }
    else
    if ( isLinux() ) {
        showElement(getElement("showLinuxError", "div"));
        hideElement(getElement("showContent", "div"));
    }
    else
    if ( !isClientInstalled() ) {
        showElement(getElement("showDMError", "div"));
        hideElement(getElement("showContent", "div"));
    }
}
 
function checkDownloadFlashNonIE(){
    if(checkForPCNotIE()){
        showElement(getElement("showDownloadFlashNonIEMessage", "div"));
        hideElement(getElement("showDownloadFlashMessage", "div"));

        showElement(getElement("downloadFlashError", "div"));
        hideElement(getElement("downloadFlashOk", "div"));
    }
}

function allowDownLoadBinFile(){

    var agt = navigator.userAgent.toLowerCase(); 
    var firefox  = (agt.indexOf("firefox") != -1);
    
    if (firefox) {
        showElement(getElement("allowDownload", "div"));
        hideElement(getElement("notAllowDownload", "div"));
    }
	else {
        showElement(getElement("notAllowDownload", "div"));
        hideElement(getElement("allowDownload", "div"));
    }
}

function checkForPCNotIE(){
    var result = false;
    
    var agt = navigator.userAgent.toLowerCase();
    var ie  = (agt.indexOf("msie") != -1);
    var win = ((agt.indexOf("win")!=-1) || (agt.indexOf("32bit")!=-1));
    
    if (win && !ie)
        result = true;
        
    return result;
}

function getElement(name, type){
    var result;
    var elements=document.getElementsByTagName(type);
    for(var i=0;i<elements.length;i++) { 
        var element = elements[i];
        if(element.id.indexOf(name) != -1){
            result = element;
            break;
        }
    }
    return result;
}

function isMacOS() {
    var agt = navigator.userAgent.toLowerCase();
    var mac = (agt.indexOf("mac")!=-1);
    return mac;
}

function isLinux() {
    var agt = navigator.userAgent.toLowerCase();
    var linux = (agt.indexOf("linux")!=-1);
    return linux;
}

function isClientInstalled() {
    var classId = "LDBJSBridge.LDBJSBridgeCTL";
    
    var agt = navigator.userAgent.toLowerCase();
    var ie  = (agt.indexOf("msie") != -1);
    var ns  = (navigator.appName.indexOf("Netscape") != -1);
    var win = ((agt.indexOf("win")!=-1) || (agt.indexOf("32bit")!=-1));
    var mac = (agt.indexOf("mac")!=-1);
    
    if (ie && win)
        detectIE(classId);
//    if (ns || !win) 
//       detectNS(classId);      
    return clientInstalled;
} 

function detectIE(ClassID) { 
    document.write('<SCRIPT LANGUAGE=VBScript>\non error resume next\nclientInstalled = IsObject(CreateObject("' + ClassID + '"))\n</SCRIPT>\n'); 
}

function detectNS(ClassID) { 
    if (nse.indexOf(ClassID) != -1) 
        if (navigator.mimeTypes[ClassID].enabledPlugin != null) 
            clientInstalled = true;
}

function invokeDownloadManager(xml){
    var ret = document.frmDownloadManager.ctlDownloadMgr.StartDownload(xml);
    if(ret == "success")
        showDownloadSuccess();
    else
        showDownloadFailure();
}

function showDownloadSuccess(){
    showElement(getElement("downloadSuccessMessage", "div"));
    hideElement(getElement("showStopMessage", "div"));
    showElement(getElement("noStopMessage", "div"));
    hideElement(getElement("downloadInProgressButtons", "div"));
    showElement(getElement("downloadSuccessButtons", "div"));
}

function showDownloadFailure(){
    showElement(getElement("downloadFailureMessage", "div"));
    hideElement(getElement("showStopMessage", "div"));
    showElement(getElement("noStopMessage", "div"));
    hideElement(getElement("downloadInProgressButtons", "div"));
    showElement(getElement("downloadFailureButtons", "div"));    
}

function showElement(element){
    element.style.display="block";
}

function hideElement(element){
    element.style.display="none";
}

function createDownloadManagerTag(id, token){
    document.write(' <OBJECT ID="ctlDownloadMgr" ');
    document.write(' CLASSID="CLSID:ABC06E55-904E-4192-A9DD-E5B136AC23BA" ');
    //document.write(' CODEBASE=""  ');
    document.write(' width="461"  ');
    document.write(' height="341"> ');
    document.write(' <PARAM NAME="_ExtentX" VALUE="11721"> ');
    document.write(' <PARAM NAME="_ExtentY" VALUE="8229"> ');
    document.write(' <PARAM NAME="AsmtProgressUpdateDivisor" VALUE="2"> ');
    document.write(' <PARAM NAME="AsmtProgressUpdateDivisor" VALUE="2"> ');
    document.write(' <PARAM NAME="CreateObjectBankFolders" VALUE="No"> ');
    document.write(' <PARAM NAME="CreateAssessmentManifest" VALUE="Yes"> ');
    document.write(' <PARAM NAME="ControlBackColor" VALUE="120,159,198"> ');
    document.write(' <PARAM NAME="PgBarBackColor" VALUE="255,255,255"> ');
    document.write(' <PARAM NAME="SECURITY_TYPE" VALUE="S"> ');
    document.write(' <PARAM NAME="ID" VALUE="' + id + '"> ');
    document.write(' <PARAM NAME="TOKEN" VALUE="' + token + '"> ');
    document.write(' <PARAM NAME="IGNORECERTIFICATE" VALUE="TRUE"> ');
    document.write(' <PARAM NAME="TechContactName" VALUE="Contact Proctor"> ');
    document.write(' </OBJECT> ');
}

function stopDownload(){
    var stop = confirm("This test has not finished loading. Are you sure you want to stop loading this test?");
    if(stop)
        document.location.href='goto_canceldownload.do';
}

var flashinstalled = 0;
var flashversion = 0;
var MSDetect = "false";
var javainstalled = 0;
var javaversionstring = "0";
var javaversionnumber = 0;

function detectFlashAndJava(){
    detectFlashVersion();
    if(flashversion == 8){
        enableUninstallFlash8();
    }
    else if(flashversion != 7){
        enableInstallFlash7();
    }
}

function detectFlashVersion(){ 
    detectFlashUsingJavascript();
    if(MSDetect == "true"){
        detectFlashUsingVBScript();
    }
    flashversion = new Number(flashversion);
}

function detectFlashUsingJavascript(){
    if (navigator.plugins && navigator.plugins.length)
    {
        navigator.plugins.refresh(true);
        x = navigator.plugins["Shockwave Flash"];
        if (x)
        {
            flashinstalled = 2;
            if (x.description)
            {
                y = x.description;
                flashversion = y.charAt(y.indexOf('.')-1);
            }
        }
        else
            flashinstalled = 1;
        if (navigator.plugins["Shockwave Flash 2.0"])
        {
            flashinstalled = 2;
            flashversion = 2;
        }
    }
    else if (navigator.mimeTypes && navigator.mimeTypes.length)
    {
        x = navigator.mimeTypes['application/x-shockwave-flash'];
        if (x && x.enabledPlugin)
            flashinstalled = 2;
        else
            flashinstalled = 1;
    }
    else
        MSDetect = "true";
}

function detectFlashUsingVBScript() { 
    document.write('<SCRIPT LANGUAGE="VBScript">on error resume next\nIf MSDetect = "true" Then\nFor i = 2 to 8\nIf Not(IsObject(CreateObject("ShockwaveFlash.ShockwaveFlash." & i))) Then\nElse\nflashinstalled = 2\nflashversion = i\nEnd If\nNext\nEnd If\nIf flashinstalled = 0 Then\nflashinstalled = 1\nEnd If</SCRIPT>'); 
}

function detectJava(){
    javainstalled=navigator.javaEnabled() ? true:false;
    if(!detectJavaVersionUsingNavigator()){ 
        detectJavaVersionUsingActiveXShell();
    }
    cleanupJavaVersionNumber();
}


function detectJavaVersionUsingNavigator(){
    navigator.plugins.refresh(true);
    var numPlugs=navigator.plugins.length;
    if (numPlugs) // can detect using this method
    {
        for (var x=0; x<numPlugs; x++)
        {
            var pluginjava = navigator.plugins[x];

            if (pluginjava.name.toLowerCase().indexOf('java plug-in') != -1)
            {
                //javaversionstring=pluginjava.description.toLowerCase().split('java plug-in ')[1].split(' for')[0];
                javaversionstring = pluginjava.description.split(' ')[1];
                if( javaversionstring )
                    javaversionstring = javaversionstring.split('1.')[1];
                else
                    javaversionstring = "1"; // could not find subversion
            }
            else if(pluginjava.name.toLowerCase().indexOf('java(tm)') != -1){
                javaversionstring = "5";
            }
        }
    }

    return numPlugs;
}

function detectJavaVersionUsingActiveXShell() {
    var shell;
    try {
        // Create WSH(WindowsScriptHost) shell, available on Windows only
        shell = new ActiveXObject("WScript.Shell");
        if (shell != null) {
        // Read JRE version from Window Registry
            try {
                javaversionstring = shell.regRead("HKEY_LOCAL_MACHINE\\Software\\JavaSoft\\Java Runtime Environment\\CurrentVersion");
            } 
            catch(e) {
            }
        }
    } 
    catch(e) {
    }
}

function cleanupJavaVersionNumber(){
    var indexofperiod = javaversionstring.indexOf(".");

    javaversionstring = javaversionstring.substring(indexofperiod + 1, javaversionstring.length);
    indexofperiod = javaversionstring.indexOf(".");
    if(indexofperiod != -1){
        javaversionstring = javaversionstring.substring(0, indexofperiod);
    }
    javaversionnumber = new Number(javaversionstring);

}

function enableInstallRequiredSoftware(){
    showElement(getElement("requireSoftware", "div"));
}

function enableInstallJava(){
    enableInstallRequiredSoftware();
    showElement(getElement("installJavaRow", "tr"));    
}

function enableUninstallFlash8(){
    enableInstallRequiredSoftware();
    showElement(getElement("uninstallFlash8Row", "tr"));
    showElement(getElement("installFlash7Row", "tr"));
}
function enableInstallFlash7(){
    enableInstallRequiredSoftware();
    showElement(getElement("installFlash7Row", "tr"));
}

function newWindow(location_)
{
    window.open(location_,'help','toolbar=yes,location=yes,directories=yes,status=yes,scrollbars=yes,menubar=yes,resizable=yes,width=560, height=430');
    return false;
}

function openSystemRequirementWindow(url) {
    if( !systemRequirementWindow || systemRequirementWindow.closed ) {
        systemRequirementWindow = window.open(url, 'System Requirements', 'toolbar=no,location=no,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes,width=560, height=430');
    } else {
        systemRequirementWindow.location = url;
    }
    systemRequirementWindow.focus();
    return false;
}

function openTermsOfUseWindow(location_)
{
    if(!termsWindow || termsWindow.closed)
    {
        termsWindow = window.open(location_,'TermsOfUse','toolbar=no,location=no,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes,width=560, height=430');
    }
    else
    {
        termsWindow.location = location_;
    }
    termsWindow.focus();
    return false;
}

function openCOPPAWindow(location_)
{
    if(!COPPAWindow || COPPAWindow.closed)
    {
        COPPAWindow = window.open(location_,'COPPA','toolbar=no,location=no,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes,width=560, height=430');
    }
    else
    {
        COPPAWindow.location = location_;
    }
    COPPAWindow.focus();
    return false;
}

function openPrivacyPolicyWindow(location_)
{

    if(!privacyWindow || privacyWindow.closed)
    {
        privacyWindow = window.open(location_,'PrivacyPolicy','toolbar=no,location=no,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes,width=560, height=430');
    }
    else
    {
        privacyWindow.location = location_;
    }
    privacyWindow.focus();
    return false;
}

function enableDeselectAllTestButton(elementId) 
{ 
    var inputs = document.getElementsByTagName("input");
    var count = 0;
    for (var i=0 ; i<inputs.length ; i++) {
        if (inputs[i].getAttribute("type") == "checkbox") {
            if (inputs[i].checked == true) {
                count++;
            }
        }
    }
    
    var element = getSafeElement(elementId);    
    if( element != null) {
        if (count == 0)
            element.setAttribute("disabled", "true");            
        else
            element.removeAttribute("disabled");            
    }
    
}
function enableValidationButtons() {
    getSafeElement("toggleSubtestValidation").removeAttribute("disabled");      
    getSafeElement("toggleSubtestCustom").removeAttribute("disabled");      
}    

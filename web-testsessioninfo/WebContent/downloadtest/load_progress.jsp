<%@ page import="java.io.*, java.util.*"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- <%@ page contentType="text/html;charset=windows-1252"%> --%>


<%
String adsUrl = (String) request.getAttribute("adsUrl");
String sdsId = (String) request.getAttribute("sdsId");
String sdsToken = (String) request.getAttribute("sdsToken");
String subtestXML = (String) request.getAttribute("downloadManagerXml");

String userAgent = request.getHeader("User-Agent").toLowerCase();
Boolean ie7 = new Boolean(userAgent.indexOf("msie 7") > 0);        
Boolean firefox = new Boolean(userAgent.indexOf("firefox") > 0);        
Boolean mac = new Boolean(userAgent.indexOf("mac") > 0);        

String objectBankPath = (String) request.getAttribute("objectBankPath");
%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<netui-data:declareBundle bundlePath="helpResources" name="help"/>
<netui-template:template templatePage="/resources/jsp/template.jsp">
    <netui-template:setAttribute name="helpLink" value="${bundle.help['help.topic.loadTestProgress']}"/>
    
<netui-template:section name="bodySection">


    <%-- ********************************************************************************************************************* --%>
    <%-- Start Page Content --%>
    <%-- ********************************************************************************************************************* --%>
<% if ( ie7.booleanValue() ) { %>

    <%-- Applet for IE 7.0 --%>

    <OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" WIDTH=1 HEIGHT=1 NAME="Communicator" codebase="http://java.sun.com/update/1.4.2/jinstall-1_4-windows-i586.cab#Version=1,4,0,0"><XMP>

        <APPLET CODE = "com.stgglobal.ADS.Communicator.class" ARCHIVE="CommunicatorS.jar,bcprov-jdk15-134MS.jar" WIDTH="1" HEIGHT="1" NAME="Communicator"></XMP>    
            <PARAM NAME="CODE" VALUE="com.stgglobal.ADS.Communicator.class" >  
            <PARAM NAME="ARCHIVE" VALUE="CommunicatorS.jar,bcprov-jdk15-134MS.jar" >    
            <PARAM NAME="NAME" VALUE="comm" > 
            <PARAM NAME="type" VALUE="application/x-java-applet;version=1.4" >   
            <PARAM NAME="scriptable" VALUE="false" > 
            <PARAM NAME="DEBUG_LEVEL" VALUE="9"/> 
            <PARAM NAME="ADS_SVC_URL" VALUE="<%=adsUrl%>" />  
            <PARAM NAME="CONTROL_MODE" VALUE="2" />   
            <PARAM NAME="SECURITY_TYPE" VALUE="S" />   
            <PARAM NAME="ID" VALUE="<%=sdsId%>" />   
            <PARAM NAME="TOKEN" VALUE = "<%=sdsToken%>" /> 
            <PARAM NAME="OBJECT_BANK_PATH" VALUE="<%=objectBankPath%>"/> 
            <PARAM NAME="IS_DATA_ENCRYPTED" VALUE="1" />  
            <PARAM NAME="TRANSMIT_TIMEOUT_SECONDS" VALUE="15" />    
            <PARAM NAME="RESTART_NUMBER" VALUE="0" />  
            <PARAM NAME="TRANSMIT_INTERVAL_SECONDS" VALUE="30" />  
            <PARAM NAME="TRANSMIT_MAX_RETRY_TIME" VALUE="20" />    
            <PARAM NAME="TRANSMIT_MAX_RETRY_COUNT" VALUE="3" />    
        </APPLET>
        
    </OBJECT>
    
<% } else { %>

    <%-- Applet for IE 6.0, Firefox, MAC OS --%>

    <script type="text/javascript">
        // reduce the flicker
        document.body.style.backgroundColor = "#fff";
        
        var objectBankPath = "C:\\Program Files\\CTB\\Online Assessment\\data\\objectbank\\";
        var Restart_Number = 0;
        
        var _info = navigator.userAgent;
        var _ns = false;         
        var _ns6 = false;
        var _ie = (_info.indexOf("MSIE") > 0 && _info.indexOf("Win") > 0 && _info.indexOf("Windows 3.1") < 0);        
        var _ns = (navigator.appName.indexOf("Netscape") >= 0 && ((_info.indexOf("Win") > 0 && _info.indexOf("Win16") < 0 && java.lang.System.getProperty("os.version").indexOf("3.5") < 0) || (_info.indexOf("Sun") > 0) || (_info.indexOf("Linux") > 0) || (_info.indexOf("AIX") > 0) || (_info.indexOf("OS/2") > 0) || (_info.indexOf("IRIX") > 0)));
        var _ns6 = ((_ns == true) && (_info.indexOf("Mozilla/5") >= 0));
        
        <% 
        String p_pc_mac_flag = "PC";
        String p_type_flag = "T"; // Have no clue what STG meant for this
        
        if( userAgent.indexOf("mac") != -1 || userAgent.indexOf("apple") != -1 ) {
            p_pc_mac_flag = "MAC";
        }
        %>

        <%  if ( firefox.booleanValue() ) { %>
                objectBankPath = "C:\\Program Files\\CTB\\Online Assessment\\data\\objectbank\\";
        <%  } 
            else 
            if( p_pc_mac_flag == "PC" && p_type_flag != "P" ) { //AR20060203 TO BE IMPROVED  %>
                document.writeln('<OBJECT ID="LDBJSBridgeCTL" CLASSID="CLSID:56871AC2-4683-4D8E-B5EB-E7E3AA40DD52" VIEWASTEXT WIDTH=0 HEIGHT = 0></OBJECT>' );
                objectBankPath =  LDBJSBridgeCTL.GetRegistryKeyValue("ObjectbankPath"); 
        <%  } 
            else 
            if ( p_pc_mac_flag == "MAC" ) { %>
                objectBankPath = "/Library/Application Support/LockdownBrowser/ObjectBank"; 
        <%  } %>

        
        var _ns = (navigator.appName.indexOf("Netscape") >= 0 && ((_info.indexOf("Win") > 0 && _info.indexOf("Win16") < 0 && java.lang.System.getProperty("os.version").indexOf("3.5") < 0) || (_info.indexOf("Sun") > 0) || (_info.indexOf("Linux") > 0) || (_info.indexOf("AIX") > 0) || (_info.indexOf("OS/2") > 0) || (_info.indexOf("IRIX") > 0)));
        var _ns6 = ((_ns == true) && (_info.indexOf("Mozilla/5") >= 0));

        if( _ie == true ) {
           document.writeln('<OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" WIDTH="1" HEIGHT="1" NAME="Communicator" ><NOEMBED><XMP>');
        }
        else if (_ns == true && _ns6 == false) {
            document.writeln('<EMBED \
                type="application/x-java-applet;version=1.4" \
                CODE="com.stgglobal.ADS.Communicator.class" \
                ARCHIVE="CommunicatorS.jar,bcprov-jdk15-134MS.jar" \
                NAME="Communicator" \
                WIDTH="1" \
                HEIGHT="1" \
                DEBUG_LEVEL="9"/ \
                ADS_SVC_URL ="<%=adsUrl%>" />"\
                CONTROL_MODE="2"/ \
                SECURITY_TYPE= "S"/ \
                ID= "<%=sdsId%>"/ \
                TOKEN= "<%=sdsToken%>" \
                OBJECT_BANK_PATH="' + objectBankPath + '"/ \
                IS_DATA_ENCRYPTED="1"/ \
                TRANSMIT_TIMEOUT_SECONDS="15"/ \
                TRANSMIT_INTERVAL_SECONDS="30"/ \
                TRANSMIT_MAX_RETRY_TIME="20"/ \
                TRANSMIT_MAX_RETRY_COUNT="3"/ \
                RESTART_NUMBER="' + Restart_Number + '"/ \
                scriptable="false"/ \
                pluginspage="http://java.sun.com/products/plugin/index.html#download"><NOEMBED><XMP>');
        }

        document.writeln('<APPLET CODE="com.stgglobal.ADS.Communicator.class" ARCHIVE="CommunicatorS.jar,bcprov-jdk15-134MS.jar" WIDTH="1" HEIGHT="1" NAME="Communicator"></XMP>    \
            <PARAM NAME="CODE" VALUE="com.stgglobal.ADS.Communicator.class" >  \
            <PARAM NAME="ARCHIVE" VALUE="CommunicatorS.jar,bcprov-jdk15-134MS.jar" >    \
            <PARAM NAME="NAME" VALUE="comm" > \
            <PARAM NAME="type" VALUE="application/x-java-applet;version=1.4" >   \
            <PARAM NAME="scriptable" VALUE="false" > \
            <PARAM NAME="DEBUG_LEVEL" VALUE="9"/> \
            <PARAM NAME="ADS_SVC_URL" VALUE="<%=adsUrl%>" />  \
            <PARAM NAME="CONTROL_MODE" VALUE="2" />   \
            <PARAM NAME="SECURITY_TYPE" VALUE="S" />   \
            <PARAM NAME="ID" VALUE="<%=sdsId%>" />   \
            <PARAM NAME="TOKEN" VALUE = "<%=sdsToken%>" /> \
            <PARAM NAME="OBJECT_BANK_PATH" VALUE="' + objectBankPath + '"/> \
            <PARAM NAME="IS_DATA_ENCRYPTED" VALUE="1" />  \
            <PARAM NAME="TRANSMIT_TIMEOUT_SECONDS" VALUE="15" />    \
            <PARAM NAME="RESTART_NUMBER" VALUE="0" />  \
            <PARAM NAME="TRANSMIT_INTERVAL_SECONDS" VALUE="30" />  \
            <PARAM NAME="TRANSMIT_MAX_RETRY_TIME" VALUE="20" />    \
            <PARAM NAME="TRANSMIT_MAX_RETRY_COUNT" VALUE="3" />    \
            </APPLET>' );
        document.writeln('</NOEMBED></EMBED></OBJECT>');
        
    </script>
<% } %>
        
        
    <script type="text/javascript">
        function AlternateAPI() {
            this.DMInitialize = AlternateAPIMethodDMInitialize;
            this.DMFinish     = AlternateAPIMethodDMFinish;
        }
        
        function AlternateAPIMethodDMInitialize() {
            return '<%=subtestXML%>';
        }
        
        function AlternateAPIMethodDMFinish() {
            showDownloadSuccess();
        }        
    </script>

    <script type="text/javascript">
        <%-- Begin snippet from SDS... --%>
xmlStr="";
gProto="N";
var laszlouid = 0;

//FA020106: global JS variable
g_error_count=0
//FA020106: end

//GS031606:	This variable acts as a flag to commit a forcefull stop of the entire download process.
shouldStop=false

var TheCommunicator = null;
var TheAPI = null;

<%-- Had to comment out and change based on need of this predownload page --%>
//TheAPI = parent.frames[0].API;
//TheCommunicator = parent.frames[0].Communicator;
//TheCommunicator.initialize();

<%-- Had to change to the following for this page only. --%>
TheAPI = new AlternateAPI();
TheCommunicator = document.applets.Communicator;

function convertString(caller)	//GS100505:To Convert al the item xml data into seperate strings
{
		//xmlStr=remove_special(xmlStr, 10)	//removing carraige returns
		xmlStr=remove_carraige(xmlStr);

		while(xmlStr.indexOf("\t")>-1)	//removing tabspaces
			{
				xmlStr=xmlStr.replace("\t","")
			}
		
		while(xmlStr.indexOf("  ")>-1)	//removing blank spaces
			{
				xmlStr=xmlStr.replace("  "," ")
			}
	//GS031606: To stop making Laszlo calls if shouldStop is set to true anytime between the download process
	if(!(shouldStop))
	{
		if(caller=="SUBTESTLIST")
			lzSetCanvasAttribute('subtestListRawXML', laszlouid + xmlStr)		//load the XML Value
		else if(caller=="SUBTEST")
			{
			lzSetCanvasAttribute('downloadSubtestRawXML',laszlouid + xmlStr)		//load the XML Value
			}
		else
			lzSetCanvasAttribute('itemRawXML', laszlouid + xmlStr)		//load the XML Value
	}
}

// FA020106:
// Modified this method to add "isLastItem" parameter
// This parameter will return "Y" incase of last item for that subtest.
function downloadItem(itemId, hAttributeValue,isLastItem)
{
	//hAttributeValue = 'F9AD5E06A6534A1374C5368CB119966A';//NA0209
	var dm_error = TheCommunicator.downloadItem(itemId, hAttributeValue);

	//GS031706: Communicator getErrorInfo implemented here
	var sync_err = TheCommunicator.getErrorInfo() + '';
	//alert('dm_error=' + dm_error + 'sync_err=' + sync_err)				
	if(sync_err != '')
		{showError(sync_err);}


	//alert("Downloading: Item "+itemId+ ","+hAttributeValue + ' error: ' + dm_error)
	if(isLastItem == 'Y')
		{
			TheCommunicator.closeASMT();
		}
	//lzSetCanvasAttribute('jsDownloadFlag',"DONE")		//load the XML Value
	//GS031606: To stop making Laszlo calls if shouldStop is set to true anytime between the download process
	if(!(shouldStop))
	{
		if(dm_error != '')
			{g_error_count = g_error_count + 1}

        // showError will call lzSetCanvasAttribute only if shouldStop is true
        // so it's safe to make this call if !shouldStop
		lzSetCanvasAttribute('jsDownloadFlag',laszlouid + "DONE," + g_error_count)
	}
}

//NA022206
function initialize()
{	
	loadUIColors();
}

//NA022206
function loadUIColors()
{	
	//var UIStr='<downloadcontent_ui bg_color="0x3333CC" progressbar_color="0xFF33FF" text_color="0xFFFF66"/>'
	//var UIStr='<downloadcontent_ui bg_color="0x789FC6" progressbar_color="0xFFFFFF" text_color="0xFFFFFF"/>'
	
    var UIStr='<downloadcontent_ui bg_color="0xFFFFFF" progressbar_color="0x336699" text_color="0x000000"/>'
	
	lzSetCanvasAttribute('uiRawXML', laszlouid + UIStr)		//load the XML Value
}

function loadSubtest(asmtid)
{		
	// PD030406: dm_success variable added    
	var dm_success = TheCommunicator.initiateASMT(asmtid,"3","")
	
	//GS031706: Communicator getErrorInfo implemented here
	var sync_err = TheCommunicator.getErrorInfo() + '';
	if(sync_err != '')
		{showError(sync_err);}

	
	//alert("initiateASMT done. getting DecryptedASMTXml for asmtid=" + asmtid)
	//alert("line 96 - "+dm_success)

	//GS031606: To stop making Laszlo calls if shouldStop is set to true anytime between the download process
	if(!(shouldStop))
	{

		/*
			GS030606:	To increment the g_error_count and send it to Laszlo if TheCommunicator.initiateASMT(asmtid,"2","")
					doesn't return OK and also let the laszlo automatically request for another Subtest 
					that is left for downloading.
		*/
		if(dm_success != 'OK')
		{
			g_error_count = g_error_count + 1
			lzSetCanvasAttribute('downloadSubtestRawXML', laszlouid + "G_ERROR_CONSTANT, "+g_error_count)		//load the XML Value
		}
		else
		{
			xmlStr = TheCommunicator.getDecryptedASMTXml() + '';
			//alert("DecryptedASMTXml ="+xmlStr)
			convertString("SUBTEST");
		}
	}
}		

function getSubtestList()
{
	xmlStr = TheAPI.DMInitialize();
	//alert("SubtestList "+xmlStr )
	convertString("SUBTESTLIST");
}

function confirmDone()
{
	//location.href="PresentationCanvas.html";
	lzSetCanvasAttribute('errorcount', laszlouid + '' + g_error_count) 
	if(g_error_count == 0)	{TheAPI.DMFinish();}
}

 
//GS010705:	This function will remove all the Carraige Returns from the XML String passed to it as a parameter
function remove_carraige(str)
{
	var outputStr = "";
	for (var i = 0; i < str.length; i++) 
	{
		if ((str.charCodeAt(i) == 13) && (str.charCodeAt(i + 1) == 10)) 
		{
			i++;
			outputStr += "";
		} 
		else 
		{
			outputStr += str.charAt(i);
		}
	}
	return outputStr;
}

//AA020306:	Function to get log file content
function getLogfileContent()
{
	// Dummy error message updated for testing.
	// To be replaced by the actual error message (reading from generated log files).
	//var errorStr="Error while Loading the Input Xml.  the xml is <hello><ok>test xml str</ok></hello><b>hhh</b> http://juno.ewebclassroom.com/SDSSvc/servlet/SDSSvc Reason - A name was started with an invalid character."
	var errorStr = TheCommunicator.getDownloadLogContents();	
	//alert("getLogfileContent"+errorStr)
	lzSetCanvasAttribute('errorLogStr',laszlouid + errorStr);	
}

//AA020306:	Function to save log file
function saveLogfilePath()
{	
	// Dummy path updated for testing.
	// To be replaced by the actual path information as per the requirement.
	//var logPathStr="Log file saved in following location : ../downloadError.log"
	var logPathStr= TheCommunicator.getDownloadLogPath();
	//alert("savelogLogfilePath"+logPathStr)
	lzSetCanvasAttribute('savelogPathStr',laszlouid + logPathStr)	
}

/*
	GS031606:	This method will trace error, perform appropriate validations with the error codes returned by the communicator
						and then will take the desired steps weather to stop or to skip the downloading process.
*/
function showError(errorStr)
{

	//GS031606: Error code values are hard coded here for testing only.
	var delimiter_err_code="0036"
	var precede_err_code="0028,0030"

	var errCodeArr= new Array()
	errorStr=errorStr.substring(0, errorStr.lastIndexOf(","))
	errCodeArr=errorStr.split(",")

	// GS031706: We are checking, if last error code is not equals to delimiter_err_code, then set "shouldStop" to "TRUE"
	if(errCodeArr[errCodeArr.length-1] != delimiter_err_code)
	{
		shouldStop=true
	}
	
	// GS031706: We are checking here in that error string, except the last error code. 
	//           If all precede_err_code does not exists, then set "shouldStop" to "TRUE"
	for(var index=0; index< (errCodeArr.length-1); index++)
	{
		if(precede_err_code.indexOf(errCodeArr[index])< 0)
		{
			shouldStop=true
			break;
		}
	}

	//GS031706: When "shouldStop" equals to "TRUE", stop proceeding further and show the "vew log" button. 
	//		    Otherwise skip that item/subtest and start downloading the next item/subtest, if avaliable.
	if(shouldStop)
	{
		g_error_count = g_error_count + 1
		lzSetCanvasAttribute('jsStopDownload',laszlouid + "STOP,"+g_error_count)		//load the XML Value
        // Had to add our specific messaging for failures.
        showDownloadFailure();
	}
}
		 
    <%-- End snippet from SDS... --%>
    </script>

    <h1><netui:content value="${bundle.web['loadTest.loadProgress.title']}"/></h1>
	
    <div id="showStopMessage">
    <p><netui:content value="${bundle.web['loadTest.loadProgress.title.message1']}"/></p>
    </div>
    <div id="noStopMessage" style="display:none">
    <p><netui:content value="${bundle.web['loadTest.loadProgress.title.message1']}"/>
    </p>
    </div>
    <div id="downloadFailureMessage"
         style="display:none">
        <ctb:message title="${bundle.web['loadTest.loadProgress.fail.title']}" style="errorMessage">
            <p><netui:content value="${bundle.web['loadTest.loadProgress.fail.message1']}"/></p>
            <p><netui:content value="${bundle.web['loadTest.loadProgress.fail.message2']}"/><a href="<netui:content value="/help/index.html#how_to_contact_i_know_support.htm"/>" onClick="return showHelpWindow(this.href);"><netui:content value="${bundle.web['loadTest.loadProgress.fail.supportLink']}"/></a>&nbsp;<netui:content value="${bundle.web['loadTest.loadProgress.fail.message3']}"/></p>
        </ctb:message>
        <br/>
    </div>
    <div id="downloadSuccessMessage"
         style="display:none">
        <ctb:message title="${bundle.web['loadTest.loadProgress.success.title']}" style="informationMessage">
            <netui:content value="${bundle.web['loadTest.loadProgress.success.message']}"/>
        </ctb:message>
        <br/>
    </div>

	<form name="errorForm">
        <!--This field will hold the error code, that will come from Query String-->
        <input type="hidden" name="errorCode" value="471">
	</form>

    <div style="background-color: #cc9; border-color: #f00; border-width: 0px; border-style: solid; width: 800px; height: 600px; overflow: hidden;">
        <div style="position: relative; top: 0px; left: 0px; border-color: #f00; border-width:0px; border-style: solid; width: 800px; height: 600px; overflow: hidden;">
        <SCRIPT>
            //This method will include the required SWF file in the html page. The function exists in the embed.js file
        
            /*
             * JavaScript library for embedding Laszlo applications
             *
             * Usage:
             * In the <html><head> of an HTML document that embeds a Laszlo application,
             * add this line:
             *   <script src="{$lps}/embed.js" language="JavaScript" type="text/javascript"/>
             * At the location within the <html><body> where the application is to be
             * embeded, add this line:
             *   <script language="JavaScript" type="text/javascript">
             *     lzEmbed({url: 'myapp.lzx?lzt=swf', bgcolor: '#000000', width: '800', height: '600'});
             * < /script>
             * where the url matches the URI that the application is served from, and
             * the other properties match the attributes of the application's canvas.
             */
            /* Write a tag start.  This code assumes that the attribute values don't
             * require inner quotes; for instance, {x: '100'} works, but
             * {url: 'a>b'} or {url: 'a"b'} won't. 
             */
             //SA041005 start
            lzEmbed({url: 'DownloadContent.lzx.swf?lzt=swf&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('includes/h.html?h='), bgcolor: '#ffffff"',  width: '800', height: '600'});
            //SA041005 end
            lzHistEmbed('includes');	 //GS100305:  To set the URl for the swf in the local includes folder
        </SCRIPT>
        </div>
    </div>

    <div id="downloadInProgressButtons">
    </div>
    <div id="downloadFailureButtons" style="display:none">
        <netui:button type="button" value="Try Again" onClick="document.location.href='load_progress.do?objectBankPath=C:\Program Files\CTB\Online Assessment\data\objectbank\'"/>
        &nbsp;&nbsp;
        <netui:button type="button" value="Finish" onClick="document.location.href='goto_homepage.do'"/>
    </div>
    <div id="downloadSuccessButtons" style="display:none">
        <netui:button type="button" value="Finish" onClick="document.location.href='goto_homepage.do'"/>
    </div>


    <%-- ********************************************************************************************************************* --%>
    <%-- End Page Content --%>
    <%-- ********************************************************************************************************************* --%>

    </netui-template:section>
</netui-template:template>




<%@ page import="java.io.*"%>
<%@ page import=" java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Presentation Canvas </TITLE>
<META NAME="Generator" CONTENT="Microsoft FrontPage 4.0">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">

<!--
 Changed the embed.js path to local folder.
=>STARTS
-->
<SCRIPT SRC="../includes/embed.js" type="text/javascript">
</SCRIPT>
<script type="text/javascript">
<!--
var _LDB_BypassSetFocusOnPageLoad = 0;

//This is to revert the change for the header group in javascript side. Because this is not going to be parsed by Flash.
String.prototype.replaceAll = function(stringToFind,stringToReplace){
    var temp = this;
    var index = temp.indexOf(stringToFind);
        while(index != -1){
            temp = temp.replace(stringToFind,stringToReplace);
            index = temp.indexOf(stringToFind);
        }
        return temp;
    }
function showFootnote(header)
{
	header = header.replaceAll("&amp;","&");
	lzSetCanvasAttribute("footnotedata", header);
}

function isMac(){
	return (window.navigator.platform.indexOf("Mac") != -1);
}

function closeBrowser()
{
	if(isMac()){
		location.href="close.html";
	}
	else{
		window.close();
	}
}

function updateLDB(){
	_LDB_BypassSetFocusOnPageLoad = 1;
}
//-->
</script>
</HEAD>
<%
    String eliminatorResource = "eliminator.swf";
    String url = request.getRequestURL().toString().trim();
    int lastSlash = url.lastIndexOf("/");
    url = url.substring(0,lastSlash).trim().replaceAll("https:","http:").replaceAll(":443",":80");
    System.out.println(url);
%>

<!--SA041005 start -->

<BODY topmargin=0 leftmargin=0 rightmargin=0 bottommargin=0 scroll=no onload="//loadSubtest('subtest')" bgcolor="#6691B4">
<!--SA041005 end -->
	<SCRIPT>
		//This method will include the required SWF file in the html page. The function exists in the embed.js file
		
		lzEmbed({url: '/ContentReviewWeb/TestClientPageFlow/TestClient.lzx.swf?lzt=swf&servletUrl=<%=url%>&eliminatorResource=<%=url%>/<%=eliminatorResource%>&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('../includes/h.html?h='), bgcolor: '#6691B4"',  width: '100%', height: '100%'});
		lzHistEmbed('../includes');
	</SCRIPT>
</BODY>
</HTML>

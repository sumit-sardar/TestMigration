<%@ page import="java.io.*, java.util.*"%>
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
<SCRIPT SRC="includes/embed.js" type="text/javascript">
</SCRIPT>
<script type="text/javascript">
<!--
var _LDB_BypassSetFocusOnPageLoad = 0;
function showFootnote(header)
{
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
    String eliminatorResource = request.getAttribute("eliminatorResource") != null ? (String) request.getAttribute("eliminatorResource")  : "resources/eliminator.swf";
    String url = request.getRequestURL().toString().trim();
    int lastSlash = url.lastIndexOf("/");
    url = url.substring(0,lastSlash).trim().replaceAll("https:","http:").replaceAll(":443",":80");
%>

<!--SA041005 start -->

<BODY topmargin=0 leftmargin=0 rightmargin=0 bottommargin=0 scroll=no onload="//loadSubtest('subtest')" bgcolor="#6691B4">
<!--SA041005 end -->
	<SCRIPT>
		//This method will include the required SWF file in the html page. The function exists in the embed.js file

		lzEmbed({url: 'TestClient.lzx.swf?lzt=swf&servletUrl=<%=url%>&eliminatorResource=<%=eliminatorResource%>&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('includes/h.html?h='), bgcolor: '#6691B4"',  width: '100%', height: '100%'});
		lzHistEmbed('includes');
	</SCRIPT>
</BODY>
</HTML>

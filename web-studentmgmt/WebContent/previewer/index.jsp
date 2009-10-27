
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
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
xmlStr=""
var laszlouid = 0;
var req;
var xmlDoc;

function showFootnote(header)
{
   	lzSetCanvasAttribute("footnotedata", header);
}


function closeBrowser()
{
    parent.close();
}

function updateLDB()
{
    // do nothing, need this since TestClient.lzx.swf calls it. 
}

</script>

</HEAD>
<%
    String eliminatorResource = "eliminator.swf";
    String url = request.getRequestURL().toString().trim();
    int lastSlash = url.lastIndexOf("/");
    url = url.substring(0,lastSlash).trim().replaceAll("https:","http:").replaceAll(":443",":80");
%>

<!--SA041005 start -->

<BODY topmargin=0 leftmargin=0 rightmargin=0 bottommargin=0 scroll=no onload="//loadSubtest('subtest')" bgcolor="#6691B4">
<!--SA041005 end -->
	
    <SCRIPT>
		//This method will include the required SWF file in the html page. The function exists in the embed.js file
        
	//	lzEmbed({url: 'TestClient.lzx.swf?lzt=swf&servletUrl=https://oasdev.ctb.com/StudentManagementWeb/previewer&eliminatorResource=<%=eliminatorResource%>&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('includes/h.html?h='), bgcolor: '#6691B4"',  width: '100%', height: '100%'});
    //  lzEmbed({url: 'TestClient.lzx.swf?lzt=swf&servletUrl=http://oastest1.ctb.com/StudentManagementWeb/previewer&eliminatorResource=<%=eliminatorResource%>&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('includes/h.html?h='), bgcolor: '#6691B4"',  width: '100%', height: '100%'});
     // lzEmbed({url: 'TestClient.lzx.swf?lzt=swf&servletUrl=http://127.0.0.1:7001/StudentManagementWeb/previewer&eliminatorResource=<%=eliminatorResource%>&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('includes/h.html?h='), bgcolor: '#6691B4"',  width: '100%', height: '100%'});
        lzEmbed({url: 'TestClient.lzx.swf?lzt=swf&servletUrl=<%=url%>&eliminatorResource=<%=eliminatorResource%>&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('includes/h.html?h='), bgcolor: '#6691B4"',  width: '100%', height: '100%'});
        //lzEmbed({url: 'TestClient.lzx?lzt=swf&servletUrl=StudentManagementWeb/previewer&eliminatorResource=<%=eliminatorResource%>&__lzhistconn='+top.connuid+'&__lzhisturl=' + escape('includes/h.html?h='), bgcolor: '#6691B4"',  width: '100%', height: '100%'});
		lzHistEmbed('includes');
	</SCRIPT>
</BODY>
</HTML>

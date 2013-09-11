<!doctype html public "-//w3c/dtd HTML 4.0//en">
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.lang.reflect.*" %>

<html>
<head><title>Clusterable Session Object Test</title></head>
<body bgcolor="#FFFFFF">
<p>
<font face="Helvetica">

<h2>
<font color=#DB1260>
Check Clusterable Session Objects
</font>
</h2>

<p>This jsp tests whether the attributes held in the current HTTP session are serialisable.
In order for web-applications to be capable of being replicated using WebLogic's in-memory clustering,
all session attributes must be serialisable.</p>
<p>
<p>Therefore the object's class must implement the <font face="Courier">java.io.Serializable</font> interface.
However, simply declaring that a class implements this interface is not sufficient and can often lead
to problems when trying to cluster a web application for the first time. This JSP can be used to help
diagnose such problems more quickly.</p>
<p>
<p>In order to guarantee that the session is truely serialisable this page tests each session attribute
by actually attempting to serialise it.
</p>

</p>

<br>

<%
boolean clusterable= true;
int attrNum= 0;
ByteArrayOutputStream baos = null;
ObjectOutputStream oos = null;
byte[] array = null;
ByteArrayInputStream bais = null;
ObjectInputStream ois = null;
Object deserializedObj = null;
String attrName= null;
Object attrValue= null;
String impSerStr= null;
String doesSerStr= null;
String pctor = null;
int size = 0;
int totalSize = 0;
Enumeration attrs = pageContext.getAttributeNamesInScope (pageContext.SESSION_SCOPE);
%>
<center>
<table border=1 cellspacing=2 cellpadding=5 bgcolor=#EEEEEE>
<tr>
<th>#</th>
<th>Attribute Name</th>
<th>Class Name</th>
<th>Serializable</th>
<th>Serialises Okay</th>
<th>No args C'TOR</th>
<th>Bytes</th>
</tr>
<%
while (attrs.hasMoreElements())
{
attrName= (String)attrs.nextElement();
attrValue= pageContext.getAttribute (attrName, pageContext.SESSION_SCOPE);
if (attrValue instanceof Serializable)
{
impSerStr= "Yes";
doesSerStr= "Yes";

pctor="<font color=\"red\"><b>No</b></font>";

try
{
baos = new ByteArrayOutputStream (2048);
oos = new ObjectOutputStream (baos);
oos.writeObject (attrValue);
oos.flush();
array = baos.toByteArray();
bais = new ByteArrayInputStream (array);
ois = new ObjectInputStream (bais);
deserializedObj = ois.readObject();

/* Public contstructor check */
Constructor[] ctors = deserializedObj.getClass().getConstructors();
for (int i=0; i< ctors.length; i++) {
if ( ( (Constructor) ctors[i]).getParameterTypes().length == 0) {
pctor="Yes";
}
}

if (!pctor.equalsIgnoreCase("Yes")) {
clusterable=false;
}

size = array.length;
totalSize += size;
}
catch (NotSerializableException nse)
{
doesSerStr= "<font color=\"red\">No </font><i>(See stack trace on console)</i>";
clusterable= false;
application.log("SerializationTestServlet.jsp DIAG: Attribute #" + attrNum + " failed to serialise: Name=" + attrName +
", Class=" + attrValue.getClass().getName() + " - " + nse.getMessage() + " ...");
nse.printStackTrace();
}
catch (Exception e)
{
doesSerStr= "<i><b>Error:</b>&nbsp;<font size=-1>" + e.getMessage() +"</font></i>";
clusterable= false;
application.log("SerializationTestServlet.jsp DIAG: Attribute #" + attrNum + " unexpected failure: Name=" + attrName +
", Class=" + attrValue.getClass().getName() + " - " + e.getMessage());
e.printStackTrace();
}
}
else
{
clusterable= false;
impSerStr= "<font color=\"red\"><b>No</b></font>";
doesSerStr= "<i>n/a</i>";
}
%>
<tr>
<td><%=attrNum++ %></td>
<td><%=attrName%></td>
<td><%=attrValue.getClass().getName()%></td>
<td><%=impSerStr%></td>
<td><%=doesSerStr%></td>
<td><font size="-2"><%=pctor%></font></td>
<td><%=size%></td>
</tr>

<%
}
%>
</table>
<br>
<%
if (clusterable)
{
%>
<font size="+2" color="blue">Your HTTP session supports clustering</font>
<%
}
else
{
%>
<font size="+2" color="red"></font>
<%
}

long mbConv = 1048576;

%>
<br>
<p>The total size of the objects in the HTTPSession is <%= totalSize %> bytes</p>

<h3>Runtime Data</h3>
<table border=1 cellspacing=2 cellpadding=5 bgcolor=#EEEEEE>
<tr><td align="left">The memory in use</td><td><%= ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / mbConv) %> Mb</td></tr>
<tr><td align="left">The current free memory</td><td><%= Runtime.getRuntime().freeMemory() / mbConv %> Mb</td></tr>
<tr><td align="left">The total memory currently allocated</td><td><%= Runtime.getRuntime().totalMemory() / mbConv %> Mb</td></tr>
<tr><td align="left">The maximum memory available</td><td><%= Runtime.getRuntime().maxMemory() / mbConv %> Mb</td></tr>
<tr><td align="left">CPU Count</td><td><%= Runtime.getRuntime().availableProcessors() %></td></tr>
</table>
<hr>
</center>
<p>

</body>
</html>

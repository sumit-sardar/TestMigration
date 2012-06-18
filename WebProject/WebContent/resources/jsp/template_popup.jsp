<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>OAS</title>


    <link href="<%=request.getContextPath()%>/resources/css/flexigrid.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/legacy.css" type="text/css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/resources/css/widgets.css" type="text/css" rel="stylesheet" />

    <link href="<%=request.getContextPath()%>/resources/css/jquery-ui-1.8.6.custom.css" type="text/css" rel="stylesheet" />

    <link href="<%=request.getContextPath()%>/resources/css/tabs.css" type="text/css" rel="stylesheet" />

    <link href="<%=request.getContextPath()%>/resources/css/popup.css" type="text/css" rel="stylesheet" />

    <link href="<%=request.getContextPath()%>/resources/css/autosuggest.css" type="text/css" rel="stylesheet" />

    <link href="<%=request.getContextPath()%>/resources/css/jquery.treeview.css" type="text/css" rel="stylesheet" />


    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/widgets.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/js_web.js"></script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/flexigrid.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/json2.js"></script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-ui-1.8.6.custom.min.js"></script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/tabs.js"></script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/popup.js"></script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jtip.js"></script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.autocomplete.js"></script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.cookie.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.treeview.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/demo.js"></script>

<style>

	body
		{
		font-family: Arial, Helvetica, sans-serif;
		font-size: 12px;
		}


</style>

</head>

  <body>
    <jsp:include page="/resources/jsp/header.jsp" />
    
    <jsp:include page="/resources/jsp/navigation.jsp" />
    
    <table class="legacyBodyLayout">
    <tr>
        <td id="legacySideNav">
            <jsp:include page="/resources/jsp/sidebar.jsp" />
        </td>
        <td id="legacyBody">

            <netui-template:includeSection name="bodySection"/>
        
        </td>
    </tr>
    </table>
  
    <jsp:include page="/resources/jsp/footer.jsp" />  
  
  </body>

</html>

<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>

<head>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">

  	<title>User Logout</title> 
  	<script type="text/javascript">
        // Browser Back Button
        window.history.forward(1); // this works for IE standalone
        window.onbeforeunload = confirmBrowseAway; //the code from here down
        // was needed to 'trick' firefox 2.x to work too
        function confirmBrowseAway()
        {
            if (1 == 1) {
                // do nothing to avoid prompt, otherwise return "some message";
            }
        }
        history.go(1);
        window.location = "/SessionWeb/logout2.jsp";
    </script>
</head>

<body>
</body>
</html>
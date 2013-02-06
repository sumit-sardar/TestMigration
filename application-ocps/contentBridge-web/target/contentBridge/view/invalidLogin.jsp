<% 
/* response.setHeader("Cache-Control","no-store"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader ("Expires", 0);   */
%>

<%@ page language="java" 
      contentType="text/html; charset=windows-1256"
      pageEncoding="windows-1256"
   %>

   <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"    
      "http://www.w3.org/TR/html4/loose.dtd">

   <html>

      <head>
       <meta http-equiv="refresh" content="6; URL=http://192.168.14.103:8080/contentBridge"> 

<script type="text/javascript">
function callme(){
	window.location="http://localhost:8080/contentBridge";
}
var countdown;
var countdown_number;

function countdown_init(x) {
    countdown_number = x;
    countdown_trigger();
}

function countdown_trigger() {
    if(countdown_number > 0) {
        countdown_number--;
        document.getElementById('countdown_text').innerHTML = countdown_number;
        if(countdown_number > 0) {
            countdown = setTimeout('countdown_trigger()', 1000);
        }
    }
}

function countdown_clear() {
    clearTimeout(countdown);
}

</script>
         <title>Invalid Login</title>
      </head>
	
      <body bgcolor="GREY" text="PINK" onload="return countdown_init(6);">
         <center>
            Sorry, you are not a registered user! <br><br>Please sign up first
            <br><br><br>
             <pre> To redirect page : <a href="#" onclick="return callme();">Click here</a> Otherwise it will be redirected in <span id="countdown_text" style="font-size: medium;font-weight: bold;color: white;"></span> seconds</pre>
         </center>
     
      </body>
      </html>
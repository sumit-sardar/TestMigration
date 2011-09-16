<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>

<table class="sortable">

    <tr class="sortable">
        <td style="background-color : #CCCC99">
        
      
		
	 <table class="transparent">
		<tr class="transparent">
	        <td  colspan="12" style="height:5px; color: #336699; font-family: Arial,Verdana,Sans Serif; font-size: 13px;  font-style: normal;  font-weight: bold;">&nbsp;
	             
	        </td>
   	 	</tr>
	    <tr class="transparent">
	        <td class="transparent"  valign="middle">
	      	<div  id= "searchheader" style="visibility:hidden; background:#990000; height:25px;  color: #FFFFFF; font-family: Arial,Verdana,Sans Serif; font-size: 13px;  font-style: normal;  font-weight: bold; vertical-align:middle;">&nbsp;Student Search</div>
	    	<!--<script>call();</script>
       		 --><div id = "orgNodeHierarchy" style="overflow:auto; height: 270px;  width: 250px; font-family: Arial, Verdana, Sans Serif; font-size: 13px; font-style: normal; font-weight: normal;">
				
			</div> 
			
		 </td>
		 	<td class="transparent" width="100px">&nbsp;</td>
		 	 <td >
	      
	    	<table id="list2" ></table>
			<div id="pager2"  ></div>
			
		 </td>
	    </tr>
	</table>
	

        </td>
    </tr>
</table>
</br>
</br>
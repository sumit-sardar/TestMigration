<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="dto.PathNode"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="ctb-widgets.tld" prefix="ctb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>


<table class="simple">
    <tr class="transparent">
        
        
<!-- column 1 -->
<td class="transparent-top" width="50%" valign="top">
<table class="transparent">
    <tr class="transparent">
        <td class="transparent " width="300"><b>Attainable Goal within Program Year – Primary</b></td>
                          
    </tr>
   <tr class="transparent">
   <td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             <select style="width:280px" >
	             <Option selected="true">None</Option>
	             <Option>Improve basic skills</Option>
	 			 <Option>Improve English skills</Option>
	 			 <Option>H.S. Diploma/GED</Option>
	 			 <Option>Get a Job</Option>
	 			 <Option>Retain a Job</Option>
	 			 <Option>Enter college or training</Option>
	 			 <Option>Work-based project</Option>
	 			 <Option>Family goal</Option>
	 			 <Option>U.S. Citizenship</Option>
	 			 <Option>Military</Option>
	 			 <Option>Personal goal</Option>
	 			 <Option>Other</Option>
	            </select>
        </td>   
        </tr>           
    <tr class="transparent">
        <td class="transparent " width="300"><b>Attainable Goal within Program Year – Secondary</b></td>
                         
    </tr>
    <tr class="transparent">
    <td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             <select style="width:280px" >
	             <Option selected="true">None</Option>
	             <Option>Improve basic skills</Option>
	 			 <Option>Improve English skills</Option>
	 			 <Option>H.S. Diploma/GED</Option>
	 			 <Option>Get a Job</Option>
	 			 <Option>Retain a Job</Option>
	 			 <Option>Enter college or training</Option>
	 			 <Option>Work-based project</Option>
	 			 <Option>Family goal</Option>
	 			 <Option>U.S. Citizenship</Option>
	 			 <Option>Military</Option>
	 			 <Option>Personal goal</Option>
	 			 <Option>Other</Option>
             </select>
        </td> 
        </tr>              
   
    <tr class="transparent">
        <td class="transparent " width="210"><b>Provider Use </b></td>
        
    </tr>
    <tr class="transparent">
    <td class="transparent-small">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text"  maxlength="64" style="width:280px"/></td>
    </tr>
</table>
</td>
<!-- column 2 -->
<td class="transparent-top" width="50%" valign="top">
<table class="transparent">
	<tr class="transparent">
        <td class="transparent-small" width="210"><b>Special Programs </b></td>
    </tr>
    <tr class="transparent">
        <td class="transparent">
        <table class="transparent">
        	<tr class="transparent">
	        	 <td class="transparent-small">
	             <input type="checkbox" name="" id = "none" value ="None" checked="true" >None</input>
	             </td>
            </tr>
            <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "community" value ="Community Corrections" checked="false" >Community Corrections</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "state" value ="State Corrections" checked="false" >State Corrections</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "homeless" value ="Homeless Program" checked="false" >Homeless Program</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "family" value ="Workplace Ed." checked="false" >Workplace Ed.</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "family" value ="Tutoring" checked="false" >Tutoring</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "family" value ="Distance Learning" checked="true" >Distance Learning</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "family" value ="Special Needs" checked="true" >Special Needs</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "family" value ="Alternative Ed. (K12)" checked="false" >Alternative Ed. (K12)</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "family" value ="Non-traditional Training" checked="false" >Non-traditional Training</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "family" value ="El Civics" checked="false" >El Civics</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "family" value ="Carl Perkins" checked="false" >Carl Perkins</input>
             </td>
            </tr>
             <tr class="transparent">
	        	 <td class="transparent-small">
             <input type="checkbox" name="" id = "family" value ="Other" checked="false" >Other</input>
             </td>
            </tr>
        </table>        
        </td>                                
    </tr>
 
    
</table>
</td>

</tr>
</table>




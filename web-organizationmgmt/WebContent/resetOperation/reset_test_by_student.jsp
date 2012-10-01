<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb"%>
<lb:bundle baseName="organizationApplicationResource" />
<div id="reset_show_by_student" style="display: none; width: 99%;">


<table class="sortable">

	<tr>
		<td><BR />
		<BR />
		</td>
	</tr>
	<tr>
		<td class="transparent"><b>Step1:</b> Specify a test access code and click <b>Search</b>. <BR />
		<BR />
		<table>
			<tr >
					<td ><span >*</span>&nbsp;Student Login:</td>
					<td width="*">
						<input type="text" tabindex="1" maxlength="32" name="studentLoginId"> </input>
					</td>
				</tr>
				<tr >
					<td >Access Code:</td>
					<td  width="*">
						<input type="text" tabindex="2" maxlength="32" name="studentTestAccessCode"> </input>
					</td>
					 <td  width="*">
					 &nbsp;&nbsp;<input class="ui-widget-header" type="button"  value="Search"	type="submit" onClick="" tabindex="3" />
					
					</td>
				</tr>
		</table>
		<BR />
		</td>
	</tr>

</table>








</div>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />
<netui-data:declareBundle bundlePath="webResources" name="web" />

<table>
	<tr class="transparent">
		<!-- column 1 -->
		<td style="vertical-align: top;" width="310">
		<table>
			<tbody>
				<tr class="transparent">
					<td nowrap="" width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.addressLine1" suffix=":" /></td>
					<td class="transparent"><input type="text" style="width: 200px;" maxlength="64" id="profileAddressLine1" name="profileAddressLine1"></td>
				</tr>
				<tr class="transparent">
					<td nowrap="" width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.addressLine2" suffix=":" /></td>
					<td class="transparent"><input type="text" style="width: 200px;" maxlength="64" id="profileAddressLine2" name="profileAddressLine2"></td>
				</tr>
				<tr class="transparent">
					<td nowrap="" width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.city" suffix=":" /></td>
					<td class="transparent"><input type="text" style="width: 200px;" maxlength="64" id="profileCity" name="profileCity"></td>
				</tr>
				<tr class="transparent">
					<td width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.state" suffix=":" /></td>
					<td class="transparent"><select id="profileStateOptions" name="profileStateOptions" style="width: 202px;"></select></td>
				</tr>
				<tr class="transparent">
					<td width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.zip" suffix=":" /></td>
					<td class="transparent"><input type="text" onkeypress="return constrainNumericChar(event);"
						style="width: 50px;" maxlength="5" id="profileZipCode1" name="profileZipCode1"> - <input type="text"
						onkeypress="return constrainNumericChar(event);" style="width: 50px;" maxlength="5" id="profileZipCode2" name="profileZipCode2">
					</td>
				</tr>
			</tbody>
		</table>
		</td>

		<td style="vertical-align: top;">
		<table>
			<tbody>
				<tr class="transparent">
					<td width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.primaryPhone" suffix=":" /></td>
					<td class="transparent"><input type="text" onkeypress="return constrainNumericChar(event);"
						style="width: 40px;" maxlength="3" id="profilePrimaryPhone1" name="profilePrimaryPhone1"> - <input type="text"
						onkeypress="return constrainNumericChar(event);" style="width: 40px;" maxlength="3" id="profilePrimaryPhone2"
						name="profilePrimaryPhone2"> - <input type="text" onkeypress="return constrainNumericChar(event);"
						style="width: 40px;" maxlength="4" id="profilePrimaryPhone3" name="profilePrimaryPhone3"> <lb:label key="dialog.myProfile.ext"
						suffix=":" /> <input type="text" onkeypress="return constrainNumericChar(event);" style="width: 40px;"
						maxlength="4" id="profilePrimaryPhone4" name="profilePrimaryPhone4"></td>
				</tr>
				<tr class="transparent">
					<td width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.secondaryPhone" suffix=":" /></td>
					<td class="transparent"><input type="text" onkeypress="return constrainNumericChar(event);"
						style="width: 40px;" maxlength="3" id="profileSecondaryPhone1" name="profileSecondaryPhone1"> - <input type="text"
						onkeypress="return constrainNumericChar(event);" style="width: 40px;" maxlength="3" id="profileSecondaryPhone2"
						name="profileSecondaryPhone2"> - <input type="text" onkeypress="return constrainNumericChar(event);"
						style="width: 40px;" maxlength="4" id="profileSecondaryPhone3" name="profileSecondaryPhone3"> <lb:label key="dialog.myProfile.ext"
						suffix=":" /> <input type="text" onkeypress="return constrainNumericChar(event);" style="width: 40px;"
						maxlength="4" id="profileSecondaryPhone4" name="profileSecondaryPhone4"></td>
				</tr>
				<tr class="transparent">
					<td width="110" class="transparent alignRight"><lb:label key="dialog.myProfile.faxNumber" suffix=":" /></td>
					<td class="transparent"><input type="text" onkeypress="return constrainNumericChar(event);"
						style="width: 40px;" maxlength="3" id="profileFaxNumber1" name="profileFaxNumber1"> - <input type="text"
						onkeypress="return constrainNumericChar(event);" style="width: 40px;" maxlength="3" id="profileFaxNumber2"
						name="profileFaxNumber2"> - <input type="text" onkeypress="return constrainNumericChar(event);"
						style="width: 40px;" maxlength="4" id="profileFaxNumber3" name="profileFaxNumber3"></td>
				</tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
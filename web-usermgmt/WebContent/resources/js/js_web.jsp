<%@ page import="java.io.*, java.util.*"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui-data:declareBundle bundlePath="oasResources" name="oas"/>
<netui-data:declareBundle bundlePath="webResources" name="web"/>
<netui-data:declareBundle bundlePath="widgetResources" name="widgets"/>
<% response.setContentType("application/x-javascript"); %>

function setFocusFirstElement() { 
    var element = document.getElementById("firstFocusId");
    if (element) {
        try{
            setFocus(element.value);
        }
        catch(err){
        }
    }
}

function setFocus(id) { 
    var element = document.getElementById(id);
    if (element != null) {
        element.focus();
    }
}

function handleEnterKey(elementId, value) {
  if (window.event && window.event.keyCode == 13) {
        var element = document.getElementById(elementId);
        if(element) {
            element.value = value;
            var actionElement = document.getElementById('actionElement');
            if(actionElement) {
                actionElement.value = elementId;
            }
            element.form.submit();
            return false;
        }        
  }
  return true;
}

function verifyDeleteUser(){
    var ret = confirm("Click 'OK' to delete this user's profile from your organization.");    
    if (ret == true) {
        setElementValue('currentAction', 'deleteUser');
        return true;
    }
    return false;    
}

function verifyExitAddUser(){
    var ret = confirm("Click 'OK' to quit editing user's information. Any changes you've made will be lost.");
    if (ret == true) {
	    var isUsrAcctMgr;
	    <%if( null!= session.getAttribute("isUsrAcctMgr")) {%>
		    isUsrAcctMgr = <%=session.getAttribute("isUsrAcctMgr").toString()%>;
		<%}%>
		if(isUsrAcctMgr == true) { 
	    	jQuery.ajax({
              	  async : false,
                  url : 'removeSessionVariable.do',
                  type : 'POST',
                  success : function(data, textStatus, XMLHttpRequest) {
                  	  console.log("Variable Deleted");
				  },
                  error : function(XMLHttpRequest, textStatus, errorThrown) {
                      console.log("Exception Occurred" + textStatus);
                }
	         });
   		}
        return true;
    }
    return false; 
}


function isBrowerTypeIE()
{
    var agt = navigator.userAgent.toLowerCase();
    return (agt.indexOf("msie") != -1);
}

function isBrowerTypeFirefox()
{
    var agt = navigator.userAgent.toLowerCase();
    return (agt.indexOf("firefox") != -1);
}

function isBrowerTypeMac()
{
    var agt = navigator.userAgent.toLowerCase();
    return (agt.indexOf("mac") != -1);
}

function updateOrgNodeSelection(element)
{
    var isUsrAcctMgr;
    var isAddUser = null; 
    var isEditUser = null;
	var postDataObject = {};
	if(undefined != document.getElementById("addUserOperation"))
		isAddUser = document.getElementById("addUserOperation").value;
	if(undefined != document.getElementById("editUserOperation"))
    	isEditUser = document.getElementById("editUserOperation").value;
	
	<%if( null!= session.getAttribute("isUsrAcctMgr")) {%>
	    isUsrAcctMgr = <%=session.getAttribute("isUsrAcctMgr").toString()%>;
	<%}%>
	if(isUsrAcctMgr == true) {
           if (element.checked) {
           postDataObject.selectedNodesOrgNodeId = element.value;
               jQuery.ajax({
               	   async : false,
                   url : 'checkHasCustomerExternalSchoolIdConfigurable.do',
                   type : 'POST',
                   dataType : 'text',
                   data : postDataObject,
                   success : function(data, textStatus, XMLHttpRequest) {
                   		if(data != undefined && data != null) {
							var x = data.split('#')[0].replace(/\s+/, "");
							var y;
							if(data.split('#').length > 1) {
								y = data.split('#')[1].replace(/\s+/, "");
							}
	                    	if(x == 'true') {
	                    		if (document.getElementById("extSchoolIdTR") != undefined) {
	                    			document.getElementById("extSchoolIdTR").style.display = "table-row";
	                    			document.getElementById("extSchoolId").disabled = false;
	                    			if(y != undefined && y != null && y != 'null' && y != '') {
	                    				document.getElementById("extSchoolId").value = y;
	                    			}
	                    		}
	                    	}
	                    	else {
	                    		document.getElementById("extSchoolIdTR").style.display = "none";
	                    		document.getElementById("extSchoolId").disabled = true;
	                    	}
                   		}else{
                   			document.getElementById("extSchoolIdTR").style.display = "none";
	                    	document.getElementById("extSchoolId").disabled = true;
                   		}
					},
	                error : function(XMLHttpRequest, textStatus, errorThrown) {
	                    console.log("Request failed: " + textStatus);
	                }
               });
           }
           if (!element.checked) {
           postDataObject.selectedNodesOrgNodeId = element.value;
           postDataObject.extSchoolIdCurrentValue = document.getElementById("extSchoolId").value;
           		jQuery.ajax({
               	   async : false,
                   url : 'uncheckHasCustomerExternalSchoolIdConfigurable.do',
                   type : 'POST',
                   dataType : 'text',
                   data : postDataObject,
                   success : function(data, textStatus, XMLHttpRequest) {
                   		if(data != undefined && data != null) {
							var x = data.split('#')[0].replace(/\s+/, "");
							var y;
							if(data.split('#').length > 1) {
								y = data.split('#')[1].replace(/\s+/, "");
							}
	                    	if(x == 'true') {
	                    		if (document.getElementById("extSchoolIdTR") != undefined) {
	                    			document.getElementById("extSchoolIdTR").style.display = "table-row";
	                    			document.getElementById("extSchoolId").disabled = false;
	                    			if(y != undefined && y != null && y != 'null' && y != '') {
	                    				document.getElementById("extSchoolId").value = y;
	                    			}
	                    		}
	                    	}
	                    	else {
	                    		document.getElementById("extSchoolIdTR").style.display = "none";
	                    		document.getElementById("extSchoolId").disabled = true;
	                    	}
                   		}else{
                   			document.getElementById("extSchoolIdTR").style.display = "none";
	                    	document.getElementById("extSchoolId").disabled = true;
                   		}
					},
	                error : function(XMLHttpRequest, textStatus, errorThrown) {
	                    console.log("Request failed: " + textStatus);
	                }
                });
           }
	}	
    
    var showType = "block";    
    if (isBrowerTypeFirefox()) {
        showType = "table-row";
    }
    if (isBrowerTypeMac()) {
        showType = "table-row";
    }
        
	var checked = element.checked;
    var orgNodeId = element.value;    
    
    var messageRow = document.getElementById("message");
    var table = document.getElementById("orgTable");

	if (checked == true) {
        messageRow.style.display = "none";

        for(var i=0; i < table.rows.length; i++) {
            var row = table.rows[i];
            var id = row.id;
            if (id == orgNodeId) {
                var selRow = document.getElementById(orgNodeId);
                selRow.style.display = showType;
            }
        }        
    }
	else {
        var checkedCount = 0;    
        for(var i=0; i < table.rows.length; i++) {
            var row = table.rows[i];
            var id = row.id;
            if (id == orgNodeId) {
                var selRow = document.getElementById(orgNodeId);
                selRow.style.display = "none";
            }
            if ((row.style.display == "block") || (row.style.display == "table-row")) {
                checkedCount = checkedCount + 1;
            }
        }        
        
        if (checkedCount == 0) {
            messageRow.style.display = showType;
        }
                
    }    
}

/////////////////////// path list /////////////////////////////

function setupOrgNodePath(orgId) 
{
    document.body.style.cursor = 'wait';
    
    var actionElement = document.getElementById('actionElement');
    actionElement.value = 'setupOrgNodePath';
    
    var currentAction = document.getElementById('currentAction');
    currentAction.value = orgId;
    
    actionElement.form.submit();
    return false;
}


function isDigit(charVal) 
{
	return (charVal >= '0' && charVal <= '9');
}

function constrainNumericChar(e) 
{
    var keyId = (window.event) ? event.keyCode : e.which;
    var keyValue = String.fromCharCode( keyId );
    var regExp = /\d/;    
    var results = false;
   
    if ( keyId == null || keyId == 0 || keyId == 8 || keyId == 9 || keyId == 13 || keyId == 27 ) {        
        results = true; // allow control characters
    } 
    else 
    if ( regExp.test(keyValue) ) {
        results = true;    
    }
    
    return results;
}

function focusNextControl(element)
{
    if (element.value.length < element.getAttribute('maxlength')) 
        return;

    var f = element.form;
    var els = f.elements;
    var len = els.length;
    var x, nextEl;
    
    for (var i=0 ; i < len ; i++) {
        x = els[i];
        if (element == x && (nextEl = els[i+1])) {
            if (nextEl.focus) 
                nextEl.focus();
            return;
        }
    }
}




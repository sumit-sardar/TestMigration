
var SelectedOrgNodeId;
var currentNodeId ;
var currentCategoryLevel;
var currentTreeArray;
var currentIndex;
var dataObj2=[];
var dataObj3=[];
var dataObj4;
var dataObj5;
var dataObj6;
var dataObj7;
var dataObj8;
var rootMap = {};
var rootNodeId;
var map = new Map();
var rootNode = [];
var type;
var asyncOver = 0;
var isPopUp = false;
var SelectedOrgName;

function getRootNodeDetails(){
	var noOfRoots = jsonData.length;
	for (var i = 0,j = noOfRoots; i < j; i++ ){
		rootMap[jsonData[i].attr.id] = jsonData[i].attr.cid;
		if(jsonData[i].attr.cid == "1"){
			rootNodeId = jsonData[i].attr.id;
		}					
		rootNode.push({data: jsonData[i].data,attr:{id: jsonData[i].attr.id,cid:jsonData[i].attr.cid,tcl:jsonData[i].attr.tcl},children : [{}]});
	}
}
function createSingleNodeScoringTree(node_id,jsondata) {
	   $("#"+node_id).jstree({
	        "json_data" : {	             
	            "data" : rootNode,
				"progressive_render" : true,
				"progressive_unload" : true
	        },
            "themes" : {
			    "theme" : "apple",
			    "dots" : false,
			    "icons" : false
			},       
	        "ui" : {  
	           "select_limit" : 1
         	}, 
				"plugins" : [ "themes", "json_data", "ui"]  
				
	    });

		$("#"+node_id).bind("loaded.jstree", 
		 	function (event, data) {
				for(var i = 0; i < rootNode.length; i++) {
					var orgcatlevel = rootNode[i].attr.cid;
					if(orgcatlevel == leafNodeCategoryId) {
						$("#"+node_id+" ul li").eq(i).find('.jstree-icon').hide();
		    		}
				}
			}
		);
		
		$("#"+node_id).delegate("a","click", function(e) {
			var isRootNode = false;
  			SelectedOrgNodeId = $(this).parent().attr("id");
  			var SelectedOrgname = $("#"+SelectedOrgNodeId).text();
  			$("#treeOrgNodeId").val(SelectedOrgNodeId);
  			$("#treeOrgName").val(SelectedOrgname);
 		    $("#displayMessageMain").hide();
 		    var gridOrg = $("#orgImmdRptGrid");
			var grid = $("#immdRptGrid");
 		    if(gridOrg[0] != undefined){
				for(var i = 0; i < rootNode.length; i++)
				{
					if (rootNode[i].attr.id == SelectedOrgNodeId)
					{
						isRootNode = true;
						$("#orgImmdRptGrid").hide();
						$("#gview_orgImmdRptGrid").hide();
						$("#orgImmdRptGridPager_left").hide();
						$("#orgImmdRptGridPager_center").hide();
						$("#orgImmdRptGridPager_right").hide();
						$("#orgImmdRptGridPager").hide();
						$("#gbox_orgImmdRptGrid").hide();
						document.getElementById('generateCSVButtonOrg').style.visibility = "hidden";
						document.getElementById('generatePDFButtonOrg').style.visibility = "hidden";
						
					}
					
				}
				if(!isRootNode)
				{
					$("#orgImmdRptGrid").show();
					$("#gview_orgImmdRptGrid").show();
					$("#orgImmdRptGridPager_left").show();
					$("#orgImmdRptGridPager_center").show();;
					$("#orgImmdRptGridPager_right").show();
					$("#orgImmdRptGridPager").show();
					$("#gbox_orgImmdRptGrid").show();
					document.getElementById('generateCSVButtonOrg').style.visibility = "visible";
					document.getElementById('generatePDFButtonOrg').style.visibility = "visible";
					processStudentTable();
				}
 		    }
 		    else if (grid[0] != undefined)
 		    {
 		    	processStudentTable();
 		    }		    
		});
		
		registerDelegate(node_id);

}


function registerDelegate(tree){
	$("#"+tree).delegate("li ins","click", function(e) {
	type = tree;
	var x = this.parentNode;		
	var categoryId  = x.getAttribute("tcl");
	if(categoryId != null || categoryId != undefined){
		currentCategoryLevel = categoryId ; 
	} else{
		currentCategoryLevel ="1";
	}
	currentNodeId = x.id;			
	var classState = $(x).hasClass("jstree-open");			
	var rootCategoryLevel = rootMap[currentNodeId];
	if (classState == false){
		if (currentCategoryLevel == 1) {	
			dataObj2 = [];	
			var indexOfRoot = getIndexOfRoot(currentNodeId);
			populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot);
		}
		var cacheData = map.get(currentNodeId);
		if (cacheData != null){
			currentTreeArray = cacheData;			
		}
		if (cacheData == null){
			switch(currentCategoryLevel){
			//Not caching at initial level because the whole data will be put in cache which may increase the cache size
			//considerably
			case "2": 	dataObj3 =getObject(jsonData,currentNodeId,currentCategoryLevel,x.parentNode.parentNode.id);
						currentIndex = dataObj3.index;
						currentTreeArray = dataObj3.jsonData;
						map.put(currentNodeId,currentTreeArray);
						break;
			case "3": 	dataObj4 = map.get(x.parentNode.parentNode.id);
						currentTreeArray =getObject(dataObj4,currentNodeId,currentCategoryLevel);
						currentIndex = currentTreeArray.index;
						currentTreeArray = currentTreeArray.jsonData;
						map.put(currentNodeId,currentTreeArray);
						break;
			case "4": 	dataObj5 = map.get(x.parentNode.parentNode.id);
						currentTreeArray =getObject(dataObj5,currentNodeId,currentCategoryLevel);
						currentIndex = currentTreeArray.index;
						currentTreeArray = currentTreeArray.jsonData;
						map.put(currentNodeId,currentTreeArray);
						break;
			case "5": 	dataObj6 = map.get(x.parentNode.parentNode.id);
						currentTreeArray =getObject(dataObj6,currentNodeId,currentCategoryLevel);
						currentIndex = currentTreeArray.index;
						currentTreeArray = currentTreeArray.jsonData;
						map.put(currentNodeId,currentTreeArray);
						break;
			case "6": 	dataObj7 = map.get(x.parentNode.parentNode.id);
						currentTreeArray =getObject(dataObj7,currentNodeId,currentCategoryLevel);
						currentIndex = currentTreeArray.index;
						currentTreeArray = currentTreeArray.jsonData;
						map.put(currentNodeId,currentTreeArray);
						break;
			case "7": 	dataObj8 =map.get(x.parentNode.parentNode.id);
						currentTreeArray =getObject(dataObj8,currentNodeId,currentCategoryLevel);
						currentIndex = currentTreeArray.index;
						currentTreeArray = currentTreeArray.jsonData;
						map.put(currentNodeId,currentTreeArray);
						break;	
			case "8": 	dataObj9 =map.get(x.parentNode.parentNode.id);
						currentTreeArray =getObject(dataObj9,currentNodeId,currentCategoryLevel);
						currentIndex = currentTreeArray.index;
						currentTreeArray = currentTreeArray.jsonData;
						map.put(currentNodeId,currentTreeArray);
						break;						
				}
			}
		}
	});
}

function getIndexOfRoot(currentNodeId){
	var numberOfRoots = jsonData.length;
	for (var i = 0,j = numberOfRoots; i < j; i++ ){
		if (jsonData[i].attr.id == currentNodeId){				
			return i;
		}
	}
}
function populateTreeImmediate(currentNodeId,currentCategoryLevel,indexOfRoot){	
	//TODO : Updation in root node is a problem need to work on that but this method is also necessary for populating the immediate //tree because if we use the cache all the 2nd level objects will be stored in cache which makes the cache very heavy
	var jsonObject = jsonData;
	jsonObject = jsonObject[indexOfRoot];
	if (dataObj2.length == 0 && jsonObject != undefined){
		for (var i = 0, j = jsonObject.children.length; i < j; i++ ){
			dataObj2.push({data: jsonObject.children[i].data,attr:{id: jsonObject.children[i].attr.id,cid:jsonObject.children[i].attr.cid, tcl:jsonObject.children[i].attr.tcl,chlen:jsonObject.children[i].hasOwnProperty("children")},children : [{}]});	
		}
	}
}

//method triggered from library
function customLoad(){
	pushInsideElement(currentNodeId,currentCategoryLevel,dataObj2,currentTreeArray);
}


function pushInsideElement(currentNodeId,currentCategoryLevel,dataObj2,currentTreeArray){
	var objArray;
	switch(currentCategoryLevel){
		case "1": objArray = dataObj2;				  
				break;
		case "2": objArray = currentTreeArray.children;
				break;
		case "3": objArray = currentTreeArray.children;
				break;
		case "4": objArray = currentTreeArray.children;
				break;
		case "5": objArray = currentTreeArray.children;
				break;
		case "6": objArray = currentTreeArray.children;
				break;
		case "7": objArray = currentTreeArray.children;
				break;
		case "8": objArray = currentTreeArray.children;
					break;
	}
	updateTree(objArray);	
}
	  
function updateTree(objArray){
	//timer.setStartTime();
	var currentElement;
	if (isPopUp){
		currentElement = $('li[id='+ currentNodeId+ ']');
		if(currentElement.length < 2) currentElement = currentElement[0];
		else currentElement = currentElement[1];
	}else {		
		currentElement = document.getElementById(currentNodeId);
	}
	currentElement.className = "jstree-open jstree-unchecked";
	var fragment = document.createDocumentFragment();
	var ulElement = document.createElement('ul');
	if(type.indexOf("innerID") < 0){
		stream(objArray,ulElement,fragment,streamPush, null, function(){
		currentElement.appendChild(fragment);
		$(currentElement.childNodes[1]).removeClass('jstree-loading'); 
	 	});	
	} else{
		stream(objArray,ulElement,fragment,streamInnerPush, null, function(){
		currentElement.appendChild(fragment);
		$(currentElement.childNodes[1]).removeClass('jstree-loading'); 
		asyncOver++;
		openNextLevel(asyncOver);
		// currentElement.childNodes[1].firstChild.style.display = "none";
		 });	
	}
 }
	  
function stream(array,element,fragment,process,context,callback){
	var treeData = array.concat();   
	setTimeout(function(){
	var start = +new Date();
		do {
			process.call(context, treeData.shift(),element,fragment);
		} while (treeData.length > 0 && (+new Date() - start < 50));
		if (treeData.length > 0){
			setTimeout(arguments.callee, 25);
		} else {
			callback(array);
		}
	}, 25);    
	
}


//Seperated outer tree push and inner tree push because each method is a performance critical operation conditions will increase the 
// tree population.	  
function streamPush(objArray,ulElement,fragment){
	var liElement = document.createElement('li');
	liElement.setAttribute("id", objArray.attr.id);
	liElement.setAttribute("cid" , objArray.attr.cid);
	liElement.setAttribute("tcl" , objArray.attr.tcl);
	var condition = objArray.attr.chlen;
	switch(condition){
		case false: liElement.className = "jstree-leaf jstree-unchecked";
					break;
		case true:  liElement.className = "jstree-closed jstree-unchecked";
					break;
		case undefined: var con = objArray.hasOwnProperty("children");
						switch(con){
							case false: liElement.className = "jstree-leaf jstree-unchecked";
								break;
							case true:  liElement.className = "jstree-closed jstree-unchecked";
								break;						
						}
						break;
	}
	liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
	ulElement.appendChild(liElement);
	fragment.appendChild(ulElement);
}
	  
function streamInnerPush(objArray,ulElement,fragment){
	var liElement = document.createElement('li');
	liElement.setAttribute("id", objArray.attr.id);
	liElement.setAttribute("cid" , objArray.attr.cid);
	liElement.setAttribute("tcl" , objArray.attr.tcl);
	var condition = objArray.attr.chlen;
	switch(condition){
		case false: liElement.className = "jstree-leaf jstree-"+ getCheckedStatus(objArray.attr.id);
					break;
		case true:  liElement.className = "jstree-closed jstree-"+ getCheckedStatus(objArray.attr.id);
					break;
		case undefined: var con = objArray.hasOwnProperty("children");
							switch(con){
								case false: liElement.className = "jstree-leaf jstree-"+ getCheckedStatus(objArray.attr.id);
											break;
								case true:  liElement.className = "jstree-closed jstree-"+ getCheckedStatus(objArray.attr.id);
											break;						
							}
						  break;
		}	
		if (objArray.attr.cid == leafNodeCategoryId){
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: inline-block;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}else{
		liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}
		if(profileEditable === "false"  && $("#classReassignable").val() === "true") {
			liElement.innerHTML = "<ins class=\"jstree-icon\">&nbsp;</ins><a href=\"#\" class=\"\"><ins class=\"jstree-checkbox\" style=\"display: none;\">&nbsp;</ins><ins class=\"jstree-icon\">&nbsp;</ins>" + objArray.data + "</a> ";
		}
		ulElement.appendChild(liElement);
		fragment.appendChild(ulElement);
	  }
	  
function getObject(jsonObject,currentNodeId,currentCategoryLevel, parentNodeId){
	var obj = new Object();
	var indexRoot = getIndexOfRoot(parentNodeId);
	if(currentCategoryLevel == 2){
		for (var i = 0, j = jsonObject[indexRoot].children.length; i < j; i++ ){
			var attrId = jsonObject[indexRoot].children[i].attr.id;
			if (attrId == currentNodeId){
				obj.jsonData = jsonObject[indexRoot].children[i];
				obj.index = i;
				return obj;
			}
		}
	} else {
		for (var i = 0, j = jsonObject.children.length; i < j; i++ ){
			var attrId = jsonObject.children[i].attr.id;
			if (attrId == currentNodeId){
				obj.jsonData = jsonObject.children[i];
				obj.index = i;
				return obj;
			}
		}	
	}
}

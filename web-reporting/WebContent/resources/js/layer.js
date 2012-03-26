/**
 * Layer.js
 *
 * @author  Actuate Corporation
 * @version 1.0
 */

///////////////////////////////////////////////////////////////////////////////
// Globals
///////////////////////////////////////////////////////////////////////////////
var g_tagMarker = "$$$Marker";
var g_tagHash = "$$$Hash";
var g_tagArea = "$$$Area";
var g_tagBalloon = "$$$Balloon";

	
///////////////////////////////////////////////////////////////////////////////

function getTargetDocument() 
{
	if ( ns4 )
	{
		return document.layers[0];
	}
	else
	{
		return document;
	}
}

///////////////////////////////////////////////////////////////////////////////

function stripTag( inputString, tag ) 
{
	var stringValue = "";
	var pos = inputString.indexOf( tag );

	if ( pos != -1 )
	{
		stringValue = inputString.slice( 0, pos );
	}
	else
	{
		stringValue = inputString;
	}

	return stringValue;
}

///////////////////////////////////////////////////////////////////////////////

function getLeft( id, targetDocument )
{
	var left;
	var layer = null;

	if ( targetDocument == null )
	{
		targetDocument = getTargetDocument();
	}

	if ( ns4 )
	{
		layer = targetDocument.layers[id];
	}
	else
	{
		if ( elementLocate(targetDocument, id) )
			layer = elementLocate(targetDocument, id).style;
		else
			return -1;
	}

	if ( !layer )
		return -1;
	
	if ( ns4 )
		left = layer.left;
	else
		left = layer.left;

	return left;
}

///////////////////////////////////////////////////////////////////////////////

function getTop( id, targetDocument )
{
	var top;
	var layer;

	if ( targetDocument == null )
	{
		targetDocument = getTargetDocument();
	}

	if ( ns4 )
	{
		layer = targetDocument.layers[id];
	}
	else
	{
		if ( elementLocate(targetDocument, id) )
			layer = elementLocate(targetDocument, id).style;
		else
			return -1;
	}

	if ( !layer )
		return -1;

	if ( ns4 )
		top = layer.top;
	else
		top = layer.top;

	return top;
}

///////////////////////////////////////////////////////////////////////////////

function getWidth( id, targetDocument )
{
	var width;
	var layer;

	if ( targetDocument == null )
	{
		targetDocument = getTargetDocument();
	}

	if ( ns4 )
	{
		layer = targetDocument.layers[id];
	}
	else
	{
		if ( elementLocate(targetDocument, id) )
			layer = elementLocate(targetDocument, id).style;
		else
			return -1;
	}

	if ( !layer )
		return -1;

	if ( ns4 )
		width = layer.clip.width;
	else
		width = layer.width;

	return width;
}

///////////////////////////////////////////////////////////////////////////////

function getHeight( id, targetDocument )
{
	var height;
	var layer;

	if ( targetDocument == null )
	{
		targetDocument = getTargetDocument();
	}

	if ( ns4 )
	{
		layer = targetDocument.layers[id];
	}
	else
	{
		if ( elementLocate(targetDocument, id) )
			layer = elementLocate(targetDocument, id).style;
		else
			return -1;
	}

	if ( !layer )
		return -1;

	if ( ns4 )
		height = layer.clip.height;
	else
		height = layer.height;

	return height;
}

///////////////////////////////////////////////////////////////////////////////

function layerExists( id, targetDocument )
{
	var layer;

	if ( targetDocument == null )
	{
		targetDocument = getTargetDocument();
	}

	if ( ns4 )
	{
		layer = targetDocument.layers[id];	
	}
	else
	{
		if ( elementLocate(targetDocument, id) )
			layer = elementLocate(targetDocument, id).style;
		else
			return false;
	}


	return layer;
}

///////////////////////////////////////////////////////////////////////////////

function addpt (a, b)
{
	a = stripTag (a, "pt");
	b = stripTag (b, "pt");

	var x = new Number(a);
	var y = new Number(b);
	var result = x + y;

	return (result.toString() + "pt");
}

///////////////////////////////////////////////////////////////////////////////

function createLayer( id 
					, nestref
					, layerObject
					, left
					, top
					, width
					, height
					, content
					, bgColor
					, bgImage
					, visibility
					, zIndex
					) 
{
	
	if ( layerExists( id, null ) ) 
		return;

	if ( ns4 ) 
	{
		var lyr;

		if ( nestref ) 
		{
			lyr = eval( "document." + nestref + ".document." + id + " = new Layer(width, document."+nestref+")" )
		}
		else 
		{
			if ( layerObject == undefined )
			{
				lyr = new Layer( width, layerWindow );
			}
			else
			{
				lyr = layerObject;
			}

			document.layers[id] = lyr;
		}

		lyr.name	= id
		lyr.left	= left
		lyr.top		= top
		lyr.width	= width;
		lyr.height	= height;

		if ( width != null ) 
			lyr.clip.width = width;

		if ( height != null ) 
			lyr.clip.height = height;

		if ( bgColor != null ) 
			lyr.bgColor = bgColor;

		if ( bgImage != null )
			lyr.background.src = bgImage;

		lyr.visibility = ( visibility == 'hidden' ) ? 'hide' : 'show'

		if ( zIndex != null ) 
			lyr.zIndex = zIndex

		if ( content ) 
		{
			lyr.document.open()
			lyr.document.write(content)
			lyr.document.close()
		}
	}
	else
	{
		var divId = stripTag( id, g_tagMarker );
		divId = stripTag( divId, g_tagHash );
		divId = stripTag( divId, g_tagArea );
		divId = stripTag( divId, g_tagBalloon );

		// The object reference to the DIV represented to by 'id'
		var objRef = elementLocate(document, divId );

		// Special Changes for IE on the Mac, if attempting to create a nested DIV.
		var b_ieMacCreateLayer =  ((g_browserType.name == "IE" && g_browserType.platform == "Mac") && objRef)? true:false;
		
		// For IE on the Mac, get positioning relative to top-level document. 
		if ( b_ieMacCreateLayer ) 
		{
			var ref = objRef.offsetParent;
			while (ref) 
			{
				left = addpt(left, ref.style.left);
				top  = addpt(top, ref.style.top);
				ref = ref.offsetParent;
			}
		}

		var str = '\n<DIV id=' + id + ' style="position:absolute; left:' + left + '; top:' + top + '; width:' + width;

		if ( height != null ) 
		{
			str += '; height:' + height;
			str += '; clip:rect(0,' + width + ',' + height + ',0)';
		}

		if ( bgColor != null ) 
			str += '; background-color:' + bgColor;	
			
		if ( bgImage != null )
		{
			str += "; background-image:URL(" + "'" + bgImage + "')";
		}


		if ( zIndex == null )
		{
			// has to have a z-index of greater than the element (id) that this layer is 
			// being created upon.
			if (b_ieMacCreateLayer)
			{	
				zIndex = objRef.style.zIndex + 1;
				str += '; z-index:' + zIndex;
			}
		}
		else
		{
			str += '; z-index:' + zIndex;
		}
					
		if ( visibility ) 
			str += '; visibility:' + visibility;

		str += ';">' + ((content)?content:'') + '</DIV>';
	
		var insertionPlace = "BeforeEnd";
		
		// For IE 5.0 on MacOS, the HTML for this layer needs to inserted after the beginning
		// of the parent element. The insertAdjacentHTML behavior is inconsistent across IE on
		// Windows and MacOS.
		if (g_browserType.name == "IE" && g_browserType.platform == "Mac")
		{
			insertionPlace = "AfterBegin";	
		}
		
		if (nestref) 
		{
			index = nestref.lastIndexOf(".");
			var nestlyr = (index != -1)? nestref.substr(index+1) : nestref;
			HTMLInsert(elementLocate(document, nestlyr), insertionPlace, str);
		}
		else 
		{
			if ( objRef ) {
				// For the Mac, always insert in the top-level document.
				if (b_ieMacCreateLayer)
					HTMLInsert(document.body, insertionPlace, str)
				else {
					HTMLInsert(getElementParent(objRef), insertionPlace, str);					
				}
			}
			else {
				HTMLInsert(document.body, insertionPlace, str)	
			}	
		}
	}

}

///////////////////////////////////////////////////////////////////////////////

function deleteLayer( id, nestref, targetDocument ) 
{
	if ( targetDocument == null )
	{
		targetDocument = getTargetDocument();
	}

	if ( ns4 ) 
	{
		if ( nestref ) 
		{
			var str = "targetDocument." + nestref + ".document." + id
			eval( str + ".visibility = 'hide'" );
			eval( "delete " + str )
		}
		else 
		{
			if ( targetDocument.layers[id] )
			{
				targetDocument.layers[id].visibility = "hidden"
			}
		}
	}
	else
	{
		if ( elementLocate(targetDocument, id) )
		{
			elementDelete(elementLocate(targetDocument, id));
		}
	}
}

///////////////////////////////////////////////////////////////////////////////

function hideLayer( id, targetDocument ) 
{
	if ( targetDocument == null )
		targetDocument = getTargetDocument();

	if ( ns4 ) 
	{
		if ( targetDocument.layers[id] )
			targetDocument.layers[id].visibility = "hidden";
	}
	else
	{
		if ( elementLocate(targetDocument, id) )
		{
			elementLocate(targetDocument, id).style.visibility = "hidden";
		}
	}
}

///////////////////////////////////////////////////////////////////////////////

function showLayer( id, targetDocument ) 
{
	if ( targetDocument == null )
		targetDocument = getTargetDocument();

	if ( ns4 ) 
	{
		if ( targetDocument.layers[id] )
			targetDocument.layers[id].visibility = "visible";
	}
	else
	{
		if ( elementLocate(targetDocument, id) )
		{
			elementLocate(targetDocument, id).style.visibility = "visible";
		}
	}
}






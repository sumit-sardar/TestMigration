/**
 * Report.js
 *
 * @author  Actuate Corporation
 * @version 1.0
 */

///////////////////////////////////////////////////////////////////////////////
// Globals
///////////////////////////////////////////////////////////////////////////////

var ns4 = (document.layers)? true:false

parent.g_bReportFrameLoaded = false; 

var g_all = "";
var g_style = "";

var g_imageDir = "../images/viewer/";

var g_searchFrame;
var g_toolbarFrame;

var g_imgHash	= g_imageDir + "img_hash8.gif";
var g_imgBlank	= g_imageDir + "img_blank.gif";

var g_tagMarker = "$$$Marker";
var g_tagArea	= "$$$Area";
var g_tagBalloon= "$$$Balloon";

var g_areaIdList;
var g_areaClassIdList;
var g_hashIdList;
var g_hashDisplayList;
var g_hashClassIdList;
var g_bInSetupSearch = false;

var g_searchIdList;
var g_searchDisplayList;
var g_searchClassIdList;

var g_balloonIdList;

//////////////
/**************************************************************************
 Contains javascript encode and unencode functions.
***************************************************************************/

var hex = new Array(
	"%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07",
	"%08", "%09", "%0a", "%0b", "%0c", "%0d", "%0e", "%0f",
	"%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17",
	"%18", "%19", "%1a", "%1b", "%1c", "%1d", "%1e", "%1f",
	"%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27",
	"%28", "%29", "%2a", "%2b", "%2c", "%2d", "%2e", "%2f",
	"%30", "%31", "%32", "%33", "%34", "%35", "%36", "%37",
	"%38", "%39", "%3a", "%3b", "%3c", "%3d", "%3e", "%3f",
	"%40", "%41", "%42", "%43", "%44", "%45", "%46", "%47",
	"%48", "%49", "%4a", "%4b", "%4c", "%4d", "%4e", "%4f",
	"%50", "%51", "%52", "%53", "%54", "%55", "%56", "%57",
	"%58", "%59", "%5a", "%5b", "%5c", "%5d", "%5e", "%5f",
	"%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67",
	"%68", "%69", "%6a", "%6b", "%6c", "%6d", "%6e", "%6f",
	"%70", "%71", "%72", "%73", "%74", "%75", "%76", "%77",
	"%78", "%79", "%7a", "%7b", "%7c", "%7d", "%7e", "%7f",
	"%80", "%81", "%82", "%83", "%84", "%85", "%86", "%87",
	"%88", "%89", "%8a", "%8b", "%8c", "%8d", "%8e", "%8f",
	"%90", "%91", "%92", "%93", "%94", "%95", "%96", "%97",
	"%98", "%99", "%9a", "%9b", "%9c", "%9d", "%9e", "%9f",
	"%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6", "%a7",
	"%a8", "%a9", "%aa", "%ab", "%ac", "%ad", "%ae", "%af",
	"%b0", "%b1", "%b2", "%b3", "%b4", "%b5", "%b6", "%b7",
	"%b8", "%b9", "%ba", "%bb", "%bc", "%bd", "%be", "%bf",
	"%c0", "%c1", "%c2", "%c3", "%c4", "%c5", "%c6", "%c7",
	"%c8", "%c9", "%ca", "%cb", "%cc", "%cd", "%ce", "%cf",
	"%d0", "%d1", "%d2", "%d3", "%d4", "%d5", "%d6", "%d7",
	"%d8", "%d9", "%da", "%db", "%dc", "%dd", "%de", "%df",
	"%e0", "%e1", "%e2", "%e3", "%e4", "%e5", "%e6", "%e7",
	"%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee", "%ef",
	"%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7",
	"%f8", "%f9", "%fa", "%fb", "%fc", "%fd", "%fe", "%ff"
);

/**
* Encode a string to the "x-www-form-urlencoded" form, enhanced
* with the UTF-8-in-URL proposal. This is what happens:
*
* <ul>
* <li><p>The ASCII characters 'a' through 'z', 'A' through 'Z',
*        and '0' through '9' remain the same.
*
* <li><p>The unreserved characters - _ . ! ~ * ' ( ) remain the same.
*
* <li><p>The space character ' ' is converted into a plus sign '+'.
*
* <li><p>All other ASCII characters are converted into the
*        3-character string "%xy", where xy is
*        the two-digit hexadecimal representation of the character
*        code
*
* <li><p>All non-ASCII characters are encoded in two steps: first
*        to a sequence of 2 or 3 bytes, using the UTF-8 algorithm;
*        secondly each of these bytes is encoded as "%xx".
* </ul>
*
* @param s The string to be encoded
* @return The encoded string
*/

function encode(s)
{
	s = new String(s);
	var sbuf = new String("");

	var len = s.length;
	for (var i = 0; i < len; i++)
	{
		var ch = s.charAt(i);
		var chCode = s.charCodeAt(i);
		if ('A' <= ch && ch <= 'Z')
		{		// 'A'..'Z'
			sbuf+=ch;
		}
		else if ('a' <= ch && ch <= 'z')	// 'a'..'z'
		{
			sbuf+=ch;
		}
		else if ('0' <= ch && ch <= '9')	// '0'..'9'
		{
			sbuf+=ch;
		}
		else if (chCode <= 0x007f)	// other ASCII
		{
			sbuf+=hex[chCode];
		}
		else if (chCode <= 0x07FF)		// non-ASCII <= 0x7FF
		{
			sbuf+=(hex[0xc0 | (chCode >> 6)]);
			sbuf+=(hex[0x80 | (chCode & 0x3F)]);
		}
		else					// 0x7FF < ch <= 0xFFFF
		{
			sbuf+=(hex[0xe0 | (chCode >> 12)]);
			sbuf+=(hex[0x80 | ((chCode >> 6) & 0x3F)]);
			sbuf+=(hex[0x80 | (chCode & 0x3F)]);
		}
	}
	return sbuf;
}


function encodeNonBreakingCode(s)
{
	s = new String(s);
	var sbuf = new String("");
	var len = s.length;
	
	for (var i = 0; i < len; i++)
	{
		var ch = s.charAt(i);
		var chCode = s.charCodeAt(i);
		if (chCode == 160)
		{
			sbuf+=' ';
		}
		else
		{
			sbuf+=ch;
		}
	}
	return sbuf;
}

function decode(s)
{
	s = new String(s);
	var sbuf = new String("") ;
	var l  = s.length;
	var ch = -1 ;
	var b, sumb = 0;
	for (var i = 0, more = -1 ; i < l ; i++)
	{
		switch (ch = s.charAt(i))
		{
			case '%':
				 ch = s.charAt (++i) ;
				 var hb = (isDigit (ch)
						   ? ch - '0'
							 : 10+ (toLowerCase(ch)).charCodeAt(0) - (new String('a')).charCodeAt(0)) & 0xF ;
				 ch = s.charAt (++i) ;
				 var lb = (isDigit (ch)
						   ? ch - '0'
							 : 10+(toLowerCase(ch)).charCodeAt(0) - (new String('a')).charCodeAt(0)) & 0xF ;
				 b = (String.fromCharCode((hb << 4) | lb)).charCodeAt(0) ;
				 break ;
			case '+':
				 b = ' '.charCodeAt(0) ;
				 break ;
			default:
				 b = ch.charCodeAt(0);
		}
		if ((b & 0xc0) == 0x80)		// 10xxxxxx (continuation byte)
		{
			sumb = String.fromCharCode(((sumb.charCodeAt(0) << 6) | (b & 0x3f))) ;	// Add 6 bits to sumb
			if (--more == 0)
			{
				sbuf += sumb;
			}
		}
		else if ((b & 0x80) == 0x00)		// 0xxxxxxx (yields 7 bits)
		{
			sbuf+=String.fromCharCode(b);
		}
		else if ((b & 0xe0) == 0xc0)		// 110xxxxx (yields 5 bits)
		{
			sumb = String.fromCharCode(b & 0x1f);
			more = 1;						// Expect 1 more byte
		}
		else if ((b & 0xf0) == 0xe0)		// 1110xxxx (yields 4 bits)
		{
			sumb = String.fromCharCode(b & 0x0f);
			more = 2;						// Expect 2 more bytes
		}
		else if ((b & 0xf8) == 0xf0)		// 11110xxx (yields 3 bits)
		{
			sumb = String.fromCharCode(b & 0x07);
			more = 3;						// Expect 3 more bytes
		}
		else if ((b & 0xfc) == 0xf8)		// 111110xx (yields 2 bits)
		{
			sumb = String.fromCharCode(b & 0x03);
			more = 4;						// Expect 4 more bytes
		}
		else 								// 1111110x (yields 1 bit)
		{
			sumb = String.fromCharCode(b & 0x01);
			more = 5;						// Expect 5 more bytes
		}
	}
	return sbuf;
}

//returns true or false.
function isDigit(charVal)
{
	return (charVal >= '0' && charVal <= '9');
}

function toLowerCase(charVal)
{
	var str = new String(charVal);
	str = str.toLowerCase();
	return str.charAt(0);
}


/**
 *
 */

function htmlEncode(s)
{
	s = new String(s);
	var sbuf = new String("");

	var len = s.length;
	for (var i = 0; i < len; i++)
	{
		var ch = s.charAt(i);
		var chCode = s.charCodeAt(i);
		if (('A' <= ch && ch <= 'Z') || ('a' <= ch && ch <= 'z') || ('0' <= ch && ch <= '9'))
		{
			sbuf+=ch;
		}
		else if (ch == ' ')
		{
			sbuf+="&nbsp;";
		}
		else if(chCode <= 255)
		{
			sbuf+="&#"+chCode+";";
		}
		else
		{
			sbuf+=ch;
		}
	}
	return sbuf;
}

function formEncode(s)
{
	s = new String(s);
	var sbuf = new String("");

	var len = s.length;
	for (var i = 0; i < len; i++)
	{
		var ch = s.charAt(i);
		var chCode = s.charCodeAt(i);
		if (('A' <= ch && ch <= 'Z') || ('a' <= ch && ch <= 'z') || ('0' <= ch && ch <= '9'))
		{
			sbuf+=ch;
		}
		else if (ch == ' ')
		{
			sbuf+="&#"+"32"+";";
		}
		else if(chCode <= 255)
		{
			sbuf+="&#"+chCode+";";
		}
		else
		{
			sbuf+=ch;
		}
	}
	return sbuf;
}
///////////////////////////////////////////////////////////////////////////////

function initGlobals() 
{
	g_all = "";
	g_style = "";

	g_bInSetupSearch = false;
	g_imageDir  = "../images/viewer/";
	g_imgHash	= g_imageDir + "img_hash8.gif";
	g_imgBlank	= g_imageDir + "img_blank.gif";
	
	g_tagMarker = "$$$Marker";
	g_tagArea	= "$$$Area";
	g_tagBalloon= "$$$Balloon";
}

///////////////////////////////////////////////////////////////////////////////

function stripTag( inputString, tag ) 
{
	var stringValue = "";
	var pos = inputString.indexOf( tag );

	if ( pos != -1 )
		stringValue = inputString.slice( 0, pos );
	else
		stringValue = inputString;

	return stringValue;
}

///////////////////////////////////////////////////////////////////////////////

function removeFromHashList( id )
{
	for ( var index = 0; index < g_hashIdList.length; index++ )
	{
		var listId = g_hashIdList[index];

		if ( listId == id )
		{
			delete g_hashIdList[index];

			removeItem( g_hashIdList, index );
			removeItem( g_hashDisplayList, index );
			removeItem( g_hashClassIdList, index );
			
			break;
		}
	}
}

///////////////////////////////////////////////////////////////////////////////

function appendToHashList( id, display, classId )
{
	if ( findItem( g_hashIdList, id ) == -1 )
	{
		g_hashIdList[ g_hashIdList.length ] = id;
		g_hashDisplayList[ g_hashDisplayList.length ] = display;
		g_hashClassIdList[ g_hashClassIdList.length ] = classId;
	}
}

///////////////////////////////////////////////////////////////////////////////

function createArea( id, displayName, classId )
{
	if ( ! parent.g_bReportFrameLoaded )
			return;

	// Check if the class has already been added to the search
	if( ! g_bInSetupSearch )
	{
		var itemIndex = findItem( g_hashClassIdList, classId );
		if ( itemIndex != -1 )
			return;
	}		

	var str = "";
	
	// Make sure the original layer exists
	if ( ! layerExists( id, null ) )
		return;

	var areaId = id + g_tagArea;

	// If the layer was hidden, just make it visible
	if ( layerExists( areaId, document ) )
	{
		showLayer( areaId, document );
		return;
	}

	var left	= getLeft(   id, null );
	var top		= getTop(    id, null );
	var width	= getWidth(  id, null );
	var height	= getHeight( id, null );

	if( ns4 )
		height = height / 1.33;

	str += "<A ID='";
	str += id + "AreaAnchor";
	str += "' HREF='javascript:myVoid()' ONCLICK=";
	str += '"';
	str += "areaClick('";
	str += areaId;
	str += "', true,"
	str += "''";
	str += ",";
	str += "true,";
	str += "'";
	str += displayName;
	str += "'";
	str += ","
	str += "'";
	str += encode(classId);
	str += "'"
	str += " ); return false;" 
	str += '"'; 

	str += "><IMG SRC=" + "\"" + g_imgBlank + "\"" + " BORDER=0";
	
	var platform = navigator.platform;

	// For MSIE 5.0 on MacOS, the width and height of the image should be in points and should be exactly
	// equal to the height and width of the DIV containing it, to avoid overflow.
	if (g_browserType.needsImageDimensions)
	{
		str += " WIDTH=" + width;
		str += " HEIGHT=" + height;
	}
	else
	{
		str += " WIDTH='100%' ";
		str += " HEIGHT='100%' ";
	}

	if(parent != null)
	{
		str += " ALT=" + '"' + parent.sSearchable + '"';
		str += " TITLE=" + '"' + parent.sSearchable  + '"';
	}
	else
	{
		str += " ALT=" + '"' + "Searchable !" + '"';
		str += " TITLE=" + '"' + "Searchable !" + '"';
	}
	str += "></IMG>";
	
	str += "</A>";

	if ( !parent.frames.toolbarframe.getRecycledLayer )
		return;
	
	var layerObject = parent.frames.toolbarframe.getRecycledLayer( width, window );

	g_areaIdList[g_areaIdList.length]           = id + g_tagArea;
	g_areaClassIdList[g_areaClassIdList.length] = classId;

	createLayer( id + g_tagArea
				 , null
				 , layerObject
				 , left
				 , top
				 , width
				 , height
				 , str
			   );
}

///////////////////////////////////////////////////////////////////////////////

function escapeQuotes(str)
{
	var pattern = /\"/g; 
	str = str.replace(pattern, "&quot;");	
	return str;
}

///////////////////////////////////////////////////////////////////////////////

function createBalloonHelp( id, balloonHelp, link, linkType, linkTarget )
{
	balloonHelp = escapeQuotes(balloonHelp);
	if ( ! parent.g_bReportFrameLoaded ) 
		return;

	var str = "";
	
	// Make sure the original layer exists
	if ( ! layerExists( id, null ) )
		return;

	var left	= getLeft(   id, null );
	var top		= getTop(    id, null );
	var width	= getWidth(  id, null );
	var height	= getHeight( id, null );

	if( ns4 )
		height = height / 1.33;

	str += "<A ID='";
	str += id + "AreaAnchor";
	str += "' HREF=" + '"';

	if (link)
		str += "javascript:handleLinkClick('" + link + "', '" + linkType + "', '" + linkTarget + "')" + '" ';
	else
		str += 'javascript:myVoid()" ';

	str += "><IMG HREF='javascript:myVoid()' SRC=" + "'" + g_imgBlank + "'" + " BORDER=0 "
	
	var platform = navigator.platform;

	// For MSIE 5.0 on MacOS, the width and height of the image should be in points and should be exactly
	// equal to the height and width of the DIV containing it, to avoid overflow.
	if (platform.indexOf("Mac") != -1)
	{
		str += " WIDTH=" + width;
		str += " HEIGHT=" + height;
	}
	else
	{
		str += " WIDTH='100%' ";
		str += " HEIGHT='100%' ";
	}

	str += " ALT=" + '"' + balloonHelp + '"'
	str += " TITLE=" + '"' + balloonHelp + '"'
	str += "></IMG></A>";

	if ( !parent.frames.toolbarframe.getRecycledLayer )
		return;
	
	var layerObject = parent.frames.toolbarframe.getRecycledLayer( width, window );

	if ( layerExists( id + g_tagBalloon, document ) ) 
	{
		showLayer(id + g_tagBalloon, document); 
	}
	else
	{
		if (!g_balloonIdList)
			g_balloonIdList		= eval( 'parent.frames.toolbarframe.g_balloonIdList' );

		if (g_balloonIdList)
			g_balloonIdList[g_balloonIdList.length] = id + g_tagBalloon;

		createLayer( id + g_tagBalloon
				 , null
				 , layerObject
				 , left
				 , top
				 , width
				 , height
				 , str
				 , null
				 , null
				 , "hidden"
			   );

		window.setTimeout("showLayer('" + id + g_tagBalloon + "'," + " document)", 500);
	}
}

///////////////////////////////////////////////////////////////////////////////
function removeFromSearch( id )
{
	removeFromHashList( id );

	if ( g_searchFrame.updateSearchFrame )
		g_searchFrame.updateSearchFrame( false, id, "", "" );

	var layer = layerExists( id, document );
	if( ! layer )
		return;

	if( layer.background.src == "" )
		return;

	if( ns4 )
		layer.background.src = null;
	else
		layer.backgroundImage = "";
}

///////////////////////////////////////////////////////////////////////////////

function areaClick( id, bUpdateSearchFrame, searchValue, searchSelected, 
					searchDisplay, searchClassId )
{

	// The default is to update the search frame
	if ( bUpdateSearchFrame == null )
		bUpdateSearchFrame = true;

	// First check if the layer exists.
	var layer = layerExists( id, document );
	if ( layer == null ) 
		return false;

	// Check if the class has already been added to the search
	if ( !g_bInSetupSearch )
	{
		var itemIndex = findItem( g_hashClassIdList, decode(searchClassId) );
		if ( itemIndex != -1 )
		{
			removeFromSearch( id );
			return false;
		}
	}

	// Shade the clicked area
	if( ns4 )
		layer.background.src = g_imgHash;
	else
	{
		layer.backgroundImage = "URL(" + g_imgHash + ")";
	}

	// Add the id to a list of hash areas
	appendToHashList( id, searchDisplay, decode(searchClassId) );
	hideOtherClassAreas( decode(searchClassId), id );

	g_searchFrame		= eval( 'parent.frames.searchframe' );

	// Update the search frame
	if ( bUpdateSearchFrame && g_searchFrame.updateSearchFrame )
	{
		g_searchFrame.updateSearchFrame( true, id, searchValue, searchSelected, 
										 searchDisplay, decode(searchClassId) );
	}

	return false;
}

///////////////////////////////////////////////////////////////////////////////

function recreateHash( id, display, classId )
{
	areaClick( id, true, "", "", display, encode(classId) );
}

///////////////////////////////////////////////////////////////////////////////

function hideOtherClassAreas( classId, keepLayerId )
{
	for ( var index = 0; index < g_areaIdList.length; index++ )
	{
		var l_classId = g_areaClassIdList[index];

		if ( l_classId == classId )
		{
			var id = g_areaIdList[index];

			if ( layerExists( id, document ) && keepLayerId != id )
			{
				hideLayer( id, document ); 
			}
		}
	}
}

///////////////////////////////////////////////////////////////////////////////
function hideAllHashItemsInPage()
{
	if ( g_hashIdList )
	{
		for ( var index = 0; index < g_hashIdList.length; index++ )
		{
			var id = g_hashIdList[index];
			hideLayer( id, document ); 
		}
	}

	if( g_hashIdList )      g_hashIdList.length      = 0;
	if( g_hashDisplayList ) g_hashDisplayList.length = 0;
	if( g_hashClassIdList ) g_hashClassIdList.length = 0;

	if ( g_areaIdList)
	{
		for ( var index = 0; index < g_areaIdList.length; index++ )
		{
			var id = g_areaIdList[index];
			hideLayer( id, document ); 
		}
	}
}

///////////////////////////////////////////////////////////////////////////////

function clearSearchLists()
{
	// Because removeFromSearch() modifies the g_hashIdList we 
	// need to keep a temporary id list.
	var tempIdList = new Array();

	if ( g_hashIdList )
	{
		for ( var index = 0; index < g_hashIdList.length; index++ )
		{
			tempIdList[index] = g_hashIdList[index];
		}
	}

	for ( index = 0; index < tempIdList.length; index++ )
	{
		removeFromSearch( tempIdList[index] );
	}

	if ( g_hashIdList )		    g_hashIdList.length	     = 0;
	if ( g_hashDisplayList )	g_hashDisplayList.length = 0;
	if ( g_hashClassIdList )	g_hashClassIdList.length = 0;

	tempIdList.length = 0;
}

///////////////////////////////////////////////////////////////////////////////

function hideSearchAreas()
{
	if ( g_areaIdList )
	{
		for ( var index = 0; index < g_areaIdList.length; index++ )
		{
			var areaName = g_areaIdList[index];
			
			if ( ns4 )
			{
				if ( document.layers[areaName] )
				{
					document.layers[areaName].visibility = 'hidden';
				}
			}
			else
			{
				if ( elementLocate(document, areaName) )
				{
					elementLocate(document, areaName).style.visibility = 'hidden';
				}
			}
		}
	}
}

///////////////////////////////////////////////////////////////////////////////

function hideBalloons()
{
	if ( g_balloonIdList )
	{
		for ( var index = 0; index < g_balloonIdList.length; index++ )
		{
			var balloonName = g_balloonIdList[index];
			
			if ( ns4 )
			{
				if ( document.layers[balloonName] )
				{
					document.layers[balloonName].visibility = 'hidden';
				}
			}
			else
			{
				if ( elementLocate(document, balloonName) )
				{
					elementLocate(document, balloonName).style.visibility = 'hidden';
				}
			}
		}
	}
}

///////////////////////////////////////////////////////////////////////////////

function inSearchMode()
{
	if ( parent.g_searchOpen )
		if ( !parent.g_searchResults && !parent.g_searchInitiated )
			  return true;

	return false;
}


function inSearchResultsMode()
{
	if ( parent.g_searchOpen )
		if ( parent.g_searchResults)
			  return true;

	return false;
}


///////////////////////////////////////////////////////////////////////////////

function setupSearch()
{
	getToolbarVariables();

	if ( ns4 ) 
	{
		 if ( !inSearchMode() && !inSearchResultsMode() )
			return;
	}
	else
	{ 
		if ( !inSearchMode() )
			return;
	}

	if ( !g_hashIdList )
		return;

	// Recreate areas in the searchList
	g_bInSetupSearch = true;
	for( var index = 0; index < g_searchIdList.length; index++ )
	{
		var id = g_searchIdList[index];
		id = stripTag( id, g_tagArea );

		if ( ns4 && inSearchResultsMode() ) // hashIdList elements are undefined in this scenario.
		{
			createArea( id, encode(g_searchDisplayList[index]), g_searchClassIdList[index] );
		}
		else
		{
			if ( !g_hashClassIdList[index] ) 
			{
				createArea( id, encode(g_searchDisplayList[index]), g_searchClassIdList[index] );
			}
			else
			{		
				createArea( id, encode(g_searchDisplayList[index]), g_hashClassIdList[index] );
			}
		}
		recreateHash( id + g_tagArea, g_searchDisplayList[index], g_searchClassIdList[index] );

		if (ns4 && inSearchResultsMode())
		{
			hideSearchAreas();
		}
	}
		
	g_bInSetupSearch = false;

	g_searchFrame = eval( 'parent.frames.searchframe' );
}

///////////////////////////////////////////////////////////////////////////////

function getToolbarVariables()
{
	g_areaIdList		= eval( 'parent.frames.toolbarframe.g_areaIdList' );
	g_areaClassIdList	= eval( 'parent.frames.toolbarframe.g_areaClassIdList' );
	g_hashIdList		= eval( 'parent.frames.toolbarframe.g_hashIdList' );

	g_hashDisplayList	= eval( 'parent.frames.toolbarframe.g_hashDisplayList' );
	g_hashClassIdList	= eval( 'parent.frames.toolbarframe.g_hashClassIdList' );

	g_searchIdList		= eval( 'parent.frames.toolbarframe.g_searchIdList' );
	g_searchDisplayList	= eval( 'parent.frames.toolbarframe.g_searchDisplayList' );
	g_searchClassIdList	= eval( 'parent.frames.toolbarframe.g_searchClassIdList' );

	g_balloonIdList		= eval( 'parent.frames.toolbarframe.g_balloonIdList' );
}

///////////////////////////////////////////////////////////////////////////////

function isReportFrameLoaded()
{
	return parent.g_bReportFrameLoaded;
}

///////////////////////////////////////////////////////////////////////////////

function checkIfAllLoaded() 
{
	if (parent.g_bReportFrameLoaded 
	   && parent.g_bSearchFrameLoaded
	   && parent.frames.searchtoolbar.searchToolbarLoaded
	   && parent.g_bToolBarFrameLoaded)
	{
		setupSearch();
	}
}

///////////////////////////////////////////////////////////////////////////////

function scrollRight()
{
    var myWidth = 0;
	if( typeof( window.innerWidth ) == 'number' ) {
	   // Non-IE browsers
	   myWidth = window.innerWidth;
	}
    else {
		if( document.documentElement && document.documentElement.clientWidth ) {
		  //IE 6+ browsers in 'standards compliant mode'
		  myWidth = document.documentElement.clientWidth;
		} 
		else {
			if ( document.body && document.body.clientWidth ) {
			  //IE 4 compatible browsers
			  myWidth = document.body.clientWidth;
			}
		}
	}
    window.scrollBy(myWidth, 0);
}

///////////////////////////////////////////////////////////////////////////////

function onLoad(scrollToRight)
{
	if ( scrollToRight == true )
	{
		if (g_browserType.name == "IE" && g_browserType.version < 5.5)
		{
			window.resizeBy(-1, -1);
		}
		scrollRight();		
		if (g_browserType.name == "IE" && g_browserType.version < 5.5)
		{
			window.resizeBy(1, 1);
		}		
	}
	onLoadInit();
}

function onLoadInit()
{

	// For browsers with the onload handler problem, we make sure onLoad was
	// not already called by forceLoad();
	if (g_browserType.doesNotAlwaysCallOnloadHandler) {
		if (parent.g_bReportFrameLoaded)
			return;
	}
	else {
		parent.g_bReportFrameLoaded = false; 
	}

	ns4 = (document.layers) ? true:false;

	initGlobals();

	g_toolbarFrame		= eval( 'parent.frames.toolbarframe' );
	g_searchFrame		= eval( 'parent.frames.searchframe' );

	if( ! g_toolbarFrame )	// Not in ViewFrameset
	    return;

	if ( parent.g_bToolBarFrameLoaded )
	{ 
		if ( g_toolbarFrame.enableToolBarButtons )
		{
			g_toolbarFrame.enableToolBarButtons();
		}
	}
	else 
	{
		if(!ns4) {
				g_toolbarFrame.location.href = parent.g_toolbarFrameURL;
		}
	}

	parent.g_bReportFrameLoaded = true;
			
	if( ns4 && ! parent.frames.searchframe )
		return;

	getToolbarVariables();

	if ( !ns4 ) 
	{
		g_all = "all.";
		g_style = ".style";
	}

	checkIfAllLoaded();

	if( ns4 )
	{
		if (parent.frames.searchframe)
			if (parent.frames.searchtoolbar.searchToolbarLoaded )
				parent.frames.searchtoolbar.enableSearchToolbar();
	}
	else
	{
		if (g_browserType.cannotDynamicallyAdjustFrameset) 
		{
			if (parent.frames.searchframe)
				if (parent.frames.searchtoolbar.searchToolbarLoaded )
					parent.frames.searchtoolbar.enableSearchToolbar();
		}
		else // Search Frame is always loaded
		{
			if (parent.frames.searchtoolbar.searchToolbarLoaded )
				parent.frames.searchtoolbar.enableSearchToolbar();
		}
	}
}

///////////////////////////////////////////////////////////////////////////////

function forceOnLoad()
{
	if (g_browserType.doesNotAlwaysCallOnloadHandler && !parent.g_bReportFrameLoaded)
		onLoad();
}

///////////////////////////////////////////////////////////////////////////////

function onUnload()
{
	parent.g_bReportFrameLoaded = false;

	if ( parent.frames.toolbarframe && parent.frames.toolbarframe.clearRecycledLayers )
		parent.frames.toolbarframe.clearRecycledLayers( window );
}

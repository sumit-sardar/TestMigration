/**
 * converter.js - common js functions.
 *
 * @author  Actuate Corporation
 * @version 1.0
 */

///////////////////////////////////////////////////////////////////////////////
// Globals
///////////////////////////////////////////////////////////////////////////////
var ns4 = (document.layers)? true:false

var g_widthCheck;
var g_heightCheck;
var g_browserVersion;
var g_tagBalloon = "$$$Balloon";
var g_platform = navigator.platform;

if ( ns4 )
{
	g_browserVersion = parseFloat(window.navigator.appVersion);
	g_widthCheck	= window.innerWidth;
	g_heightCheck	= window.innerHeight;
	window.onResize = resizeFix;
}

function setupConverter()
{
	// Netscape Resize Fix

	if ( document.layers )
	{
		g_widthCheck	= window.innerWidth;
		g_heightCheck	= window.innerHeight;
		window.onResize = resizeFix;
	}

	g_platform = navigator.platform;
}

function setPageNo(page)
{
	parent.g_bToolbarPageNumberSet = false;

	// Need to set the page number this early because the user could
	// click on the hyperlinks really fast before the report.js onload
	// method is called.

	if ( parent.g_currentPageNumber )
	{
		parent.g_currentPageNumber = page;
	}

	// Need to store the location cookie this early because the user could
	// click on the hyperlinks really fast before the report.js onload
	// method is called.
	if ( parent.updateLocationStoreCookie )
	{
		parent.updateLocationStoreCookie();
	}

	if (parent.frames.toolbarframe && parent.g_bToolBarFrameLoaded && parent.frames.toolbarframe.setPageNumber)
	{
		var retVal = parent.frames.toolbarframe.setPageNumber(page);
		if (retVal)
		{
			parent.g_bToolbarPageNumberSet = true;
		}
	}
}

///////////////////////////////////////////////////////////////////////////////


function NumToHex(iNum) // ACCEPTS UPTO 64k
{
	if (iNum > 255)
	{
		iQuo = iNum / 256;
		iRem = iNum % 256;
		iQuo = iQuo - (iRem / 256);
		return NumToHex(iQuo) + NumToHex(iRem);
	}
	base = iNum / 16;
	rem = iNum % 16;
	base = base - (rem / 16);
	baseS = MakeHex(base);
	remS = MakeHex(rem);
	return baseS + '' + remS;
}

function MakeHex(x)
{
	if((x >= 0) && (x <= 9))
	return x;
	else {
	switch(x) {
	case 10: return "A";
	case 11: return "B";
	case 12: return "C";
	case 13: return "D";
	case 14: return "E";
	case 15: return "F";
		  }
	   }
}


function debugText(sText)
{
	s = "";
	for (i = 0; i < sText.length; i++)
	{
		c = sText.charCodeAt(i);
		s += "[" + NumToHex(c) + "]";
	}
	return s;
}


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




///////////////////////////////////////////////////////////////////////////////

function mouseOver( id, alias, balloonHelp, classId, className, link, linkType, linkTarget )
{
	if (!ns4) {
		if (g_browserType.name == "IE")
		{
			window.event.cancelBubble = true;
		}
	}

	if (inSearchMode() && alias)
	{
		// Escape the alias, because it could have special
		// string chars ",' in it
		if(className.substring(0, 2) == "($" )
		{
			createArea( id, encode(alias), className );
		}
		else
		{
			createArea( id, encode(alias), classId );
		}
	}
	//balloon help is not supposed to show in search mode
	else if (!inSearchMode())
	{
		if (balloonHelp)
		{
			createBalloonHelp( id, balloonHelp, link, linkType, linkTarget );
		}
	}
}


///////////////////////////////////////////////////////////////////////////////

function resizeFix()
{
	// If we are in the frameset, and the frameset is resized then reload the whole thing.
	if (eval( 'parent.g_inViewFrameset' ) && parent.g_inViewFrameset == true)	// In ViewFrameset
	{
		if ( parent.g_widthCheck  != parent.innerWidth || parent.g_heightCheck != parent.innerHeight )
		{
			parent.location.reload();
			return;
		}
	}

	// the browser version used here needs to be confirmed by Netscape (re: incident #120903)
	if ( g_browserVersion > 4.08 || g_widthCheck != window.innerWidth || g_heightCheck != window.innerHeight )
	{
		// For MacOS, we should reload the whole frameset. Reloading the frame only will cause the Netscape
		// browser to crash with a "type 2"(memory) error.

		if (g_platform.indexOf("Mac") != -1)
		{
			parent.location.reload();
		}
		else
		{
		window.stop();
		window.setTimeout( 'window.location.reload();', 1000 );
	}
}
}

///////////////////////////////////////////////////////////////////////////////


/**
 * Note: Duplicate definition; also defined in viewer.js
 */
function removeParams(sObjURL, sParam)
{
	var pattern = sParam + "=[^&]*&?";
	var regExp = new RegExp(pattern, "i");
	var resultArray = regExp.exec(sObjURL);
	var listArray = sObjURL.split(regExp);
	var sRetURL = null;

	// Check if the parameter is existing in the url
	if (resultArray != null)
	{
		// Remove the  the parameter
		if( listArray.length == 1 || listArray[1] == "")
		{
			sRetURL = sObjURL.substring(0 , (listArray[0].length-1));
		}
		else
		{
			sRetURL = sObjURL.replace(regExp, "");
		}
	}
	else
	{
		sRetURL = sObjURL;
	}
	return sRetURL;
}

/**
 *
 */
function isROIFile(sFQFileName)
{
	if (sFQFileName == null) return null;
	iDot = sFQFileName.lastIndexOf(".");
	if (iDot == -1) return "";
	iSemiColon = sFQFileName.lastIndexOf(";");
	if (iSemiColon == -1) iSemiColon = iDot+4;
	var sFileExt = sFQFileName.substring(iDot + 1, iSemiColon);
	return (sFileExt.toLowerCase() == "roi");
}

/**
 *
 */
function isStandardProtocol(link)
{
	var bProtocolExists = false;

	if(	(link.indexOf( "http:" ) == 0) ||
		(link.indexOf( "https:")  == 0) ||
		(link.indexOf( "mailto:")  == 0) ||
		(link.indexOf( "file:")  == 0) ||
		(link.indexOf( "rotp:")  == 0) ||
		(link.indexOf( "ftp:")  == 0) )
	{
		bProtocolExists = true;
	}
	return bProtocolExists;
}


// different types of RC directives, which can be a part of hyperlinks.
// There can be many more directives which should be added if not found in this list.

var NONE = 0;
var VIEWPAGE = 1;
var SEARCH = 2;
var SUBMIT = 3;
var REQUEST = 4;
var GETREPORTDATA = 5;
var GETSTYLESHEET = 6;
var GETDYNAMICDATA = 7;
var GETSTATICDATA = 8;
var DROP = 9;
var VIEWDEFAULT = 10;
var VIEWFRAMESET = 11;
var VIEWTOC = 12;
var VIEWNAVIGATION = 13;
var SAVEAS = 14;

/**
 *	This function will return an integer array of length 2.
 * The 1st element will be directive constant and 2nd will be its index in the link
 */

function getDirectiveFromLink(link)
{
	var iArrReutrnValue = new Array(2);
	var iDirectiveIndex = -1;

	iArrReutrnValue [0] = NONE;


	if ((iDirectiveIndex = link.toLowerCase().indexOf('?viewpage', 0)) != -1)
	{
		iArrReutrnValue [0] = VIEWPAGE;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?search')) != -1)
	{
		iArrReutrnValue [0] = SEARCH;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?submit')) != -1)
	{
		iArrReutrnValue [0] = SUBMIT;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?request')) != -1)
	{
		iArrReutrnValue [0] = REQUEST;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?getreportdata')) != -1)
	{
		iArrReutrnValue [0] = GETREPORTDATA;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?getstylesheet')) != -1)
	{
		iArrReutrnValue [0] = GETSTYLESHEET;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?getdynamicdata')) != -1)
	{
		iArrReutrnValue [0] = GETDYNAMICDATA;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?getstaticdata')) != -1)
	{
		iArrReutrnValue [0] = GETSTATICDATA;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?drop')) != -1)
	{
		iArrReutrnValue [0] = DROP;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?viewdefault')) != -1)
	{
		iArrReutrnValue [0] = VIEWDEFAULT;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?viewframeset')) != -1)
	{
		iArrReutrnValue [0] = VIEWFRAMESET;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?viewtoc')) != -1)
	{
		iArrReutrnValue [0] = VIEWTOC;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?viewnavigation')) != -1)
	{
		iArrReutrnValue [0] = VIEWNAVIGATION;
	}
	else if ((iDirectiveIndex = link.toLowerCase().indexOf('?saveas')) != -1)
	{
		iArrReutrnValue [0] = SAVEAS;
	}

	iArrReutrnValue [1] = iDirectiveIndex;

	return iArrReutrnValue;
}

/**
 * resolveNestedLinkUrl method resolves the url for nested search condition
 */
function resolveNestedLinkUrl(link)
{
	var resolvedURL = link;
	var doesNestedExist = true;

	var iSearchStart = link.toLowerCase().indexOf('&search&');
	if(iSearchStart != -1)
	{
		var iEndSearch = link.toLowerCase().indexOf('&endsearch', iSearchStart);
		if (iEndSearch != -1)
		{
			//DECODES THE VALUE TO AVOID DOUBLE ENCODING
			var decodedValue = decode(link.substring(iSearchStart+8, iEndSearch));
			var newString	= "&searchcriteria="+encode(decodedValue);
			var oldString	= link.substring(iSearchStart, iEndSearch+10);
			resolvedURL		= resolvedURL.replace(oldString, newString);
		}
	}
	// IF OTHER DIRECTIVES ALSO NEED TO BE RESOLVED IF BLOCK SHOULD BE ADDED OVER HERE

	return resolvedURL;
}

function resolveLinkUrl(link)
{
	var sSearchCriteria = "";
	var sResolvedLink = "";
	var sReportName = "";
	var sRemainingLink = "";
	var sOrgLink = link;

	if (!(ns4 && g_browserVersion == 4.08))	 // DO NOT DECODE FOR NS 4.08
		link = decode(link);

	link = resolveNestedLinkUrl(link);
	var iLinkDirectiveAndIndex = getDirectiveFromLink(link);

	switch (iLinkDirectiveAndIndex[0])
	{
		case 1://VIEWPAGE
		{
			sReportName = link.substring(0, iLinkDirectiveAndIndex[1]);
			var sSearchStart = link.toLowerCase().indexOf('search&');
			var sEndSearch = link.toLowerCase().indexOf('&endsearch');
			var sOtherViewOptions = "";
			if (sSearchStart != -1 && sEndSearch != -1)
			{
				// 7 is length of "search&"
				sSearchCriteria = "&searchcriteria=" + encode(link.substring((sSearchStart + 7), sEndSearch));
				// 9 is length of "?viewpage"
				sOtherViewOptions = link.substring((iLinkDirectiveAndIndex[1] + 9) , (sSearchStart - 1));
			}
			else
			{
				sSearchCriteria = "";
				// 9 is length of "?viewpage"
				sOtherViewOptions = link.substring((iLinkDirectiveAndIndex[1] + 9));
			}
			sResolvedLink = "../servlet/ViewPage?name=" + encode(sReportName) + sOtherViewOptions + sSearchCriteria;
			break;
		}
		case 2://SEARCH
		{
			sReportName = link.substring(0, iLinkDirectiveAndIndex[1]);

			// 8 is length of "?search&"
			var iEndSearchIndex = link.toLowerCase().indexOf('&endsearch', (iLinkDirectiveAndIndex[1] + 8));

			sSearchCriteria = link.substring((iLinkDirectiveAndIndex[1] + 8), iEndSearchIndex);
			sResolvedLink = "../viewer/viewframeset.jsp?name=" + encode(sReportName) + "&searchcriteria=" + encode(sSearchCriteria);
			break;
		}
		case 3://SUBMIT
		{
			sReportName = link.substring(0, iLinkDirectiveAndIndex[1]);
			// 7 is length of "?submit"
			sRemainingLink = link.substring(iLinkDirectiveAndIndex[1] + 7);
			if (sRemainingLink.toLowerCase().indexOf("__scheduletype=immediate") != -1)
			{
				sResolvedLink = "../newrequest/do_executereport.jsp?__executableName=" + encode(sReportName) + sRemainingLink;
			}
			else
			{
				sResolvedLink = "../newrequest/do_submitjob.jsp?__executableName=" + encode(sReportName) + sRemainingLink;
			}
			break;
		}
		//CLEANUP: DO NOT REMOVE THE FOLLOWING CASE
		/*case REQUEST:
		{
			sReportName = link.substring(0, iLinkDirectiveAndIndex[1]);
			// 8 is length of "?request"
			sRemainingLink = link.substring(iLinkDirectiveAndIndex[1] + 8);
			sResolvedLink = "../newrequest/index.jsp?__executableName=" + encode(sReportName) + sRemainingLink;
			break;
		}*/
		case 5://GETREPORTDATA
		{
			sReportName = link.substring(0, iLinkDirectiveAndIndex[1]);
			// 14 is length of "?getreportdata"
			sRemainingLink = link.substring(iLinkDirectiveAndIndex[1] + 14);
			sResolvedLink = "../servlet/GetReportData?name=" + encode(sReportName) + sRemainingLink;
			break;
		}
		case 6://GETSTYLESHEET
		{
			break;
		}
		case 7://GETDYNAMICDATA
		{
			break;
		}
		case 8://GETSTATICDATA:
		{
			break;
		}
		case 9://DROP:
		{
			break;
		}
		case 10://VIEWDEFAULT:
		{
			break;
		}
		case 11://VIEWFRAMESET:
		{
			break;
		}
		case 12://VIEWTOC:
		{
			break;
		}
		case 13://VIEWNAVIGATION:
		{
			break;
		}
		case 14://SAVEAS:
		{
			break;
		}
		case 0://NONE:
		default:
		{
			if (isROIFile(sOrgLink))
			{
				sReportName = sOrgLink.substring(0, sOrgLink.toLowerCase().lastIndexOf(".roi") + 4);// 4 is length of ".roi"
				//ENCODING # BECAUSE IT IS NOT ENCODED BY SERVER
				sReportName = sReportName.replace(/#/g, encode("#"));
				sRemainingLink = sOrgLink.substring(sOrgLink.toLowerCase().lastIndexOf(".roi") + 4);// 4 is length of ".roi"

				if (sRemainingLink.length > 0)
				{
					if (sRemainingLink.charAt(0) == '?')
					{
						sRemainingLink = "&"+sRemainingLink.substring(1);
					}
					else if(sRemainingLink.charAt(0) != '&')
					{
						sRemainingLink = "&"+sRemainingLink;
					}
				}
				sResolvedLink = "../viewer/viewframeset.jsp?name=" + sReportName + sRemainingLink;
			}
			else
			{
				sResolvedLink = "../servlet/DownloadFile?name=" + sOrgLink;
			}
		}
	}
	return sResolvedLink;
}


/**
 * Decodes special characters ", ' and & that arrive from the view server
 */
function decodeSpecialCharacters(sText)
{
	sDecoded = "";
	nLen = sText.length;
	for (i = 0; i < nLen; i++)
	{
		c = sText.charAt(i);
		if (c == '%')
		{
			sCode = sText.substring(i + 1, i + 3);
			if (sCode == "22") // DBLQUOTE "
				sDecoded += "\"";
			else
			if (sCode == "25") // PERCENT %
				sDecoded += "%";
			else
			if (sCode == "27") // QUOTE '
				sDecoded += "'";
			else
			if (sCode == "26") // AMPERSTAND &
				sDecoded += "&";
			else
				alert("Unhandled link character %" + sCode);
			i += 2;
			continue;
		}
		else
			sDecoded += c;
	}
	return sDecoded;
}


/**
 *
 */
function debugDataValue(sText)
{
	iSDV = sText.indexOf("DataValue=");
	if (iSDV == -1)
	{
		iSDV = sText.indexOf("DataValue%3d");
		if (iSDV != -1) iSDV += 12;
	}
	else
		iSDV += 10;

	iEDV = sText.indexOf("&EndSearch");
	if (iEDV == -1) iEDV = sText.indexOf("&serverURL");
	if (iSDV == -1 || iEDV == -1)
	{
		alert("Could not extract DataValue");
		return;
	}
	sDV = sText.substring(iSDV, iEDV);
	alert("Extracted data value is ... " + sDV + "\nDebug = " + debugText(sDV));
}

///////////////////////////////////////////////////////////////////////////////

// If needed the target of the hyperlink in the report page, can be
// modified in this method

function handleLinkClick( link, linkType, targetWindow )
{
	var bStdProtocol = isStandardProtocol(link);
	if (!bStdProtocol)
	{
		var sAbsPath = getAbsPathForTransientReport(link);
		if (sAbsPath != null)
			link = sAbsPath;
		if (g_browserType.name == "IE" && g_browserType.platform != "Mac" && g_browserType.version < 5.5)
			link = escapeLink(link);
	}
	
	sSuffixURL = "";
	if (!inSearchMode() || !isReportFrameLoaded())
	{
		var url;

		g_browserVersion = parseFloat(window.navigator.appVersion);
		if (ns4 && g_browserVersion == 4.08)
		{
			// Only Netscape 4.08 seem to have problem if relative URL contains ':'.
			// Absolute URLs, start with "http", seem to be ok.
			if( ( link.lastIndexOf( "http:" ) == -1 ) &&
				( link.lastIndexOf( "https:") == -1 ) &&
				( link.lastIndexOf( "mailto:") == -1 ) &&
				( link.lastIndexOf( "file:") == -1 ) &&
				( link.lastIndexOf( "ftp:") == -1 ) )
			{
				url = link.replace( /:/g, "%3A" );
			}
			else
				url = link;

			url = link;
		}
		else
		{
			url = link;
		}

		// support for javascript protocol
		if ( link.indexOf( "javascript:" ) == 0 )
		{
			// look for the escaped parentheses
			var left = link.indexOf( "%28" );
			var right = link.lastIndexOf( "%29" );
			if ( ( left > 10 ) &&
				 ( right > 13 ) )
			{
				// unescape the left paren and the right paren
				url = link.substring(0, left) + decode(link.substring(left, left+3)) +
				      link.substring(left+3, right) + decode(link.substring(right, right+3)) +
					  link.substring(right+3, link.length);
			}
		}

		if (targetWindow)
		{
			// replace any character that is not a letter, numeral, or underscore with an underscore
			targetWindow = targetWindow.replace( /\W/g, "_" );

			if (! bStdProtocol)
				url += sSuffixURL;

			var win = window.open('', targetWindow);

			var protocol = win.location.protocol;

			if(protocol != "http:" && protocol != "https:" &&
				protocol !=  "mailto:" &&	protocol !=  "file:" &&
				protocol !=  "ftp:" )
			{
				win.location.href = removeParams(url, "closex")  + ((url.indexOf("?") == -1)?"?":"&") + "closex=true";
			}
			else
			{
				win.location.href = url;
			}

			return;
		}

		if (linkType == "Internal")
		{
			//Replace viewer/viewdefault.jsp with servlet/ViewPage
			//In order not to load in frameset mode within a frame of the existing frameset
			
			var queryIndex = url.indexOf("command=viewdefault");
			if (queryIndex != -1)
				url = url.substring(0, queryIndex) + 
					"command=viewpage" + url.substring(queryIndex+19, url.length);
			//CONNECTION HANDLER CHANGE
			if (parent != null && parent.sConnectionHandle != null)
			{
				sSuffixURL += "&connectionHandle=" + encode(parent.sConnectionHandle);
			}
			if (parent != null && parent.sID != null) // VIA viewframeset.jsp
			{
				sSuffixURL += "&id=" + parent.sID;
				//url = removeParams(url, "name");
			}
			else // SCAN WINDOW URL DIRECTLY FOR (ID, ConnectionHandle)
			{
				sWindowURL = window.location.href;
				iID = sWindowURL.toLowerCase().indexOf("?id=");
				iCH = sWindowURL.toLowerCase().indexOf("?connectionhandle=");
				if (iID == -1) iID = sWindowURL.toLowerCase().indexOf("&id=");
				if (iCH == -1) iCH = sWindowURL.toLowerCase().indexOf("&connectionhandle=");
				if (iCH >= 0 && iID >= 0)
				{
					iCH_End = sWindowURL.indexOf("&", iCH + 1);
					iID_End = sWindowURL.indexOf("&", iID + 1);
					if (iCH_End == -1) iCH_End = sWindowURL.length();
					if (iID_End == -1) iID_End = sWindowURL.length();
					sSuffixURL += "&id=" + sWindowURL.substring(iID + 4, iID_End);
					sSuffixURL += "&connectionHandle=" + sWindowURL.substring(iCH + 18);
				}
			}
			if (! bStdProtocol)
				url += sSuffixURL;

			if (parent.frames.reportframe)
			{
				parent.frames.reportframe.location.replace(url);
			}
			else
			{
				parent.location.href = url;
			}

            parent.bIsLinkInternal = true;

		}
		else//if (linkType == "External")
		{
			// Store the action in the viewframeset. It will be needed to
			// identify the cause of the frameset load. We identify external
			// search hyperlinks as "EHL_CLICK". All other causes for frameset
			// loads are identified as "UNKNOWN". This way we know the right
			// page number to take the use to, when the frameset loads.

			if (link.toLowerCase().indexOf("?search") != -1)
			{
				parent.g_loadAction = "EHL_CLICK";

				// Need to update the cookie right, now. Can't do it when the frameset
				// unloads because the unLoad() may not be called, especially when the
				// frameset didn't load completely. Also for Netscape, in the frameset
				// unload method the doucment is no longer in scope.

				if( parent.updateLocationStoreCookie) {
					parent.updateLocationStoreCookie();
				}
			}
			
			url += "&__newWindow=false"
			if (! bStdProtocol)
				url += sSuffixURL;
			parent.location.href = url;
		}
	}
}

///////////////////////////////////////////////////////////////////////////////


function getFolderFromFilename(sFileName)
{
	if (sFileName == null)
		return null;

	var iLastSlash = sFileName.lastIndexOf("/");

	if (iLastSlash < 0)
		return "";

	var sFolder = sFileName.substring(0, iLastSlash);

	if (sFolder=="")
		sFolder = "/";

	if(sFolder == "/")
		return sFolder;
	else
		return sFolder+"/";
}

function getAbsolutePathForLink(linkExp)
{
	var fileName2 = "";
	var fileName1 = "";
	var destReport = "";
	var windowURL = window.document.URL;

	windowURL = decode(windowURL);

	//Getting the entire filename from window url.
	var i = windowURL.toLowerCase().indexOf("name=", 0); // WARNING: THIS ONLY MAPS TO "name=|outputName=|execName="
	if (i == -1) return null;
	var j = windowURL.indexOf("&", i);
	if (j == -1)
		fileName1 = windowURL.substring(i + 5, windowURL.length);
	else
		fileName1 = windowURL.substring(i + 5, j);

	//Getting the entire filename of dest report file from linkExp considering version.
	var k = linkExp.indexOf(";", 0);
	if (k == -1) k = linkExp.indexOf("?", 0);
	if (k == -1)
	{
		fileName2 = linkExp;
		linkExp = "";
	}
	else
		fileName2 = linkExp.substring(0, k);

	//Getting only the folder name from first file name
	var sFolder1 = getFolderFromFilename(fileName1);

	//Getting only the folder name from dest report file name
	var sFolder2 = getFolderFromFilename(fileName2);

	//Extracting only the dest report file name
	var l = fileName2.indexOf(sFolder2, 0);
	var q = fileName2.lastIndexOf("/");
	if( q != -1)
		destReport = fileName2.substring(q + 1, fileName2.length);
	else
		destReport = fileName2;

	//Constructing the actual absolute url for linkExp
	var linkExpAbsolute = "";

	if ((sFolder2 == "/") || (sFolder2.charAt(0) =='/'))
	{
		//It is an absolute path for dest report;
		return;
	}

	if (sFolder2 == "")
	{
		// Dest report in the current folder
		linkExpAbsolute = encode(sFolder1)+destReport+linkExp.substring( k, linkExp.length);
		return linkExpAbsolute;
	}

	if (sFolder2.indexOf("..") != -1)
	{
		//Dest report url is relative with ..
		var arr = sFolder2.split("/");
		sFolder1 = sFolder1.substring(0, sFolder1.length-1 );
		for (var m = 0; m < arr.length; m++)
		{
			if (arr[m] == "..")
			{
				var n = sFolder1.lastIndexOf("/");
				sFolder1 = sFolder1.substring(0, n);
			}
		}
		var p = sFolder2.lastIndexOf("..");
		sFolder = sFolder1+sFolder2.substring(p+2, sFolder2.length);
		linkExpAbsolute = encode(sFolder)+destReport+linkExp.substring(k, linkExp.length);
		return linkExpAbsolute;
	}
	else
	{
		sFolder = sFolder1 + sFolder2;
		linkExpAbsolute = encode(sFolder)+destReport+linkExp.substring(k, linkExp.length);
		return linkExpAbsolute;
	}
}

function getAbsPathForTransientReport(link)
{
	// We only need re-direct relative URLs within transient report
	// Assuming the URLs contains "name=/$transient/" 

	// Getting the path name of the report 
	var index1 = link.toLowerCase().indexOf("name=", 0);
	if (index1 == -1) return null;
	index1 = index1 + 5;
	var destFile;
	var index2 = link.indexOf("&", index1);
	if (index2 == -1)
		destFile = link.substring(index1, link.length);
	else
		destFile = link.substring(index1, index2);

	// Checking if it is URL need re-direct		
	if( destFile.toLowerCase().indexOf("/$transient/") != 0 &&
	    destFile.toLowerCase().indexOf("/%24transient/") != 0)
	{
		return null;
	}
	
	// Getting the file name only
	var index3 = destFile.toLowerCase().indexOf("transient/");
	destFile = destFile.substring(index3 + 10, destFile.length);

	var fileName1 = "";
	var windowURL = window.document.URL;
	windowURL = decode(windowURL);

	//Getting the entire filename from window url.
	var i;
	var offset;

	// WARNING: THIS ONLY MAPS TO "name=|outputName=|execName="
	i = windowURL.toLowerCase().indexOf("outputname=", 0);
	offset = 11;
	if (i == -1)
	{
		i = windowURL.toLowerCase().indexOf("name=", 0); // WARNING: THIS ONLY MAPS TO "name=|outputName=|execName="
		offset = 5;
	}
	if (i == -1)
	{
		return null;
	}
	
	var j = windowURL.indexOf("&", i);
	if (j == -1)
		fileName1 = windowURL.substring(i + offset, windowURL.length);
	else
		fileName1 = windowURL.substring(i + offset, j);

	//Getting only the folder name from first file name
	var sFolder1 = getFolderFromFilename(fileName1);

	//Constructing the actual absolute url for linkExp
	var linkExpAbsolute = "";
	linkExpAbsolute = link.substring(0, index1)+encode(sFolder1)+destFile+link.substring(index2, link.length);
	return linkExpAbsolute;
}

// Since IE 5.0 un-escape the Javascript function parameters, do escape again here
function escapeLink(link)
{
	var tmpString = link.toLowerCase();
	// unescape the pathname of the file
	var from = tmpString.indexOf("?name=");
	if (from != -1)
	{
		from = from + 6;
		var to = tmpString.indexOf(".roi");
		if (to == -1)
			to = tmpString.indexOf(".rox");
		if (to == -1)
			to = tmpString.indexOf(".rov");
		if (to == -1)
			to = tmpString.length;
		if (from > to)
			from = to;
		var subLink = link.substring(from, to);
		subLink = unescape(subLink); 
		link = link.substring(0, from) + escape(subLink) + link.substring(to, link.length);
	}

	// unescape the search criteria
	tmpString = link;
	var from = tmpString.indexOf("&searchcriteria=");
	if (from != -1)
	{
		from = from + 16;
		var to = link.length;
		if (from > to)
			from = to;
		var subLink = link.substring(from, to);
		subLink = unescape(subLink); 
		link = link.substring(0, from) + escape(subLink);
	}

	return link;
}
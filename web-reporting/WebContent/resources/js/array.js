/**
 * Array.js
 *
 * @author  Actuate Corporation
 * @version 1.0
 */

///////////////////////////////////////////////////////////////////////////////
// Globals
///////////////////////////////////////////////////////////////////////////////

function findItem( array, itemValue )
{
	if ( !array )
		return -1;

	for ( var index = 0; index < array.length; index++ )
	{
		if ( array[index] == itemValue )
			return index;
	}

	return -1;
}

///////////////////////////////////////////////////////////////////////////////

function listItems( array )
{
	for ( var index = 0; index < array.length; index++ )
	{
		alert( 'array[' + index + '] = ' + array[index] );
	}
}

///////////////////////////////////////////////////////////////////////////////

function removeItem( array, itemIndex )
{
	for ( var index = itemIndex; index < array.length; index++ )
	{
		var next = index + 1;
		if ( next <= array.length )
			array[index] = array[next];
	}

	array.length--;
}

///////////////////////////////////////////////////////////////////////////////

function emptyArray( array )
{
	array.length = 0;
}

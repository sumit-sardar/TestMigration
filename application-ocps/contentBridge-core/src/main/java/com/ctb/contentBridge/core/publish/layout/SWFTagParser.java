/*
 * Created on Nov 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.contentBridge.core.publish.layout;

import java.io.IOException;

import com.ctb.contentBridge.core.publish.anotherbigidea.flash.interfaces.SWFShape;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.interfaces.SWFText;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.readers.TagParser;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.structs.AlphaColor;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.structs.Color;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.structs.Matrix;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.structs.Rect;
import com.ctb.contentBridge.core.publish.anotherbigidea.io.InStream;

/**
 * @author wen-jin_chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SWFTagParser extends TagParser
{
    public int maxWidth;
    public int maxHeight;
    
    public SWFTagParser( SWFTagTypes tagtypes ) 
    {
        super( tagtypes );
        maxWidth = 0;
        maxHeight = 0;
    }
    
    public int getMaxWidth()
    {
        return maxWidth;
    }
    
    public int getMaxHeight()
    {
        return maxHeight;
    }
    
    public void header( int version, long length,
                        int twipsWidth, int twipsHeight,
                        int frameRate, int frameCount ) throws IOException
    {
        super.header( version, length, 
                         twipsWidth, twipsHeight, 
                         frameRate, frameCount );
        maxWidth = twipsWidth;
        maxHeight = twipsHeight;
    }
    
    public void tempRectCheck( Rect bound )
    {
        if ( bound.getMinX() < 0 || bound.getMinY() < 0 )
        {
            System.out.println( "minx " + bound.getMinX() );
            System.out.println( "miny " + bound.getMinY() );
        }
    }
    
    protected void parseDefineTextField( InStream in ) throws IOException
    {
        int id = in.readUI16();

        Rect boundary = new Rect( in );
        tempRectCheck( boundary );
        if ( boundary.getMaxX() > maxWidth )
            maxWidth = boundary.getMaxX();
        if ( boundary.getMaxY() > maxHeight )
            maxHeight = boundary.getMaxY();
        Matrix matrix = new Matrix( in );
        
        int flags     = in.readUI16();
        int fontId    = in.readUI16();
        int fontSize  = in.readUI16();
        AlphaColor textColor = new AlphaColor( in );
        
        int charLimit = ( (flags & TEXTFIELD_LIMIT_CHARS ) != 0 ) ? in.readUI16() : 0;
        
        int alignment   = in.readUI8();
        int leftMargin  = in.readUI16();
        int rightMargin = in.readUI16();
        int indentation = in.readUI16();
        int lineSpacing = in.readUI16();
                
        String fieldName = in.readString();
        String initialText = ( (flags & TEXTFIELD_HAS_TEXT ) != 0 ) ? in.readString() : null;
        
        tagtypes.tagDefineTextField( id, fieldName, initialText, boundary, flags,
                                     textColor, alignment, fontId, fontSize,
                                     charLimit, leftMargin, rightMargin, 
                                     indentation, lineSpacing );
    }
    
    protected void parseDefineText( int type, InStream in ) throws IOException
    {
        int    id     = in.readUI16();        
        Rect   bounds = new Rect( in );
        tempRectCheck( bounds );
        if ( bounds.getMaxX() > maxWidth )
            maxWidth = bounds.getMaxX();
        if ( bounds.getMaxY() > maxHeight )
            maxHeight = bounds.getMaxY();
        Matrix matrix = new Matrix( in );
        
        SWFText text = ( type == TAG_DEFINETEXT ) ? 
                           tagtypes.tagDefineText( id, bounds, matrix ) :
                           tagtypes.tagDefineText2( id, bounds, matrix );
        
        if( text == null ) return;
        
        int glyphBits   = in.readUI8();
        int advanceBits = in.readUI8();
        
        //--Read multiple text records
        int firstByte;
        
        while( ( firstByte = in.readUI8()) != 0 )
        {
            if( (firstByte & 0x80) == 0 ) //Glyph Record
            {
                //--Get number of glyph entries
                int glyphCount = firstByte & 0x7f;
        
                int[] glyphs   = new int[ glyphCount ];
                int[] advances = new int[ glyphCount ];
            
                //--Read the glyph entries
                for( int i = 0; i < glyphCount; i++ )
                {
                    glyphs[i]   = (int)in.readUBits( glyphBits );
                    advances[i] = in.readSBits( advanceBits );
                }            

                text.text( glyphs, advances );
            }
            else //Style Record
            {
                int flags = firstByte;
            
                int fontId = 0;
                
                if( ( flags & TEXT_HAS_FONT ) != 0 )
                {
                    fontId = in.readUI16();
                }
            
                if( ( flags & TEXT_HAS_COLOR ) != 0 )
                {
                    text.color( (type == TAG_DEFINETEXT2) ? 
                                    new AlphaColor( in ) : 
                                    new Color( in ));
                }

                if( ( flags & TEXT_HAS_XOFFSET ) != 0 )
                {
                    text.setX( in.readSI16() );
                }

                if( ( flags & TEXT_HAS_YOFFSET ) != 0 ) //x & y are in reverse order from flag bits
                {
                    text.setY ( in.readSI16() );
                }

                if( ( flags & TEXT_HAS_FONT ) != 0 )
                {
                    int textHeight = in.readUI16();
                    
                    text.font( fontId, textHeight );
                }
            }
        }             

        text.done();
    }
    
    protected void parseDefineShape( int type, InStream in ) throws IOException
    {
        int  id    = in.readUI16();
        Rect rect  = new Rect( in );
        tempRectCheck( rect );
        if ( rect.getMaxX() > maxWidth )
            maxWidth = rect.getMaxX();
        if ( rect.getMaxY() > maxHeight )
            maxHeight = rect.getMaxY();
        
        SWFShape shape = null;
        
        switch( type )
        {
            case TAG_DEFINESHAPE : shape = tagtypes.tagDefineShape( id, rect ); break;
            case TAG_DEFINESHAPE2: shape = tagtypes.tagDefineShape2( id, rect ); break;
            case TAG_DEFINESHAPE3: shape = tagtypes.tagDefineShape3( id, rect ); break;
            default: break;
        }
        
        if( shape == null ) return;
        
        parseShape( in, shape,
                    true /*has style*/, 
                    type == TAG_DEFINESHAPE3 /*has alpha*/ );        
    }

}

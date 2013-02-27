/*
 * Created on Nov 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.contentBridge.core.publish.layout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.ctb.contentBridge.core.publish.anotherbigidea.flash.SWFConstants;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.interfaces.SWFTags;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.movie.FontLoader;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.readers.TagParser;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.structs.Rect;
import com.ctb.contentBridge.core.publish.anotherbigidea.io.InStream;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.readers.SWFSaxParser;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.zip.Inflater;
import sun.net.www.content.text.PlainTextInputStream;


/**
 * @author Wen-Jin_Chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SWFImageSizeDeterminer 
{
    protected SWFTagParser consumer;
    String imagePath;
    InStream in = null;
    int height;
    int width;
    
    public SWFImageSizeDeterminer( String path ) 
    {
        super();
        imagePath = path;
        height = 0;
        width = 0;
    }
    
    public byte[] getRemoteFileContent( String src ) throws Exception
    {
        int EACH_READ_SIZE = 1000;
        PlainTextInputStream inputStream = (PlainTextInputStream) new URL(src).getContent();
        byte[] result = null;
        byte[] buffer = new byte[ EACH_READ_SIZE ];
        int readSize;
        boolean done = false;
        while( !done )
        {
            readSize = inputStream.read( buffer );
            if ( readSize <= 0 )
                done = true;
            else
            {
                if ( result != null )
                {
                    byte[] newResult = new byte[ result.length + readSize ];
                    for ( int i = 0; i < result.length; i++ )
                        newResult[ i ] = result[ i ];
                    for ( int i = 0; i < readSize; i++ )
                        newResult[ i + result.length ] = buffer[ i ];
                    result = newResult;
                }
                else if ( readSize == EACH_READ_SIZE )
                {
                    result = buffer;
                    buffer = new byte[ EACH_READ_SIZE ];
                }
                else
                {
                    byte[] newResult = new byte[ readSize ];
                    for ( int i = 0; i < readSize; i++ )
                        newResult[ i ] = buffer[ i ];
                    result = newResult;
                }
            }
        }
        inputStream.close();
        result[0] = 0x46;
        return result;
    }
    
    public String decompressSWC( String src ) throws Exception
    {
        String filePath = null;
        byte[] content = getRemoteFileContent( src );
        PlainTextInputStream inputStream = (PlainTextInputStream) new URL(src).getContent();
 //       inputStream = new FileInputStream( new File( src ));
        int byteValue = inputStream.read();
        byteValue = inputStream.read();
        byteValue = inputStream.read();
        byteValue = inputStream.read();
        long ui32 = inputStream.read();
        ui32 += (inputStream.read()) << 8;
        ui32 += (inputStream.read()) << 16;
        ui32 += (inputStream.read()) << 24;
        Inflater decompresser = new Inflater();
        decompresser.setInput( content, 8, content.length - 8 );
        byte[] result = new byte[ (int) ui32 - 8 ];
        int resultLength = decompresser.inflate( result );
        decompresser.end();
        String tempFolder = "./";
        File tempfile = File.createTempFile( "src_", ".swf", new File( tempFolder ) );
        filePath = tempfile.getPath();
        FileOutputStream outStream = new FileOutputStream( tempfile );
        outStream.write( content, 0, 8 );
        outStream.write( result, 0, resultLength );
        outStream.close(); 
        return filePath;
    }
    
    public void checkSize() throws Exception
    {
        String filePath = null;
        FontLoader fontloader = new FontLoader();
        consumer = new SWFTagParser( fontloader );
        //FileInputStream fis = new FileInputStream( new File( imagePath ) );
        PlainTextInputStream fis = (PlainTextInputStream) new URL(imagePath).getContent();
        in = new InStream( fis );
        if( ( in.readUI8() == 0x43 ) &&  // "C"
            ( in.readUI8() == 0x57 ) &&  // "W"
            ( in.readUI8() == 0x53 ) )   // "S"
        {
            fis.close();
 /*           fis = (PlainTextInputStream) new URL(imagePath).getContent();
            File aFile = new File( "c:/unzipped/src_abc.swf" );
            FileOutputStream outStream = new FileOutputStream( aFile );
            byte[] buffer = new byte[ 1000 ];
            int readSize;
            while( ( readSize = fis.read( buffer )) > 0 )
            {
                outStream.write( buffer, 0, readSize );
                outStream.flush();
            }
            fis.close();
            outStream.close(); */
            filePath = decompressSWC( imagePath );
            FileInputStream fis1 = new FileInputStream( new File( filePath ) );
            in = new InStream( fis1 );
        }
        else
        {
            fis.close();
            fis = (PlainTextInputStream) new URL(imagePath).getContent();
            in = new InStream( fis );
        }
        if ( readHeader() )
        {
	        readTags();
	        width = consumer.getMaxWidth() / 20;
	        height = consumer.getMaxHeight() / 20;
        }
        fis.close();
        if ( filePath != null )
        {
            File tobedelete = new File( filePath );
            tobedelete.delete();
        }
    }
    
    public boolean readHeader() throws Exception
    {
        boolean result;
//      --Verify File Signature
        if( ( in.readUI8() != 0x46 ) ||  // "F"
            ( in.readUI8() != 0x57 ) ||  // "W"
            ( in.readUI8() != 0x53 ) )   // "S"
        {
 //           throw new IOException( "Invalid SWF File Signature" );
            return false;
        }
        else
            result = true;

        int  version   = in.readUI8();
        long length    = in.readUI32();
        Rect frameSize = new Rect( in );
        int frameRate  = in.readUI16() >> 8;
        int frameCount = in.readUI16();                
        
        consumer.header( version, length, 
                         frameSize.getMaxX(), frameSize.getMaxY(), 
                         frameRate, frameCount );    
        return result;
    }
    
    /**
     * Drive the consumer by reading SWF tags only
     */
    public void readTags() throws IOException 
    {
        while( readOneTag() != SWFConstants.TAG_END );
    }
    
    /**
     * Drive the consumer by reading one tag
     * @return the tag type
     */
    public int readOneTag() throws IOException 
    {
        int header = in.readUI16();
        
        int  type   = header >> 6;    //only want the top 10 bits
        int  length = header & 0x3F;  //only want the bottom 6 bits
        boolean longTag = (length == 0x3F);
        
        if( longTag )
        {
            length = (int)in.readUI32();
        }
        
        byte[] contents = in.read( length );
        
        consumer.tag( type, longTag, contents );
        
        return type;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public int getWidth()
    {
        return width;
    }
}

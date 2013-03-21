/****************************************************************
 * Copyright (c) 2001, David N. Main, All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the 
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the following 
 * disclaimer. 
 * 
 * 2. Redistributions in binary form must reproduce the above 
 * copyright notice, this list of conditions and the following 
 * disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 * 
 * 3. The name of the author may not be used to endorse or 
 * promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ****************************************************************/
package com.ctb.contentBridge.core.publish.anotherbigidea.flash.sound;

import java.io.*;
import java.util.*;

import com.ctb.contentBridge.core.publish.anotherbigidea.flash.SWFConstants;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.structs.Color;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.writers.SWFWriter;
import com.ctb.contentBridge.core.publish.anotherbigidea.flash.writers.TagWriter;

/**
 * MP3 Utilities
 */
public class MP3Helper
{
    /**
     * Read an MP3 input file.
     * Write the Sound Stream Header to the SWFTagTypes interface.
     * Return a list of byte[] - one for each Streaming Sound Block
     */
    public static ArrayList streamingBlocks( InputStream mp3, SWFTagTypes tags,
                                             int framesPerSecond ) 
        throws IOException
    {
        ArrayList list = new ArrayList();
        
        MP3Frame frame = MP3Frame.readFrame( mp3 );
        
        int samplesPerFrame = frame.getSamplesPerFrame();
        int sampleRate = frame.getSampleRate();
        
        int mp3FramesPerSecond = sampleRate / samplesPerFrame;
        int mp3FramesPerSwfFrame = mp3FramesPerSecond / framesPerSecond;
        
        boolean isStereo = frame.isStereo();
        int blockSize = ( frame.getDataLength() + 4 ) * mp3FramesPerSwfFrame;
        
        int rate = SWFConstants.SOUND_FREQ_5_5KHZ;
        if     ( sampleRate >= 44000 ) rate = SWFConstants.SOUND_FREQ_44KHZ;
        else if( sampleRate >= 22000 ) rate = SWFConstants.SOUND_FREQ_22KHZ;
        else if( sampleRate >= 11000 ) rate = SWFConstants.SOUND_FREQ_11KHZ;
        
        tags.tagSoundStreamHead( rate, true, isStereo,
                                 SWFConstants.SOUND_FORMAT_MP3,
                                 rate, true, isStereo, 
                                 mp3FramesPerSwfFrame * samplesPerFrame );
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream( blockSize + 1000 );
        
        while( true && frame != null )
        {        
            //--Write dummy sample count
            bout.write(0);
            bout.write(0);
            
            //--Write DelaySeek of zero
            bout.write(0);
            bout.write(0);
        
            int frameCount = mp3FramesPerSwfFrame;
            int sampleCount = 0;
            
            while( frameCount > 0 && frame != null )
            {
                sampleCount += frame.getSamplesPerFrame();
                frame.write( bout );
                frameCount--;
                frame = MP3Frame.readFrame( mp3 );
            }
            
            bout.flush();
            byte[] bytes = bout.toByteArray();
            bytes[0] = (byte)(sampleCount & 0xFF);
            bytes[1] = (byte)(sampleCount >> 8);
            
            list.add( bytes );
            bout.reset();            
        }
        
        mp3.close();
        
        return list;
    }
    
    /**
     * Makes a streaming SWF from an MP3.
     * args[0] = MP3 in filename
     * args[1] = SWF out filename
     */
    public static void main( String[] args ) throws IOException
    {
        FileInputStream mp3 = new FileInputStream( args[0] );                                                 
        SWFWriter swfwriter = new SWFWriter( args[1] );
        
        SWFTagTypes tags = new TagWriter( swfwriter );
        
        tags.header( 5, -1, 200, 200, 12, -1 );
        tags.tagSetBackgroundColor( new Color(255,255,255));
        
        ArrayList blocks = MP3Helper.streamingBlocks( mp3, tags, 12 );
        
        for( Iterator it = blocks.iterator(); it.hasNext(); )
        {
            byte[] data = (byte[])it.next();
            
            tags.tagSoundStreamBlock( data );
            tags.tagShowFrame();
        }
        
        tags.tagEnd();
    }
}

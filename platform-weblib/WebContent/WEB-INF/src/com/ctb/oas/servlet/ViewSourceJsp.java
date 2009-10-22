package com.ctb.oas.servlet; 

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewSourceJsp extends HttpServlet
{ 
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) {
        viewSource(request, response);
    }
    
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) {
        viewSource(request, response);
    }

    
    protected void viewSource( HttpServletRequest request, HttpServletResponse response )
     {
        try {
            if( request.getPathTranslated() == null ) {
                response.sendError( HttpServletResponse.SC_BAD_REQUEST );
                return;
            }
                
    
            File file = new File( request.getPathTranslated() );
            
            if( !file.canRead() ) {
                response.sendError( HttpServletResponse.SC_FORBIDDEN );
                return;
            } else if( file.isDirectory() ) {
                response.sendError( HttpServletResponse.SC_FORBIDDEN );
                return;
            } else if( !file.getName().toLowerCase().endsWith(".jsp") ) {
                response.sendError( HttpServletResponse.SC_BAD_REQUEST );
                return;
            }
    
            
            BufferedReader reader = new BufferedReader( new FileReader(file) );
            ServletOutputStream out = response.getOutputStream();
                
            response.setContentType("text/plain");
            String line = reader.readLine();
            while( line != null ) {
                out.println( line );
                line = reader.readLine();
            }
        } catch( FileNotFoundException fnfe ) {
            fnfe.printStackTrace(System.err);
        } catch( IOException ioe ) {
            ioe.printStackTrace(System.err);
        }
    }
} 

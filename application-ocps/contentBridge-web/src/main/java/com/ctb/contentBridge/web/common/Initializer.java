/**
 * 
 */
package com.ctb.contentBridge.web.common;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.ctb.contentBridge.core.upload.service.ContentCreatorService;
import com.ctb.contentBridge.web.util.PublishOrderPollingAction;

/**
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public class Initializer extends HttpServlet {
	public void init() throws ServletException
    {     
		try{
          System.out.println("************");
          System.out.println("*** Servlet Initialized successfully ***..");
          System.out.println("***********");
          
          PublishOrderPollingAction mvPublishOrderPollingAction = new PublishOrderPollingAction();
          Thread mvThread = new Thread(mvPublishOrderPollingAction);
          mvThread.start();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
    }
}

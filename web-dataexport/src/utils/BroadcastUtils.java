package utils; 
 
import java.util.ArrayList;
import java.util.List;

import com.ctb.bean.testAdmin.BroadcastMessage;
import com.ctb.bean.testAdmin.BroadcastMessageData;
import com.ctb.util.SQLutils;
 

public class BroadcastUtils 
{ 
    public static List getBroadcastMessages(com.ctb.control.db.BroadcastMessageLog message, String userName) {
    	
    	List broadcastMessages = new ArrayList();
    	
        try {
           BroadcastMessageData bmd = new BroadcastMessageData();
           Integer [] prodId = message.getFrameworkProductForUser(userName);
           Integer pageSize = null;
           String qString = "''";
           
           if (prodId != null && prodId.length > 0 ){
        	   qString = SQLutils.convertArraytoString(prodId);
           }
          
           bmd.setBroadcastMessages(message.getProductSpecificBroadcastMsg(qString), null);
           
           BroadcastMessage[] bcMessages = bmd.getBroadcastMessages();
           if (bcMessages.length > 0) {
                for (int i=0; i<bcMessages.length ; i++) {
                	broadcastMessages.add(bcMessages[i]);
                }
           } 
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
        return broadcastMessages;
    }
    
    public static String buildBroadcastMessages(List broadcastMessages)
    {        
        String bcmIdentifier = "<div style='display: none'>BroadcastMessageIdentifier</div>";    	
        String html = bcmIdentifier + "<table class='simpletable'>";        
		String messages = "You have no messages at this time. The Messages link will display a numbered red square <span class='messageheader'>&nbsp;</span> when you have active messages.";
		
        if (broadcastMessages.size() > 0)
        {
            html += "<tr class='tableHeader'>";
            html += "<th align='left'>&nbsp;&nbsp;Message</th><th >Date</th></tr>";
            html += "</tr>";
            messages = "";
            for (int i=0; i<broadcastMessages.size(); i++) {
            	BroadcastMessage bm = (BroadcastMessage)broadcastMessages.get(i);
                html += "<tr class='simpletable'>";
            	html += "<td class='simpletable'>" + bm.getMessage() + "</td>";
            	String dateStr = DateUtils.formatDateToDateString(bm.getCreatedDateTime());
            	html += "<td class='simpletable'>" + dateStr + "</td>";
                html += "</tr>";
            }
        }   
        else {
            html += "<tr class='simpletable'><td class='simpletable alignCenter'>";
        	html += "<br/>";
        	html += messages;
        	html += "<br/><br/>";
            html += "</td></tr>";
        }
		
        html += "</table>";
        
        return html;    
    }
    

    
} 

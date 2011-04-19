package itemPlayer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import utils.AssetInfo;
import utils.ItemPlayerUtils;
import utils.MemoryCache;

import com.ctb.bean.testAdmin.ItemData;
import com.ctb.util.OASLogger;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException;


@Jpf.Controller(nested = true)
public class ItemPlayerController extends PageFlowController {
	private static final long serialVersionUID = 1L;
	
	
	   @Control()
	    private com.ctb.control.crscoring.TestScoring testScoring;

	/**
	 * Callback that is invoked when this controller instance is created.
	 */
	@Override
	protected void onCreate() {
	}

	/**
	 * Callback that is invoked when this controller instance is destroyed.
	 */
	@Override
	protected void onDestroy(HttpSession session) {
	}
	
	
	private String itemNumber = null;
    private String firstName = null;
    private String lastName = null;
    private String fullName = null;
    
    // Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app

    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="colorFontPreview.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "ViewQuestion.do")
    })
    protected Forward begin()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" return-action="ViewQuestionDone"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     returnAction = "ViewQuestionDone")
    })
    protected Forward ViewQuestionDone()
    {      
        return new Forward("success");
    }
        
    /**
     * @jpf:action
     * @jpf:forward name="success" path="color_font_preview.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "color_font_preview.jsp")
    })
    protected Forward ViewQuestion()
    {      
        String value = null;
        String param = (String)getSession().getAttribute("param");
        System.out.println("param"+param);
        if (param != null) {
        	
            StringTokenizer tn = new StringTokenizer(param, ",");
           this.itemNumber = param;
          //  int count = tn.countTokens();  
           // this.itemNumber = tn.nextToken();
            if ((this.itemNumber == null)) {           
                this.itemNumber = "1";               
            }
            this.getRequest().setAttribute("itemNumber", this.itemNumber);  
            System.out.println("ViewQuestion " + this.itemNumber);
        }
       
        return new Forward("success");
    }
   
    
    /**
     * @jpf:action
     * @jpf:forward name="subtest" path="subtest.jsp"
     * @jpf:forward name="item1" path="item1.jsp"
     * @jpf:forward name="item2" path="item2.jsp"
     * @jpf:forward name="item3" path="item3.jsp"
     */
    @Jpf.Action(forwards = { 
    		@Jpf.Forward(name = "item1", path = "item1.jsp"), 
			@Jpf.Forward(name = "item2", path = "item2.jsp") 
			
		}
	)
    protected Forward ContentServlet()
    {
    	String OK = "<OK />";
        String ERROR = "<ERROR />";
		MemoryCache aMemoryCache = MemoryCache.getInstance();
		HashMap assetMap = aMemoryCache.getAssetMap();
        String result = OK; 
        String method = getRequest().getParameter("method");
        String requestXml = getRequest().getParameter("requestXML");
        String itemId = getRequest().getParameter("itemNum");
        String imageId = getRequest().getParameter("imageId");
        try{
        
            if (method.equals("downloadItem"))
                result = OK; 
            else 
            if (method.equals("getSubtest"))
                return new Forward("subtest"); 
            else 
            if (method.equals("getItem")) {        
          
      ItemData item = this.testScoring.getItemXML(itemId);	
       // 	ItemData item = this.testScoring.getItemXML("9D_RE_Sample_A_copy");
            String itemXML = new String(item.getItem());
           itemXML = ItemPlayerUtils.doUTF8Chars(itemXML);
            
           byte [] itemEncodedXML = itemXML.getBytes("UTF-8");
         //  byte [] decryptedContent = item.getItem();
            org.jdom.Document itemDoc = null;
			synchronized(aMemoryCache.saxBuilder) {
					itemDoc = aMemoryCache.saxBuilder.build(new ByteArrayInputStream(itemEncodedXML));
			}
			org.jdom.Element element = (org.jdom.Element) itemDoc.getRootElement();
			element = element.getChild("assets");
			if (element != null) {
				List imageList = element.getChildren();
				for (int i = 0; i < imageList.size(); i++) {
					element = (org.jdom.Element) imageList.get(i);
					 imageId = element.getAttributeValue("id");
					if (!assetMap.containsKey(imageId)) {
						String mimeType = element.getAttributeValue("type");
						String ext = mimeType.substring(mimeType
								.lastIndexOf("/") + 1);
						String b64data = element.getText();
						b64data = ItemPlayerUtils.replaceAll(b64data,"&amp;#43;","+"); //To Escape Base64 special character "+"
						byte[] imageData = Base64.decode(b64data);
						AssetInfo aAssetInfo = new AssetInfo();
						aAssetInfo.setData(imageData);
						aAssetInfo.setExt(ext);
						assetMap.put(imageId, aAssetInfo);
					}
				}
			}
			String itemxml = updateItem(itemEncodedXML, assetMap);
		
            System.out.println("**************************Item Xml**********************" + item.getItemId() + " :: " +  item.getItem().toString() + " ::  " + itemxml);
            
            
           HttpServletResponse resp = this.getResponse();     
 		   resp.setContentType("text/xml");
            resp.flushBuffer();
 	        OutputStream stream = resp.getOutputStream();
 	        stream.write(itemxml.getBytes());
 	        stream.close();
           
            }
            else 
            if (method.equals("getImage")) 
                return getImage(requestXml);
            else
                result = ERROR;  
                      
            // return response to client
          //  this.writeResponse(result);
        }
        catch(MalformedByteSequenceException e){
              	e.printStackTrace();
        }
        catch(Exception e) { 
        	OASLogger.getLogger("TestAdmin").error(
					"Exception occurred while retrieving the item.", e); 
             e.printStackTrace();
        }
        
        return null; 
    }


    /**
     * 
     */
    private Forward getImage(String imageRequestXml)
    {
    	
    	System.out.println("Get Image XMl Called");
        Forward result = null;
        String imageId = null;
        MemoryCache aMemoryCache = MemoryCache.getInstance();
		HashMap assetMap = aMemoryCache.getAssetMap();
        try
        {
        	imageId = ItemPlayerUtils.parseTag("imageid=", imageRequestXml);
        	System.out.println("get Image image Id" + imageId);
        		if (imageId == null || "".equals(imageId.trim())) // invalid image id
				throw new Exception("No image id in request.");
			
			if (!assetMap.containsKey(imageId)) 
				throw new Exception("Image with id '"+imageId+
						"' not found in memory cache. Please call getItem before getImage.");

			AssetInfo assetInfo = (AssetInfo) assetMap.get(imageId);
			if (assetInfo == null)
				throw new Exception("Image with id '"+imageId+
				"' not found in memory cache. Please call getItem before getImage.");
			HttpServletResponse resp = this.getResponse();    
            String MIMEType = assetInfo.getMIMEType();
            resp.setContentType( MIMEType );
            byte[] data = assetInfo.getData();
            int size = data.length;
            resp.setContentLength( size );
            ServletOutputStream myOutput = resp.getOutputStream();
            myOutput.write( data );
            myOutput.flush();
            myOutput.close();			
		} catch (Exception e) {
		e.printStackTrace();
           
		}
        return result;
    }
    
    /**
     * 
     * 
     * 
     * @param itemBytes
     * @param assetMap
     * @return
     * @throws Exception
     */
	private String updateItem( byte[] itemBytes, HashMap assetMap ) throws Exception
    {
		MemoryCache aMemoryCache = MemoryCache.getInstance();
		org.jdom.Document itemDoc = null;
		synchronized(aMemoryCache.saxBuilder) {
          itemDoc = aMemoryCache.saxBuilder.build( new ByteArrayInputStream( itemBytes ) );
		}
        org.jdom.Element rootElement = (org.jdom.Element) itemDoc.getRootElement();
        if (rootElement.getChild( "assets" )!=null)
        	rootElement.getChild( "assets" ).detach();
        List items = extractAllElement( ".//image_widget", rootElement);
        for ( int i = 0; i < items.size(); i++ )
        {
            org.jdom.Element element = ( org.jdom.Element )items.get( i );
            String id = element.getAttributeValue( "image_ref" );
            if ( id != null && assetMap.containsKey( id ))
                element.setAttribute( "src", id );
        }
        XMLOutputter aXMLOutputter = new XMLOutputter();
        StringWriter aStringWriter = new StringWriter();
        aXMLOutputter.output( rootElement, aStringWriter );
        return aStringWriter.getBuffer().toString();
    }
    
	
	public static List extractAllElement(String pattern, Element element ) throws Exception
	{
//		TO-DO: this will only work with simple './/name' queries as is . . .
		ArrayList results = new ArrayList();
		pattern = pattern.substring(pattern.indexOf(".//") + 3);
		List children = element.getChildren();
		Iterator iterator = children.iterator();
		while(iterator.hasNext()) {
			Element elem = (Element) iterator.next();
			if(pattern.equals(elem.getName())) {
				results.add(elem);
			}
			results.addAll(extractAllElement(".//" + pattern, elem));
		}
		return results;
	}
   
    /**
     * write xml content to response 
     * 
     */
    private void writeResponse(String xml) throws IOException {
        HttpServletResponse response = this.getResponse();
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.println(xml);            
        out.flush();
        out.close();        
    }

     /**
     * login xml
     * 
     */
    private String getLoginXML() 
    {       
        /*String questionBgColor = this.accommodations.getQuestion_bgrdColor();
        questionBgColor = questionBgColor.replaceAll("#", "0x");
        String questionFgColor = this.accommodations.getQuestion_fontColor();
        questionFgColor = questionFgColor.replaceAll("#", "0x");
        String questionFontSize = this.accommodations.getFontSize();
    
        String answerBgColor = this.accommodations.getAnswer_bgrdColor();
        answerBgColor = answerBgColor.replaceAll("#", "0x");
        String answerFgColor = this.accommodations.getAnswer_fontColor();
        answerFgColor = answerFgColor.replaceAll("#", "0x");
        String answerFontSize = this.accommodations.getFontSize();*/
        
        String calculator = "0";
        String magnifier = "0";
        String screenReader = "1";
        String untimed = "0";
        String rest_break = "0";        
        String timeLimit = "0";
        String highlighter = "true";
            
        String pageTitle = "Color and Font Preview";
        
        
        String loginXML = "<tmssvc_response method=\"login_response\"><login_response restart_number=\"0\" restart_flag=\"false\" lsid=\"1:swirl50346\" ><status status_code=\"200\"/>" +
         "<testing_session_data><cmi.core student_first_name=\"" + this.firstName + "\" student_last_name=\"" + this.lastName + "\"student_middle_name=\"\" student_id=\"12345\" />" +
          "<lms.student.accommodations calculator=\"" + calculator + "\" magnifier=\"" + magnifier + 
          "\" screen_reader=\"" + screenReader + "\" untimed=\"" + untimed + "\" rest_break=\"" + rest_break + "\" highlighter=\"" + highlighter+ 
          "\"><stereotype_style stereotype=\"directions\"\"/></lms.student.accommodations></testing_session_data>" +
         "<manifest title=\"" + pageTitle.replaceAll( "& ", "&amp; " ) + "\">" +
         "<sco cmi.core.total_time=\"0:0:0\" adsid=\"10\" item_encryption_key=\"n7673nBJ2n27bB4oAfme7Ugl5VV42g8\" asmt_encryption_key=\"1\" asmt_hash=\"1003A05C5AFDD27F24A5F05B627C52E9\"" +
         " title=\"" + pageTitle.replaceAll( "& ", "&amp; " ) + 
         "\" sco_unit_type=\"SUBTEST\" sco_unit_question_number_offset=\"0\" sco_duration_minutes=\""+ timeLimit + "\" id=\"24105\" cmi.core.entry=\"ab-initio\" force_logout=\"false\" />" + 
         "<terminator id=\"SEE_YOU_LATER\" /> </manifest> </login_response> </tmssvc_response>";
         
        return loginXML;    
    }

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}	
}
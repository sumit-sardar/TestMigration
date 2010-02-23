package previewer;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import dto.StudentAccommodationsDetail;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.jdom.Element;



/**
 * @jpf:controller nested="true"
 *  */
@Jpf.Controller(nested = true)
public class PreviewerController extends PageFlowController
{
    static final long serialVersionUID = 1L;

    private StudentAccommodationsDetail accommodations = null;
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
                     path = "colorFontPreview.do")
    })
    protected Forward begin()
    {
        this.accommodations = new StudentAccommodationsDetail();
        
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" return-action="colorFontPreviewDone"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     returnAction = "colorFontPreviewDone")
    })
    protected Forward colorFontPreviewDone()
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
    protected Forward colorFontPreview()
    {      
        String value = null;
        String param = (String)getSession().getAttribute("param");
        
        if (param != null)
        {
            StringTokenizer tn = new StringTokenizer(param, ",");

            value = this.accommodations.getColorHexMapping(tn.nextToken());
            this.accommodations.setQuestion_bgrdColor(value);
            this.getRequest().setAttribute("question_bgrdColor", value);
            
            value = this.accommodations.getColorHexMapping(tn.nextToken());
            this.accommodations.setQuestion_fontColor(value);
            this.getRequest().setAttribute("question_fontColor", value);
                            
            value = this.accommodations.getColorHexMapping(tn.nextToken());
            this.accommodations.setAnswer_bgrdColor(value);
            this.getRequest().setAttribute("answer_bgrdColor", value);
            
            value = this.accommodations.getColorHexMapping(tn.nextToken());
            this.accommodations.setAnswer_fontColor(value);
            this.getRequest().setAttribute("answer_fontColor", value);
            
            value = tn.nextToken();
            this.accommodations.setFontSize(value);
            this.getRequest().setAttribute("fontSize", value);

            this.firstName = null;
            this.lastName = null;
            this.fullName = null;
            
            int count = tn.countTokens();              
            if (tn.countTokens() > 0)                        
                this.firstName = tn.nextToken();
            if (tn.countTokens() > 0)                        
                this.lastName = tn.nextToken();
            
            if ((this.firstName == null) && (this.lastName == null))
            {           
                this.firstName = "New";
                this.lastName = "Student";
            }
            else
            {
                if (this.firstName == null)
                    this.firstName = "";
                if (this.lastName == null)
                    this.lastName = "";     
                
                this.firstName = this.firstName.replace('^', ',');           
                this.lastName = this.lastName.replace('^', ',');           
            }    
            this.fullName = this.firstName + " " + this.lastName;
                        
            this.getRequest().setAttribute("studentName", this.fullName);
        }
        
        return new Forward("success");
    }
   
    /**
     * @jpf:action
      */
    @Jpf.Action()
    protected Forward PersistenceServlet()
    {    
        String OK = "<OK />";
        String ERROR = "<ERROR />";
     
        String result = OK; 
        String method = getRequest().getParameter("method");
        String inxml = getRequest().getParameter("requestXML");
        
        if (method.equals("login"))
            result = getLoginXML(); 
        else if (method.equals("save") || method.equals("feedback") || method.equals("uploadAuditFile") || method.equals("writeToAuditFile"))
            result = OK; 
        else
            result = ERROR;  
              
        try
        {
            this.writeResponse(result);
        }
        catch (IOException e)
        {
        }
        
        return null; 
    }
   
    /**
     * @jpf:action
     * @jpf:forward name="subtest" path="subtest.jsp"
     * @jpf:forward name="item1" path="item1.jsp"
     * @jpf:forward name="item2" path="item2.jsp"
     * @jpf:forward name="item3" path="item3.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "subtest",
                     path = "subtest.jsp"), 
			@Jpf.Forward(name = "item1", path = "item1.jsp"), 
			@Jpf.Forward(name = "item2", path = "item2.jsp"), 
			@Jpf.Forward(name = "item3", path = "item3.jsp")
		}
	)
    protected Forward ContentServlet()
    {
        String OK = "<OK />";
        String ERROR = "<ERROR />";
        
        String result = OK; 
        String method = getRequest().getParameter("method");
        String inxml = getRequest().getParameter("requestXML");
    
        try{
        
            if (method.equals("downloadItem"))
                result = OK; 
            else 
            if (method.equals("getSubtest"))
                return new Forward("subtest"); 
            else 
            if (method.equals("getItem")) {        
            
           
                if (inxml.indexOf("itemid=\"1\"") > 0)
                    return new Forward("item1"); 
                if (inxml.indexOf("itemid=\"2\"") > 0)
                    return new Forward("item2"); 
                if (inxml.indexOf("itemid=\"3\"") > 0)
                    return new Forward("item3");  
                                 
            }
            else 
            if (method.equals("getImage")) 
                return getImage(inxml);
            else
                result = ERROR;  
                      
            // return response to client
            this.writeResponse(result);
        }
        catch(Exception e) { 
            e.printStackTrace();
        }
        
        return null; 
    }


    /**
     * 
     */
    private Forward getImage(String inxml)
    {
        Forward result = null;
        try
        {
           String imageId = "1";
            if (inxml.indexOf("imageid=\"snacks_10rSB.swf\"") > 0)
                imageId = "snacks_10rSB.swf";
            if (inxml.indexOf("imageid=\"2.4.1.02A_09mOAS.swf\"") > 0)
                imageId = "2.4.1.02A_09mOAS.swf";
            if (inxml.indexOf("imageid=\"2.4.1.02B_09mOAS.swf\"") > 0)
                imageId = "2.4.1.02B_09mOAS.swf";
            if (inxml.indexOf("imageid=\"2.4.1.02C_09mOAS.swf\"") > 0)
                imageId = "2.4.1.02C_09mOAS.swf";
            if (inxml.indexOf("imageid=\"2.4.1.02D_09mOAS.swf\"") > 0)
                imageId = "2.4.1.02D_09mOAS.swf";
                      
            getRequest().setAttribute( "imageType", "application/x-shockwave-flash" );
            
            String filePath = "/images/" + imageId ;
                     
            BufferedInputStream bis = new BufferedInputStream( this.getClass().getResourceAsStream(filePath));
                        
            int size = bis.available();
            byte[] data = new byte[ size ];
            bis.read( data );
            bis.close();
            getResponse().setContentType( "application/x-shockwave-flash" );
            getResponse().setContentLength( size );
            ServletOutputStream myOutput = getResponse().getOutputStream();
            myOutput.write( data );
            myOutput.flush();
            myOutput.close();
            result = null;
           
        }
        catch (Exception e) 
        {
            StackTraceElement [] trace = e.getStackTrace();
            StringBuffer sb = new StringBuffer();
            sb.append("\n" + e.getMessage() + "\n");
            for( int i = 0; i < trace.length; i++ ) 
            {
                sb.append( trace[i].getClassName() + "." + trace[i].getMethodName() + "()  " + trace[i].getFileName() + " line " + trace[i].getLineNumber() + "\n");
            }
            getRequest().setAttribute( "errorMessage", sb.toString() );
            result = new Forward("error");
        }
        return result;
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
        String questionBgColor = this.accommodations.getQuestion_bgrdColor();
        questionBgColor = questionBgColor.replaceAll("#", "0x");
        String questionFgColor = this.accommodations.getQuestion_fontColor();
        questionFgColor = questionFgColor.replaceAll("#", "0x");
        String questionFontSize = this.accommodations.getFontSize();
    
        String answerBgColor = this.accommodations.getAnswer_bgrdColor();
        answerBgColor = answerBgColor.replaceAll("#", "0x");
        String answerFgColor = this.accommodations.getAnswer_fontColor();
        answerFgColor = answerFgColor.replaceAll("#", "0x");
        String answerFontSize = this.accommodations.getFontSize();
        
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
          "\"><stereotype_style stereotype=\"directions\" bgcolor=\"" +  questionBgColor+ "\" font_color=\"" + questionFgColor + "\" font_magnification=\"" + questionFontSize +
          "\"/><stereotype_style stereotype=\"stimulus\" bgcolor=\"" +  questionBgColor+ "\" font_color=\"" + questionFgColor + "\" font_magnification=\"" + questionFontSize +
          "\"/><stereotype_style stereotype=\"stem\" bgcolor=\"" + questionBgColor+ "\" font_color=\"" + questionFgColor+ "\" font_magnification=\"" + questionFontSize +
          "\"/><stereotype_style stereotype=\"answerArea\" bgcolor=\"" + answerBgColor+ "\" font_color=\"" + answerFgColor+ "\" font_magnification=\"" + answerFontSize +
          "\"/></lms.student.accommodations></testing_session_data>" +
         "<manifest title=\"" + pageTitle.replaceAll( "& ", "&amp; " ) + "\">" +
         "<sco cmi.core.total_time=\"0:0:0\" adsid=\"10\" item_encryption_key=\"n7673nBJ2n27bB4oAfme7Ugl5VV42g8\" asmt_encryption_key=\"1\" asmt_hash=\"1003A05C5AFDD27F24A5F05B627C52E9\"" +
         " title=\"" + pageTitle.replaceAll( "& ", "&amp; " ) + 
         "\" sco_unit_type=\"SUBTEST\" sco_unit_question_number_offset=\"0\" sco_duration_minutes=\""+ timeLimit + "\" id=\"24105\" cmi.core.entry=\"ab-initio\" force_logout=\"false\" />" + 
         "<terminator id=\"SEE_YOU_LATER\" /> </manifest> </login_response> </tmssvc_response>";
         
        return loginXML;    
    }
    
}

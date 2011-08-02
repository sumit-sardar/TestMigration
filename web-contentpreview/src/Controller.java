import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.xmlbeans.XmlOptions;
import com.ctb.bean.content.AcknowledgementBean;
import com.ctb.bean.content.AnswerChoiceBean;
import com.ctb.bean.content.DeliverableUnitBean;
import com.ctb.bean.content.ItemBean;
import com.ctb.bean.content.SchedulableUnitBean;
import com.ctb.content.layout.AssessmentLayoutProcessor;
import com.ctb.content.layout.EditorReview;
import com.ctb.content.layout.ItemLayoutProcessor;
import com.ctb.content.layout.LMLRenderer;
import com.ctb.util.OASContentPreviewLogger;
import java.io.BufferedWriter;
import java.io.BufferedInputStream;
import javax.servlet.ServletOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import weblogic.logging.NonCatalogLogger;

/**
 * This is the default controller for a blank web application.
 *
 * @jpf:controller
 *  */
@Jpf.Controller()
public class Controller extends PageFlowController
{   
    public String assessmentTitle;

    public SchedulableUnitBean [] schedulableUnits;
    
    public DeliverableUnitBean [] deliverableUnits;
    
    public Element [] items;
    
    public AcknowledgementBean [] acknowledgements;
    
    public String editorReviewURL;
    
    public String CQAReviewMessage;
    public String QAReviewMessage;
    public String DEVReviewMessage;
    
    public String MaxPanelWidth;
    
    public List unicodeList;
    
    private transient NonCatalogLogger logger = OASContentPreviewLogger.getLogger();
    
    private StringBuffer sb;
    
    private String editorReviewFile = null;
    private String assessmentID;
    
    public boolean errorState = false;
    public List errorItemLML = null;
    // Get parameters, fixing defaults if not sent.
    public static final String OK = "<OK />";
    public static final String ERROR = "<ERROR />";
    //String studentName      = "Doe, John";
    String studentFirstName   = "John";
    String studentLastName      = "Doe";
    String calculator       = "0";
  //  String magnifier        = "0";
    String screenReader     = "0";
    String untimed          = "0";
    String highlighter      = "1";
    String answerFgColor; //    = "0x000000";
    String answerBgColor; //    = "0xFFFFCC";
    String answerFontSize; //   = "1";
    String questionFgColor; //  = "0x000000";
    String questionBgColor; //  = "0xFFFFFF";
    String questionFontSize; // = "1";
    String rest_break = "0";
    String eliminatorResource;

    int selectedDeliverableUnit = 1;
    
    private boolean includeAcknowledgment = false;
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "index",
                     path = "index.jsp")
    })
    protected Forward begin()
    {
        return new Forward("index");
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="preview.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "preview.jsp")
    })
    protected Forward preview(OptionsForm form) throws Exception
    {

        if (!"".equals(form.studentFirstName))
            this.studentFirstName = form.studentFirstName;

        if (!"".equals(form.studentLastName))
            this.studentLastName = form.studentLastName;

        if (!"".equals(form.calculator))
            this.calculator = form.calculator;
     //   if(!"".equals(form.magnifier)) this.magnifier = form.magnifier;
        if (!"".equals(form.screenReader))
            this.screenReader = form.screenReader;
        if (!"".equals(form.untimed))
            this.untimed = form.untimed;
        if (!"".equals(form.highlighter))
            this.highlighter = form.highlighter;
        if (!"".equals(form.answerBgColor))
            this.answerBgColor = form.answerBgColor;
        if (!"".equals(form.answerFgColor))
            this.answerFgColor = form.answerFgColor;
        if (!"".equals(form.answerFontSize))
            this.answerFontSize = form.answerFontSize;
        if (!"".equals(form.questionBgColor))
            this.questionBgColor = form.questionBgColor;
        if (!"".equals(form.questionFgColor))
            this.questionFgColor = form.questionFgColor;
        if (!"".equals(form.questionFontSize))
            this.questionFontSize = form.questionFontSize;
        if (!"".equals(form.rest_break))
            this.rest_break = form.rest_break;
        if (!"".equals(form.eliminatorResource))
            this.eliminatorResource = form.eliminatorResource;
        this.selectedDeliverableUnit = form.deliverableUnit;
        populateSubtestInfo();
        getRequest().setAttribute("eliminatorResource", this.eliminatorResource);
        return new Forward("success");
    }
    

    /**
     * @jpf:action
     * @jpf:forward name="success" path="options.jsp"
     * @jpf:forward name="error" path="error.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "options.jsp"), 
        @Jpf.Forward(name = "error",
                     path = "error.jsp")
    })
    protected Forward upload(UploadForm form)
    {
        editorReviewURL = null;
        CQAReviewMessage = "";
        QAReviewMessage = "";
        DEVReviewMessage = "";
        sb = new StringBuffer();
        String assessmentXML = "";
        try 
        {
            Map params = getRequest().getParameterMap();
            Iterator iter = params.keySet().iterator();
            while (iter.hasNext())
            {
                Object name = (String)iter.next();
                Object value = params.get(name);
                logger.debug("Upload: got param: " +
                             name +
                             ": " +
                             value);
            }
            assessmentXML = getRequest().getParameter("assessmentXML");
            if (assessmentXML == null) 
                assessmentXML = form.getAssessmentXML();
            else  
            {            
      //          assessmentXML = ItemLayoutProcessor.replaceAll( assessmentXML, "%20+", "%20&#x002B;" );
                assessmentXML = URLDecoder.decode(assessmentXML);
            }
            // editorReviewFile = assessmentXML;
//            tempBackup( assessmentXML );
            backupSource(assessmentXML);
            unicodeList = new ArrayList();
            assessmentXML = AssessmentLayoutProcessor.adjustXMLSrcString(assessmentXML, unicodeList);
            
            constructAssessment(assessmentXML);
            constructAnswerKey();
            assessmentXML = null;
            getRequest().setAttribute( "testTitle", this.assessmentTitle );           
        } 
        catch (Exception e) 
        {
            StackTraceElement [] trace = e.getStackTrace();
            sb.append("\n" +
                      e.getMessage() +
                      "\n");
            for (int i=0; i < trace.length; i++)
            {
                sb.append(trace[i].getClassName() + "." + trace[i].getMethodName() + "()  " + trace[i].getFileName() + " line " + trace[i].getLineNumber() + "\n");
            }
            sb.append("\n" +
                      assessmentXML +
                      "\n");
            getRequest().setAttribute("errorMessage", sb.toString());
            return new Forward("error");
        }
        return new Forward("success", new OptionsForm());
    }
    
    protected void tempBackup(String assessmentXML) throws Exception
    {
        File tempfile = new File("c:/eclipse/workspace/application-contentbridge/z.xml");
        FileWriter fileOut = new FileWriter(tempfile);
        fileOut.write(assessmentXML);
        fileOut.close();
    }
    
    protected void backupSource(String assessmentXML) throws Exception
    { 
        String tempFolder = "tmp";
        File tempfile = File.createTempFile("src_", ".tmp", new File(tempFolder));
        editorReviewFile = tempfile.getPath();
        FileWriter fileOut = new FileWriter(tempfile);
        fileOut.write(assessmentXML);
        fileOut.close();
    }
    
    public static String printStackTrace(Exception e) 
    {
        String result = null;
        try
        {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printer = new PrintWriter(stringWriter);
            e.printStackTrace(printer);
            printer.flush();
            printer.close();
            result = stringWriter.getBuffer().toString();
        }
        catch (Exception e1)
        {          
            result = "Nested Exception inside ServletUtils::getStackTrace";
        }
        return result;
    }
    
    public void populateSubtestInfo() throws Exception
    {
        StringBuffer sb2 = new StringBuffer();      
        DeliverableUnitBean subtest = this.deliverableUnits[this.selectedDeliverableUnit - 1];
        if (subtest.getOrderReferences() == null || subtest.getItemReferences() == null)
        {
            errorState = false;
            errorItemLML = null;
            StringBuffer itemReferences = new StringBuffer();
            StringBuffer orderReferences = new StringBuffer();
            ItemBean [] subtestItems = subtest.getItems();
            List subtestLmls = new ArrayList();
            int errorCount = 1;
            for (int i=0; i < subtestItems.length; i++) 
            {
                int id = i + 1;
                ItemBean item = subtestItems[ i ];
                String itemType = item.getItemType();
                
                Element itemLML;
                try
                {
                    itemLML = renderItemToLml(String.valueOf(id), item.getXml());
                    subtestLmls.add(itemLML);
                }
                catch (Exception e)
                {
                    errorState = true;
                    itemReferences.append("<f id=\"" +
                                          errorCount +
                                          "\" h=\"F9AD5E06A6534A1374C5368CB119966F\" k=\"1\" type=\"" +
                                          itemType +
                                          "\"/>");
                    orderReferences.append("<e id=\"" +
                                           errorCount +
                                           "\"/>");
                    createErrorItemLML(item.getXml().getAttributeValue("ID"), printStackTrace(e), errorCount);
                    errorCount++;
                }
                logger.debug("adding item ref: " +
                             id);
                sb2.append("<f id=\"" +
                           id +
                           "\" h=\"F9AD5E06A6534A1374C5368CB119966F\" k=\"1\" type=\"" +
                           itemType +
                           "\"/>\n");
            }
            for (int i=0; i < subtestItems.length; i++) 
            {
                int id = i + 1;
                ItemBean item = subtestItems[ i ];
                item.setXml(null);
            }
            if (errorState)
            {
                subtest.setItemReferences(itemReferences.toString());
                subtest.setOrderReferences(orderReferences.toString());
            }
            else
            {
                String orderString = AssessmentLayoutProcessor.processSubtestStimulus(subtestLmls, true);
                for (int i = 0; i < subtestItems.length && i < subtestLmls.size(); i++) 
                {
                    ItemBean item = subtestItems[ i ];
                    String lmlString = LMLRenderer.xmlToString(( Element )subtestLmls.get(i));
                    item.setLml(lmlString);
                }
                subtest.setItemReferences(sb2.toString());
                subtest.setOrderReferences(orderString);
            }
            System.gc();
        }
    }
    
    /**
     * write xml content to response 
     * 
     */
    public void writeResponse(String xml) throws IOException
    {
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
    public String getLoginXML() 
    {
            
        DeliverableUnitBean subtest = this.deliverableUnits[this.selectedDeliverableUnit - 1];
        this.questionBgColor = this.questionBgColor != null ? this.questionBgColor : "";
        this.questionFgColor = this.questionFgColor != null ? this.questionFgColor : "";
        this.answerBgColor = this.answerBgColor != null ? this.answerBgColor : "0xFFFFB0";
        this.answerFgColor = this.answerFgColor != null ? this.answerFgColor : "";
        this.questionFontSize = this.questionFontSize != null ? this.questionFontSize : "1.0";
        this.answerFontSize = this.answerFontSize != null ? this.answerFontSize : "1.0";    
        String timeLimit = subtest.getTimeLimit();
        if (timeLimit == null)
            timeLimit = "0";
                    
        String loginXML = "<tmssvc_response method=\"login_response\"><login_response restart_number=\"0\" restart_flag=\"false\" lsid=\"1:swirl50346\" tts_speed_value=\"M\"><status status_code=\"200\"/>" +
                          "<testing_session_data><cmi.core student_first_name=\"" +
                          this.studentFirstName +
                          "\" student_last_name=\"" +
                          this.studentLastName +
                          "\" student_middle_name=\"\" student_id=\"12345\" />" +
                          "<lms.student.accommodations calculator=\"" +
                          this.calculator + 
                          "\" screen_reader=\"" +
                          this.screenReader +
                          "\" untimed=\"" +
                          this.untimed +
                          "\" highlighter=\"" +
                          this.highlighter +
                          "\" rest_break=\"" +
                          this.rest_break + 
                          "\"><stereotype_style stereotype=\"directions\" bgcolor=\"" +
                          this.questionBgColor +
                          "\" font_color=\"" +
                          this.questionFgColor +
                          "\" font_magnification=\"" +
                          this.questionFontSize +
                          "\"/><stereotype_style stereotype=\"stimulus\" bgcolor=\"" +
                          this.questionBgColor +
                          "\" font_color=\"" +
                          this.questionFgColor +
                          "\" font_magnification=\"" +
                          this.questionFontSize +
                          "\"/><stereotype_style stereotype=\"stem\" bgcolor=\"" +
                          this.questionBgColor +
                          "\" font_color=\"" +
                          this.questionFgColor +
                          "\" font_magnification=\"" +
                          this.questionFontSize +
                          "\"/><stereotype_style stereotype=\"answerArea\" bgcolor=\"" +
                          this.answerBgColor +
                          "\" font_color=\"" +
                          this.answerFgColor +
                          "\" font_magnification=\"" +
                          this.answerFontSize +
                          "\"/></lms.student.accommodations></testing_session_data>" +
                          "<manifest title=\"" +
                          subtest.getTitle().replaceAll( "& ", "&amp; " ) +
                          "\">" +
                          "<sco cmi.core.total_time=\"0:0:0\" adsid=\"10\" item_encryption_key=\"n7673nBJ2n27bB4oAfme7Ugl5VV42g8\" asmt_encryption_key=\"1\" asmt_hash=\"1003A05C5AFDD27F24A5F05B627C52E9\"" +
                          " title=\"" +
                          subtest.getTitle().replaceAll( "& ", "&amp; " ) + 
                          "\" sco_unit_type=\"SUBTEST\" sco_unit_question_number_offset=\"0\" sco_duration_minutes=\"" +
                          timeLimit +
                          "\" id=\"24105\" cmi.core.entry=\"ab-initio\" force_logout=\"true\" />" + 
                          "<terminator id=\"SEE_YOU_LATER\" /> </manifest> </login_response> </tmssvc_response>";
         
        return loginXML;
    
    }
   
   
    /**
     * @jpf:action
     * @jpf:forward path="error.jsp" name="error"
      */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(path = "error.jsp",
                     name = "error")
    })
    protected Forward PersistenceServlet()
    {
        String result = this.OK; 
        String method = getRequest().getParameter("method");
        String inxml = getRequest().getParameter("requestXML");
        
        if (method.equals("login"))
            result = getLoginXML(); 
        else if (method.equals("save") || method.equals("feedback") || method.equals("uploadAuditFile") || method.equals("writeToAuditFile"))
            result = this.OK; 
        else
            result = this.ERROR;  
              
        try
        {
            // return response to client
            this.writeResponse(result);
        }
        catch (IOException e) 
        {
            StackTraceElement [] trace = e.getStackTrace();
            StringBuffer sb = new StringBuffer();
            sb.append("\n" +
                      e.getMessage() +
                      "\n");
            for (int i = 0; i < trace.length; i++) 
            {
                sb.append(trace[i].getClassName() + "." + trace[i].getMethodName() + "()  " + trace[i].getFileName() + " line " + trace[i].getLineNumber() + "\n");
            }
            getRequest().setAttribute( "errorMessage", sb.toString() );
            return new Forward("error");
        }
        return new Forward("success"); 
    }
   
    /**
     * @jpf:action
     * @jpf:forward path="error.jsp" name="error"
     * @jpf:forward path="subtest.jsp" name="subtest"
     * @jpf:forward path="item.jsp" name="item"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(path = "error.jsp",
                     name = "error"), 
        @Jpf.Forward(path = "subtest.jsp",
                     name = "subtest"), 
        @Jpf.Forward(path = "item.jsp",
                     name = "item")
    })
    protected Forward ContentServlet()
    {
        String result = this.OK; 
        String method = getRequest().getParameter("method");
        String inxml = getRequest().getParameter("requestXML");
        try
        {
             
            if (method.equals("downloadItem"))
                result = this.OK; 
            else if (method.equals("getSubtest"))
            {
                getSubtestXML(); 
                return new Forward("subtest");
            }     
            else if (method.equals("getItem"))
            {
                getClientItemXML(inxml); 
                return new Forward("item");
            }    
            else if (method.equals("getImage")) 
                return getImage(inxml);
            else if (method.equals("getLocalResource")) 
                return getLocalResource();
            else
                result = this.ERROR;  
       
            // return response to client
            this.writeResponse(result);
        }
        catch (Exception e) 
        {
            StackTraceElement [] trace = e.getStackTrace();
            StringBuffer sb = new StringBuffer();
            sb.append("\n" +
                      e.getMessage() +
                      "\n");
            for (int i = 0; i < trace.length; i++) 
            {
                sb.append(trace[i].getClassName() + "." + trace[i].getMethodName() + "()  " + trace[i].getFileName() + " line " + trace[i].getLineNumber() + "\n");
            }
            getRequest().setAttribute( "errorMessage", sb.toString() );
            return new Forward("error");
        }
        return new Forward("success"); 
    }

    protected Forward getLocalResource() throws IOException
    {
        String filename = getRequest().getParameter("resourcePath");
        
        Forward result = null;

        try
        {
            
            if (filename == null || "".equals(filename.trim())) 
                throw new Exception("No filename in request.");

            String resourcePath = getSession().getServletContext().getRealPath("resources");

            String filePath = resourcePath + "/" + filename;
            
            System.out.println("Loading resource: " +
                               filePath);
        
            FileInputStream fstream = new FileInputStream(filePath);
            DataInputStream in = new DataInputStream(fstream);

            ServletOutputStream myOutput = getResponse().getOutputStream();
            byte[] data = new byte[4096];
            int cnt = 0;
            int size = 0;
            int index= filename.lastIndexOf(".");
            String ext = filename.substring(index + 1);
            AssetInfo assetInfo = new AssetInfo();
            assetInfo.setExt(ext);
            String mimeType = assetInfo.getMIMEType();
            getResponse().setContentType(mimeType);

            while ((cnt = in.read(data, 0, 4096)) == 4096)
            {
                size += cnt;
                myOutput.write(data);
            }
            size += cnt;
            size = ((size / 4096) + 1) * 4096;
            getResponse().setContentLength( size );
            myOutput.write(data);
            in.close();	 
            myOutput.flush();
            myOutput.close();	

            result = new Forward("success");
        }
        catch (Exception e)
        {
            StackTraceElement [] trace = e.getStackTrace();
            StringBuffer sb = new StringBuffer();
            sb.append("\n" +
                      e.getMessage() +
                      "\n");
            for (int i = 0; i < trace.length; i++) 
            {
                sb.append(trace[i].getClassName() + "." + trace[i].getMethodName() + "()  " + trace[i].getFileName() + " line " + trace[i].getLineNumber() + "\n");
            }
            getRequest().setAttribute( "errorMessage", sb.toString() );
            result = new Forward("error");
        }
        return result;
    }
    
    public class AssetInfo 
    {
        public byte[] data;
        public String mimeType;
        
        public AssetInfo()
        {
            super();
        }
        
        public void setExt(String ext)
        {
            mimeType = "image/gif";
            if ("swf".equals( ext ))
                mimeType = "application/x-shockwave-flash";
            if ("gif".equals( ext ))
                mimeType = "image/gif";
            if ("jpg".equals( ext ))
                mimeType = "image/jpg";
        }
        
        public void setData(byte[] data_)
        {
            data = data_;
        }
        
        public String getMIMEType()
        {
            return mimeType;
        }
        
        public byte[] getData()
        {
            return data;
        }

    }

    /**
     * 
     */
    protected Forward getImage(String inxml)
    {
        Forward result = null;
        try
        {
            /*String file = getRequest().getParameter("file");
            if ( file.lastIndexOf( "/" ) >= 0 )
                file = file.substring( file.lastIndexOf( "/" ) + 1 );
            String ext = file.substring( file.lastIndexOf( "." ) + 1 ).toLowerCase();
            String imageFolder = getUserImageFolderPath();
            file = imageFolder + "/" + file;*/
            
            org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
            ByteArrayInputStream bais = new ByteArrayInputStream(inxml.toString().getBytes());
            org.jdom.Document requestDoc = saxBuilder.build(bais);
       
            Element requestElement = requestDoc.getRootElement();
            Element imageElement = requestElement.getChild("get_image");
            
            String imageId = imageElement.getAttributeValue("imageid");
            //String imageFolder = getUserImageFolderPath();
            //imageId = imageFolder + "/" + imageId;
            String ext = imageId.substring(imageId.lastIndexOf(".") + 1).toLowerCase();
            
            String mimeType = null;
            if ("swf".equals( ext ))
                mimeType = "application/x-shockwave-flash";
            else if ("gif".equals( ext ))
                mimeType = "image/gif";
            else // let it die if not jpg
                mimeType = "image/jpg";
            getRequest().setAttribute( "imageType", mimeType );
            File imgFile = new File(imageId);
            FileInputStream fis = new FileInputStream(imgFile);
            int size = fis.available();
            byte[] data = new byte[ size ];
            fis.read(data);
            fis.close();
            getResponse().setContentType( mimeType );
            getResponse().setContentLength( size );
            ServletOutputStream myOutput = getResponse().getOutputStream();
            myOutput.write(data);
            myOutput.flush();
            myOutput.close();
            result = new Forward("success");
        }
        catch (Exception e) 
        {
            StackTraceElement [] trace = e.getStackTrace();
            StringBuffer sb = new StringBuffer();
            sb.append("\n" +
                      e.getMessage() +
                      "\n");
            for (int i = 0; i < trace.length; i++) 
            {
                sb.append(trace[i].getClassName() + "." + trace[i].getMethodName() + "()  " + trace[i].getFileName() + " line " + trace[i].getLineNumber() + "\n");
            }
            getRequest().setAttribute( "errorMessage", sb.toString() );
            result = new Forward("error");
        }
        return result;
    }

    /**
     *
     */
    protected void getSubtestXML() throws Exception
    {
   //     errorState = false;
   //     errorItemLML = null;
        getRequest().setAttribute("studentFirstName", this.studentFirstName);
        getRequest().setAttribute("studentLastName", this.studentLastName);
        getRequest().setAttribute("calculator", this.calculator);
       // getRequest().setAttribute("magnifier", this.magnifier);
        getRequest().setAttribute("screenReader", this.screenReader);
        getRequest().setAttribute("untimed", this.untimed);
        getRequest().setAttribute("highlighter", this.highlighter);
        getRequest().setAttribute("answerBgColor", this.answerBgColor);
        getRequest().setAttribute("answerFgColor", this.answerFgColor);
        getRequest().setAttribute("answerFontSize", this.answerFontSize);
        getRequest().setAttribute("questionBgColor", this.questionBgColor);
        getRequest().setAttribute("questionFgColor", this.questionFgColor);
        getRequest().setAttribute("questionFontSize", this.questionFontSize);
        getRequest().setAttribute("rest_break", this.rest_break);
        getRequest().setAttribute("eliminatorResource", this.eliminatorResource);
        StringBuffer sb2 = new StringBuffer();       
        DeliverableUnitBean subtest = this.deliverableUnits[this.selectedDeliverableUnit - 1];
        getRequest().setAttribute("startingQuestionNumber", subtest.getStartingQuestionNumber());
        getRequest().setAttribute("title", subtest.getTitle().replaceAll( "& ", "&amp; " ));
        String timeLimit = subtest.getTimeLimit();
        if (timeLimit == null)
            timeLimit = "0";
        getRequest().setAttribute("timeLimit", timeLimit );
        
        if (subtest.getOrderReferences() == null || subtest.getItemReferences() == null)
        {
            StringBuffer itemReferences = new StringBuffer();
            StringBuffer orderReferences = new StringBuffer();
            ItemBean [] subtestItems = subtest.getItems();
            List subtestLmls = new ArrayList();
            int errorCount = 1;
            for (int i=0; i < subtestItems.length; i++) 
            {
                int id = i + 1;
                ItemBean item = subtestItems[ i ];
                String itemType = item.getItemType();
                    
                Element itemLML;
                try
                {
                    itemLML = renderItemToLml(String.valueOf(id), item.getXml());
                    subtestLmls.add(itemLML);
                }
                catch (Exception e)
                {
                    errorState = true;
                    itemReferences.append("<f id=\"" +
                                          errorCount +
                                          "\" h=\"F9AD5E06A6534A1374C5368CB119966F\" k=\"1\"  type=\"" +
                                          itemType +
                                          "\"/>");
                    orderReferences.append("<e id=\"" +
                                           errorCount +
                                           "\"/>");
                    createErrorItemLML(item.getXml().getAttributeValue("ID"), e.getMessage(), errorCount);
                    errorCount++;
                }
                logger.debug("adding item ref: " +
                             id);
                sb2.append("<f id=\"" +
                           id +
                           "\" h=\"F9AD5E06A6534A1374C5368CB119966F\" k=\"1\" type=\"" +
                           itemType +
                           "\"/>\n");
            }
            if (errorState)
            {
                getRequest().setAttribute("itemReferences", itemReferences.toString() );
                getRequest().setAttribute("orderReferences", orderReferences.toString() );
                return ;
            }
            for (int i=0; i < subtestItems.length; i++) 
            {
                int id = i + 1;
                ItemBean item = subtestItems[ i ];
                item.setXml(null);
            }
            
            String orderString = AssessmentLayoutProcessor.processSubtestStimulus(subtestLmls, true);
            for (int i = 0; i < subtestItems.length; i++) 
            {
                ItemBean item = subtestItems[ i ];
                String lmlString = LMLRenderer.xmlToString(( Element )subtestLmls.get(i));
//                writeLML( lmlString, String.valueOf( i + 1 ));
                item.setLml(lmlString);
            }
            subtest.setItemReferences(sb2.toString());
            subtest.setOrderReferences(orderString);
            System.gc();
        }
        getRequest().setAttribute("itemReferences", subtest.getItemReferences());
        getRequest().setAttribute("orderReferences", subtest.getOrderReferences() );
        
        return;
    }
    
    public void writeLML(String lml, String order) throws Exception
    {
        String path = "c:/eclipse/workspace/application-contentpreview/contentPreviewWeb/PresentationCanvas/xmls/";
        File exportFile = new File(path + "item" + order + ".xml");
        BufferedWriter fileOut = new BufferedWriter(new FileWriter(exportFile));
        fileOut.write(lml);
        fileOut.close();
    }
    
    protected void createErrorItemLML(String itemId, String errorMessage, int order) throws Exception
    {
        FileInputStream is = new FileInputStream(new File("error_item.xml"));
        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
        org.jdom.Document itemDoc = saxBuilder.build(is);
        org.jdom.Element root = itemDoc.getRootElement();
        List selectors = ItemLayoutProcessor.extractAllElement(".//selector", root);
        for (int i = selectors.size() - 1; i >= 0; i--)
        {
            Element selector = ( Element )selectors.get(i);
            if (!selector.getAttributeValue("identifier").equals( "A" ) && !selector.getAttributeValue("identifier").equals( "B" ))
                selector.detach();
        }
        Element item_model = root.getChild("item_model");
        item_model.setAttribute("correct", "A");
        item_model.setAttribute("iid", itemId);
        item_model.setAttribute("eid", String.valueOf(order));
        Element textWidget = ItemLayoutProcessor.extractSingleElement("//text_error_widget", root);
        textWidget.setName("text_widget");
        String errorCDATA = "<p>Encounter following problem when processing item <b>" +
                            itemId +
                            "</b><br/><br/><p>" +
                            errorMessage +
                            "</p>";
        CDATA element = new CDATA(errorCDATA);
        List content = new ArrayList();
        content.add(element);
        textWidget.setContent(content);
        if (errorItemLML == null)
            errorItemLML = new ArrayList();
        errorItemLML.add(root);
        is.close();
    }
   
    
    /**
     * Item XML for the test client
     */
    protected void getClientItemXML(String inxml) throws Exception
    {
         
        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
        ByteArrayInputStream bais = new ByteArrayInputStream(inxml.toString().getBytes());
        org.jdom.Document requestDoc = saxBuilder.build(bais);
        
        Element requestElement = requestDoc.getRootElement();
        Element itemElement = requestElement.getChild("get_item");
             
        String itemId = itemElement.getAttributeValue("itemid");
               
        if (errorState)
        {
            int index = Integer.parseInt(itemId) - 1;
            index = index % errorItemLML.size();
            Element choosenItem = ( Element )errorItemLML.get(index);
            getRequest().setAttribute( "item", LMLRenderer.xmlToString(choosenItem) );
            return ;
        }
        else
        {
            Element xml = null;
           
            String subtestId = getRequest().getParameter("subtest");
            logger.debug("request for item ref: " +
                         itemId);
            DeliverableUnitBean subtest = null;
            if (subtestId == null)
                subtest = this.deliverableUnits[this.selectedDeliverableUnit - 1];
            else
                subtest = this.deliverableUnits[Integer.parseInt(subtestId) - 1];
            ItemBean item = subtest.getItems()[Integer.parseInt(itemId) - 1];
            xml = item.getXml();
            if (item.getLml() == null)
                item.setLml(LMLRenderer.xmlToString(renderItemToLml(itemId, xml)));
                
            getRequest().setAttribute("item", item.getLml());
            return ;
           
        }
        
    }

        
    /** XML for the answer key
     * @jpf:action
     * @jpf:forward name="success" path="item.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "item.jsp")
    })
    protected Forward getItemXML() 
    {
        String itemId = getRequest().getParameter("item");
        if (errorState)
        {
            int index = Integer.parseInt(itemId) - 1;
            index = index % errorItemLML.size();
            Element choosenItem = ( Element )errorItemLML.get(index);
            getRequest().setAttribute( "item", LMLRenderer.xmlToString(choosenItem) );
            return new Forward("success");
        }
        else
        {
            Element xml = null;
            try 
            {
                String subtestId = getRequest().getParameter("subtest");
                logger.debug("getItemXML() request for item ref: " +
                             itemId);
                DeliverableUnitBean subtest = null;
                if (subtestId == null)
                    subtest = this.deliverableUnits[this.selectedDeliverableUnit - 1];
                else
                    subtest = this.deliverableUnits[Integer.parseInt(subtestId) - 1];
                ItemBean item = subtest.getItems()[Integer.parseInt(itemId) - 1];
                xml = item.getXml();
                if (item.getLml() == null)
                    item.setLml(LMLRenderer.xmlToString(renderItemToLml(itemId, xml)));
                
                getRequest().setAttribute("item", item.getLml());
                logger.debug("getItemXML() request is handled for item ref: " +
                             itemId);
                return new Forward("success");
            } 
            catch (Exception e) 
            {
                logger.error("**********Caught " +
                             e.toString());
                StackTraceElement [] trace = e.getStackTrace();
                sb.append("\n" +
                          e.getMessage() +
                          "\n");
                for (int i=0; i < trace.length; i++)
                {
                    sb.append(trace[i].getClassName() + "." + trace[i].getMethodName() + "()  " + trace[i].getFileName() + " line " + trace[i].getLineNumber() + "\n");
                }
                getRequest().setAttribute("errorMessage", sb.toString());
                try 
                {
                    sb.append("\n" +
                              elementToString(xml) +
                              "\n");
                }
                catch (Exception e1)
                {
                    logger.error("**********Caught " +
                                 e1.toString());
                }
                return new Forward("error");
            }
            catch (Throwable t)
            {
                logger.error("**********Caught " +
                             t.toString());
                t.printStackTrace();
                return new Forward("error");
            }
        }
    }

        
    /**
     * @jpf:action
     * @jpf:forward name="success" path="answerKey.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "answerKey.jsp")
    })
    protected Forward viewAnswerKey()
    {
        getRequest().setAttribute( "testTitle", this.assessmentTitle );
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="acknowledgements.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "acknowledgements.jsp")
    })
    protected Forward viewAcknowledgements()
    {
        getRequest().setAttribute( "testTitle", this.assessmentTitle );
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="options.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "options.jsp")
    })
    protected Forward show_options()
    {
        getRequest().setAttribute( "testTitle", this.assessmentTitle );
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward path="options.jsp" name="success"
     * @jpf:forward path="error.jsp" name="error"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(path = "options.jsp",
                     name = "success"), 
        @Jpf.Forward(path = "error.jsp",
                     name = "error")
    })
    protected Forward doEditorReveiw()
    {
        try 
        {
            editorReviewURL = EditorReview.writeAssessmentFile(editorReviewFile, assessmentID);
            getRequest().setAttribute( "testTitle", this.assessmentTitle );
        }
        catch (Exception e) 
        {
            StackTraceElement [] trace = e.getStackTrace();
            sb.append("\n" +
                      e.getMessage() +
                      "\n");
            for (int i=0; i < trace.length; i++)
            {
                sb.append(trace[i].getClassName() + "." + trace[i].getMethodName() + "()  " + trace[i].getFileName() + " line " + trace[i].getLineNumber() + "\n");
            }
            getRequest().setAttribute("errorMessage", sb.toString());
            return new Forward("error");
        }
        return new Forward("success");
    }
    
    /**
     * @jpf:action
     * @jpf:forward path="options.jsp" name="success"
     * @jpf:forward path="error.jsp" name="error"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(path = "options.jsp",
                     name = "success"), 
        @Jpf.Forward(path = "error.jsp",
                     name = "error")
    })
    protected Forward doCQAReview()
    {
        try 
        {
            CQAReviewMessage = null;       
            String folder = getRequest().getParameter( "state" );
            String testName = getRequest().getParameter( "testname" );
            EditorReview.sendToCQA(editorReviewFile, testName, folder, "eqoas109-mgt.edmz.mcgraw-hill.com", "oasuser", "ditp2luh");
            CQAReviewMessage = "CQA is updated.";
            getRequest().setAttribute( "testTitle", this.assessmentTitle );
            System.gc();
        }
        catch ( Exception e ) 
        {
            StackTraceElement [] trace = e.getStackTrace();
            sb.append("\n" + e.getMessage() + "\n");
            for(int i=0;i<trace.length;i++) {
                sb.append(trace[i].getClassName() + "." + trace[i].getMethodName() + "()  " + trace[i].getFileName() + " line " + trace[i].getLineNumber() + "\n");
            }
            getRequest().setAttribute("errorMessage", sb.toString());
            return new Forward("error");
        }
        return new Forward("success");
    }
    
    /**
     * @jpf:action
     * @jpf:forward path="options.jsp" name="success"
     * @jpf:forward path="error.jsp" name="error"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(path = "options.jsp", name = "success"), 
			@Jpf.Forward(path = "error.jsp", name = "error")
		}
	)
    protected Forward doQAReview()
    {
        try 
        {
            QAReviewMessage = null;       
            String folder = getRequest().getParameter( "state1" );
            String testName = getRequest().getParameter( "testname1" );
            EditorReview.sendToCQA( editorReviewFile, testName, folder, "nj09mhe5338.mhe.mhc", "oasuser", "oasuser123" );
            EditorReview.sendToCQA( editorReviewFile, testName, folder, "nj09mhe5339.mhe.mhc", "oasuser", "oasuser123" );
            QAReviewMessage = "QA is updated.";
            getRequest().setAttribute( "testTitle", this.assessmentTitle );
            System.gc();
        }
        catch ( Exception e ) 
        {
            StackTraceElement [] trace = e.getStackTrace();
            sb.append("\n" + e.getMessage() + "\n");
            for(int i=0;i<trace.length;i++) {
                sb.append(trace[i].getClassName() + "." + trace[i].getMethodName() + "()  " + trace[i].getFileName() + " line " + trace[i].getLineNumber() + "\n");
            }
            getRequest().setAttribute("errorMessage", sb.toString());
            return new Forward("error");
        }
        return new Forward("success");
    }
    
    /**
     * @jpf:action
     * @jpf:forward path="options.jsp" name="success"
     * @jpf:forward path="error.jsp" name="error"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(path = "options.jsp", name = "success"), 
			@Jpf.Forward(path = "error.jsp", name = "error")
		}
	)
    protected Forward doDEVReview()
    {
        try
        {
            DEVReviewMessage = null;       
            String folder = getRequest().getParameter( "state2" );
            String testName = getRequest().getParameter( "testname2" );
            EditorReview.sendToCQA( editorReviewFile, testName, folder
                       //         , "dagobah.mhe.mhc", "oasuser", "oasuser123", "https://dagobah.mhe.mhc/" );
                                    , "dagobah.mhe.mhc", "oasuser", "oasuser123" );
            DEVReviewMessage = "DEV is updated.";
            getRequest().setAttribute( "testTitle", this.assessmentTitle );
            System.gc();
        }
        catch ( Exception e ) 
        {
            StackTraceElement [] trace = e.getStackTrace();
            sb.append("\n" + e.getMessage() + "\n");
            for(int i=0;i<trace.length;i++) {
                sb.append(trace[i].getClassName() + "." + trace[i].getMethodName() + "()  " + trace[i].getFileName() + " line " + trace[i].getLineNumber() + "\n");
            }
            getRequest().setAttribute("errorMessage", sb.toString());
            return new Forward("error");
        }
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="options.jsp"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "options.jsp")
		}
	)
    protected Forward backToOption()
    {
        getRequest().setAttribute( "testTitle", this.assessmentTitle );
        return new Forward("success");
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class UploadForm extends FormData
    {
        private String assessmentXML;

        public void setAssessmentXML(String assessmentXML)
        {
            this.assessmentXML = assessmentXML;
        }

        public String getAssessmentXML()
        {
            return this.assessmentXML;
        }
    }
    
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class OptionsForm extends FormData
    {
        private String studentLastName      = "Doe";
        private String studentFirstName      = "John";
        private String calculator       = "0";
       // private String magnifier        = "0";
        private String screenReader     = "0";
        private String untimed          = "0";
        private String highlighter      = "true";
        private String answerFgColor;  //    = "0x000000";
        private String answerBgColor; //    = "0xFFFFCC";
        private String answerFontSize;  // = "1";
        private String questionFgColor; //  = "0x000000";
        private String questionBgColor; //  = "0xFFFFFF";
        private String questionFontSize; // = "1";
        private String rest_break = "0";
        private int deliverableUnit = 1;
        private String eliminatorResource="resources/eliminator.swf";

        /**
         * @return Returns the deliverableUnit.
         */
        public int getDeliverableUnit() {
            return deliverableUnit;
        }
        /**
         * @param deliverableUnit The deliverableUnit to set.
         */
        public void setDeliverableUnit(int deliverableUnit) {
            this.deliverableUnit = deliverableUnit;
        }
        /**
         * @return Returns the answerBgColor.
         */
        public String getAnswerBgColor() {
            return answerBgColor;
        }
        /**
         * @param answerBgColor The answerBgColor to set.
         */
        public void setAnswerBgColor(String answerBgColor) {
            this.answerBgColor = answerBgColor;
        }
        /**
         * @return Returns the answerFgColor.
         */
        public String getAnswerFgColor() {
            return answerFgColor;
        }
        /**
         * @param answerFgColor The answerFgColor to set.
         */
        public void setAnswerFgColor(String answerFgColor) {
            this.answerFgColor = answerFgColor;
        }
        /**
         * @return Returns the answerFontSize.
         */
        public String getAnswerFontSize() {
            return answerFontSize;
        }
        /**
         * @param answerFontSize The answerFontSize to set.
         */
        public void setAnswerFontSize(String answerFontSize) {
            this.answerFontSize = answerFontSize;
        }
        /**
         * @return Returns the calculator.
         */
        public String getCalculator() {
            return calculator;
        }
        /**
         * @param calculator The calculator to set.
         */
        public void setCalculator(String calculator) {
            this.calculator = calculator;
        }
        /**
         * @return Returns the magnifier.
         */
      /*  public String getMagnifier() {
            return magnifier;
        }*/
        /**
         * @param magnifier The magnifier to set.
         */
      /*  public void setMagnifier(String magnifier) {
            this.magnifier = magnifier;
        }*/
        /**
         * @return Returns the questionBgColor.
         */
        public String getQuestionBgColor() {
            return questionBgColor;
        }
        /**
         * @param questionBgColor The questionBgColor to set.
         */
        public void setQuestionBgColor(String questionBgColor) {
            this.questionBgColor = questionBgColor;
        }
        /**
         * @return Returns the questionFgColor.
         */
        public String getQuestionFgColor() {
            return questionFgColor;
        }
        /**
         * @param questionFgColor The questionFgColor to set.
         */
        public void setQuestionFgColor(String questionFgColor) {
            this.questionFgColor = questionFgColor;
        }
        /**
         * @return Returns the questionFontSize.
         */
        public String getQuestionFontSize() {
            return questionFontSize;
        }
        /**
         * @param questionFontSize The questionFontSize to set.
         */
        public void setQuestionFontSize(String questionFontSize) {
            this.questionFontSize = questionFontSize;
        }
        /**
         * @return Returns the screenReader.
         */
        public String getScreenReader() {
            return screenReader;
        }
        /**
         * @param screenReader The screenReader to set.
         */
        public void setScreenReader(String screenReader) {
            this.screenReader = screenReader;
        }
        /**
         * @return Returns the studentLastName.
         */
        public String getStudentLastName() {
            return studentLastName;
        }
        /**
         * @param studentName The studentLastName to set.
         */
        public void setStudentLastName(String studentName) {
            this.studentLastName = studentName;
        }
        /**
         * @return Returns the studentFirstName.
         */
        public String getStudentFirstName() {
            return studentFirstName;
        }
        /**
         * @param studentName The studentName to set.
         */
        public void setStudentFirstName(String studentName) {
            this.studentFirstName = studentName;
        }
        /**
         * @return Returns the untimed.
         */
        public String getUntimed() {
            return untimed;
        }
        /**
         * @param untimed The untimed to set.
         */
        public void setUntimed(String untimed) {
            this.untimed = untimed;
        }
        
        /**
         * @return Returns the highlighter.
         */
        public String getHighlighter() {
            return highlighter;
        }
        /**
         * @param highlighter The highlighter to set.
         */
        public void setHighlighter(String highlighter) {
            this.highlighter = highlighter;
        }
        /**
         * @return Returns the rest_break.
         */
        public String getRest_break() {
            return rest_break;
        }
        /**
         * @param rest_break The rest_break to set.
         */
        public void setRest_break(String rest_break ) {
            this.rest_break = rest_break;
        }
        
        public void setEliminatorResource(String eliminatorResource)
        {
            this.eliminatorResource = eliminatorResource;
        }

        public String getEliminatorResource()
        {
            return this.eliminatorResource;
        }
        
    }
    
    public String getAttributeValue( Element element, String key ) throws Exception
    {   // unicodeList
        String value = element.getAttributeValue( key );
        if ( value != null )
            value = replaceUnicode( value );  
        return value;
    }
    
    public String replaceUnicode( String text )throws Exception
    {
        return AssessmentLayoutProcessor.replaceUnicode( text, unicodeList );
    }
    
    private void constructAssessment( String assessmentString ) throws Exception 
    {
        HashMap ackMap = new HashMap();
        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
        ByteArrayInputStream bais = new ByteArrayInputStream( assessmentString.getBytes());
        org.jdom.Document assessmentDoc = saxBuilder.build( bais );
        Element assessmentElement = assessmentDoc.getRootElement();
        MaxPanelWidth = getAttributeValue( assessmentElement, "MaxPanelWidth" );
        this.assessmentTitle = getAttributeValue( assessmentElement, "Title" );
        this.assessmentTitle = this.assessmentTitle.replaceAll( "&amp;", "&" );
        this.assessmentID = getAttributeValue( assessmentElement, "ID" );
        String includeAckFlag = getAttributeValue( assessmentElement, "IncludeAcknowledgment" );
        if (includeAckFlag != null && "yes".equals(includeAckFlag.toLowerCase()))
            this.includeAcknowledgment = true;
        else 
            this.includeAcknowledgment = false;
        
        ArrayList testSchedulableUnitList = new ArrayList();
        ArrayList testDeliverableUnitList = new ArrayList();
        ArrayList testItemList = new ArrayList();
        ArrayList testAcknowledgementList = new ArrayList();
        List SchedulableUnits = assessmentElement.getChildren( "SchedulableUnit" );
        for( int i = 0; i < SchedulableUnits.size(); i++ )
        {
            Element SchedulableUnit = ( Element )SchedulableUnits.get( i );
            sb.append("processing schedulable unit " + ( i + 1) + ". . . \n");
            SchedulableUnitBean ts = new SchedulableUnitBean();
            ts.setTitle( getAttributeValue( SchedulableUnit, "Title" ).replaceAll( "&amp;", "&" ));
            testSchedulableUnitList.add( ts );
            List DeliverableUnits = SchedulableUnit.getChildren( "DeliverableUnit" );
            ArrayList tsDeliverableUnitList = new ArrayList();
            for( int j = 0; j < DeliverableUnits.size(); j++ )
            {
                Element DeliverableUnit = ( Element )DeliverableUnits.get( j );
                sb.append("   processing deliverable unit " + (j + 1) + ". . . \n");
                DeliverableUnitBean td = new DeliverableUnitBean();
                String startingQuestionNumber = DeliverableUnit.getAttributeValue( "StartItemNumber" );
                if ( startingQuestionNumber == null )
                    startingQuestionNumber = " ";
                else
                    startingQuestionNumber = "starting_question_number=\"" + startingQuestionNumber + "\"";
                td.setStartingQuestionNumber( startingQuestionNumber );
                td.setTitle( getAttributeValue( DeliverableUnit, "Title" ).replaceAll( "&amp;", "&" ) );
                td.setId( getAttributeValue( DeliverableUnit, "ID" ) );
                td.setIndex( String.valueOf( testDeliverableUnitList.size() + 1) );
                td.setTimeLimit( DeliverableUnit.getAttributeValue( "TimeLimit" ) );
                tsDeliverableUnitList.add( td );
                testDeliverableUnitList.add( td );
                List Items = ItemLayoutProcessor.extractAllElement( ".//Item", DeliverableUnit );
                ArrayList tdItemList = new ArrayList();
                for( int k = 0; k < Items.size(); k++ )
                {
                    Element Item = ( Element )Items.get( k );
                    String itemType = Item.getAttributeValue( "ItemType");                  
                    sb.append("      processing item " + (k + 1) + " itemType="+itemType+". . . \n");
                    ItemBean item = new ItemBean();
                    item.setId( Item.getAttributeValue( "ID" ) );
                    item.setIndex( k + 1 );
                    item.setXml( Item );
                    item.setItemType(itemType);
                    tdItemList.add( item );
                    testItemList.add( Item );
                    testAcknowledgementList.addAll( extractAcknowledgements( Item , ackMap ));
                    sb.append("      . . . finished\n");
                }
                td.setItems((ItemBean []) tdItemList.toArray(new ItemBean[0]));
                sb.append("   . . . finished\n");
            }
            ts.setDeliverableUnits((DeliverableUnitBean [])tsDeliverableUnitList.toArray( new DeliverableUnitBean[0] ));
            sb.append(". . . finished\n");
        }
        this.schedulableUnits = (SchedulableUnitBean []) testSchedulableUnitList.toArray(new SchedulableUnitBean[0]);
        this.deliverableUnits = (DeliverableUnitBean []) testDeliverableUnitList.toArray(new DeliverableUnitBean[0]);
        this.items = (Element []) testItemList.toArray(new Element[0]);
        this.acknowledgements = (AcknowledgementBean []) testAcknowledgementList.toArray(new AcknowledgementBean[0]);
    }
    
    private void constructAnswerKey() throws Exception
    {
        int itemCount = 0;
        for(int k = 0; k < this.schedulableUnits.length ;k++ ) 
        {
            SchedulableUnitBean sub = this.schedulableUnits[k];
            for( int j = 0; j < sub.getDeliverableUnits().length; j++ ) 
            {
                DeliverableUnitBean dub = sub.getDeliverableUnits()[j];
                for( int i = 0 ; i < dub.getItems().length; i++ ) 
                {
                    ItemBean item = dub.getItems()[i];
                    AnswerChoiceBean [] answerChoices = null;
                    Element Item = this.items[ itemCount ];
                    List SelectedResponses = ItemLayoutProcessor.extractAllElement( ".//AnswerChoice", Item );
                    if( SelectedResponses.size() > 0 ) 
                    {
                        answerChoices = new AnswerChoiceBean[ SelectedResponses.size() ];
                        for( int l = 0; l < SelectedResponses.size(); l++ ) 
                        {
                            Element AnswerChoice = ( Element )SelectedResponses.get( l );
                            answerChoices[l] = new AnswerChoiceBean();
                            XmlOptions options = new XmlOptions();
                            answerChoices[l].setText( "" );
                            Element Text = AnswerChoice.getChild( "Text" );
                            if ( Text != null )
                            {
                                String text = elementToString( Text );
                                text = stripXML( text ).trim();
                                text = replaceUnicode( text, unicodeList );
                                text = text.replaceAll( "&apos;", "'" );
                                text = text.replaceAll( "&quot;", "\"");
                                text = text.replaceAll( "&nbsp;", " ");
                                answerChoices[l].setText( text );
                            }
                            List Flashs = ItemLayoutProcessor.extractAllElement( ".//Flash", AnswerChoice );
                            for( int m = 0; m < Flashs.size(); m++ )
                            {
                                Element Flash = ( Element )Flashs.get( m );
                                answerChoices[l].setText( answerChoices[l].getText() + " " + Flash.getAttributeValue( "FileName"));
                            }
                            answerChoices[l].setType( AnswerChoice.getAttributeValue( "Type" ));
                        }
                    }
                    else
                    {
                        Element ConstructedResponse = ItemLayoutProcessor.extractSingleElement( ".//ConstructedResponse", Item );
                        if (ConstructedResponse != null) {                       
                            String minPoints = ConstructedResponse.getAttributeValue( "MinScorePts" );
                            String maxPoints = ConstructedResponse.getAttributeValue( "MaxScorePts" );
                            answerChoices = new AnswerChoiceBean[ 1 ];
                            answerChoices[0] = new AnswerChoiceBean();
                            answerChoices[0].setText( minPoints + " - " + maxPoints );
                            answerChoices[0].setType( " " );
                        }
                    }
                    item.setAnswerChoices( answerChoices );
                    itemCount++;
                }
            }
        }
        this.items = null;
    }
    
    public String elementToString( Element theRoot ) throws Exception
    {
        Element clone = ( Element )theRoot.clone();
        XMLOutputter aXMLOutputter = new XMLOutputter();
        StringWriter out = new StringWriter();
        aXMLOutputter.output( clone, out );
        StringBuffer outStringBuffer = out.getBuffer();
        String outString = outStringBuffer.toString();
        return outString;
    }
    
    public String replaceUnicode( String text, List unicodeList_ )throws Exception
    {
        return AssessmentLayoutProcessor.replaceUnicode( text, unicodeList_ );
    }
    
    private ArrayList extractAcknowledgements( Element item, HashMap ackMap ) throws Exception
    {
        ArrayList testAcknowledgementList = new ArrayList();
        List Acknowledgements = ItemLayoutProcessor.extractAllElement( ".//Acknowledgment", item );
        for( int i = 0; i < Acknowledgements.size(); i++ )
        {
            Element Acknowledgement = ( Element )Acknowledgements.get( i );
            AcknowledgementBean ackBean = new AcknowledgementBean();
            ackBean.setItemId( item.getAttributeValue( "ID" ));
            ackBean.setText( "" );
            Element Text = Acknowledgement.getChild( "Text" );
            if ( Text != null )
            {
                String text = elementToString( Text );
           //     text = stripXML( text ).trim();
                text = replaceUnicode( text, unicodeList );
                text = text.replaceAll( "&apos;", "'" );
                if ( !ackMap.containsKey( text ) )
                {
                    ackBean.setText( text );
                    ackMap.put( text, new Integer( 1 ) );
                    testAcknowledgementList.add( ackBean );
                }
            }
            // testAcknowledgementList.add( ackBean );
        }
        return testAcknowledgementList;
    }

    private String stripXML(String xml) {
        StringBuffer sb = new StringBuffer();
        while(xml.length() > 0) {
            int startTag = xml.indexOf("<");
            int endTag = xml.indexOf(">");
            if(startTag < 0 || endTag < 0) {
                sb.append(xml);
                xml = "";
            } else {
                sb.append(xml.substring(0, startTag));
                xml = xml.substring(endTag+1);
            }
        }
        return sb.toString();
    }

    private Element renderItemToLml(String id, Element xml ) throws Exception{
            Element item = LMLRenderer.renderCTBItemToLML(Integer.valueOf(id).intValue(), xml, unicodeList, MaxPanelWidth, this.includeAcknowledgment);
            logger.debug("Rendered item: " + id);
            return item;
    }
    
}

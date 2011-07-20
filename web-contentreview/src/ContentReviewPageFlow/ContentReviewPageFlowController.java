package ContentReviewPageFlow;

import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import com.ctb.bean.content.DeliverableUnitBean;
import com.ctb.bean.content.FileNameComparator;
import com.ctb.bean.content.ItemBean;
import com.ctb.bean.content.SchedulableUnitBean;
import com.ctb.bean.content.TestBean;
import com.ctb.util.web.sanitizer.JavaScriptSanitizer;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import global.Global;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * This is the default controller for a blank web application.
 *
 * @jpf:controller
 *  */
@Jpf.Controller()
public class ContentReviewPageFlowController extends PageFlowController
{
    
    public static boolean forReal = true;
    static final long serialVersionUID = 1L;
    public String pageError;
    public String userName;
    public String password;
    public String[] availableTests;
    
    public String subtestTitle;
    public String testTitle;
    public TestBean[] tests;
    public HashMap userMap;
    protected global.Global globalApp;   
    
    /**
     * @jpf:action
     * @jpf:forward name="login" path="login.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "login",
                     path = "login.jsp")
    })
    protected Forward begin()
    {
        cleanActionData();
        return new Forward("login");
    }

    public void getUserTests() throws Exception
    {
        String userFolderPath = getUserFolderPath();
        File dir = new File(userFolderPath);
        availableTests = dir.list();
        List tempList = new ArrayList();
        for (int i = 0; i < availableTests.length; i++)
            tempList.add(availableTests[ i ]);
        Collections.sort(tempList, new FileNameComparator());
        tests = new TestBean[ availableTests.length ];
        for (int i = 0; i < tempList.size(); i++)
        {
            TestBean aTestBean = new TestBean();
            aTestBean.setIndex(String.valueOf(i));
            String file = ( String )tempList.get(i);
            if (i == 0)
                testTitle = file.substring(0, file.length() - 4);
            aTestBean.setTitle(file.substring(0, file.length() - 4));
            tests[ i ] = aTestBean;
        }
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="login" path="login.jsp"
     * @jpf:forward name="success" path="selectTests.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "login",
                     path = "login.jsp"), 
        @Jpf.Forward(name = "success",
                     path = "selectTests.jsp")
    })
    protected Forward selectTests()
    {
        if (globalApp.userLogined.equals("false"))
            return new Forward("login");
        else
            return new Forward("success");
    }
        
    
    /**
     * @jpf:action
     * @jpf:forward name="error" path="/error.jsp"
     * @jpf:forward name="login" path="login.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "error",
                     path = "/error.jsp"), 
        @Jpf.Forward(name = "login",
                     path = "login.jsp")
    })
    protected Forward userLogin()
    {
        Forward result = null;
        
        try
        {
            pageError = null;
            globalApp.userLogined = "false";
            password = getRequest().getParameter( "pwdPassword" );
            populateUserMap();
            if (userMap.containsKey(userName))
            {
                UserInfo aUserInfo = ( UserInfo )userMap.get(userName);
                if (aUserInfo.password.equals(password))
                {
                    globalApp.userLogined = "true";
                    globalApp.userFolder = aUserInfo.state;
                    
                }
            }
            if (globalApp.userLogined.equals("true")) 
            {
                getUserTests();
                getResponse().sendRedirect("/ContentReviewWeb/ContentReviewPageFlow/selectTests.do"); 
            }    
            else
            {
                result = new Forward("login");
                pageError = "Invalid user name or password";
                getRequest().setAttribute( "PAGE_ERROR", pageError );
            }
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
            getRequest().setAttribute( "errorMessage", JavaScriptSanitizer.sanitizeString(sb.toString()) );
            return new Forward("error");
        }
        
        return result;
    }
    
    public String elementToString(Element theRoot) throws Exception
    {
        Element clone = ( Element )theRoot.clone();
        XMLOutputter aXMLOutputter = new XMLOutputter();
        StringWriter out = new StringWriter();
        aXMLOutputter.output(clone, out);
        StringBuffer outStringBuffer = out.getBuffer();
        String outString = outStringBuffer.toString();
        return outString;
    }
    
    
    
    public String getUserFolderPath() throws Exception
    {
        String userFolderPath = null;
        //forReal = false;
        
        if (forReal)
            userFolderPath = "/export/data/contentdemo/" +
                             globalApp.userFolder;
        else
            userFolderPath = "c:/contentdemo/" +
                             globalApp.userFolder;
        return userFolderPath;
    }
    
    public void setCurrentSubtest(String subtestTitle) 
    {
        
        globalApp.currentDeliverableUnitBean = null;
        for (int i = 0; i < globalApp.SchedulableUnits.length && globalApp.currentDeliverableUnitBean == null; i++)
        {
            SchedulableUnitBean aSchedulableUnitBean = globalApp.SchedulableUnits[ i ];
            DeliverableUnitBean[] deliverableUnitBeans = aSchedulableUnitBean.getDeliverableUnits();
            for (int j = 0; j < deliverableUnitBeans.length && globalApp.currentDeliverableUnitBean == null; j++)
            {
                DeliverableUnitBean aDeliverableUnitBean = deliverableUnitBeans[ j ];
                if (aDeliverableUnitBean.getTitle().equals( subtestTitle ))
                {
                    globalApp.currentDeliverableUnitBean = aDeliverableUnitBean;
                }
            }
        }
    }
    
    public void constructAssessment(Element assessment) throws Exception
    {
        boolean firstTime = true;
        List children = assessment.getChildren("SchedulableUnit");
        globalApp.SchedulableUnits = new SchedulableUnitBean[ children.size() ];
        for (int i = 0; i < children.size(); i++)
        {
            SchedulableUnitBean aSchedulableUnitBean = new SchedulableUnitBean();
            globalApp.SchedulableUnits[ i ] = aSchedulableUnitBean;
            Element SchedulableUnit = ( Element )children.get(i);
            aSchedulableUnitBean.setTitle(SchedulableUnit.getAttributeValue("Title"));
            List DeliverableUnits = SchedulableUnit.getChildren("DeliverableUnit");
            DeliverableUnitBean[] DeliverableUnitBeans = new DeliverableUnitBean[ DeliverableUnits.size() ];
            for (int j = 0; j < DeliverableUnits.size(); j++)
            {
                DeliverableUnitBean aDeliverableUnitBean = new DeliverableUnitBean();
                DeliverableUnitBeans[ j ] = aDeliverableUnitBean;
                Element DeliverableUnit = ( Element )DeliverableUnits.get(j);
                if (firstTime)
                {
                    subtestTitle = DeliverableUnit.getAttributeValue("Title");
                    firstTime = false;
                }
                
                String startingQuestionNumber = DeliverableUnit.getAttributeValue("starting_question_number");
                if (startingQuestionNumber == null)
                    startingQuestionNumber = " ";
                else
                    startingQuestionNumber = "starting_question_number=\"" +
                                             startingQuestionNumber +
                                             "\"";
                aDeliverableUnitBean.setStartingQuestionNumber(startingQuestionNumber);
                aDeliverableUnitBean.setTitle(DeliverableUnit.getAttributeValue("Title"));
                Element ob_element_list = DeliverableUnit.getChild("ob_element_list");
                Element ob_element_select_order = DeliverableUnit.getChild("ob_element_select_order");
                aDeliverableUnitBean.setTimeLimit(DeliverableUnit.getAttributeValue("TimeLimit"));
                aDeliverableUnitBean.setItemReferences(elementToString(ob_element_list));
                aDeliverableUnitBean.setOrderReferences(elementToString(ob_element_select_order));
                List items = DeliverableUnit.getChildren("element_package");
                ItemBean[]  testItems = new ItemBean[ items.size() ];
                aDeliverableUnitBean.setItems(testItems);
                for (int k = 0; k < items.size(); k++)
                {
                    Element inItem = ( Element )items.get(k);
                    ItemBean aItemBean = new ItemBean();
                    testItems[ k ] = aItemBean;
                    aItemBean.setLml(elementToString(inItem));
                }
            }
            aSchedulableUnitBean.setDeliverableUnits(DeliverableUnitBeans);
        }
    }
   
    public void loadSubtests(String testName) throws Exception
    {
        TestBean theTestBean = null;
        for (int i = 0; i < tests.length; i++)
        {
            TestBean aTestBean = (  TestBean )tests[ i ];
            if (aTestBean.getTitle().equals( testName ))
            {
                theTestBean = aTestBean;
                break;
            }
        }
        String userFolderPath = getUserFolderPath();
        String fileLocation = userFolderPath + "/" + testName + ".xml";
        File inputFile = new File(fileLocation);
        org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
        org.jdom.Document assessmentDoc = saxBuilder.build(inputFile);
        Element assessment = assessmentDoc.getRootElement();
        constructAssessment(assessment);
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="/TestClientPageFlow/TestClientPageFlowController.jpf"
     * @jpf:forward name="login" path="begin.do"
     * @jpf:forward name="stay" path="options.jsp"
     * @jpf:forward name="error" path="/error.jsp"
     * @jpf:forward name="back" path="selectTests.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "/TestClientPageFlow/TestClientPageFlowController.jpf"), 
        @Jpf.Forward(name = "login",
                     path = "begin.do"), 
        @Jpf.Forward(name = "stay",
                     path = "options.jsp"), 
        @Jpf.Forward(name = "error",
                     path = "/error.jsp"), 
        @Jpf.Forward(name = "back",
                     path = "selectTests.do")
    })
    protected Forward preview(OptionsForm form)
    {
        Forward result = null;
        
        if (globalApp.userLogined.equals("false"))
            try
            {
                getResponse().setDateHeader("Expires", 0);
                getResponse().setHeader("Cache-Control", "no-cache");
                getResponse().setHeader("Pragma", "no-cache");
                getResponse().sendRedirect("/ContentReviewWeb/ContentReviewPageFlow/begin.do");
            }
            catch (IOException e)
            {
                return new Forward("error");
            }
        else
        {
            String back = this.getRequest().getParameter( "back" );
            if (back != null)
            {
                try
                {
                    getUserTests();
                    return new Forward("back");
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
                    getRequest().setAttribute( "errorMessage", JavaScriptSanitizer.sanitizeString(sb.toString()) );
                    return new Forward("error");
                }
                
            }
            
            
            if (!"".equals(form.studentFirstName))
                globalApp.studentFirstName = form.studentFirstName;
            
            if (!"".equals(form.studentLastName))
                globalApp.studentLastName = form.studentLastName;

            globalApp.calculator = form.calculator;
        //    globalApp.magnifier = form.magnifier;
            globalApp.screenReader = form.screenReader;
			globalApp.speedAdjustment = form.speedAdjustment;
            globalApp.untimed = form.untimed;
            globalApp.highlighter = form.highlighter;
            globalApp.answerBgColor = form.answerBgColor;
            globalApp.answerFgColor = form.answerFgColor;
            globalApp.answerFontSize = form.answerFontSize;
            globalApp.questionBgColor = form.questionBgColor;
            globalApp.questionFgColor = form.questionFgColor;
            globalApp.questionFontSize = form.questionFontSize;
            globalApp.rest_break = form.rest_break;
            globalApp.eliminatorResource = form.eliminatorResource;
            
            globalApp.auditoryCalming = form.auditoryCalming;
            globalApp.maskingRuler = form.maskingRuler;
            globalApp.magnifyingGlass = form.magnifyingGlass;
            globalApp.extendedTime = form.extendedTime;
                
            setCurrentSubtest(this.subtestTitle);
            getRequest().setAttribute("eliminatorResource", form.eliminatorResource);
            
            String preview = this.getRequest().getParameter( "preview" );
            if (preview == null)
            {
                this.getRequest().setAttribute( "questionBgColor", form.questionBgColor);
                this.getRequest().setAttribute( "answerBgColor", form.answerBgColor );
                form.answerFgColor = getAcceptableForeColor(form.answerBgColor, form.answerFgColor);
                form.questionFgColor = getAcceptableForeColor(form.questionBgColor, form.questionFgColor);
                
                result = new Forward("stay");
            }
            else
            {
                result = new Forward("success");
            }
        }
        return result;
    }
    
    String getAcceptableForeColor(String backgroundColor, String currentForeColor)
    {
        String resultColor = null;
        if (backgroundColor == null)
            resultColor = currentForeColor;
        else if (backgroundColor.equals("0x000000"))
        {
            if (currentForeColor != null && (currentForeColor.equals("0x00CC00") || currentForeColor.equals("0xFFFF99") || currentForeColor.equals("0xFFFFFF")))
                resultColor = currentForeColor;
            else
                resultColor = "0x00CC00";
        }
        else if (backgroundColor.equals("0x000080"))
        {
            resultColor = "0xFFFFFF";
        }
        else
        {
            if (currentForeColor != null)
            {
                if (currentForeColor.equals("0x000000") || currentForeColor.equals("0x000080") || currentForeColor.equals("0x663300"))
                    resultColor = currentForeColor;
                else
                    resultColor = "0x000000";
            }
        }
        return resultColor;
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="error" path="/error.jsp"
     * @jpf:forward name="login" path="login.jsp"
     * @jpf:forward name="success" path="options.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "error",
                     path = "/error.jsp"), 
        @Jpf.Forward(name = "login",
                     path = "login.jsp"), 
        @Jpf.Forward(name = "success",
                     path = "options.jsp")
    })
    protected Forward showSubtests(OptionsForm form)
    {
        Forward result = null;
        String avail = "false";
        if (globalApp.userLogined.equals("false"))
            result = new Forward("login");
        else
        {
            try
            {
                for (int i = 0; i < availableTests.length; i++) 
                    if ((this.testTitle + ".xml").equals(availableTests[i]))
                        avail = "true";
                if (avail.equals("true"))
                {        
                    loadSubtests(this.testTitle);
                    this.getRequest().setAttribute( "questionBgColor", "0xFFFFFF" );
                    this.getRequest().setAttribute( "answerBgColor", "0xFFFFB0" );
                    result = new Forward("success");
                }
                else
                {
                    result = new Forward("error");
                }
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
                getRequest().setAttribute( "errorMessage", JavaScriptSanitizer.sanitizeString(sb.toString()) );
                result = new Forward("error");
            }
        }
        return result;
    }

  
    
    protected void cleanActionData()
    {
        pageError = null;
        userName = null;
        password = null;
        availableTests = null;
        globalApp.userLogined = "false";
        globalApp.currentDeliverableUnitBean = null;
        
    }

    /**
     * @jpf:action
     * @jpf:forward name="login" path="login.jsp"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "login", path = "login.jsp")
		}
	)
    protected Forward Logout()
    {
        cleanActionData();
        getSession().invalidate();
        return new Forward("login");
    }

    public class UserInfo implements Serializable
    {
        public String userName;
        public String password;
        public String state;
    }
    
    public void populateUserMap() throws Exception
    {
        userMap = new HashMap();
        String inputFileName;
        if ( forReal )
            inputFileName = "/export/data/contentdemo/users.txt";
        else
            inputFileName = "c:/contentdemo/users.txt";
        BufferedReader inputFileReader = new BufferedReader( new FileReader( inputFileName ) );
		String line = null;
		while ( ( line = inputFileReader.readLine()) != null ) 
		{
			if ( line != null && line.trim().length() > 0 ) 
			{
                UserInfo aUserInfo = new UserInfo();
				line = line.replaceAll( ",", " ," );
                StringTokenizer st = new StringTokenizer( line, ",", false );
                int pos = 0;
                while( st.hasMoreTokens() ) 
                {
                    String token = st.nextToken();
                    token = token.trim();
                    if ( pos == 0 )
                        aUserInfo.userName = token;
                    else if ( pos == 1 )
                        aUserInfo.password = token;
                    else
                        aUserInfo.state = token;
                    pos++;
                }
                userMap.put( aUserInfo.userName, aUserInfo );
			}
		}
    }
    
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class OptionsForm extends SanitizedFormData
    {
        private String subtestTitle;

        private String studentLastName      = "Doe";
        private String studentFirstName      = "John";
        private String calculator       = "false";
     //   private String magnifier        = "false";
        private String screenReader     = "false";
		private String speedAdjustment  = "false";
        private String untimed          = "false";
        private String highlighter      = "true";
        private String rest_break = "false";
        private String answerFgColor; //      = "0x000000";
        private String answerBgColor; //    = "0xFFFFCC";
        private String answerFontSize; //   = "1";
        private String questionFgColor; //   = "0x000000";
        private String questionBgColor; //  = "0xFFFFFF";
        private String questionFontSize; // = "1";
        private int deliverableUnit = 1;
        private String eliminatorResource="/ContentReviewWeb/resources/eliminator.swf";
        private String auditoryCalming  = "false";
        private String maskingRuler    	= "false";
        private String magnifyingGlass  = "false";
        private String extendedTime     = "false";
        

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
   /*     public String getMagnifier() {
            return magnifier;
        }*/
        /**
         * @param magnifier The magnifier to set.
         */
    /*    public void setMagnifier(String magnifier) {
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
		 * @return Returns the speedAdjustment.
         */
        public String getSpeedAdjustment() {
            return speedAdjustment;
        }
        /**
         * @param speedAdjustment The speedAdjustment to set.
         */
        public void setSpeedAdjustment(String speedAdjustment) {
            this.speedAdjustment = speedAdjustment;
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
         * @return Returns the untimed.
         */
        public String getHighlighter() {
            return highlighter;
        }
        /**
         * @param untimed The untimed to set.
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

        public void setSubtestTitle(String subtestTitle)
        {
            this.subtestTitle = subtestTitle;
        }

        public String getSubtestTitle()
        {
            return this.subtestTitle;
        }
        
        public void setEliminatorResource(String eliminatorResource)
        {
            this.eliminatorResource = eliminatorResource;
        }

        public String getEliminatorResource()
        {
            return this.eliminatorResource;
        }
        /**
         * @return Returns the auditoryCalming.
         */
		public String getAuditoryCalming() {
			return auditoryCalming;
		}
		/**
         * @param auditoryCalming The auditoryCalming to set.
         */
		public void setAuditoryCalming(String auditoryCalming) {
			this.auditoryCalming = auditoryCalming;
		}
		/**
         * @return Returns the maskingRuler.
         */
		public String getMaskingRuler() {
			return maskingRuler;
		}
		/**
         * @param maskingRuler The maskingRuler to set.
         */
		public void setMaskingRuler(String maskingRuler) {
			this.maskingRuler = maskingRuler;
		}
		/**
         * @return Returns the magnifyingGlass.
         */
		public String getMagnifyingGlass() {
			return magnifyingGlass;
		}
		/**
         * @param magnifyingGlass The magnifyingGlass to set.
         */
		public void setMagnifyingGlass(String magnifyingGlass) {
			this.magnifyingGlass = magnifyingGlass;
		}
		/**
         * @return Returns the extendedTime.
         */
		public String getExtendedTime() {
			return extendedTime;
		}
		/**
         * @param extendedTime The extendedTime to set.
         */
		public void setExtendedTime(String extendedTime) {
			this.extendedTime = extendedTime;
		}
                     
    }

	public String getUserName() {
		return userName;
	}
}

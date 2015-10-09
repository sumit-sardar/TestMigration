package util; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.ctb.bean.testAdmin.SubtestAccessCodeDetail;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;

import data.ImageVO;
import data.TableVO;
import data.TestAdminVO;
import data.TestRosterVO;

public class IndividualTestTicketsReportUtils extends ReportUtils
{ 
    // image location
    private static final String WAVING_MAN_URL = "/images/wavingMan.gif";// "/TestAdministrationWeb/resources/images/wavingMan.gif";

    // page coordinates
    private static final float LEFT_X = 70f;
    private static final float WAVING_MAN_X = 458f;
    private static final float KEYBOARD_X = 156f;
    private static final float INFO_X = 170;
    private static final float WATERMARK_X = 530f;

    private static final float TITLE_Y = 752f;
    private static final float LINE_Y = 726f;
    private static final float STUDENT_NAME_Y = 715f;
    private static final float STUDENT_ID_Y = 700f;
    private static final float CLASS_NAME_Y = 685f;
    private static final float TEST_NAME_Y = 667f;
    private static final float LOCATION_Y = 635f;
    private static final float LOGIN_INFO_Y = 607f;
    private static final float LOGIN_INFO_TABLE_Y = 587f;
    private static final float LOGIN_INSTRUCTIONS_Y = 508f;
    /**
     * New coordinates for printing Session Name (Changes done for story OAS-1642)
     * START
     */
    private static final float TEST_NAME_SESSION_Y = 685f;
    private static final float SESSION_NAME_Y = 657f;
    private static final float CLASS_NAME_SESSION_Y = 622f;
    private static final float LOCATION_SESSION_Y = 608f;
    private static final float LOGIN_INFO_SESSION_Y = 585f;
    private static final float LOGIN_INFO_TABLE_SESSION_Y = 565f;
    private static final float LOGIN_INSTRUCTIONS_SESSION_Y = 495f;
    /**
     * END
     */
    
    private static final float KEYBOARD_SHORTCUTS_Y = 464f;
    private static final float KEYBOARD_SHORTCUTS_TEXT_1_Y = 442f;
    private static final float KEYBOARD_SHORTCUTS_TEXT_2_Y = 398f;
    private static final float KEYBOARD_SHORTCUTS_TEXT_3_Y = 358f;
    private static final float KEYBOARD_SHORTCUTS_TABLE_Y = 317f;
    
    
    // Added for TABE Adaptive Story : OAS-831 - TABE Adaptive - Add text to individual test ticket
    private static final float LOGIN_INFO_TA = 473f;
    private static final float LOGIN_INSTRUCTIONS_TA = 459f;
    private static final float KEYBOARD_SHORTCUTS_Y_TA = 430f;
    private static final float KEYBOARD_SHORTCUTS_TEXT_1_Y_TA = 408f;
    private static final float KEYBOARD_SHORTCUTS_TEXT_2_Y_TA = 364f;
    private static final float KEYBOARD_SHORTCUTS_TEXT_3_Y_TA = 324f;
    private static final float KEYBOARD_SHORTCUTS_TABLE_Y_TA = 283f;
    private static final float ADDITIONAL_Y_COPYWRITE = 370f;
    
    
    private static final float FOOTER_Y = 72f;
    private static final float WAVING_MAN_Y = 488f;
    /**
     * New coordinate for printing Session Name (Changes done for story OAS-1642)
     * START
     */
    private static final float WAVING_MAN_SESSION_Y = 475f;
    /**
     * END
     */
    private static final float WATERMARK_Y = 30f;

    private static final float PAGE_WIDTH = 460f;
    private static final float INFO_LABEL_WIDTH = 110f;  //460f;
    private static final float INFO_VALUE_WIDTH = 300f;
    private static final float LINE_WIDTH = 480f;
    private static final float LOGIN_WIDTH = 388f;
    private static final float KEYBOARD_WIDTH = 306f;
    private static final float WATERMARK_WIDTH = 50f;
    //START - Added For CR ISTEP2011CR007 (Multiple Test Ticket)
    private static final float ADDITIONAL_Y = 385f;
    private static final float UNADDITIONAL_Y = 0f;
    private static final float DOTTED_LINE_Y = 400f;
    //END - Added For  CR ISTEP2011CR007 (Multiple Test Ticket)

    // table column width ratios
    private static float[] LOGIN_WIDTHS = new float[] {1.3f, 3f};
    private static float[] KEYBOARD_WIDTHS = new float[] {1f, 1f};
    
    // table borders
    private static final float LOGIN_BORDER = 1f;
    private static final float LOGIN_INSTRUCTIONS_BORDER = 0.5f;
    private static final float KEYBOARD_BORDER = 2f;
    
    // page text
    private static final String PAGE_NAME_LABEL = "Individual Test Ticket";
	private static final String STUDENT_NAME_LABEL = "Student Name:";
	private static final String STUDENT_ID_LABEL = "Student ID:";
	private static final String CLASS_NAME_LABEL = "Class Name:";
	private static final String TEST_NAME_LABEL = "Test Name:";
	private static final String SESSION_NAME_LABEL = "Session Name:";
    private static final String LOGIN_INFORMATION = "Your login information";
	private static final String LOCATION_LABEL = "Location:";
	private static final String LOGIN_ID_LABEL = "Login ID:";
	private static final String PASSWORD_LABEL = "Password:";
    private static final String TEST_ACCESS_CODE_LABEL = "Test Access Code:";
    private static final String LOGIN_INSTRUCTIONS = "Wait for the teacher or test proctor to give you the Test Access Code.";
    private static final String INFO_FOR_TA_LABEL = "Note:";
    private static final String TA_INSTRUCTIONS_FOR_INDIVIDUAL_MULTIPLE = "You must view each item in the test to receive a final score.";
    private static final String KEYBOARD_SHORTCUTS_LABEL = "Keyboard Shortcuts";
    private static final String KEYBOARD_SHORTCUTS_TEXT_1 = "To move through the test, use the buttons on each screen, or you may use these keyboard shortcuts.";
    private static final String KEYBOARD_SHORTCUTS_TEXT_2 = "If two keys are used together, a + sign is shown between them.  You don't need to press the + key.";
    private static final String KEYBOARD_SHORTCUTS_TEXT_3 = "For example, to Go Back, press the Ctrl key and hold it down as you press the J key.";
    private static final String DESCRIPTION_LABEL = "Description";
    private static final String ENTER_ANSWSER_LABEL = "Enter an answer:";
    private static final String GO_BACK_LABEL = "Go Back:";
    private static final String GO_ON_LABEL = "Go On:";
    private static final String MARK_UNMARK_LABEL = "Mark/Unmark for review:";
    private static final String SHOW_HIDE_LABEL = "Show/Hide time:";
    private static final String STOP_LABEL = "Stop:";
    private static final String FINISH_TEST_LABEL = "Finish Test:";
    private static final String PAUSE_LABEL = "Pause:";
    private static final String KEYBOARD_SHORTCUT_LABEL = "Keyboard Shortcut";
    private static final String ENTER_ANSWER_SHORTCUT = "A B C D E";
    private static final String GO_BACK_SHORTCUT = "Ctrl + J";
    private static final String GO_ON_SHORTCUT = "Ctrl + K";
    private static final String MARK_UNMARK_SHORTCUT = "Ctrl + L";
    private static final String SHOW_HIDE_SHORTCUT = "Ctrl + O";
    private static final String STOP_SHORTCUT = "Ctrl + S";
    private static final String FINISH_TEST_SHORTCUT = "Ctrl + F";
    private static final String PAUSE_SHORTCUT = "Ctrl + U";
    private static final String WATERMARK_TEXT = "TTI";
    
    // global variables
	private Collection rosterList = null;
	private TestAdminVO testAdmin = null;
	//START - Changed for CR GA2011CR001
	private Boolean isStudentIdConfigurable = Boolean.FALSE;
    private String studentIdLabelName = "Student ID";
    //END - Changed for CR GA2011CR001
    private TableVO noPauseKeyboards = null;
    private TableVO pauseKeyboards = null;
    //START - Added For  CR ISTEP2011CR007 (Multiple Test Ticket)
    private boolean isMultiIndividualTkt = false;
    private boolean isTabeAdaptive = false;
    //END - Added For  CR ISTEP2011CR007 (Multiple Test Ticket)
    
    private String accessAllow = null; // Changed for TABE BAUM - 028
    private String printClassName = null;
    private String printSessionName = null;
    
    private boolean multipleAccessAllow = false;
    private boolean isTestBreak = false;
    private SubtestAccessCodeDetail[] subtestAccessList = null;
    private boolean removeKeyShortcut = false;
    
    //START - Added For  CR ISTEP2011CR007 (Multiple Test Ticket)
    protected boolean createStaticAboveTables() throws DocumentException, IOException {
        addTitle(UNADDITIONAL_Y);
        addStaticGeneralInformation(UNADDITIONAL_Y);
        addStaticLoginInformation(UNADDITIONAL_Y);
        addFooter(UNADDITIONAL_Y);
        addWatermark(UNADDITIONAL_Y);
        return true;
    }
    protected boolean createStaticBelowTables() throws DocumentException, IOException {
    	addFooter((this.isTabeAdaptive)?ADDITIONAL_Y_COPYWRITE:ADDITIONAL_Y);
        addWatermark((this.isTabeAdaptive)?ADDITIONAL_Y_COPYWRITE:ADDITIONAL_Y);
    	addDottedLine();
        addTitle((this.isTabeAdaptive)?ADDITIONAL_Y_COPYWRITE:ADDITIONAL_Y);
        addStaticGeneralInformation((this.isTabeAdaptive)?ADDITIONAL_Y_COPYWRITE:ADDITIONAL_Y);
        addStaticLoginInformation((this.isTabeAdaptive)?ADDITIONAL_Y_COPYWRITE:ADDITIONAL_Y);
        return true;
    }
    //END - Added For  CR ISTEP2011CR007 (Multiple Test Ticket)
    
    protected boolean setDynamicTables() throws DocumentException, IOException{
        this.dynamicTables = (Collection)this.pages.get(currentPageIndex);
        return (this.dynamicTables != null && this.dynamicTables.size() > 0);
    }
    protected boolean setImages() throws DocumentException, IOException{
        if(currentPageIndex == 0){
           addWavingMan();
        }
        return true;
    }
    
     protected void setup(Object[] args) throws DocumentException, IOException{
        super.initializeGlobals(new Object[] {args[3], args[4], args[5], PageSize.LETTER, args[6]});
        this.rosterList = (Collection)args[0];
        this.testAdmin = (TestAdminVO)args[1];
        //START - Changed for CR GA2011CR001
        this.isStudentIdConfigurable = (Boolean)args[8];
        this.studentIdLabelName = (String)args[9];
        //END - Changed for CR GA2011CR001
        this.accessAllow = (String)args[10]; // Added for TABE BAUM - 028
        this.printClassName = (String)args[11];
      //START - Changed for CR ISTEP2011CR007 (Multiple Test Ticket)
      	this.isMultiIndividualTkt = (Boolean)args[2];
        
      	this.multipleAccessAllow = (Boolean)args[12];
      	this.subtestAccessList = (SubtestAccessCodeDetail[])args[13];
      	this.isTabeAdaptive = (Boolean)args[14];
      	this.printSessionName = (String)args[15];
      	this.removeKeyShortcut = (Boolean)args[16];
        if(!isMultiIndividualTkt) {
        	if(!removeKeyShortcut) {
	        	this.getKeyboardShortcutsTables();
	        	addKeyboardShortcuts();
        	}
        	addWavingMan();
        	this.createStaticAboveTables();
        	this.createPages();
        }else {
        	 this.createStaticAboveTables();
        	 this.createStaticBelowTables();
        	 this.createMultiTktPages();
        }	
        //END - Changed for  CR ISTEP2011CR007 (Multiple Test Ticket)
        
       
    }
    
    //START - Changed for CR ISTEP2011CR007 (Multiple Test Ticket)
    private void createPages() throws DocumentException, IOException{
    	float yValue = UNADDITIONAL_Y;
    	this.totalPages = this.rosterList.size();
        for(Iterator it=this.rosterList.iterator(); it.hasNext();){
            TestRosterVO student = (TestRosterVO)it.next();
            ArrayList tables = new ArrayList();
            tables.add(getStudentNameValue(student, yValue));
            tables.add(getStudentIdValue(student, yValue));
            
            if(this.printSessionName.equals("true")) {
            	if(this.printClassName.equals("true"))
                	tables.add(getClassNameValue(student, yValue+CLASS_NAME_Y-CLASS_NAME_SESSION_Y));
            	tables.add(getLoginTable(student, yValue+LOGIN_INFO_TABLE_Y-LOGIN_INFO_TABLE_SESSION_Y));
            } else {
            	if(this.printClassName.equals("true"))
                	tables.add(getClassNameValue(student, yValue));
            	tables.add(getLoginTable(student, yValue));
            }
            if(!this.removeKeyShortcut) {
            	tables.add(getKeyboardShortcutsTable(student));
            }
            this.pages.add(tables);
        }
    }
    //END - Changed for CR ISTEP2011CR007 (Multiple Test Ticket)
    
   //START - Added for CR ISTEP2011CR007 (Multiple Test Ticket)
    private void createMultiTktPages() throws DocumentException, IOException{
    	boolean flag = true;
    	float yValue = UNADDITIONAL_Y;
        this.totalPages = this.rosterList.size();
        for(Iterator it=this.rosterList.iterator(); it.hasNext();){
            TestRosterVO student = (TestRosterVO)it.next();
            ArrayList tables = new ArrayList();
            tables.add(getStudentNameValue(student, yValue));
            tables.add(getStudentIdValue(student, yValue));
            
            if(this.printSessionName.equals("true")) {
            	if(this.printClassName.equals("true"))
                	tables.add(getClassNameValue(student, yValue+CLASS_NAME_Y-CLASS_NAME_SESSION_Y));
            	tables.add(getLoginTable(student, yValue+LOGIN_INFO_TABLE_Y-LOGIN_INFO_TABLE_SESSION_Y));
            } else {
            	if(this.printClassName.equals("true"))
                	tables.add(getClassNameValue(student, yValue));
            	tables.add(getLoginTable(student, yValue));
            }
           if(flag){
            	//this.createStaticAboveTables();
            	yValue = (this.isTabeAdaptive)?ADDITIONAL_Y_COPYWRITE:ADDITIONAL_Y;
            	flag =false;	
            	
            	}
            else{
            	//this.createStaticBelowTables();
            	yValue = UNADDITIONAL_Y;
            	flag =true;	
            	}
            
            this.pages.add(tables);
        }
    }
    //END - Added for CR ISTEP2011CR007 (Multiple Test Ticket)
    
    private void addTitle(float yValue) throws DocumentException{
        addTitleText(yValue);
        addTitleLine(yValue);
    }
    
    private void addTitleText(float yValue) throws DocumentException{
         this.staticTables.add(
            tableUtils.getTitleTable(PAGE_NAME_LABEL,
                                     PAGE_WIDTH,
                                     LEFT_X,
                                     (TITLE_Y-yValue)));
    }
    
    private void addTitleLine(float yValue) throws DocumentException{
    	this.staticTables.add(
            tableUtils.getLineTable(LINE_WIDTH,
                                    LEFT_X,
                                    (LINE_Y-yValue)));
    }
    
    //START - Changed for CR ISTEP2011CR007 (Multiple Test Ticket)
    private void addDottedLine() throws DocumentException{
    	this.staticTables.add(
            tableUtils.getDottedLineTable(LINE_WIDTH,
                                    LEFT_X,
                                    (DOTTED_LINE_Y)));
    }
    //END - Changed for CR ISTEP2011CR007 (Multiple Test Ticket)
    
    private void addFooter(float yValue) throws DocumentException{
        this.staticTables.add(
            tableUtils.getCopywriteTable(PAGE_WIDTH,
                                         LEFT_X,
                                         (FOOTER_Y + yValue)));
     }
    
    private void addWatermark(float yValue) throws DocumentException{
        this.staticTables.add(
            tableUtils.getWatermarkTable(WATERMARK_TEXT,
                                         WATERMARK_WIDTH,
                                         WATERMARK_X,
                                         (WATERMARK_Y + yValue )));
     }
    
    private void addStaticGeneralInformation(float yValue) throws DocumentException{
        addStudentNameLabel(yValue);
        addStudentIdLabel(yValue);
        
        if(this.printSessionName.equals("true")) {
        	addTestNameLabel(yValue+TEST_NAME_Y-TEST_NAME_SESSION_Y);
	        addTestNameValue(yValue+TEST_NAME_Y-TEST_NAME_SESSION_Y);
	        addSessionNameLabel(yValue);
            addSessionNameValue(yValue);
            if(this.printClassName.equals("true"))
            	addClassNameLabel(yValue+CLASS_NAME_Y-CLASS_NAME_SESSION_Y);
	        addLocationLabel(yValue+LOCATION_Y-LOCATION_SESSION_Y);
            addLocationValue(yValue+LOCATION_Y-LOCATION_SESSION_Y);
        } else {
        	if(this.printClassName.equals("true"))
            	addClassNameLabel(yValue);
        	addTestNameLabel(yValue);
	        addTestNameValue(yValue);
	        addLocationLabel(yValue);
	        addLocationValue(yValue);
        }
        
    }
    
    private void addStudentNameLabel(float yValue) throws DocumentException{
        this.staticTables.add( 
             tableUtils.getLabelTable(STUDENT_NAME_LABEL,
                                      INFO_LABEL_WIDTH,
                                      LEFT_X,
                                      (STUDENT_NAME_Y-yValue)));
    }

    private TableVO getStudentNameValue(TestRosterVO student,float yValue) throws DocumentException{
        return tableUtils.getInfoTable(getStudentName(student),
                                       INFO_VALUE_WIDTH,
                                       INFO_X,
                                       (STUDENT_NAME_Y-yValue));
   }

    private void addStudentIdLabel(float yValue) throws DocumentException{
    	//START - Changed for CR GA2011CR001
    	String studentIdLabel = STUDENT_ID_LABEL;
    	if(isStudentIdConfigurable) {
    		if(studentIdLabelName.toCharArray().length > 20)
    			studentIdLabel = studentIdLabelName.substring(0, 20);
    		else
    			studentIdLabel = studentIdLabelName;
    		studentIdLabel = studentIdLabel.trim() + ":";
    	}
    	//END - Changed for CR GA2011CR001
        this.staticTables.add( 
             tableUtils.getLabelTable(studentIdLabel,
                                      INFO_LABEL_WIDTH,
                                      LEFT_X,
                                      (STUDENT_ID_Y-yValue)));
    }

    private TableVO getStudentIdValue(TestRosterVO student, float yValue) throws DocumentException{
        return tableUtils.getInfoTable(getStudentId(student),
                                       INFO_VALUE_WIDTH,
                                       INFO_X,
                                       (STUDENT_ID_Y-yValue));
   }

    private void addTestNameLabel(float yValue) throws DocumentException{
        this.staticTables.add( 
             tableUtils.getLabelTable(TEST_NAME_LABEL,
                                      INFO_LABEL_WIDTH,
                                      LEFT_X,
                                      (TEST_NAME_Y-yValue)));
   }

    private void addTestNameValue(float yValue) throws DocumentException{
         this.staticTables.add( 
            tableUtils.getInfoTable(getTestName(),
                                    INFO_VALUE_WIDTH,
                                    INFO_X,
                                    (TEST_NAME_Y-yValue)));
     }
    
    private void addSessionNameLabel(float yValue) throws DocumentException{
        this.staticTables.add( 
             tableUtils.getLabelTable(SESSION_NAME_LABEL,
                                      INFO_LABEL_WIDTH,
                                      LEFT_X,
                                      (SESSION_NAME_Y-yValue)));
   }

    private void addSessionNameValue(float yValue) throws DocumentException{
         this.staticTables.add( 
            tableUtils.getInfoTable(getSessionName(),
                                    INFO_VALUE_WIDTH,
                                    INFO_X,
                                    (SESSION_NAME_Y-yValue)));
     }
    
    private void addLocationLabel(float yValue) throws DocumentException{
        if(hasLocation()){
            this.staticTables.add( 
                 tableUtils.getLabelTable(LOCATION_LABEL,
                                          INFO_LABEL_WIDTH,
                                          LEFT_X,
                                          (LOCATION_Y-yValue)));
        }
    }

    private void addLocationValue(float yValue) throws DocumentException{
        if(hasLocation()){
            this.staticTables.add( 
                tableUtils.getInfoTable(getLocation(),
                                        INFO_VALUE_WIDTH,
                                        INFO_X,
                                        (LOCATION_Y-yValue)));
        }
   }

    private void addStaticLoginInformation(float yValue) throws DocumentException, IOException{
    	if(this.printSessionName.equals("true")) {
    		addLoginLabel(yValue+LOGIN_INFO_Y-LOGIN_INFO_SESSION_Y);
	        addLoginInstructions(yValue+LOGIN_INSTRUCTIONS_Y-LOGIN_INSTRUCTIONS_SESSION_Y);
    	} else {
	        addLoginLabel(yValue);
	        addLoginInstructions(yValue);
    	}
        if(this.isTabeAdaptive)addStaticInfoForTA(yValue);// Added for TABE Adaptive Story : OAS-831 - TABE Adaptive - Add text to individual test ticket
    }
 
    private void addLoginInstructions(float yValue) throws DocumentException{
        if(this.accessAllow.equals("false")) { //Changed for TABE BAUM - 028
    	this.staticTables.add( 
            tableUtils.getBorderedTable(LOGIN_INSTRUCTIONS,
                                        LOGIN_WIDTH,
                                        LEFT_X,
                                        (LOGIN_INSTRUCTIONS_Y-yValue),
                                        LOGIN_INSTRUCTIONS_BORDER));
        }
    }
    
    private void addStaticInfoForTA(float yValue) throws DocumentException, IOException{
    	addLabelInfoForTA(yValue);
    	addInstructionsForTA(yValue);
    }
    
    private void addLabelInfoForTA(float yValue) throws DocumentException{
        this.staticTables.add( 
             tableUtils.getLabelTable(INFO_FOR_TA_LABEL,
                                      PAGE_WIDTH,
                                      LEFT_X,
                                     (LOGIN_INFO_TA-yValue)));
    }
    
    private void addInstructionsForTA(float yValue) throws DocumentException{
        	this.staticTables.add( 
                    tableUtils.getInfoTable(TA_INSTRUCTIONS_FOR_INDIVIDUAL_MULTIPLE,
				                            LOGIN_WIDTH,
				                            LEFT_X,
				                            (LOGIN_INSTRUCTIONS_TA-yValue)));
    }
    
    private void addWavingMan() throws DocumentException, IOException{
        ImageVO wavingMan = null;
        if(this.printSessionName.equals("true")) {
        	wavingMan = new ImageVO(WAVING_MAN_URL, WAVING_MAN_X, WAVING_MAN_SESSION_Y);
        } else {
        	wavingMan = new ImageVO(WAVING_MAN_URL, WAVING_MAN_X, WAVING_MAN_Y);
        }
        
        if(this.images == null){
            this.images = new ArrayList();
        }
        this.images.add(wavingMan);
    }

    private void addLoginLabel(float yValue) throws DocumentException{
        this.staticTables.add( 
             tableUtils.getLabelTable(LOGIN_INFORMATION,
                                      PAGE_WIDTH,
                                      LEFT_X,
                                      (LOGIN_INFO_Y-yValue)));
    }
    
    private TableVO getLoginTable(TestRosterVO student,float yValue) throws DocumentException {
        return tableUtils.getLoginTable(getLoginInfoTexts(student, yValue),
                                        LOGIN_WIDTH,
                                        LOGIN_WIDTHS,
                                        LEFT_X,
                                        (LOGIN_INFO_TABLE_Y-yValue),
                                        LOGIN_BORDER);
    }
    
    private String[] getLoginInfoTexts(TestRosterVO student,float yValue){
        String[] result = new String[6];
        result[0] = LOGIN_ID_LABEL;
        result[1] = getLoginId(student);
        result[2] = PASSWORD_LABEL;
        result[3] = getPassword(student);
        result[4] = TEST_ACCESS_CODE_LABEL;
        // Changed for TABE BAUM - 028
        if(this.accessAllow.equals("true") && !this.multipleAccessAllow)
        	result[5] = getAccessCode();        
        else if(this.multipleAccessAllow && this.accessAllow.equals("true") && "F".equalsIgnoreCase(this.testAdmin.getEnforceBreaks()))
        	result[5] = getAccessCode();        
        else if(this.multipleAccessAllow && this.accessAllow.equals("true") && "T".equalsIgnoreCase(this.testAdmin.getEnforceBreaks()))
        	result[5] = getMultipleAccessCode();
        else
        	result[5] = " ";        
        return result;
    }
    private void addKeyboardShortcuts() throws DocumentException{
    	if (this.isTabeAdaptive){
    		addKeyboardShortcutsTitleForTA();
    		addKeyboardShortcutsTextForTA();    		
    	}else{
    		addKeyboardShortcutsTitle();
    		addKeyboardShortcutsText();
    	}
    }
    
    private void addKeyboardShortcutsTitle() throws DocumentException{
        this.staticKeyboardTables.add( 
             tableUtils.getLabelTable(KEYBOARD_SHORTCUTS_LABEL,
                                      PAGE_WIDTH,
                                      LEFT_X,
                                      KEYBOARD_SHORTCUTS_Y));
    }
    
    private void addKeyboardShortcutsText() throws DocumentException{
        addKeyboardShortcutsText1(KEYBOARD_SHORTCUTS_TEXT_1_Y);
        addKeyboardShortcutsText2(KEYBOARD_SHORTCUTS_TEXT_2_Y);
        addKeyboardShortcutsText3(KEYBOARD_SHORTCUTS_TEXT_3_Y);
    }
    
    private void addKeyboardShortcutsTitleForTA() throws DocumentException{
        this.staticKeyboardTables.add( 
             tableUtils.getLabelTable(KEYBOARD_SHORTCUTS_LABEL,
                                      PAGE_WIDTH,
                                      LEFT_X,
                                      KEYBOARD_SHORTCUTS_Y_TA));
    }
    
    private void addKeyboardShortcutsTextForTA() throws DocumentException{
        addKeyboardShortcutsText1(KEYBOARD_SHORTCUTS_TEXT_1_Y_TA);
        addKeyboardShortcutsText2(KEYBOARD_SHORTCUTS_TEXT_2_Y_TA);
        addKeyboardShortcutsText3(KEYBOARD_SHORTCUTS_TEXT_3_Y_TA);
    }
    
    
    private void addKeyboardShortcutsText1(float KEYBOARD_SHORTCUTS_TEXT_1_Y) throws DocumentException{
        this.staticKeyboardTables.add( 
             tableUtils.getInfoTable(KEYBOARD_SHORTCUTS_TEXT_1,
                                     PAGE_WIDTH,
                                     LEFT_X,
                                     KEYBOARD_SHORTCUTS_TEXT_1_Y));
    }
    
    private void addKeyboardShortcutsText2(float KEYBOARD_SHORTCUTS_TEXT_2_Y) throws DocumentException{
        this.staticKeyboardTables.add( 
             tableUtils.getInfoTable(KEYBOARD_SHORTCUTS_TEXT_2,
                                     PAGE_WIDTH,
                                     LEFT_X,
                                     KEYBOARD_SHORTCUTS_TEXT_2_Y));
      }
     
     private void addKeyboardShortcutsText3(float KEYBOARD_SHORTCUTS_TEXT_3_Y) throws DocumentException{
        this.staticKeyboardTables.add( 
             tableUtils.getInfoTable(KEYBOARD_SHORTCUTS_TEXT_3,
                                     PAGE_WIDTH,
                                     LEFT_X,
                                     KEYBOARD_SHORTCUTS_TEXT_3_Y));
     }
     
     private TableVO getKeyboardShortcutsTable(TestRosterVO student) throws DocumentException{
        if(hasPauseAccommodation(student)){
            return this.pauseKeyboards;
        }
        else{
            return this.noPauseKeyboards;
        }
    }
    
     private void getKeyboardShortcutsTables() throws DocumentException{
    	float TABLE_Y = (this.isTabeAdaptive)? KEYBOARD_SHORTCUTS_TABLE_Y_TA : KEYBOARD_SHORTCUTS_TABLE_Y ;
        this.pauseKeyboards =  
             tableUtils.getHeaderBorderTable(getKeyboardShortcutsTexts(true),
                                             KEYBOARD_WIDTH,
                                             KEYBOARD_WIDTHS,
                                             KEYBOARD_X,
                                             TABLE_Y,
                                             KEYBOARD_BORDER);
        this.noPauseKeyboards =  
             tableUtils.getHeaderBorderTable(getKeyboardShortcutsTexts(false),
                                             KEYBOARD_WIDTH,
                                             KEYBOARD_WIDTHS,
                                             KEYBOARD_X,
                                             TABLE_Y,
                                             KEYBOARD_BORDER);
    }
    
    private String getStudentName(TestRosterVO student){
        StringBuffer buff = new StringBuffer();
        buff.append(student.getLastName());
        buff.append(", ");
        buff.append(student.getFirstName());
        String middleName = student.getMiddleName();
        if(middleName != null && middleName.length() > 0){
            buff.append(" ");
            buff.append(middleName);
        }
        return tableUtils.infoEllipsis(buff.toString(), INFO_VALUE_WIDTH - 20);
    }

    private boolean hasLocation(){
        String location = testAdmin.getLocation();
        return(location != null && 
               location.trim().length() > 0);
    }
    private String getLocation(){
        return tableUtils.infoEllipsis(getNonBlankString(testAdmin.getLocation()), INFO_VALUE_WIDTH - 20);
    }
     
     private String getStudentId(TestRosterVO student){
        return getNonBlankString(student.getStudentNumber());
    }

   private String getPassword(TestRosterVO student){
        return getNonBlankString(student.getPassword());
   }   
   private String getLoginId(TestRosterVO student){
        return getNonBlankString(student.getLoginName());
   }
   //Added for TABE BAUM - 028
   private String getAccessCode(){
       return getNonBlankString(testAdmin.getAccessCode());
  }  
   
   private String getMultipleAccessCode(){
	   String accessCodeSubtestList = "";
	   for (SubtestAccessCodeDetail sACD:this.subtestAccessList){
		   accessCodeSubtestList += sACD.getAccessCode()+" ("+sACD.getSubtestName()+")\n";
	   }	   	   
       return getNonBlankString(accessCodeSubtestList);
  }
   
   private String getTestName(){
        return getNonBlankString(testAdmin.getTestName());
   }  
   
   private String getSessionName(){
       return getNonBlankString(testAdmin.getSessionName());
   }
   private String[] getKeyboardShortcutsTexts(boolean pause){
        int totalCells = pause ? 18 : 16;
        String[] result = new String[totalCells];
        result[0] = DESCRIPTION_LABEL;
        result[1] = KEYBOARD_SHORTCUT_LABEL;
        result[2] = ENTER_ANSWSER_LABEL;
        result[3] = ENTER_ANSWER_SHORTCUT;
        result[4] = GO_BACK_LABEL;
        result[5] = GO_BACK_SHORTCUT;
        result[6] = GO_ON_LABEL;
        result[7] = GO_ON_SHORTCUT;
        result[8] = MARK_UNMARK_LABEL;
        result[9] = MARK_UNMARK_SHORTCUT;
        result[10] = SHOW_HIDE_LABEL;
        result[11] = SHOW_HIDE_SHORTCUT;
        result[12] = STOP_LABEL;
        result[13] = STOP_SHORTCUT;
        result[14] = FINISH_TEST_LABEL;
        result[15] = FINISH_TEST_SHORTCUT;
        if(pause){
            result[16] = PAUSE_LABEL;
            result[17] = PAUSE_SHORTCUT;
        }
        return result;
   }
   private boolean hasPauseAccommodation(TestRosterVO student){
        return student.getHasPause();
   }
   
   private void addClassNameLabel(float yValue) throws DocumentException{
       this.staticTables.add( 
            tableUtils.getLabelTable(CLASS_NAME_LABEL,
                                     INFO_LABEL_WIDTH,
                                     LEFT_X,
                                    (CLASS_NAME_Y-yValue)));
  }
   
   private TableVO getClassNameValue(TestRosterVO student, float yValue) throws DocumentException{
       return tableUtils.getInfoTable(getClassName(student),
                                      INFO_VALUE_WIDTH,
                                      INFO_X,
                                      (CLASS_NAME_Y-yValue));
  }
  
   private String getClassName(TestRosterVO student){
       return getNonBlankString(student.getClassName());
   }
} 

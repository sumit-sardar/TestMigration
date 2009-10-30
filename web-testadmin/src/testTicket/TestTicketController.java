package testTicket;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.TestElementData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.SessionStudentData;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.bean.testAdmin.TestProductData;
import com.ctb.bean.testAdmin.User;
import data.SubtestVO;
import data.TestAdminVO;
import data.TestRosterVO;
import data.TestSummaryVO;
import data.TestVO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import utils.FilterSortPageUtils;
import utils.IndividualTestTicketsReportUtils;
import utils.SummaryTestTicketsReportUtils;
import utils.TestSessionUtils;

/**
 * @jpf:controller nested="true"
 *  */
@Jpf.Controller(nested = true)
public class TestTicketController extends PageFlowController
{

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.ScheduleTest scheduleTest;


    private List testTicketTestList = null;
    
    private String userName;
    
    private ScheduledSession scheduledSession = null;

    private String schedulerName = null;

// Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app

    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="index.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "index.jsp")
    })
    protected Forward begin()
    {
        init();
        return new Forward("success");
    }
    
    private void init()
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        this.userName = principal.toString();
        
        getSession().setAttribute("userName", this.userName);
    }    
    
    /**
     * @jpf:action
     * @jpf:forward name="success" return-action="currentPage"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     returnAction = "currentPage")
    })
    protected Forward individualTestTicket()
    {
        try
        {
            init();
            
            String studentId = (String)getRequest().getParameter("studentId");
            String testAdminId = (String)getRequest().getParameter("testAdminId");
            String orgNodeId = (String)getRequest().getParameter("orgNodeId");
            Integer sessionId = new Integer(testAdminId); 
            TestSessionData tsd = getTestSessionDetails(sessionId);
            ScheduledSession session = this.getScheduledSession(sessionId);

            TestAdminVO testAdmin = buildTestAdminVO(tsd, session);
            RosterElementData red = getRosterForTestSessionAndOrgNode(orgNodeId, sessionId);
//            TestElementData ted = this.getTestsForProductForUser(
//                                                testAdmin.getProductId(),
//                                                null, null, null);
//            this.testTicketTestList = buildTestList(ted, testAdmin.getProductId());
            Collection rosterList = buildRosterList(red, session.getStudents());    
            
            if (studentId != null)
            {
                // this is from Rapid Registration, so show only this student
                if (red.getRosterElements().length == 0)
                {
                    red = getRosterForTestSession(sessionId); 
                    rosterList = buildRosterList(red, session.getStudents());    
                }
                rosterList = buildStudentRoster(rosterList, new Integer(studentId));   
            }
            
            testAdmin.setTestName(session.getTestSession().getTestName());
            IndividualTestTicketsReportUtils util = new IndividualTestTicketsReportUtils();
            String server = getRequest().getServerName();
            int port = getRequest().getServerPort();
            getResponse().setContentType("application/pdf");
            getResponse().setHeader("Content-Disposition","attachment; filename=TestTicketIndividual.pdf ");
            getResponse().setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            util.generateReport(new Object[]{
                                rosterList, 
                                testAdmin, 
                                getResponse().getOutputStream(), 
                                server,
                                new Integer(port),
                                this.getRequest().getScheme(),
                                Boolean.FALSE});
        }
        catch (IOException ie)
        {
            ie.printStackTrace();
        }

        return null;
    }

    private TestProductData getTestProductDataForUser()
    {
        TestProductData tpd = null;                
        try
        {
            tpd = this.scheduleTest.getTestProductsForUser(this.schedulerName,null,null,null);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }        
        return tpd;
    }
    
    
    private String getLevelOrGrade(Integer productId)
    {
        String result = "Level";
        TestProductData testProductData = this.getTestProductDataForUser();

        TestProduct [] tps = testProductData.getTestProducts(); 
                
        boolean foundProduct = false;
        for (int i=0; i < tps.length && !foundProduct; i++)
        {
            if (productId.equals(tps[i].getProductId()))
            {
                String[] levels = tps[i].getLevels();
                String[] grades = tps[i].getGrades();
                if (levels.length > 0)
                {
                    if (levels[0] != null)
                    {
                        result = "Level";
                    }
                }
                if (grades.length > 0)
                {
                    if (grades[0] != null)
                    {
                        result = "Grade";
                    }
                }
                foundProduct = true;
            } 
        }
        return result;
    }
    
    /**
     * @jpf:action
     */
	@Jpf.Action()
    protected Forward summaryTestTicket() throws CTBBusinessException
    {
        try{
            init();
            
            String testAdminId = (String)getRequest().getParameter("testAdminId");
            String orgNodeId = (String)getRequest().getParameter("orgNodeId");
           
            Integer sessionId = new Integer(testAdminId); 
            TestSessionData tsd = getTestSessionDetails(sessionId);
            this.scheduledSession = this.getScheduledSession(sessionId);
            TestAdminVO testAdmin = buildTestAdminVO(tsd, this.scheduledSession);
            RosterElementData red = getRosterForTestSessionAndOrgNode(orgNodeId, sessionId);
            TestSession testSession = this.scheduledSession.getTestSession();
                
                
            this.schedulerName = testSession.getCreatedBy();
            User user = this.scheduleTest.getUserDetails(this.userName, schedulerName);
            Customer customer = user.getCustomer();
            String hideAccommodations = customer.getHideAccommodations();
            Boolean supportAccommodations = Boolean.TRUE;
            if ((hideAccommodations != null) && hideAccommodations.equalsIgnoreCase("T")) {
                supportAccommodations = Boolean.FALSE;
            }
            
            TestElementData ted = this.getTestsForProductForUser(testAdmin.getProductId(),
                                                                 null, 
                                                                 null, 
                                                                 null);
                                                                 
            this.testTicketTestList = buildTestList(ted, testAdmin.getProductId());
            TestVO test = getTestTicketTestById(getTestId(tsd));
            
            Collection rosterList = buildRosterList(red, this.scheduledSession.getStudents());   
            TestSummaryVO summary = this.getTestSessionSummary(rosterList);
            summary.setSupportAccommodations(supportAccommodations);
            
            TestProduct testproduct = this.testSessionStatus.getProductForTestAdmin(this.userName, sessionId);
            Boolean isTabeProduct = TestSessionUtils.isTabeProduct(TestSessionUtils.getProductType(testproduct.getProductType())); 
            if (isTabeProduct.booleanValue()) {
                test.setLevel(null);
                String duration = getTestSessionDuration(testAdmin);
                test.setDuration(duration);
                for(Iterator it=rosterList.iterator(); it.hasNext() ; ) {
                    TestRosterVO roster = (TestRosterVO)it.next();
                    roster.setForm(null);
                }
            }
            
            
            testAdmin.setTestName(this.scheduledSession.getTestSession().getTestName());
            SummaryTestTicketsReportUtils util = new SummaryTestTicketsReportUtils();
            String server = getRequest().getServerName();
            int port = getRequest().getServerPort();
            getResponse().setContentType("application/pdf");
            getResponse().setHeader("Content-Disposition","attachment; filename=TestTicketSummary.pdf");
            getResponse().setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            util.generateReport(new Object[]{
                                rosterList, 
                                testAdmin, 
                                summary,
                                test,
                                getResponse().getOutputStream(), 
                                server,
                                new Integer(port),
                                this.getRequest().getScheme(),
                                isTabeProduct,
                                testproduct});
        }
        catch(IOException ie){
            ie.printStackTrace();
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        

        return null;
    }    
    
    private RosterElementData getRosterForTestSessionAndOrgNode(String orgNodeId, 
                                                      Integer testAdminId) 
    {
        RosterElementData red = null;
        Integer orgNodeInteger = new Integer(orgNodeId);
        try {      
            SortParams sort = FilterSortPageUtils.buildSortParams(FilterSortPageUtils.LAST_NAME_SORT, 
                                                                  FilterSortPageUtils.ASCENDING, 
                                                                  FilterSortPageUtils.FIRST_NAME_SORT, 
                                                                  FilterSortPageUtils.ASCENDING);
            red = this.testSessionStatus.getRosterForTestSessionAndOrgNode(this.userName, 
                                                                           testAdminId, 
                                                                           orgNodeInteger,
                                                                           null, 
                                                                           null, 
                                                                           sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return red;
    }

    private RosterElementData getRosterForTestSession(Integer testAdminId) 
    {
        RosterElementData red = null;
        try {      
            SortParams sort = FilterSortPageUtils.buildSortParams(FilterSortPageUtils.LAST_NAME_SORT, 
                                                                  FilterSortPageUtils.ASCENDING, 
                                                                  FilterSortPageUtils.FIRST_NAME_SORT, 
                                                                  FilterSortPageUtils.ASCENDING);
            red = this.testSessionStatus.getRosterForTestSession(this.userName, 
                                                                 testAdminId, 
                                                                 null, 
                                                                 null, 
                                                                 sort);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return red;
    }

     private TestSessionData getTestSessionDetails(Integer sessionId) 
    {
        TestSessionData tsd = null;
        try {      
            tsd = this.testSessionStatus.getTestSessionDetails(this.userName, sessionId);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return tsd;
    }
    
      private TestAdminVO buildTestAdminVO(TestSessionData tsd, ScheduledSession session)
    {
        TestSession[] testSessions = tsd.getTestSessions();
        TestSession testSession = testSessions[0];
        String testName = testSession.getTestAdminName();
        String location = testSession.getLocation();
        String accessCode = testSession.getAccessCode();
        String sessionName = testSession.getTestAdminName();
        String level = null;
        Date startDate = testSession.getLoginStartDate();
        Date endDate = testSession.getLoginEndDate();
        Date startTime = testSession.getDailyLoginStartTime();
        Date endTime = testSession.getDailyLoginEndTime();
        String timeZone = testSession.getTimeZone();
        String timeLimit = testSession.getEnforceTimeLimit();
        String enforceBreaks = testSession.getEnforceBreak();
        String isRandomize = testSession.getIsRandomize();
        Integer productId = testSession.getProductId();
        String tutorial = null;
        List subtests  = new ArrayList();
        Integer itemSetId = testSession.getItemSetId();
        
        TestElement [] testElements = session.getScheduledUnits();
        
        for (int i=0; i<testElements.length; i++) {
            int durationMinutes = testElements[i].getTimeLimit().intValue()/60;
            String duration;
            if (durationMinutes == 0) 
                duration = "Untimed";
            else 
                duration = durationMinutes +" minutes";
            String subtestTAC = accessCode;
            if("t".equals(enforceBreaks.toLowerCase())){
                subtestTAC = testElements[i].getAccessCode();
            }
            SubtestVO subtestVO = new SubtestVO(testElements[i].getItemSetId(),
                                                i+1+"", 
                                                testElements[i].getItemSetName(), 
                                                duration, 
                                                subtestTAC,
                                                testElements[i].getSessionDefault());
            subtests.add(subtestVO);
        }
        
        
        TestAdminVO result =  new TestAdminVO( sessionName,
                                testName, 
                                location, 
                                accessCode,
                                level,
                                startDate,
                                endDate,
                                startTime,
                                endTime,
                                timeZone,
                                timeLimit,
                                enforceBreaks,
                                isRandomize,
                                tutorial,
                                subtests,
                                productId);
        result.setItemSetId(itemSetId);
        return result;  
    }

    private TestElementData getTestsForProductForUser(Integer productId, 
                                                      FilterParams filter, 
                                                      PageParams page, 
                                                      SortParams sort)
        throws CTBBusinessException
    {
        TestElementData ted = this.scheduleTest.getTestsForProduct(this.schedulerName, productId, filter, page, sort);    
        return ted;
    }
    
    private List buildSummaryTicketsTestList(Integer testAdminId) throws CTBBusinessException
    {
        List result = new ArrayList();
        if (testAdminId == null)
            return result;
        
        TestElementData suTed = this.testSessionStatus.getTestElementsForTestSession(this.schedulerName, testAdminId, null, null, null);
        TestElement [] usTes = suTed.getTestElements();
        for(int i=0; i< usTes.length; i++){
            TestElement te = usTes[i];
            int durationMinutes = te.getTimeLimit().intValue()/60;
            String duration;
            if (durationMinutes == 0) 
                duration = "Untimed";
            else 
                duration = durationMinutes +" minutes";
            SubtestVO subtestVO = new SubtestVO(te.getItemSetId(),i+1+"", te.getItemSetName(), 
                    duration, te.getAccessCode(), te.getSessionDefault());
            result.add(subtestVO);
        }
        return result;
        
    }

    private List buildTestList(TestElementData ted, Integer productId) throws CTBBusinessException
    {
        
        List result = new ArrayList();
        if (ted == null)
            return result;
        String levelOrGrade = this.getLevelOrGrade(productId);
        TestElement [] tes = ted.getTestElements();
        //todo: ask Nate to fix bug here.
        for (int i=0; i<tes.length && tes[i]!=null ; i++)
        {
            String accessCode = tes[i].getAccessCode();
            
            List subtests = new ArrayList();

            Integer itemSetId = tes[i].getItemSetId();
            TestElementData suTed = this.scheduleTest.getSchedulableUnitsForTest(this.schedulerName,
                                                                                 itemSetId, 
                                                                                 new Boolean(true),
                                                                                 null,
                                                                                 null,
                                                                                 null);
            TestElement [] usTes = suTed.getTestElements();
            for (int j=0; j<usTes.length; j++)
            {
                int durationMinutes = usTes[j].getTimeLimit().intValue()/60;
                String duration;
                if (durationMinutes == 0) 
                    duration = "Untimed";
                else 
                    duration = durationMinutes +" minutes";
                SubtestVO subtestVO = new SubtestVO(usTes[j].getItemSetId(),j+1+"", usTes[j].getItemSetName(), 
                        duration, usTes[j].getAccessCode(), usTes[j].getSessionDefault());
                subtests.add(subtestVO);
            }
    
            int durationMinutes = tes[i].getTimeLimit().intValue()/60;
            String duration;
            if (durationMinutes == 0) 
                duration = "Untimed";
            else 
                duration = durationMinutes +" minutes";
    
            String level = "";
            if (levelOrGrade.equals("Grade")) 
                level = tes[i].getGrade();
            else
                level = tes[i].getItemSetLevel();
            
            TestVO testVO = new TestVO(tes[i].getItemSetId(), 
                                      tes[i].getItemSetName(), 
                                      level, 
                                      duration, 
                                      subtests);
            testVO.setLevelOrGrade(levelOrGrade);
            testVO.setAccessCode(accessCode);
            testVO.setForms(tes[i].getForms());
            
            testVO.setOverrideFormAssignment(tes[i].getOverrideFormAssignmentMethod());
            testVO.setOverrideLoginStartDate(tes[i].getOverrideLoginStartDate());
                    
            result.add(testVO);
        }
        return result;
    }

     private ScheduledSession getScheduledSession(Integer sessionId) 
    {
        ScheduledSession session = null;
        try {      
            session = this.scheduleTest.getScheduledSession(this.userName, sessionId);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return session;
    }

    private Collection buildStudentRoster(Collection rosterList, Integer studentId)
    {
        ArrayList result = new ArrayList();
        for(Iterator it=rosterList.iterator(); it.hasNext() ; ){
            TestRosterVO roster = (TestRosterVO)it.next();
            if (roster.getStudentId().intValue() == studentId.intValue()) {
                result.add(roster);
                break;
            }
        }
        return result;
    }
    
    private Collection buildRosterList(RosterElementData red, SessionStudent[] students)
    {
        HashMap map = new HashMap();
        RosterElement[] rosterElements = red.getRosterElements();
        for (int i=0 ; i<rosterElements.length ; i++) {
            RosterElement rosterElt = rosterElements[i];
            if (rosterElt != null) {
                TestRosterVO vo = new TestRosterVO(rosterElt);
                vo.setSeq(i);
                map.put(vo.getStudentId(), vo);
            }
        }     
        if(students != null){
            for(int i=0; i<students.length; i++){
                SessionStudent student = students[i];
                TestRosterVO roster = (TestRosterVO)map.get(student.getStudentId());
                if(roster != null){
                    setAccommodations(roster, student);
                }
            }
        }   
        TestRosterVO[] rosters = new TestRosterVO[rosterElements.length];
        for(Iterator it = map.values().iterator(); it.hasNext();){
            TestRosterVO roster = (TestRosterVO)it.next();
            rosters[roster.getSeq()] = roster;
        }
        ArrayList result = new ArrayList();
        for(int i=0; i<rosters.length; i++){
            result.add(rosters[i]);
        }
        return result;
    }
    
    private void setAccommodations(TestRosterVO roster, SessionStudent student){
        roster.setCalculator(student.getCalculator());
        roster.setHasColorFontAccommodations(student.getHasColorFontAccommodations());
        roster.setScreenReader(student.getScreenReader());
        roster.setTestPause(student.getTestPause());
        roster.setUntimedTest(student.getUntimedTest());
        roster.setHighLighter(student.getHighLighter());/* 51931 Deferred Defect For HighLighter*/
        roster.setHasAccommodations(student.getHasAccommodations());
        List accommodationList = new ArrayList();
        if(!stringToBoolean(student.getHasAccommodations())){
            accommodationList.add("--");
        }
        else{
            if(stringToBoolean(student.getCalculator())){
                accommodationList.add("Calculator");
            }
            if(stringToBoolean(student.getTestPause())){
                roster.setHasPause(true);
                accommodationList.add("Pause");
            }
            if(stringToBoolean(student.getUntimedTest())){
                accommodationList.add("Untimed");
            }
            if(stringToBoolean(student.getHasColorFontAccommodations())){
                accommodationList.add("Color/Font");
            }
            if(stringToBoolean(student.getScreenReader())){
                accommodationList.add("Screen Reader");
            }
            /* 51931 Deferred Defect For HighLighter*/
            if(stringToBoolean(student.getHighLighter())){
                accommodationList.add("Highlighter");
            }
        }
        
        String[] accommodations = new String[accommodationList.size()];
        for(int i=0; i<accommodationList.size(); i++){
            accommodations[i] = (String)accommodationList.get(i);
        }
        roster.setAccommodations(accommodations);
    }

    private boolean stringToBoolean(String in){
        boolean result = true;
        if(in == null ||
           in.toLowerCase().equals("false") ||
           in.toLowerCase().equals("f")){
            result = false;
        }
        return result;
    }
    private String getTestSessionDuration(TestAdminVO testAdmin)
    {
        int totalTime = 0;
        List subtests = testAdmin.getSubtests();
        for(int i=0;i<subtests.size();i++){            
            SubtestVO subtestVO = (SubtestVO) subtests.get(i);
            String duration = subtestVO.getDuration();
            totalTime = totalTime + Integer.parseInt((duration.substring(0,duration.indexOf("minutes"))).trim());            
        }
        String timeLimit = totalTime + " minutes";
        return timeLimit;
    }

    //todo: rewrite to put in a Hashtable
    private TestVO getTestTicketTestById(Integer testId) 
    {
        if (this.testTicketTestList == null)
            return null;
        Iterator it = this.testTicketTestList.iterator();
        while (it.hasNext())
        {
            TestVO testVO= (TestVO)it.next();
            if (testVO.getId().equals(testId))
                return testVO;
        }
        return null;
    } 

    private Integer getTestId(TestSessionData tsd)
    {
        TestSession[] testSessions = tsd.getTestSessions();
        TestSession testSession = testSessions[0];
        return testSession.getItemSetId();
    }

    private TestSummaryVO getTestSessionSummary(Collection rosterList)
    {
        int total = 0;
        int accommodated = 0;
        int calculator = 0;
        int screenReader = 0;
        int colorFont = 0;
        int pause = 0;
        int untimed = 0;
        int highLighter = 0; /* 51931 Deferred Defect For HighLighter*/
        for(Iterator it = rosterList.iterator(); it.hasNext();){
            TestRosterVO roster = (TestRosterVO)it.next();
            if(stringToBoolean(roster.getHasAccommodations())){
                accommodated++;
            }
            if(stringToBoolean(roster.getCalculator())){
                calculator++;
            }
            if(stringToBoolean(roster.getScreenReader())){
                screenReader++;
            }
            if(stringToBoolean(roster.getHasColorFontAccommodations())){
                colorFont++;
            }
            if(stringToBoolean(roster.getTestPause())){
                pause++;
            }
            if(stringToBoolean(roster.getUntimedTest())){
                untimed++;
            }
			/* 51931 Deferred Defect For HighLighter*/
            if(stringToBoolean(roster.getHighLighter())){
                highLighter++;
            }
        }
        /* 51931 Deferred Defect For HighLighter*/
        return new TestSummaryVO(new Integer(total),
                                 new Integer (accommodated),
                                 new Integer (calculator),
                                 new Integer (screenReader),
                                 new Integer (colorFont),
                                 new Integer (pause),
                                 new Integer (untimed),
                                 new Integer (highLighter));
     } 

}

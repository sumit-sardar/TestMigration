package downloadtest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.ActiveSession;
import com.ctb.bean.testAdmin.ActiveSessionData;
import com.ctb.bean.testAdmin.ActiveTest;
import com.ctb.bean.testAdmin.ActiveTestData;
import com.ctb.bean.testAdmin.CustomerSDS;
import com.ctb.bean.testAdmin.CustomerSDSData;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.testSessionInfo.dto.ActiveSessionVO;
import com.ctb.testSessionInfo.dto.ActiveTestVO;
import com.ctb.testSessionInfo.utils.FileSizeUtils;
import com.ctb.testSessionInfo.utils.FilterSortPageUtils;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;

import noNamespace.Scotype;
import noNamespace.TitleDocument;
import noNamespace.Titletype;


/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class DownloadTestController extends PageFlowController
{
    static final long serialVersionUID = 1L;

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;

    private String userName = null;
    private HashMap selectedTests = null;
    private HashMap testsOnPage   = null;
    private Integer selectTestPageRequested = null;
    private String selectTestSortColumn = null;
    private String selectTestSortOrderBy = null;
    private ActiveTestData confirmAtd = null;
    private Integer confirmTestPageRequested = null;
    private String confirmTestSortColumn = null;
    private String confirmTestSortOrderBy = null;
    private boolean errorNoItemSetSelected = false;
    private boolean errorNoTestsAvailable = false;
    private String viewTestSessionItemSetId = null;
    private String viewTestSessionItemSetName = null;
    
    private static final int SELECT_TESTS_PAGE_SIZE = FilterSortPageUtils.PAGESIZE_20;
    private static final int CONFIRM_TESTS_PAGE_SIZE = FilterSortPageUtils.PAGESIZE_20;
    private static final int VIEW_SESSIONS_PAGE_SIZE = FilterSortPageUtils.PAGESIZE_20;

    
    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="start" path="select_tests.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "start",
                     path = "select_tests.do")
    })
    protected Forward begin()
    {
        this.getLoggedInUserPrincipal();
        String sessionId = (String)getSession().getAttribute("sessionId");        
        this.selectedTests = new HashMap();
        this.testsOnPage = new HashMap();
        SelectTestsForm form = new SelectTestsForm();
        form.init();
        this.updatePageFlowFormVarsForSelectTest(form);
        return new Forward("start", form);
    }
    

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/homepage/HomePageController.jpf"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "/homepage/HomePageController.jpf")
    })
    protected Forward goto_homepage()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="cancel_download.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "cancel_download.jsp")
    })
    protected Forward goto_canceldownload()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/downloadclient/DownloadClientController.jpf"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "/downloadclient/DownloadClientController.jpf")
    })
    protected Forward goto_downloadclient()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="select_tests.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "select_tests.jsp")
    })
    protected Forward select_tests(SelectTestsForm form)
    {
        form.validateValues();
         
        // update page flow member vars
        this.updateSelectedTestsFromForm(form);
        this.updatePageFlowFormVarsForSelectTest(form);
        
        // retrieve information for user tests, build form and request vars
        PageParams testPage = FilterSortPageUtils.buildPageParams(this.selectTestPageRequested, SELECT_TESTS_PAGE_SIZE);
        SortParams testSort = getTestSortParams(this.selectTestSortColumn, this.selectTestSortOrderBy);
        ActiveTestData atd = this.getActiveTestsForUser(testPage, testSort);
                
        ArrayList testList = this.buildTestList(atd); 
        form.setMaxPage(atd.getFilteredPages());
        
        PagerSummary testPagerSummary = this.buildTestPagerSummary(atd, form.getTestPageRequested()); 
        
        // update page flow member vars
        this.testsOnPage = getHashMapForArrayList(testList);
        this.updateSelectedTestsInForm(form);
        
        // put information on request for jsp         
        this.getRequest().setAttribute("tests", testList);
        this.getRequest().setAttribute("testPagerSummary", testPagerSummary);
        this.getRequest().setAttribute("selectTestErrorStyle", this.getStyle(this.errorNoItemSetSelected));
        this.getRequest().setAttribute("noTestErrorStyle", this.getStyle(testList.size() == 0));
        
        this.errorNoItemSetSelected = false;
        
        this.getRequest().setAttribute("isDownloadTest", Boolean.TRUE);
        this.getRequest().setAttribute("deselectAllTestDisableButton", noTestSelected());
        this.getRequest().setAttribute("noTestToDownload", new Boolean(testList.size() == 0));
        
        return new Forward("success", form);
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="select_tests.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "select_tests.jsp")
    })
    protected Forward selectAllTests(SelectTestsForm form)
    {
        form.validateValues();
        
        // update page flow member vars
        this.updateSelectedTestsToAllTests();
        this.updatePageFlowFormVarsForSelectTest(form);
        
        // retrieve information for user tests, build form and request vars
        PageParams testPage = FilterSortPageUtils.buildPageParams(this.selectTestPageRequested, SELECT_TESTS_PAGE_SIZE);
        SortParams testSort = getTestSortParams(this.selectTestSortColumn, this.selectTestSortOrderBy);
        ActiveTestData atd = this.getActiveTestsForUser(testPage, testSort);
        ArrayList testList = this.buildTestList(atd); 
        form.setMaxPage(atd.getFilteredPages());
        
        PagerSummary testPagerSummary = this.buildTestPagerSummary(atd, form.getTestPageRequested()); 
        
        // update page flow member vars
        this.testsOnPage = getHashMapForArrayList(testList);
        this.updateSelectedTestsInForm(form);
        
        // put information on request for jsp         
        this.getRequest().setAttribute("tests", testList);
        this.getRequest().setAttribute("testPagerSummary", testPagerSummary);
        
        this.getRequest().setAttribute("selectTestErrorStyle", this.getStyle(this.errorNoItemSetSelected));
        this.getRequest().setAttribute("noTestErrorStyle", this.getStyle(testList.size() == 0));
        this.getRequest().setAttribute("deselectAllTestDisableButton", noTestSelected());
        
        this.errorNoItemSetSelected = false;
        return new Forward("success", form);
    }

    private void updateSelectedTestsToAllTests()
    {
        ActiveTestData atd = this.getActiveTestsForUser(null, null);
        ArrayList testList = this.buildTestList(atd); 
        this.selectedTests = getHashMapForArrayList(testList);
    }
     
    private void updateSelectedTestsToNoTests()
    {
        this.selectedTests = new HashMap();
    }
      
    /**
     * @jpf:action
     * @jpf:forward name="success" path="select_tests.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "select_tests.jsp")
    })
    protected Forward deselectAllTests(SelectTestsForm form)
    {
        form.validateValues();
        
        // update page flow member vars
        this.updateSelectedTestsToNoTests();
        this.updatePageFlowFormVarsForSelectTest(form);
        
        // retrieve information for user tests, build form and request vars
        PageParams testPage = FilterSortPageUtils.buildPageParams(this.selectTestPageRequested, SELECT_TESTS_PAGE_SIZE);
        SortParams testSort = getTestSortParams(this.selectTestSortColumn, this.selectTestSortOrderBy);
        ActiveTestData atd = this.getActiveTestsForUser(testPage, testSort);
        ArrayList testList = this.buildTestList(atd); 
        form.setMaxPage(atd.getFilteredPages());
        
        PagerSummary testPagerSummary = this.buildTestPagerSummary(atd, form.getTestPageRequested()); 
        
        // update page flow member vars
        this.testsOnPage = getHashMapForArrayList(testList);
        this.updateSelectedTestsInForm(form);
        
        // put information on request for jsp         
        this.getRequest().setAttribute("tests", testList);
        this.getRequest().setAttribute("testPagerSummary", testPagerSummary);
        
        this.getRequest().setAttribute("selectTestErrorStyle", this.getStyle(this.errorNoItemSetSelected));
        this.getRequest().setAttribute("noTestErrorStyle", this.getStyle(testList.size() == 0));
        this.getRequest().setAttribute("deselectAllTestDisableButton", noTestSelected());
        
        this.errorNoItemSetSelected = false;
        return new Forward("success", form);
    }
    
    private SortParams getTestSortParams(String sortColumn, String sortOrder)
    {
        String[] defaultSortNames = {"ItemSetName", "ContentSize"};
        String[] defaultSortOrderBys = {FilterSortPageUtils.ASCENDING, FilterSortPageUtils.ASCENDING};
        String[] sortNames = new String[2];
        String[] sortOrderBys = new String[2];
        if (sortColumn != null)
        {
            sortNames[0] = sortColumn;
            sortOrderBys[0] = sortOrder;
        }
        else
        {
            sortNames[0] = defaultSortNames[0];
            sortOrderBys[0] = defaultSortOrderBys[0];
            sortColumn = defaultSortNames[0];
        }
        int j=1;
        for (int i=0; i < defaultSortNames.length && j < 2; i++)
        {
            if (!defaultSortNames[i].equals(sortColumn))
            {
                sortNames[j] = defaultSortNames[i];
                sortOrderBys[j] = defaultSortOrderBys[i];
                j++;
            }
        }
        return FilterSortPageUtils.buildSortParams(sortNames, sortOrderBys);
    }
    
    private String getStyle(boolean visible_)
    {
        String result = "display:none";
        if (visible_)
            result = "display:block";
        return result;
    }
    
    private void updateSelectedTestsInForm(SelectTestsForm form)
    {
        ArrayList formItemSetIds = new ArrayList();
        Set selectedItemSetIds = this.selectedTests.keySet();
        for (Iterator it=this.testsOnPage.keySet().iterator(); it.hasNext(); )
        {
            String itemSetIdOnPage = (String)it.next().toString();
            if (selectedItemSetIds.contains(itemSetIdOnPage))
            {
                formItemSetIds.add(itemSetIdOnPage);
            }
        }
        form.setItemSetIds(getStringArrayFromArraylist(formItemSetIds));
    }
    
    private String[] getStringArrayFromArraylist(ArrayList list)
    {
        String[] result = new String[list.size()];
        int i=0;
        for (Iterator it=list.iterator(); it.hasNext(); )
        {
            result[i++] = (String)it.next();
        }
        return result;
    }
    
    private void updatePageFlowFormVarsForSelectTest(SelectTestsForm form)
    {
        this.selectTestPageRequested = form.getTestPageRequested();
        this.selectTestSortColumn = form.getTestSortColumn();
        this.selectTestSortOrderBy = form.getTestSortOrderBy();
    }

    private void updatePageFlowFormVarsForConfirmTest(SelectTestsForm form)
    {
        this.confirmTestPageRequested = form.getTestPageRequested();
        this.confirmTestSortColumn = form.getTestSortColumn();
        this.confirmTestSortOrderBy = form.getTestSortOrderBy();
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="select_tests.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "select_tests.do")
    })
    protected Forward returnto_select_tests(SelectTestsForm form)
    {
        form.init();
        form.setTestPageRequested(this.selectTestPageRequested);
        form.setTestSortColumn(this.selectTestSortColumn);
        form.setTestSortOrderBy(this.selectTestSortOrderBy);
        form.setItemSetIds(this.getStringItemSetIds());
        return new Forward("success", form);
    }
    
    private String[] getStringItemSetIds()
    {
        Collection itemSetIds = this.selectedTests.keySet();
        String[] result = new String[itemSetIds.size()];
        int i=0;
        for (Iterator it=itemSetIds.iterator(); it.hasNext(); )
        {
            String itemSetId = (String)it.next();
            result[i] = itemSetId;
            i++;
        }
        return result;
    }
    
    private HashMap getHashMapForArrayList(ArrayList testList)
    {
        HashMap result = new HashMap();
        for (Iterator it = testList.iterator(); it.hasNext(); )
        {
            ActiveTestVO test = (ActiveTestVO)it.next();
            result.put(test.getItemSetId().toString(), test);
        }
        return result;
    }
    
    // add any newly selected tests in form to page flow selectedTests
    // remove any unselected tests in form from page flow selectedTests
    private void updateSelectedTestsFromForm(SelectTestsForm form)
    {
        ArrayList selectedItemSetIdsOnThisPage = this.getArrayListFromStringArray(form.getItemSetIds());
        for (Iterator it=this.testsOnPage.keySet().iterator(); it.hasNext(); )
        {
            String itemSetIdOnPage = (String)it.next();
            if (selectedItemSetIdsOnThisPage.contains(itemSetIdOnPage))
            {
                ActiveTestVO test = (ActiveTestVO)this.testsOnPage.get(itemSetIdOnPage);
                this.selectedTests.put(itemSetIdOnPage, test);
            }
            else
            {
                this.selectedTests.remove(itemSetIdOnPage);
            }
        }
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="confirm_load_tests.do"
     * @jpf:forward name="failure" path="select_tests.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "confirm_load_tests.do"), 
        @Jpf.Forward(name = "failure",
                     path = "select_tests.do")
    })
    protected Forward tests_selected(SelectTestsForm inForm)
    {
        this.updateSelectedTestsFromForm(inForm);
        if (this.selectedTests.size() < 1)
        {
            this.errorNoItemSetSelected = true;
            return new Forward("failure", inForm);
        }
        else
        {
            this.updatePageFlowFormVarsForSelectTest(inForm);
            SelectTestsForm outForm = new SelectTestsForm();
            outForm.init();
            return new Forward("success", outForm);
        }
    }

    private void initializeConfirmAtd()
    {
        this.confirmAtd = new ActiveTestData();
        this.confirmAtd.setActiveTests(getActiveTests(), new Integer(CONFIRM_TESTS_PAGE_SIZE));
    }
    
    private ActiveTest[] getActiveTests()
    {
        ActiveTest[] result = new ActiveTest[this.selectedTests.size()];
        int i=0;
        for (Iterator it = this.selectedTests.keySet().iterator(); it.hasNext(); )
        {
            ActiveTestVO vo = (ActiveTestVO)this.selectedTests.get(it.next());
            ActiveTest test = new ActiveTest();
            test.setContentSize(vo.getContentSize());
            test.setItemSetId(vo.getItemSetId());
            test.setItemSetName(vo.getItemSetName());
            test.setLoginEndDate(vo.getLoginEndDate());
            test.setLoginStartDate(vo.getLoginStartDate());
            test.setSubtests(createSubTests(vo.getTdItemSetIds()));
            test.setItemSetGrade(vo.getItemSetGrade());
            test.setItemSetLevel(vo.getItemSetLevel());
            result[i] = test;
            i++;
        }
        return result;
    }
    
    private TestElement[] createSubTests(Integer[] tdItemSetIds)
    {
        TestElement[] result = new TestElement[tdItemSetIds.length];
        for (int i=0; i < tdItemSetIds.length; i++)
        {
            TestElement testElement = new TestElement();
            testElement.setItemSetId(tdItemSetIds[i]);
            result[i] = testElement;
        }
        return result;
    }
    
    private String getTotalLoadSize()
    {
        double value = 0.0;
        Collection keys = this.selectedTests.keySet();
        for (Iterator it=keys.iterator(); it.hasNext(); )
        {
            ActiveTestVO test = (ActiveTestVO)this.selectedTests.get(it.next());
            value += test.getDisplayContentBytes().doubleValue();
        }
        String result = FileSizeUtils.getDisplayValue(value);
        return result;
    }
    
    private ArrayList getArrayListFromStringArray(String[] in)
    {
        ArrayList result = new ArrayList();
        if (in != null)
        {
            for (int i=0; i < in.length; i++)
            {
                result.add(in[i]);
            }
        }
        return result;
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="confirm_load_tests.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "confirm_load_tests.do")
    })
    protected Forward returnto_confirm_load_tests()
    {
        SelectTestsForm form = new SelectTestsForm();
        form.init();
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="confirm_load_tests.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "confirm_load_tests.jsp")
    })
    protected Forward confirm_load_tests(SelectTestsForm form)
    {        
        this.updatePageFlowFormVarsForConfirmTest(form);
        this.initializeConfirmAtd();

        // create page and sort params for confirm_load_tests
        Integer pageNumber = form.getTestPageRequested();
        PageParams testPage = FilterSortPageUtils.buildPageParams(form.getTestPageRequested(), CONFIRM_TESTS_PAGE_SIZE);
        SortParams testSort = FilterSortPageUtils.buildSortParams(form.getTestSortColumn(), form.getTestSortOrderBy());
        
        try
        {
            this.confirmAtd.applySorting(testSort);
            this.confirmAtd.applyPaging(testPage);
        }
        catch (Exception e)
        {
            // what do I do here?
        }
        
        List testList = this.buildTestList(this.confirmAtd);
        form.setMaxPage(this.confirmAtd.getTotalPages());
        // build user tests request information for jsp         
        SelectTestsForm outForm = new SelectTestsForm();
        outForm.init();
        PagerSummary testPagerSummary = buildConfirmTestPagerSummary(this.confirmTestPageRequested); 
        this.getRequest().setAttribute("tests", testList);
        this.getRequest().setAttribute("testPagerSummary", testPagerSummary);
        this.getRequest().setAttribute("totalLoadSize", this.getTotalLoadSize());
        
        // display tests that the user selected
        return new Forward("success", form);
    }

    private Integer getConfirmMaxPage()
    {
        return new Integer(getMaxPage(this.selectedTests.size(), CONFIRM_TESTS_PAGE_SIZE));
    }
    
    private int getMaxPage(int numItems, int numPerPage)
    {
        int floor = numItems / numPerPage;
        int result = floor;
        int mod = numItems % numPerPage;
        if (mod != 0)
            result = floor + 1;
        return result;
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="view_test_sessions.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "view_test_sessions.do")
    })
    protected Forward goto_view_test_sessions(SelectTestsForm inForm)
    {
        this.updateSelectedTestsFromForm(inForm);

        this.viewTestSessionItemSetId = this.getRequest().getParameter("itemSetId");
        this.viewTestSessionItemSetName = this.getRequest().getParameter("itemSetName");
        ViewSessionsForm form = new ViewSessionsForm();
        form.init();
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="view_test_sessions.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "view_test_sessions.jsp")
    })
    protected Forward view_test_sessions(ViewSessionsForm form)
    {
        form.validateValues();
        
        PageParams pageParams = FilterSortPageUtils.buildPageParams(form.getSessionPageRequested(), VIEW_SESSIONS_PAGE_SIZE);
        //SortParams sortParams = FilterSortPageUtils.buildSortParams(form.getSessionSortColumn(), form.getSessionSortOrderBy());
        SortParams sortParams = getSessionSortParams(form.getSessionSortColumn(), form.getSessionSortOrderBy());
        ActiveSessionData asd = this.getActiveSessionsForTest(new Integer(this.viewTestSessionItemSetId), pageParams, sortParams);
        List sessionList = buildSessionList(asd); 
        form.setMaxPage(asd.getFilteredPages());
        
        // build user tests request information for jsp         
        PagerSummary sessionPagerSummary = buildSessionPagerSummary(asd, form.getSessionPageRequested()); 
        this.getRequest().setAttribute("itemSetName", this.viewTestSessionItemSetName);
        this.getRequest().setAttribute("sessions", sessionList);
        this.getRequest().setAttribute("sessionPagerSummary", sessionPagerSummary);

        return new Forward("success", form);
    }

    private SortParams getSessionSortParams(String sortColumn, String sortOrder)
    {
        String[] defaultSortNames = {"OrgNodeName", "TestAdminName", "LoginStartDate", "LoginEndDate"};
        String[] defaultSortOrderBys = {FilterSortPageUtils.ASCENDING, FilterSortPageUtils.ASCENDING,FilterSortPageUtils.ASCENDING,FilterSortPageUtils.ASCENDING};
        String[] sortNames = new String[4];
        String[] sortOrderBys = new String[4];
        if (sortColumn != null)
        {
            sortNames[0] = sortColumn;
            sortOrderBys[0] = sortOrder;
        }
        else
        {
            sortNames[0] = defaultSortNames[0];
            sortOrderBys[0] = defaultSortOrderBys[0];
            sortColumn = defaultSortNames[0];
        }
        int j=1;
        for (int i=0; i < defaultSortNames.length; i++)
        {
            if (!defaultSortNames[i].equals(sortColumn)){
                sortNames[j] = defaultSortNames[i];
                sortOrderBys[j] = defaultSortOrderBys[i];
                j++;
            }
        }
        return FilterSortPageUtils.buildSortParams(sortNames, sortOrderBys);
    }
    /**
     * @jpf:action
     * @jpf:forward name="success" path="load_progress.do"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "load_progress.do")
		}
	)
    protected Forward goto_load_progress()
    {
        return new Forward("success");
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="load_progress.jsp"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "load_progress.jsp")
		}
	)
    protected Forward load_progress()
    {
        String objectBankPath = (String)this.getRequest().getParameter("objectBankPath");
        
        // construct download manager xml from tests that user selected
        String xml = this.getDownloadManagerXml();
        String sdsId = null;
        String sdsToken = null;
        try{
            CustomerSDSData sdsData = testSessionStatus.getCustomerSDSListForUser(this.userName, null, null, null);
            CustomerSDS[] sdsArray = sdsData.getCustomerSDSs();
            CustomerSDS sds = sdsArray[0];
            sdsId = sds.getName();
            sdsToken = sds.getToken();
        }
        catch(Exception e){ // sds id and token will be null, download will fail
            String stackTrace = e.getStackTrace().toString();
            e.printStackTrace();
        } 


        this.getRequest().setAttribute("downloadManagerXml", xml);
        this.getRequest().setAttribute("sdsId", sdsId);
        this.getRequest().setAttribute("sdsToken", sdsToken);
        this.getRequest().setAttribute("adsUrl", this.getAdsUrl());
        this.getRequest().setAttribute("objectBankPath", objectBankPath);
        return new Forward("success");
    }
    
    private String getDownloadManagerXml(){
    	
        TitleDocument document = TitleDocument.Factory.newInstance();
        document.setTitle(this.getTitle());
        String result = document.xmlText();
        // yank for progress2
        //result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + result;
        return result;
    }

	
    private Titletype getTitle(){
        Titletype result = Titletype.Factory.newInstance();
        result.setMode("proctor");
        result.setName("name");
        result.setID("id");
        result.setSCOArray(this.getScoArray());
        return result;
    }
   
    
	
    private Scotype[] getScoArray(){
        String adsUrl = getAdsUrl();
        ArrayList scos = new ArrayList();
        for (Iterator it = this.selectedTests.keySet().iterator(); it.hasNext();){
            ActiveTestVO vo = (ActiveTestVO)this.selectedTests.get(it.next());
            Integer[] tdItemSetIds = vo.getTdItemSetIds();
            for(int j=0; j<tdItemSetIds.length; j++){
                Scotype sco = Scotype.Factory.newInstance();
                sco.setUrl( "not used" );
                sco.setAaAsmtId( tdItemSetIds[j].toString() );
                sco.setTimeLimitSeconds( "0" );
                sco.setTitle( "title" );
                sco.setName( "name" );
                sco.setSeq( String.valueOf(j+1) );
                scos.add(sco);
            }
        }
        Scotype[] result = new Scotype[scos.size()];
        int i=0;
        for(Iterator it=scos.iterator(); it.hasNext();){
            Scotype sco = (Scotype)it.next();
            result[i] = sco;
            i++;
        }

        return result;
    }

    
    
    private String getAdsUrl(){
        
//        String uri = this.getRequest().getRequestURI().toLowerCase();
//        String url = this.getRequest().getRequestURL().toString().toLowerCase();
//        String start = url.replaceAll(uri, "");
               
//         String serverName = this.getRequest().getServerName();
//        serverName = "dagobah.mhe.mhc";
//        String result = "http://" + serverName + "/ADSSvc/servlet/ADSSvc";
//        serverName = "https://dagobah.mhe.mhc/ADSSvc/servlet/ADSSvc";
        String serverName = "";
        
        if( this.getRequest().isSecure() ) 
            serverName += "https://";
        else 
            serverName += "http://";
        
        serverName += this.getRequest().getServerName() + "/ADSSvc/servlet/ADSSvc";
        return serverName;
    }
    
    private void getLoggedInUserPrincipal()
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        if (principal != null) 
            this.userName = principal.toString();
        getSession().setAttribute("userName", this.userName);
    }
 
     private ActiveTestData getActiveTestsForUser(PageParams page, SortParams sort){
        ActiveTestData atd = new ActiveTestData();                
        try {      
            atd = this.testSessionStatus.getActiveTestsForUser(this.userName, null, page, sort);            
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return atd;
     }
     
     private ActiveSessionData getActiveSessionsForTest(Integer itemSetId, PageParams page, SortParams sort){
        ActiveSessionData result = new ActiveSessionData();                
        try {      
            result = this.testSessionStatus.getActiveSessionsForTest(this.userName, itemSetId, null, page, sort);            
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return result;
     }
     
     private List buildSessionList(ActiveSessionData asd) 
    {
        List result = new ArrayList();                       
        ActiveSession[] sessions = asd.getActiveSessions();            
        for (int i=0 ; i<sessions.length ; i++) {
            ActiveSession as = sessions[i];
            if (as != null) {
                ActiveSessionVO vo = new ActiveSessionVO(as);
                result.add(vo);
            }
        }
        return result;
    }
    
   private ArrayList buildTestList(ActiveTestData atd) 
    {
        ArrayList result = new ArrayList();                       
        ActiveTest[] tests = atd.getActiveTests();            
        for (int i=0 ; i<tests.length ; i++) {
            ActiveTest at = tests[i];
            if (at != null) {
                ActiveTestVO vo = new ActiveTestVO(at);                
                String isn = at.getItemSetName();
                if ((isn.indexOf("TABE 9") == 0) || (isn.indexOf("TABE 10") == 0)) {
                    vo.setItemSetLevel(null);
                }
                result.add(vo);
            }
        }
        return result;
    }
    
    private PagerSummary buildTestPagerSummary(ActiveTestData atd, Integer pageRequested) 
    {
        PagerSummary result = new PagerSummary();
        result.setCurrentPage(pageRequested);
        result.setTotalObjects(atd.getTotalCount());
        //result.setTotalFilteredObjects(atd.getFilteredCount());        
        result.setTotalFilteredObjects(null);        
        result.setTotalPages(atd.getFilteredPages());
        
        return result;
    }
    
     private PagerSummary buildSessionPagerSummary(ActiveSessionData asd, Integer pageRequested) 
    {
        PagerSummary result = new PagerSummary();
        result.setCurrentPage(pageRequested);
        result.setTotalObjects(asd.getTotalCount());
        //result.setTotalFilteredObjects(asd.getFilteredCount());        
        result.setTotalFilteredObjects(null);        
        result.setTotalPages(asd.getFilteredPages());
        
        return result;
    }
    
    private PagerSummary buildConfirmTestPagerSummary(Integer pageRequested) 
    {
        int numTotalTests = this.selectedTests.size();
        int maxPageNumber = this.getConfirmMaxPage().intValue();
        PagerSummary result = new PagerSummary();
        result.setCurrentPage(pageRequested);
        result.setTotalObjects(new Integer(numTotalTests));
        result.setTotalFilteredObjects(this.getNumberOnPage(pageRequested.intValue(), CONFIRM_TESTS_PAGE_SIZE, numTotalTests));
        result.setTotalPages(new Integer(maxPageNumber));
        
        return result;
    }
    
    private Integer getNumberOnPage(int pageRequested, int numPerPage, int numTotal){
        int result = numPerPage;
        int lastPageNumber = this.getMaxPage(numTotal, numPerPage);
        int mod = numTotal % numPerPage;
        if(numTotal <= numPerPage)
            result = numTotal;
        else if(pageRequested == lastPageNumber && mod != 0)
            result = mod;
        return new Integer(result);
    }
    
    private String noTestSelected()
    {
        if (this.selectedTests.size() == 0)
            return "true";
        else
            return "false";            
    }
    
   /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class SelectTestsForm extends SanitizedFormData
    {
        private Integer testPageRequested = null;
        private String testSortColumn = null;
        private String testSortOrderBy = null;
        private Integer maxPage = null;                
        private String actionElement = null;
        private String[] itemSetIds = null;
        
        public SelectTestsForm()
        {
        }
        public void init()
        {
            this.testPageRequested = new Integer(1);
            this.testSortColumn = FilterSortPageUtils.TEST_DEFAULT_SORT;
            this.testSortOrderBy = FilterSortPageUtils.ASCENDING;
            this.maxPage = new Integer(1);      

            this.actionElement = "none";   
        }        
        public void validateValues() 
        {
            if (this.testSortColumn == null)
                this.testSortColumn = FilterSortPageUtils.TEST_DEFAULT_SORT;
            if (this.testSortOrderBy == null)
                this.testSortOrderBy = FilterSortPageUtils.ASCENDING;
            
            if ((this.maxPage == null) || (this.maxPage.intValue() == 0))
                this.maxPage = new Integer(1);
            
            if (this.testPageRequested == null)
                this.testPageRequested = new Integer(1);
            if (this.testPageRequested.intValue() <= 0)
                this.testPageRequested = new Integer(1);
            if (this.testPageRequested.intValue() > this.maxPage.intValue())
                this.testPageRequested = new Integer(this.maxPage.intValue());
        }
        public void setActionElement(String actionElement)
        {
            this.actionElement = actionElement;
        }
        public String getActionElement()
        {
            return this.actionElement;
        }
        public void setTestSortColumn(String testSortColumn)
        {
            this.testSortColumn = testSortColumn;
        }
        public String getTestSortColumn()
        {
            return this.testSortColumn != null ? this.testSortColumn : FilterSortPageUtils.TEST_DEFAULT_SORT;
        }        
        public void setTestSortOrderBy(String testSortOrderBy)
        {
            this.testSortOrderBy = testSortOrderBy;
        }
        public String getTestSortOrderBy()
        {
            return this.testSortOrderBy != null ? this.testSortOrderBy : FilterSortPageUtils.DESCENDING;
        }        
        public void setTestPageRequested(Integer testPageRequested)
        {
            this.testPageRequested = testPageRequested;
        }
        public Integer getTestPageRequested()
        {
            if (this.testPageRequested == null)
                return new Integer(1);
            if (this.testPageRequested.intValue() <= 0)
                return new Integer(1);
            return this.testPageRequested;
        }
        public void setItemSetIds(String[] itemSetIds)
        {
            this.itemSetIds = itemSetIds;
        }
        public String[] getItemSetIds()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.itemSetIds == null || this.itemSetIds.length == 0)
            {
                this.itemSetIds = new String[1];
            }
                 
            return this.itemSetIds;
        }
        public void setMaxPage(Integer maxPage)
        {
            this.maxPage = maxPage;
        }
        public Integer getMaxPage()
        {
            return this.maxPage;
        }
        public void reset(org.apache.struts.action.ActionMapping mapping,
                          javax.servlet.http.HttpServletRequest request)
        {
        }
        
    }
    
   /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ViewSessionsForm extends SanitizedFormData
    {
        private Integer sessionPageRequested = null;
        private String sessionSortColumn = null;
        private String sessionSortOrderBy = null;
        private Integer maxPage = null;
                
        private String actionElement = null;

        private String[] sessionIds = null;
        
        public ViewSessionsForm()
        {
        }
        public void init()
        {
            this.sessionPageRequested = new Integer(1);
            this.sessionSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT;
            this.sessionSortOrderBy = FilterSortPageUtils.ASCENDING;
            this.maxPage = new Integer(1);      

            this.sessionIds = new String[5];
             
            this.actionElement = "none";   
        }        
        public void resetValuesForAction() 
        {
            if ((actionElement.indexOf("sessionSortColumn") != -1) ||
                (actionElement.indexOf("sessionSortOrderBy") != -1)) {
                this.sessionPageRequested = new Integer(1);
            }            
        }
        public void validateValues() 
        {
            if (this.sessionSortColumn == null)
                this.sessionSortColumn = FilterSortPageUtils.TEST_DEFAULT_SORT;
            if (this.sessionSortOrderBy == null)
                this.sessionSortOrderBy = FilterSortPageUtils.DESCENDING;
            
            if ((this.maxPage == null) || (this.maxPage.intValue() == 0))
                this.maxPage = new Integer(1);
            
            if (this.sessionPageRequested == null)
                this.sessionPageRequested = new Integer(1);
            if (this.sessionPageRequested.intValue() <= 0)
                this.sessionPageRequested = new Integer(1);
            if (this.sessionPageRequested.intValue() > this.maxPage.intValue())
                this.sessionPageRequested = new Integer(this.maxPage.intValue());
        }
        public void setActionElement(String actionElement)
        {
            this.actionElement = actionElement;
        }
        public String getActionElement()
        {
            return this.actionElement;
        }
        public void setSessionSortColumn(String sessionSortColumn)
        {
            this.sessionSortColumn = sessionSortColumn;
        }
        public String getSessionSortColumn()
        {
            return this.sessionSortColumn != null ? this.sessionSortColumn : FilterSortPageUtils.TEST_DEFAULT_SORT;
        }        
        public void setSessionSortOrderBy(String sessionSortOrderBy)
        {
            this.sessionSortOrderBy = sessionSortOrderBy;
        }
        public String getSessionSortOrderBy()
        {
            return this.sessionSortOrderBy != null ? this.sessionSortOrderBy : FilterSortPageUtils.DESCENDING;
        }        
        public void setSessionPageRequested(Integer sessionPageRequested)
        {
            this.sessionPageRequested = sessionPageRequested;
        }
        public Integer getSessionPageRequested()
        {
            if (this.sessionPageRequested == null)
                return new Integer(1);
            if (this.sessionPageRequested.intValue() <= 0)
                return new Integer(1);
            return this.sessionPageRequested;
        }
        public void setSessionIds(String[] sessionIds)
        {
            this.sessionIds = sessionIds;
        }
        public String[] getSessionIds()
        {
            return this.sessionIds;
        }
        public void setMaxPage(Integer maxPage)
        {
            this.maxPage = maxPage;
        }
        public Integer getMaxPage()
        {
            return this.maxPage;
        }
        public void reset(org.apache.struts.action.ActionMapping mapping,
                          javax.servlet.http.HttpServletRequest request)
        {
        }
        
    }
}